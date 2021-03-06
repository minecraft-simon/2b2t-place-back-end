package org.theancients.placebackend.player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Instant firstSeen;
    private Instant lastSeen;
    private boolean banned;
    private int customCooldown;
    private int accountLimitation; // 0 = none, 1 = known bot, 2 = unknown bot, 3 = restricted symbol

    public Player() {
    }

    public Player(Instant firstSeen) {
        this.firstSeen = firstSeen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(Instant firstSeen) {
        this.firstSeen = firstSeen;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Instant lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public int getCustomCooldown() {
        return customCooldown;
    }

    public void setCustomCooldown(int customCooldown) {
        this.customCooldown = customCooldown;
    }

    public int getAccountLimitation() {
        return accountLimitation;
    }

    public void setAccountLimitation(int accountLimitation) {
        this.accountLimitation = accountLimitation;
    }

}
