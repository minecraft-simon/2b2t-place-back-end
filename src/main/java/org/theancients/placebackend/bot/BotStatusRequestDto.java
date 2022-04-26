package org.theancients.placebackend.bot;

import org.springframework.stereotype.Controller;

public class BotStatusRequestDto {

    private String username;
    private int status;

    @Override
    public String toString() {
        return "BotStatusRequestDto{" +
                "username='" + username + '\'' +
                ", status=" + status +
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

}
