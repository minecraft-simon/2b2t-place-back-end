package org.theancients.placebackend.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    private static final Object LOCK = new Object();

    public void createJob(PixelDto pixelDto) {
        int posId = pixelDto.getX() * 128 + pixelDto.getY() + 1;
        Job job = jobRepository.findById(posId).orElse(new Job());
        job.setId(posId);
        job.setX(pixelDto.getX());
        job.setY(pixelDto.getY());
        job.setColor(pixelDto.getColor());
        jobRepository.save(job);
    }

    public void createJobs(List<PixelDto> pixelDtos) {
        for (PixelDto pixelDto : pixelDtos) {
            createJob(pixelDto);
        }
    }

    public List<JobDto> getJobsForBot(long botId, int posX, int posY) {
        List<Job> existingJobs = jobRepository.findAllByBotId(botId);
        if (!existingJobs.isEmpty()) {
            return convertToJobDto(existingJobs);
        } else {
            return convertToJobDto(assignNewJobs(botId, posX, posY));
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

    private synchronized List<Job> assignNewJobs(long botId, int posX, int posY) {
        /*
        List<Job> unassignedJobs = jobRepository.findAllByBotId(0);
        List<Job> assignedJobs = new ArrayList<>();
        int count = 0;
        for (Job job : unassignedJobs) {
            job.setBotId(botId);
            assignedJobs.add(job);
            count++;
            if (count >= 10) {
                break;
            }
        }

         */
        findClosestJobs(posX, posY);
        //jobRepository.saveAll(assignedJobs);
        return new ArrayList<>();
    }

    private void findClosestJobs(int posX, int posY) {
        List<JobWithDistance> jobsWithDistance = new ArrayList<>();
        List<Job> unassignedJobs = jobRepository.findAllByBotId(0);
        for (Job job : unassignedJobs) {
            int distance = squareDistance(job, posX, posY);
            JobWithDistance jobWithDistance = new JobWithDistance(job, distance);
            jobsWithDistance.add(jobWithDistance);
        }

        Collections.sort(jobsWithDistance);
        // loop through list, find a maximum of 10 closest jobs
        List<Job> jobsToAssign = new ArrayList<>();
        for (int i = 0; i < jobsWithDistance.size(); i++) {
            jobsToAssign.add(jobsWithDistance.get(i).getJob());
            if (i >= 9) {
                break;
            }
        }


    }

    private List<JobDto> convertToJobDto(List<Job> jobs) {
        List<JobDto> jobDtos = new ArrayList<>();
        for (Job job : jobs) {
            jobDtos.add(new JobDto(job.getId(), job.getX(), job.getY(), job.getColor()));
        }
        return jobDtos;
    }

    private int squareDistance(Job job, int x, int y) {
        return (int) (Math.pow(job.getX() - x, 2) + Math.pow(job.getY() - y, 2));
    }

}
