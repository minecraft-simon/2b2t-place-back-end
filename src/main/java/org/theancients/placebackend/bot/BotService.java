package org.theancients.placebackend.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.authentication.PendingAuthentication;
import org.theancients.placebackend.bot.temporary.ActiveBot;
import org.theancients.placebackend.job.JobDto;
import org.theancients.placebackend.job.JobService;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BotService {

    @Autowired
    private BotRepository botRepository;

    @Autowired
    private JobService jobService;

    private static final Object LOCK = new Object();

    private Set<ActiveBot> activeBots = new HashSet<>();

    @Scheduled(fixedRate = 1000)
    private void processInactiveBots() {
        removeInactiveBots();
        flagInactiveBotsInDatabase();
    }

    private void removeInactiveBots() {
        synchronized (LOCK) {
            Set<ActiveBot> inactiveBots = new HashSet<>();
            Instant now = Instant.now();
            for (ActiveBot activeBot : activeBots) {
                long lastPing = Duration.between(activeBot.getLastPing(), now).getSeconds();
                if (lastPing >= 3 && activeBot.getStatus() > 0) {
                    activeBot.setStatus(0);
                    inactiveBots.add(activeBot);
                }
            }
            activeBots.removeAll(inactiveBots);
        }
    }

    private void flagInactiveBotsInDatabase() {
        List<Bot> bots = botRepository.findAll();
        List<Bot> inactiveBots = new ArrayList<>();
        Instant now = Instant.now();
        for (Bot bot : bots) {
            long lastPing = Duration.between(bot.getLastPing(), now).getSeconds();
            if (lastPing >= 3 && bot.getStatus() > 0) {
                bot.setStatus(0);
                inactiveBots.add(bot);
            }
        }
        botRepository.saveAll(inactiveBots);
    }

    public BotStatusResponseDto updateStatus(BotStatusRequestDto request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            return null;
        }

        long botId = updateBotDatabase(request);
        updateActiveBots(botId, request);

        BotStatusResponseDto response = new BotStatusResponseDto();
        int posX = (int) request.getPosition()[0];
        int posY = (int) request.getPosition()[1];
        List<JobDto> jobsForBot = jobService.getJobsForBot(botId, posX, posY);
        response.setJobs(jobsForBot);

        return response;
    }

    private long updateBotDatabase(BotStatusRequestDto request) {
        String username = request.getUsername();
        Bot bot = botRepository.findByUsername(username).orElse(new Bot(username));
        bot.setStatus(request.getStatus());
        bot.setInventory(Arrays.toString(request.getInventory()));
        bot.setPosition(Arrays.toString(request.getPosition()));
        bot.setLastPing(Instant.now());
        return botRepository.save(bot).getId();
    }

    private void updateActiveBots(long botId, BotStatusRequestDto request) {
        ActiveBot activeBot = new ActiveBot();
        activeBot.setId(botId);
        activeBot.setUsername(request.getUsername());
        activeBot.setStatus(request.getStatus());
        activeBot.setInventory(request.getInventory());
        activeBot.setPosition(request.getPosition());
        activeBot.setLastPing(Instant.now());
        synchronized (LOCK) {
            activeBots.remove(activeBot);
            activeBots.add(activeBot);
        }
    }

}
