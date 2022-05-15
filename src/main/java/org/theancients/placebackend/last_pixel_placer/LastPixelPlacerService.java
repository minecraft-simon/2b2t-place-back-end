package org.theancients.placebackend.last_pixel_placer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;
import org.theancients.placebackend.player.PlayerService;

import java.awt.*;
import java.util.Optional;

@Service
public class LastPixelPlacerService {

    @Autowired
    private LastPixelPlacerRepository lastPixelPlacerRepository;

    @Autowired
    private PlayerService playerService;

    public void update(String playerName, PixelDto pixelDto) {
        int posId = pixelDto.getX() * 128 + pixelDto.getY() + 1;
        LastPixelPlacer lastPixelPlacer = lastPixelPlacerRepository.findById(posId).orElse(new LastPixelPlacer());
        lastPixelPlacer.setId(posId);
        lastPixelPlacer.setPlayer(playerService.getPlayerId(playerName));
        lastPixelPlacer.setX(pixelDto.getX());
        lastPixelPlacer.setY(pixelDto.getY());
        lastPixelPlacer.setColor(pixelDto.getColor());
        lastPixelPlacerRepository.save(lastPixelPlacer);
    }

    public String getLastPixelPlacer(Point point) {
        if (point == null) {
            return "";
        }
        int x = point.x;
        int y = point.y;
        int posId = x * 128 + y + 1;
        Optional<LastPixelPlacer> optional = lastPixelPlacerRepository.findById(posId);
        if (optional.isPresent()) {
            long playerId = optional.get().getPlayer();
            return playerService.getPlayerNameById(playerId);
        }
        return "";
    }

}
