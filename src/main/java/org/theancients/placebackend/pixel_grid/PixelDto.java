package org.theancients.placebackend.pixel_grid;

public class PixelDto {

    private int x;
    private int y;
    private byte color;

    public PixelDto() {
    }

    public PixelDto(int x, int y, byte color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public String toString() {
        return "PixelDto{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
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

}
