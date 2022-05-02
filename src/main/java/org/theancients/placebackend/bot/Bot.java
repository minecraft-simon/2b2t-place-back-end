package org.theancients.placebackend.bot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private int status;
    private String inventory;
    private String position;
    private Instant lastPing;

    public Bot() {

    }

    public Bot(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", inventory='" + inventory + '\'' +
                ", position='" + position + '\'' +
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

    public Instant getLastPing() {
        return lastPing;
    }

    public void setLastPing(Instant lastPing) {
        this.lastPing = lastPing;
    }

}
