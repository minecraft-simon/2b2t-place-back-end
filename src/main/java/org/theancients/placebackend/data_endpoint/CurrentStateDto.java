package org.theancients.placebackend.data_endpoint;

import org.theancients.placebackend.highlight.Highlight;
import org.theancients.placebackend.job.Job;

import java.time.Instant;
import java.util.List;

public class CurrentStateDto {

    private long serverTime = Instant.now().getEpochSecond();
    private List<Job> jobs;
    private List<Highlight> highlights;
    private int authenticatedPlayers;
    private int cooldownSeconds;
    private int botsActive;

    public long getServerTime() {
        return serverTime;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Highlight> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<Highlight> highlights) {
        this.highlights = highlights;
    }

    public int getAuthenticatedPlayers() {
        return authenticatedPlayers;
    }

    public void setAuthenticatedPlayers(int authenticatedPlayers) {
        this.authenticatedPlayers = authenticatedPlayers;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public int getBotsActive() {
        return botsActive;
    }

    public void setBotsActive(int botsActive) {
        this.botsActive = botsActive;
    }
}
