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

    public Player getPlayer(String playerName) {
        return playerRepository.findByName(playerName).orElse(null);
    }

    public Player createOrGetPlayer(String playerName) {
        Optional<Player> optionalPlayer = playerRepository.findByName(playerName);
        if (optionalPlayer.isEmpty()) {
            Instant now = Instant.now();
            Player player = new Player();
            player.setName(playerName);
            player.setFirstSeen(now);
            player.setLastSeen(now);
            return playerRepository.save(player);
        } else {
            return optionalPlayer.get();
        }
    }

    public void startCooldown(String playerName) {
        Player player = getPlayer(playerName);
        int cooldownSeconds = getCooldownSeconds();
        int customCooldown = player.getCustomCooldown();
        if (customCooldown > 0) {
            cooldownSeconds = customCooldown;
        }
        Instant cooldownEnd = Instant.now().plusSeconds(cooldownSeconds);
        playerCooldown.put(player.getName(), cooldownEnd);
    }

    public int getCooldownSeconds() {
        return cooldownActive() ? settingService.getInt("cooldown_seconds", 59) : 0;
    }

    public int getCooldownSeconds(String playerName) {
        Player player = getPlayer(playerName);
        if (player == null) {
            return getCooldownSeconds();
        }
        int customCooldown = player.getCustomCooldown();
        if (customCooldown > 0) {
            return customCooldown;
        }
        return getCooldownSeconds();
    }

    public Instant getCooldownEnd(Player player) {
        Instant cooldownEnd = playerCooldown.get(player.getName());
        if (cooldownEnd == null || !cooldownActive() || cooldownEnd.isBefore(Instant.now())) {
            return Instant.ofEpochSecond(0L);
        }
        return cooldownEnd;
    }

    public Instant getCooldownEnd(String playerName) {
        Player player = getPlayer(playerName);
        if (player != null) {
            return getCooldownEnd(player);
        }
        return Instant.ofEpochSecond(0);
    }

    public int getCooldownSecondsLeft(String username) {
        Instant cooldownEnd = getCooldownEnd(username);
        long secondsLeft = Duration.between(Instant.now(), cooldownEnd).getSeconds();
        return (int) Math.max(secondsLeft, 0);
    }

    public int getCooldownSecondsLeft(Player player) {
        Instant cooldownEnd = getCooldownEnd(player);
        long secondsLeft = Duration.between(Instant.now(), cooldownEnd).getSeconds();
        return (int) Math.max(secondsLeft, 0);
    }

    public boolean playerHasCooldown(Player player) {
        return getCooldownEnd(player).isAfter(Instant.now());
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

    public String getCooldownMessage(Player player) {
        if (player.getAccountLimitation() == 1) {
            return "This account has an increased cooldown because it has been identified as a bot that was used too aggressively. If you think this is unjustified, message us on Discord: https://discord.com/invite/AtkWp8DBtm";
        }
        if (player.getAccountLimitation() == 2) {
            return "This account has an increased cooldown because it has been identified as a bot that is not known to the admin team. If you think this is unjustified, message us on Discord: https://discord.com/invite/AtkWp8DBtm";
        }
        return "";
    }


}
