package org.theancients.placebackend.recorded_pixel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;
import org.theancients.placebackend.player.PlayerService;

import java.util.*;

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

    public List<ScoreDto> getLeaderboard() {
        Map<Long, Integer> placedPixels = countPixels();
        Map<Long, String> playerMap = playerService.getPlayerMap();
        List<ScoreDto> leaderboard = new ArrayList<>();
        placedPixels.forEach((playerId, count) -> {
            ScoreDto scoreDto = new ScoreDto();
            scoreDto.setPlayerName(playerMap.get(playerId));
            scoreDto.setScore(count);
            leaderboard.add(scoreDto);
        });
        Collections.sort(leaderboard);
        Collections.reverse(leaderboard);
        return leaderboard.subList(0, Math.min(leaderboard.size(), 10));
    }

    private Map<Long, Integer> countPixels() {
        List<RecordedPixel> recordedPixels = recordedPixelRepository.findAll();
        Map<Long, Integer> counts = new HashMap<>();
        for (RecordedPixel recordedPixel : recordedPixels) {
            long playerId = recordedPixel.getPlayerId();
            if (counts.containsKey(playerId)) {
                counts.put(playerId, counts.get(playerId) + 1);
            } else {
                counts.put(playerId, 1);
            }
        }
        return counts;
    }

}
