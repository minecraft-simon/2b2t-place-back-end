package org.theancients.placebackend.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.setting.SettingService;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SettingService settingService;

    private Map<String, Instant> playerCooldown = new ConcurrentHashMap<>();

    public long getPlayerId(String name) {
        Instant now = Instant.now();
        Player player = playerRepository.findByName(name).orElse(new Player(now));
        player.setName(name);
        player.setLastSeen(now);
        return playerRepository.save(player).getId();
    }

    public int startCooldown(String user) {
        int cooldownSeconds = getCooldownSeconds();
        Instant cooldownEnd = Instant.now().plusSeconds(cooldownSeconds);
        playerCooldown.put(user, cooldownEnd);
        return cooldownSeconds;
    }

    public int getCooldownSeconds() {
        return cooldownActive() ? settingService.getInt("cooldown_seconds", 59) : 0;
    }

    public Instant getCooldownEnd(String username) {
        if (username == null) {
            return Instant.ofEpochSecond(0L);
        }
        Instant cooldownEnd = playerCooldown.get(username);
        if (cooldownEnd == null || !cooldownActive() || cooldownEnd.isBefore(Instant.now())) {
            return Instant.ofEpochSecond(0L);
        }
        return cooldownEnd;
    }

    public int getCooldownSecondsLeft(String username) {
        Instant cooldownEnd = getCooldownEnd(username);
        long secondsLeft = Duration.between(Instant.now(), cooldownEnd).getSeconds();
        return (int) Math.max(secondsLeft, 0);
    }

    public boolean playerHasCooldown(String username) {
        return getCooldownEnd(username).isAfter(Instant.now());
    }

    private boolean cooldownActive() {
        return settingService.getBoolean("cooldown_active", false);
    }

    public String getPlayerNameById(long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get().getName();
        } else {
            return "";
        }
    }

    public boolean isBanned(String playerName) {
        Optional<Player> optionalPlayer = playerRepository.findByName(playerName);
        return optionalPlayer.map(Player::isBanned).orElse(false);
    }


}
