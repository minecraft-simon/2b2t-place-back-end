package org.theancients.placebackend.data_endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.highlight.HighlightService;
import org.theancients.placebackend.job.JobRepository;
import org.theancients.placebackend.place_bot.PlaceBotService;
import org.theancients.placebackend.player.PlayerRepository;
import org.theancients.placebackend.player.PlayerService;

@Service
public class DataService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private HighlightService highlightService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlaceBotService placeBotService;

    public CurrentStateDto getCurrentState() {
        CurrentStateDto currentStateDto = new CurrentStateDto();
        currentStateDto.setJobs(jobRepository.findAll());
        currentStateDto.setHighlights(highlightService.getHighlightsForVisualization());
        currentStateDto.setAuthenticatedPlayers((int) playerRepository.count());
        currentStateDto.setCooldownSeconds(playerService.getCooldownSeconds());
        currentStateDto.setBotsActive(placeBotService.getActiveBotIds().size());
        return currentStateDto;
    }

}
