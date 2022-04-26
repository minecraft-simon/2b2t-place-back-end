package org.theancients.placebackend.job;

public class JobDto {

    private int jobId;
    private int x;
    private int y;
    private byte color;

    public JobDto() {
    }

    public JobDto(int jobId, int x, int y, byte color) {
        this.jobId = jobId;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public String toString() {
        return "JobDto{" +
                "jobId=" + jobId +
                ", x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

}
