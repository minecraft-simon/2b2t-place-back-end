package org.theancients.placebackend.pixel_grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.job.JobService;
import org.theancients.placebackend.player.PlayerService;
import org.theancients.placebackend.recorded_pixel.RecordedPixelService;

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

    @Value("${application.pixelGridId:2}")
    private long pixelGridId;

    private PixelGrid pixelGrid;

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

        List<PixelDto> pixelDtos = new ArrayList<>();
        byte[] pixels = this.pixelGrid.getPixels();
        for (int i = 0; i < pixels.length; i++) {
            byte color = pixels[i];
            int x = i / 128;
            int y = i % 128;
            PixelDto pixelDto = new PixelDto(x, y, color);
            pixelDtos.add(pixelDto);
        }
        //jobService.createJobs(pixelDtos);
    }

    @Scheduled(fixedRate = 10000)
    private void savePixelGrid() {
        pixelGridRepository.save(pixelGrid);
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

        // check if cooldown is active
        if (playerService.playerHasCooldown(playerName)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }

        // set cooldown
        int cooldownSeconds = playerService.startCooldown(playerName);

        savePixel(pixelDto);

        recordedPixelService.recordPixel(playerName, pixelDto);

        jobService.createJob(pixelDto);

        PixelUpdateResponseDto pixelUpdateResponseDto = new PixelUpdateResponseDto(pixelDto, cooldownSeconds);

        return ResponseEntity.ok(pixelUpdateResponseDto);
    }

    private void savePixel(PixelDto pixelDto) {
        int arrayPos = pixelDto.getX() * 128 + pixelDto.getY();
        pixelGrid.getPixels()[arrayPos] = pixelDto.getColor();
    }

}
