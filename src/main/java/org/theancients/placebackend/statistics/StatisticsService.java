package org.theancients.placebackend.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.anonymous_session.AnonymousSessionRepository;
import org.theancients.placebackend.anonymous_session.AnonymousSessionService;
import org.theancients.placebackend.highlight.HighlightRepository;
import org.theancients.placebackend.job.JobRepository;
import org.theancients.placebackend.player.PlayerRepository;
import org.theancients.placebackend.recorded_pixel.RecordedPixelRepository;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private AnonymousSessionRepository anonymousSessionRepository;

    @Autowired
    private HighlightRepository highlightRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RecordedPixelRepository recordedPixelRepository;

    @Scheduled(fixedRate = 60000)
    private void record() {
        record("sessions", Math.toIntExact(anonymousSessionRepository.count()));
        record("highlights", Math.toIntExact(highlightRepository.count()));
        record("jobs", Math.toIntExact(jobRepository.count()));
        record("players", Math.toIntExact(playerRepository.count()));
        record("recorded_pixels", Math.toIntExact(recordedPixelRepository.count()));
    }

    private void record(String name, int value) {
        Statistics statistics = new Statistics();
        statistics.setName(name);
        statistics.setValue(value);
        statisticsRepository.save(statistics);
    }

}
