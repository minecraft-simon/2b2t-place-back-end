package org.theancients.placebackend.bot;

import org.springframework.stereotype.Controller;

public class BotStatusRequestDto {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
