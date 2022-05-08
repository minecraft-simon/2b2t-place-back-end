package org.theancients.placebackend.place_bot.temporary;

import org.theancients.placebackend.place_bot.PlaceBotPosition;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

public class ActivePlaceBot {

    private long id;
    private String username;
    private int status;
    private int[] inventory;
    private PlaceBotPosition placeBotPosition;
    private Instant lastPing;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivePlaceBot activePlaceBot = (ActivePlaceBot) o;
        return id == activePlaceBot.id && Objects.equals(username, activePlaceBot.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "ActivePlaceBot{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", inventory=" + Arrays.toString(inventory) +
                ", placeBotPosition=" + placeBotPosition +
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

    public int[] getInventory() {
        return inventory;
    }

    public void setInventory(int[] inventory) {
        this.inventory = inventory;
    }

    public PlaceBotPosition getPlaceBotPosition() {
        return placeBotPosition;
    }

    public void setPlaceBotPosition(PlaceBotPosition placeBotPosition) {
        this.placeBotPosition = placeBotPosition;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

}
