package org.theancients.placebackend.chat_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    @Autowired
    private ChatBotRepository chatBotRepository;

    public void updateStatus(ChatBotStatusRequestDto request) {
        if (request == null
                || request.getUsername() == null || request.getUsername().isBlank()
                || request.getServer() == null || request.getServer().isBlank()) {
            return;
        }

    }

}
