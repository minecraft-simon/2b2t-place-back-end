package org.theancients.placebackend.chat_bot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {
    Optional<ChatBot> findByUsernameAndServer(String username, String server);
}
