package org.theancients.placebackend.recorded_pixel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;

@Service
public class RecordedPixelService {

    @Autowired
    private RecordedPixelRepository recordedPixelRepository;

    public void recordPixel(String playerName, PixelDto pixelDto) {
        RecordedPixel recordedPixel = new RecordedPixel();
        recordedPixel.setPlayerName(playerName);
        recordedPixel.setX(pixelDto.getX());
        recordedPixel.setY(pixelDto.getY());
        recordedPixel.setColor(pixelDto.getColor());
        recordedPixelRepository.save(recordedPixel);
    }

}
