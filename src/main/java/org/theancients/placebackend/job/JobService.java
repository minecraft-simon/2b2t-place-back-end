package org.theancients.placebackend.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.List;

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

    public List<JobDto> getJobsForBot(long botId) {
        List<Job> existingJobs = jobRepository.findAllByBotId(botId);
        if (!existingJobs.isEmpty()) {
            return convertToJobDto(existingJobs);
        } else {
            return convertToJobDto(assignNewJobs(botId));
        }
    }

    private List<Job> assignNewJobs(long botId) {
        List<Job> unassignedJobs = jobRepository.findAllByBotId(0);
        for (Job unassignedJob : unassignedJobs) {
            unassignedJob.setBotId(botId);
        }
        jobRepository.saveAll(unassignedJobs);
        return unassignedJobs;
    }

    private List<JobDto> convertToJobDto(List<Job> jobs) {
        List<JobDto> jobDtos = new ArrayList<>();
        for (Job job : jobs) {
            jobDtos.add(new JobDto(job.getId(), job.getX(), job.getY(), job.getColor()));
        }
        return jobDtos;
    }

}
