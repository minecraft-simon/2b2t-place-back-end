package org.theancients.placebackend.pixel_grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.job.JobDto;
import org.theancients.placebackend.job.JobService;
import org.theancients.placebackend.recorded_pixel.RecordedPixel;
import org.theancients.placebackend.recorded_pixel.RecordedPixelService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PixelGridService {

    @Autowired
    private PixelGridRepository pixelGridRepository;

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

        /*
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
         */
    }

    @Scheduled(fixedRate = 10000)
    private void savePixelGrid() {
        pixelGridRepository.save(pixelGrid);
    }

    public PixelGrid getPixelGrid() {
        return pixelGrid;
    }

    public HttpStatus updatePixel(String playerName, PixelDto pixelDto) {
        if (pixelDto == null || pixelDto.getX() < 0 || pixelDto.getX() > 127 || pixelDto.getY() < 0 || pixelDto.getY() > 127) {
            return HttpStatus.BAD_REQUEST;
        }
        if (pixelDto.getColor() < 0 || pixelDto.getColor() > 15) {
            return HttpStatus.BAD_REQUEST;
        }
        int pos = pixelDto.getX() * 128 + pixelDto.getY();
        pixelGrid.getPixels()[pos] = pixelDto.getColor();
        recordedPixelService.recordPixel(playerName, pixelDto);

        jobService.createJob(pos, pixelDto);

        return HttpStatus.OK;
    }

}
