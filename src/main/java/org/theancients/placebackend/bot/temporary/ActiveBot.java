package org.theancients.placebackend.bot.temporary;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

public class ActiveBot {

    private long id;
    private String username;
    private int status;
    private int[] inventory;
    private double[] position;
    private Instant lastPing;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveBot activeBot = (ActiveBot) o;
        return id == activeBot.id && Objects.equals(username, activeBot.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "ActiveBot{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", inventory=" + Arrays.toString(inventory) +
                ", position=" + Arrays.toString(position) +
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

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

}
