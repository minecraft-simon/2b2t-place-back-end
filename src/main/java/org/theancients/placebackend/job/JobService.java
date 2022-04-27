package org.theancients.placebackend.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.pixel_grid.PixelDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Job> jobs = findClosestJobs(posX, posY);
        for (Job job : jobs) {
            job.setBotId(botId);
        }
        jobRepository.saveAll(jobs);
        return jobs;
    }

    private List<Job> findClosestJobs(int posX, int posY) {
        List<Job> unassignedJobs = jobRepository.findAllByBotId(0);
        List<Job> sortedJobs = sortJobsByDistance(unassignedJobs, posX, posY);
        sortedJobs = sortedJobs.stream().limit(10).collect(Collectors.toList()); //find x closest jobs
        sortedJobs = sortByOptimalWalkingPath(sortedJobs);// get the shortest route through the jobs

        return sortedJobs;
    }

    private List<JobDto> convertToJobDto(List<Job> jobs) {
        List<JobDto> jobDtos = new ArrayList<>();
        for (Job job : jobs) {
            jobDtos.add(new JobDto(job.getId(), job.getX(), job.getY(), job.getColor()));
        }
        return jobDtos;
    }

    private Job findClosestJob(List<Job> jobs, int posX, int posY) {
        List<Job> sorted = sortJobsByDistance(jobs, posX, posY);
        if (sorted.isEmpty()) {
            return null;
        } else {
            return sorted.get(0);
        }
    }

    private List<Job> sortJobsByDistance(List<Job> jobs, int posX, int posY) {
        if (jobs.size() <= 1) {
            return jobs;
        }
        List<JobWithDistance> jobsWithDistance = new ArrayList<>();
        for (Job job : jobs) {
            int distance = squareDistance(job, posX, posY);
            JobWithDistance jobWithDistance = new JobWithDistance(job, distance);
            jobsWithDistance.add(jobWithDistance);
        }

        Collections.sort(jobsWithDistance);

        List<Job> sortedJobs = new ArrayList<>();
        for (JobWithDistance jobWithDistance : jobsWithDistance) {
            sortedJobs.add(jobWithDistance.getJob());
        }
        return sortedJobs;
    }

    private int squareDistance(Job job, int x, int y) {
        return (int) (Math.pow(job.getX() - x, 2) + Math.pow(job.getY() - y, 2));
    }

    private List<Job> sortByOptimalWalkingPath(List<Job> jobs) {
        if (jobs.size() <= 1) {
            return jobs;
        }

        List<Job> sortedJobs = new ArrayList<>();
        sortedJobs.add(jobs.remove(0));
        do {
            Job lastJob = sortedJobs.get(sortedJobs.size() - 1);
            Job closestJob = findClosestJob(jobs, lastJob.getX(), lastJob.getY());
            jobs.remove(closestJob);
            sortedJobs.add(closestJob);
        } while (!jobs.isEmpty());
        return sortedJobs;
    }

}
