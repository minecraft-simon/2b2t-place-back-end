package org.theancients.placebackend.bot;

public class BotMessageDto {

    private String username;
    private String player;
    private String message;

    @Override
    public String toString() {
        return "BotMessage{" +
                "username='" + username + '\'' +
                ", player='" + player + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
