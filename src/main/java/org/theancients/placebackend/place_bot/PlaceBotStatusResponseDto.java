package org.theancients.placebackend.place_bot;

import org.theancients.placebackend.job.JobDto;

import java.util.List;

public class PlaceBotStatusResponseDto {

    private List<JobDto> jobs;

    public List<JobDto> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDto> jobs) {
        this.jobs = jobs;
    }

}
