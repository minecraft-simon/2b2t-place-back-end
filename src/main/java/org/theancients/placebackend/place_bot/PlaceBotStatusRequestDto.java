package org.theancients.placebackend.place_bot;

import java.util.Arrays;

public class PlaceBotStatusRequestDto {

    private String username;
    private int status;
    private int[] inventory;
    private double[] position;
    private boolean chatBotEnabled;

    @Override
    public String toString() {
        return "PlaceBotStatusRequestDto{" +
                "username='" + username + '\'' +
                ", status=" + status +
                ", inventory=" + Arrays.toString(inventory) +
                ", position=" + Arrays.toString(position) +
                ", chatBotEnabled=" + chatBotEnabled +
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

    public boolean isChatBotEnabled() {
        return chatBotEnabled;
    }

    public void setChatBotEnabled(boolean chatBotEnabled) {
        this.chatBotEnabled = chatBotEnabled;
    }

}
