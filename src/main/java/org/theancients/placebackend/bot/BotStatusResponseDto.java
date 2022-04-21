package org.theancients.placebackend.bot;

import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.List;

public class BotStatusResponseDto {

    private List<int[]> jobs = new ArrayList<>();

    public List<int[]> getJobs() {
        return jobs;
    }

    public void setJobs(List<int[]> jobs) {
        this.jobs = jobs;
    }

}
