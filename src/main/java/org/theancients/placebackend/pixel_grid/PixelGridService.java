package org.theancients.placebackend.pixel_grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.highlight.Highlight;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PixelGridService {

    @Autowired
    private PixelGridRepository pixelGridRepository;

    private PixelGrid pixelGrid;

    @PostConstruct
    private void init() {
        pixelGrid = pixelGridRepository.findById(1L).get();
    }

    @Scheduled(fixedRate = 10000)
    private void savePixelGrid() {
        pixelGridRepository.save(pixelGrid);
    }

    public PixelGrid getPixelGrid() {
        return pixelGrid;
    }

    public HttpStatus updatePixel(PixelDto pixelDto) {
        if (pixelDto == null || pixelDto.getX() < 0 || pixelDto.getX() > 127 || pixelDto.getY() < 0 || pixelDto.getY() > 127) {
            return HttpStatus.BAD_REQUEST;
        }
        if (pixelDto.getColor() < 0 || pixelDto.getColor() > 15) {
            return HttpStatus.BAD_REQUEST;
        }
        int pos = pixelDto.getY() + pixelDto.getX() * 128;
        pixelGrid.getPixels()[pos] = pixelDto.getColor();
        return HttpStatus.OK;
    }



}
