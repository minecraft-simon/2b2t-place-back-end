package org.theancients.placebackend.bot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotRepository extends JpaRepository<Bot, Long> {
    Optional<Bot> findByUsername(String username);
}
