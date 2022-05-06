package org.theancients.placebackend.pixel_grid;

import java.time.Instant;

public class PixelUpdateResponseDto {

    private int x;
    private int y;
    private byte color;
    private int cooldownSeconds;

    public PixelUpdateResponseDto(PixelDto pixelDto, int cooldownSeconds) {
        x = pixelDto.getX();
        y = pixelDto.getY();
        color = pixelDto.getColor();
        this.cooldownSeconds = cooldownSeconds;
    }

    @Override
    public String toString() {
        return "PixelUpdateResponse{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                ", cooldownSeconds=" + cooldownSeconds +
                '}';
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

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

}
