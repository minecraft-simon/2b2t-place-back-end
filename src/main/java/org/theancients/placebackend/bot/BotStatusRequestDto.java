package org.theancients.placebackend.bot;

import org.springframework.stereotype.Controller;

import java.util.Arrays;

public class BotStatusRequestDto {

    private String username;
    private int status;
    private int[] position;

    @Override
    public String toString() {
        return "BotStatusRequestDto{" +
                "username='" + username + '\'' +
                ", status=" + status +
                ", position=" + Arrays.toString(position) +
                '}';
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

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}
