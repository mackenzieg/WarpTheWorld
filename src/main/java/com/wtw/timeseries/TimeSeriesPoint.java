package com.wtw.timeseries;

public class TimeSeriesPoint {

    private float[] dimensions;

    private long time;

    public TimeSeriesPoint(float[] dimensions, long time) {
        this.dimensions = dimensions;
        this.time = time;
    }

    public TimeSeriesPoint(float[] dimensions) {
        this.dimensions = dimensions;
        this.time = System.nanoTime();
    }

    public TimeSeriesPoint() {
        this.dimensions = new float[0];
        this.time = System.nanoTime();
    }

    public int length() {
        return this.dimensions.length;
    }

    public long getTime() {
        return time;
    }

    public float[] getDimensions() {
        return dimensions;
    }

    public float getDimension(int index) {
        return dimensions[index];
    }
}
