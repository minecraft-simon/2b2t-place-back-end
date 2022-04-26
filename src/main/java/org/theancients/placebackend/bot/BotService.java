package org.theancients.placebackend.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.job.Job;
import org.theancients.placebackend.job.JobDto;
import org.theancients.placebackend.job.JobService;

import java.time.Instant;
import java.util.List;

@Service
public class BotService {

    @Autowired
    private BotRepository botRepository;

    @Autowired
    private JobService jobService;

    public BotStatusResponseDto updateStatus(BotStatusRequestDto request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            return null;
        }
        String username = request.getUsername();
        Bot bot = botRepository.findByUsername(username).orElse(new Bot(username));
        bot.setStatus(request.getStatus());
        bot.setLastPing(Instant.now());
        bot = botRepository.save(bot);

        BotStatusResponseDto response = new BotStatusResponseDto();
        int posX = request.getPosition()[0];
        int posY = request.getPosition()[1];
        List<JobDto> jobsForBot = jobService.getJobsForBot(bot.getId(), posX, posY);
        response.setJobs(jobsForBot);

        return response;
    }

}
