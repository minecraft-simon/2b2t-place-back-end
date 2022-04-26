package org.theancients.placebackend.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    private static final Object LOCK = new Object();

    public void createJob(int posId, PixelDto pixelDto) {
        Job job = jobRepository.findById(posId).orElse(new Job());
        job.setId(posId);
        job.setX(pixelDto.getX());
        job.setY(pixelDto.getY());
        job.setColor(pixelDto.getColor());
        jobRepository.save(job);
    }

    public void createJobs(List<PixelDto> pixelDtos) {
        List<Job> jobs = new ArrayList<>();
        for (PixelDto pixelDto : pixelDtos) {
            Job job = new Job();
            int posId = pixelDto.getX() * 128 + pixelDto.getY();
            job.setId(posId);
            job.setX(pixelDto.getX());
            job.setY(pixelDto.getY());
            job.setColor(pixelDto.getColor());
            jobs.add(job);
            createJob(posId, pixelDto);
        }
    }

    public List<JobDto> getJobsForBot(long botId) {
        List<Job> existingJobs = jobRepository.findAllByBotId(botId);
        if (!existingJobs.isEmpty()) {
            return convertToJobDto(existingJobs);
        } else {
            return convertToJobDto(assignNewJobs(botId));
        }
    }

    public void jobFinished(JobDto jobDto) {
        Optional<Job> optionalJob = jobRepository.findById(jobDto.getJobId());
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            if (jobDto.getColor() == job.getColor()) {
                jobRepository.delete(job);
            }
        }
    }

    private List<Job> assignNewJobs(long botId) {
        synchronized (LOCK) {
            List<Job> unassignedJobs = jobRepository.findAllByBotId(0);
            int count = 0;
            for (Job unassignedJob : unassignedJobs) {
                unassignedJob.setBotId(botId);
                count++;
                if (count >= 10) {
                    break;
                }
            }
            jobRepository.saveAll(unassignedJobs);
            return unassignedJobs;
        }
    }

    private List<JobDto> convertToJobDto(List<Job> jobs) {
        List<JobDto> jobDtos = new ArrayList<>();
        for (Job job : jobs) {
            jobDtos.add(new JobDto(job.getId(), job.getX(), job.getY(), job.getColor()));
        }
        return jobDtos;
    }

}
