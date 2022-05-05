package org.theancients.placebackend.recorded_pixel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;
import org.theancients.placebackend.player.PlayerService;

@Service
public class RecordedPixelService {

    @Autowired
    private RecordedPixelRepository recordedPixelRepository;

    @Autowired
    private PlayerService playerService;

    public void recordPixel(String playerName, PixelDto pixelDto) {
        RecordedPixel recordedPixel = new RecordedPixel();
        recordedPixel.setPlayerId(playerService.getPlayerId(playerName));
        recordedPixel.setX(pixelDto.getX());
        recordedPixel.setY(pixelDto.getY());
        recordedPixel.setColor(pixelDto.getColor());
        recordedPixelRepository.save(recordedPixel);
    }

}
