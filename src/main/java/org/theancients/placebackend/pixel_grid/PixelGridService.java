package org.theancients.placebackend.pixel_grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.job.JobService;
import org.theancients.placebackend.last_pixel_placer.LastPixelPlacerService;
import org.theancients.placebackend.player.Player;
import org.theancients.placebackend.player.PlayerService;
import org.theancients.placebackend.recorded_pixel.RecordedPixelService;
import org.theancients.placebackend.setting.SettingService;

import javax.annotation.PostConstruct;
import java.time.Instant;
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

    private PixelGrid pixelGrid;

    private static final Object LOCK = new Object();

    @PostConstruct
    private void init() {
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

        playerService.createPlayer(playerName);
        Player player = playerService.getPlayer(playerName);

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
        byte existingColor = pixelGrid.getPixels()[arrayPos];
        if (existingColor == pixelDto.getColor()) {
            PixelUpdateResponseDto pixelUpdateResponseDto = new PixelUpdateResponseDto(pixelDto, 0, "");
            return ResponseEntity.ok(pixelUpdateResponseDto);
        }

        // set cooldown
        playerService.startCooldown(playerName);
        int cooldownSeconds = playerService.getCooldownSecondsLeft(playerName);
        String cooldownMessage = playerService.getCooldownMessage(playerName);

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
