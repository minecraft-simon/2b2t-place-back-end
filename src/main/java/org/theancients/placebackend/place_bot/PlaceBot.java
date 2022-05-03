package org.theancients.placebackend.place_bot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class PlaceBot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private int status;
    private String inventory;
    private String position;
    private boolean chatBotEnabled;
    private Instant lastPing;

    public PlaceBot() {

    }

    public PlaceBot(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "PlaceBot{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", inventory='" + inventory + '\'' +
                ", position='" + position + '\'' +
                ", chatBotEnabled=" + chatBotEnabled +
                ", lastPing=" + lastPing +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isChatBotEnabled() {
        return chatBotEnabled;
    }

    public void setChatBotEnabled(boolean chatBotEnabled) {
        this.chatBotEnabled = chatBotEnabled;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

}
