package org.theancients.placebackend.job;

public class JobWithDistance implements Comparable<JobWithDistance> {

    private Job job;
    private int distance;

    public JobWithDistance() {
    }

    public JobWithDistance(Job job, int distance) {
        this.job = job;
        this.distance = distance;
    }

    @Override
    public int compareTo(JobWithDistance other) {
        return Integer.compare(distance, other.distance);
    }

    @Override
    public String toString() {
        return "JobWithDistance{" +
                "job=" + job +
                ", distance=" + distance +
                "}\n";
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}
