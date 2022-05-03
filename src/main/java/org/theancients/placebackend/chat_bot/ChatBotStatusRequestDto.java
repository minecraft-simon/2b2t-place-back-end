package org.theancients.placebackend.chat_bot;

import java.util.Arrays;

public class ChatBotStatusRequestDto {

    private String username;
    private String server;

    @Override
    public String toString() {
        return "ChatBotStatusRequestDto{" +
                "username='" + username + '\'' +
                ", server='" + server + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

}
