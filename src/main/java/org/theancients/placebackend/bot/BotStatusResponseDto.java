package org.theancients.placebackend.bot;

import org.theancients.placebackend.job.JobDto;
import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.List;

public class BotStatusResponseDto {

    private List<JobDto> jobs;

    public List<JobDto> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDto> jobs) {
        this.jobs = jobs;
    }

}
