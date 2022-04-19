package org.theancients.placebackend.bot;

import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.List;

public class BotStatusResponseDto {

    private List<PixelDto> jobs = new ArrayList<>();

    public List<PixelDto> getJobs() {
        return jobs;
    }

    public void setJobs(List<PixelDto> jobs) {
        this.jobs = jobs;
    }
}
