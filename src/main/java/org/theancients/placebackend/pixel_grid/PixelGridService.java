package org.theancients.placebackend.pixel_grid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.theancients.placebackend.job.JobService;
import org.theancients.placebackend.last_pixel_placer.LastPixelPlacerService;
import org.theancients.placebackend.player.Player;
import org.theancients.placebackend.player.PlayerService;
import org.theancients.placebackend.recorded_pixel.RecordedPixelService;
import org.theancients.placebackend.setting.SettingService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PixelGridService {

    @Autowired
    private PixelGridRepository pixelGridRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RecordedPixelService recordedPixelService;

    @Autowired
    private JobService jobService;

    @Autowired
    private LastPixelPlacerService lastPixelPlacerService;

    @Autowired
    private SettingService settingService;

    @Value("${application.pixelGridId:2}")
    private long pixelGridId;

    @Value("${application.endgameTemplateId}")
    private long endgameTemplateId;

    private PixelGrid pixelGrid;
    private byte[] endgameTemplate;

    private static final Object LOCK = new Object();

    @PostConstruct
    private void init() throws IOException {
        Optional<PixelGrid> optionalPixelGrid = pixelGridRepository.findById(pixelGridId);
        if (optionalPixelGrid.isPresent()) {
            this.pixelGrid = optionalPixelGrid.get();
        } else {
            PixelGrid pixelGrid = new PixelGrid();
            pixelGrid.setId(pixelGridId);
            byte[] pixels = new byte[16384];
            Arrays.fill(pixels, (byte) 0);
            pixelGrid.setPixels(pixels);
            this.pixelGrid = pixelGrid;
        }

        //loadTemplateFromJson();
        loadEndgameTemplate();
    }

    private void loadTemplateFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = ResourceUtils.getFile("classpath:sprite.json");
        PixelDto[] pixelDtos = objectMapper.readValue(file, PixelDto[].class);
        byte[] pixels = new byte[16384];
        for (int i = 0; i < pixelDtos.length; i++) {
            PixelDto pixelDto = pixelDtos[i];
            pixels[i] = pixelDto.getColor();
        }
        PixelGrid pixelGrid = pixelGridRepository.findById(6L).get();
        pixelGrid.setPixels(pixels);
        pixelGridRepository.save(pixelGrid);
    }

    private void loadEndgameTemplate() {
        endgameTemplate = pixelGridRepository.findById(endgameTemplateId).get().getPixels();
    }

    @Scheduled(fixedRate = 1000)
    private void savePixelGrid() {
        boolean createJobsFromPixelGrid = settingService.getBoolean("create_jobs_from_pixel_grid", false);
        if (createJobsFromPixelGrid) {
            settingService.setBoolean("create_jobs_from_pixel_grid", false);
            transformPixelGridToJobs();
        }
        synchronized (LOCK) {
            pixelGridRepository.save(pixelGrid);
        }
    }

    private void transformPixelGridToJobs() {
        List<PixelDto> pixelDtos = new ArrayList<>();
        byte[] pixels = this.pixelGrid.getPixels();
        for (int i = 0; i < pixels.length; i++) {
            byte color = pixels[i];
            int x = i / 128;
            int y = i % 128;
            PixelDto pixelDto = new PixelDto(x, y, color);
            pixelDtos.add(pixelDto);
        }
        jobService.createJobs(pixelDtos);
    }

    public PixelGrid getPixelGrid() {
        return pixelGrid;
    }

    public ResponseEntity<PixelUpdateResponseDto> updatePixel(String playerName, PixelDto pixelDto) {
        if (pixelDto == null || pixelDto.getX() < 0 || pixelDto.getX() > 127 || pixelDto.getY() < 0 || pixelDto.getY() > 127) {
            return ResponseEntity.badRequest().body(null);
        }
        if (pixelDto.getColor() < 0 || pixelDto.getColor() > 15) {
            return ResponseEntity.badRequest().body(null);
        }

        Player player = playerService.createOrGetPlayer(playerName);

        if (player.isBanned()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }

        if (!settingService.getBoolean("allow_pixel_updates", false)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }

        if (settingService.getBoolean("maintenance_mode", false)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }

        // check if cooldown is active
        if (playerService.playerHasCooldown(player)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }

        int arrayPos = pixelDto.getX() * 128 + pixelDto.getY();

        // when endgame-mode is activated, every pixel's color will get replace with the template pixel color
        if (settingService.getBoolean("activate_endgame", false)) {
            pixelDto.setColor(endgameTemplate[arrayPos]);
        }

        byte existingColor = pixelGrid.getPixels()[arrayPos];
        if (existingColor == pixelDto.getColor()) {
            PixelUpdateResponseDto pixelUpdateResponseDto = new PixelUpdateResponseDto(pixelDto, 0, "");
            return ResponseEntity.ok(pixelUpdateResponseDto);
        }

        // set cooldown
        playerService.startCooldown(playerName);
        int cooldownSeconds = playerService.getCooldownSecondsLeft(player);
        String cooldownMessage = playerService.getCooldownMessage(player);

        // save pixel
        synchronized (LOCK) {
            pixelGrid.getPixels()[arrayPos] = pixelDto.getColor();
            lastPixelPlacerService.update(playerName, pixelDto);
            recordedPixelService.recordPixel(playerName, pixelDto);
            jobService.createJob(pixelDto);
        }

        PixelUpdateResponseDto pixelUpdateResponseDto = new PixelUpdateResponseDto(pixelDto, cooldownSeconds, cooldownMessage);

        return ResponseEntity.ok(pixelUpdateResponseDto);
    }

}
