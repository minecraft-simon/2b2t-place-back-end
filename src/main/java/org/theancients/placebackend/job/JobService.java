package org.theancients.placebackend.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public void createJob(int pos, PixelDto pixelDto) {
        Job job = jobRepository.findById(pos).orElse(new Job());
        job.setId(pos);
        job.setX(pixelDto.getX());
        job.setY(pixelDto.getY());
        job.setColor(pixelDto.getColor());
        jobRepository.save(job);
    }

}
