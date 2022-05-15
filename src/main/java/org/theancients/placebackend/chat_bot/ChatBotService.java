package org.theancients.placebackend.chat_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatBotService {

    @Autowired
    private ChatBotRepository chatBotRepository;

    private Map<String, String> availableChatBots = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 1000)
    private void refreshBotState() {
        Instant now = Instant.now();
        List<ChatBot> chatBots = chatBotRepository.findAll();
        for (ChatBot chatBot : chatBots) {
            long lastPing = Duration.between(chatBot.getLastPing(), now).getSeconds();
            if (lastPing >= 3) {
                chatBot.setStatus(0);
                chatBotRepository.save(chatBot);
            }
        }
    }

    public void updateStatus(ChatBotStatusRequestDto request) {
        if (request == null
                || request.getUsername() == null || request.getUsername().isBlank()
                || request.getServer() == null || request.getServer().isBlank()) {
            return;
        }
        ChatBot chatBot = chatBotRepository.findByUsernameAndServer(request.getUsername(), request.getServer()).orElse(new ChatBot());
        chatBot.setUsername(request.getUsername());
        chatBot.setServer(request.getServer());
        chatBot.setStatus(1);
        chatBot.setLastPing(Instant.now());
        chatBotRepository.save(chatBot);
    }

    public Map<String, String> getAvailableChatBots() {
        Map<String, String> map = new HashMap<>();
        //map.put("2b2t", "OurPlace");
        map.put("archive", "OurPlace");
        return map;
        //return availableChatBots;
    }
}
