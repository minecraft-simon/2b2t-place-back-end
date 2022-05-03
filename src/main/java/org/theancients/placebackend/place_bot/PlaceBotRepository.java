package org.theancients.placebackend.place_bot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceBotRepository extends JpaRepository<PlaceBot, Long> {
    Optional<PlaceBot> findByUsername(String username);
}
