package org.theancients.placebackend.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public long lookUpPlayer(String name) {
        Instant now = Instant.now();
        Player player = playerRepository.findByName(name).orElse(new Player(now));
        player.setName(name);
        player.setLastSeen(now);
        return playerRepository.save(player).getId();
    }

}
