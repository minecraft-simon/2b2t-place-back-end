package org.theancients.placebackend.data_endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.theancients.placebackend.last_pixel_placer.LastPixelPlacerService;
import org.theancients.placebackend.recorded_pixel.RecordedPixelService;
import org.theancients.placebackend.recorded_pixel.ScoreDto;
import org.theancients.placebackend.statistics.Statistics;
import org.theancients.placebackend.statistics.StatisticsRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private RecordedPixelService recordedPixelService;

    @GetMapping("current-state")
    @RolesAllowed("ROLE_BOT")
    public CurrentStateDto getCurrentState() {
        return dataService.getCurrentState();
    }

    @GetMapping("leaderboard")
    @RolesAllowed("ROLE_BOT")
    public List<ScoreDto> getLeaderboard() {
        return recordedPixelService.getLeaderboard();
    }

    @GetMapping("statistics")
    @RolesAllowed("ROLE_BOT")
    public List<Statistics> getStatistics() {
        return statisticsRepository.findAll();
    }
}
