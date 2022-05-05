package org.theancients.placebackend.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.setting.SettingService;

import java.time.Instant;
import java.util.Map;
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

    public Instant startCooldown(String user) {
        Instant cooldownEnd = Instant.now().plusSeconds(getCooldownSeconds());
        playerCooldown.put(user, cooldownEnd);
        return cooldownEnd;
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

    public boolean playerHasCooldown(String username) {
        return getCooldownEnd(username).isAfter(Instant.now());
    }

    private boolean cooldownActive() {
        return settingService.getBoolean("cooldown_active", false);
    }



}
