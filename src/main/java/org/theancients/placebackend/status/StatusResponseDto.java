package org.theancients.placebackend.status;

import org.theancients.placebackend.pixel_grid.PixelGrid;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StatusResponseDto {

    private List<Point> highlights = new ArrayList<>();
    private PixelGrid pixelGrid;
    private String identity;
    private String authToken;
    private int pollingDelay;
    private int cooldownSeconds;
    private int cooldownSecondsLeft;
    private boolean maintenanceMode;
    private boolean sessionExpired;

    public List<Point> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<Point> highlights) {
        this.highlights = highlights;
    }

    public PixelGrid getPixelGrid() {
        return pixelGrid;
    }

    public void setPixelGrid(PixelGrid pixelGrid) {
        this.pixelGrid = pixelGrid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getPollingDelay() {
        return pollingDelay;
    }

    public void setPollingDelay(int pollingDelay) {
        this.pollingDelay = pollingDelay;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public int getCooldownSecondsLeft() {
        return cooldownSecondsLeft;
    }

    public void setCooldownSecondsLeft(int cooldownSecondsLeft) {
        this.cooldownSecondsLeft = cooldownSecondsLeft;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public boolean isSessionExpired() {
        return sessionExpired;
    }

    public void setSessionExpired(boolean sessionExpired) {
        this.sessionExpired = sessionExpired;
    }

}
