package org.theancients.placebackend.place_bot;

public class PlaceBotPosition {

    private double x;
    private double y;
    private double rotation;

    public PlaceBotPosition() {
    }

    public PlaceBotPosition(double[] position) {
        this.rotation = position[2];
        this.x = clamp(position[0]);
        this.y = clamp(position[1]);
    }

    private double clamp(double value) {
        if (value < 0) {
            value = 0;
            this.rotation = Double.NaN;
        }
        if (value > 128) {
            value = 128;
            this.rotation = Double.NaN;
        }
        return value;
    }

    @Override
    public String toString() {
        return "PlaceBotPosition{" +
                "x=" + x +
                ", y=" + y +
                ", rotation=" + rotation +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

}
