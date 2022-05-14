package org.theancients.placebackend.place_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.chat_bot.ChatBotService;
import org.theancients.placebackend.chat_bot.ChatBotStatusRequestDto;
import org.theancients.placebackend.place_bot.temporary.ActivePlaceBot;
import org.theancients.placebackend.job.JobDto;
import org.theancients.placebackend.job.JobService;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlaceBotService {

    @Autowired
    private PlaceBotRepository placeBotRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private ChatBotService chatBotService;

    private static final Object LOCK = new Object();

    private Set<ActivePlaceBot> activePlaceBots = new HashSet<>();

    @Scheduled(fixedRate = 1000)
    private void processInactiveBots() {
        removeInactiveBots();
        flagInactiveBotsInDatabase();
    }

    private void removeInactiveBots() {
        synchronized (LOCK) {
            Set<ActivePlaceBot> inactivePlaceBots = new HashSet<>();
            Instant now = Instant.now();
            for (ActivePlaceBot activePlaceBot : activePlaceBots) {
                long lastPing = Duration.between(activePlaceBot.getLastPing(), now).getSeconds();
                if (lastPing >= 3) {
                    activePlaceBot.setStatus(0);
                    inactivePlaceBots.add(activePlaceBot);
                }
            }
            activePlaceBots.removeAll(inactivePlaceBots);
        }
    }

    private void flagInactiveBotsInDatabase() {
        List<PlaceBot> placeBots = placeBotRepository.findAll();
        List<PlaceBot> inactivePlaceBots = new ArrayList<>();
        Instant now = Instant.now();
        for (PlaceBot placeBot : placeBots) {
            long lastPing = Duration.between(placeBot.getLastPing(), now).getSeconds();
            if (lastPing >= 3 && placeBot.getStatus() > 0) {
                placeBot.setStatus(0);
                inactivePlaceBots.add(placeBot);
            }
        }
        placeBotRepository.saveAll(inactivePlaceBots);
    }

    public PlaceBotStatusResponseDto updateStatus(PlaceBotStatusRequestDto request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            return null;
        }

        if (request.isChatBotEnabled()) {
            ChatBotStatusRequestDto requestDto = new ChatBotStatusRequestDto();
            requestDto.setUsername(request.getUsername());
            requestDto.setServer("2b2t.org");
            chatBotService.updateStatus(requestDto);
        }

        long botId = updateBotDatabase(request);
        updateActiveBots(botId, request);

        PlaceBotStatusResponseDto response = new PlaceBotStatusResponseDto();
        int posX = (int) request.getPosition()[0];
        int posY = (int) request.getPosition()[1];
        List<JobDto> jobsForBot = jobService.getJobsForBot(botId, posX, posY);
        response.setJobs(jobsForBot);

        return response;
    }

    public Map<String, PlaceBotPosition> getBotPositions() {
        Map<String, PlaceBotPosition> botPositions = new ConcurrentHashMap<>();
        synchronized (LOCK) {
            for (ActivePlaceBot activePlaceBot : activePlaceBots) {
                botPositions.put(activePlaceBot.getUsername(), activePlaceBot.getPlaceBotPosition());
            }
        }
        return botPositions;
    }

    private long updateBotDatabase(PlaceBotStatusRequestDto request) {
        String username = request.getUsername();
        PlaceBot placeBot = placeBotRepository.findByUsername(username).orElse(new PlaceBot(username));
        placeBot.setStatus(request.getStatus());
        placeBot.setInventory(Arrays.toString(request.getInventory()));
        placeBot.setPosition(Arrays.toString(request.getPosition()));
        placeBot.setLastPing(Instant.now());
        placeBot.setChatBotEnabled(request.isChatBotEnabled());
        return placeBotRepository.save(placeBot).getId();
    }

    private void updateActiveBots(long botId, PlaceBotStatusRequestDto request) {
        ActivePlaceBot activePlaceBot = new ActivePlaceBot();
        activePlaceBot.setId(botId);
        activePlaceBot.setUsername(request.getUsername());
        activePlaceBot.setStatus(request.getStatus());
        activePlaceBot.setInventory(request.getInventory());
        activePlaceBot.setPlaceBotPosition(new PlaceBotPosition(request.getPosition()));
        activePlaceBot.setLastPing(Instant.now());
        synchronized (LOCK) {
            activePlaceBots.remove(activePlaceBot);
            activePlaceBots.add(activePlaceBot);
        }
    }

}
