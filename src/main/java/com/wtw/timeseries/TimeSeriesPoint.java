package com.wtw.timeseries;

import com.google.common.base.Preconditions;
import lombok.Getter;

public class TimeSeriesPoint {

    @Getter
    private float[] dimensions;

    @Getter
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

    public int dimensionSize() {
        Preconditions.checkNotNull(this.dimensions);

        return this.dimensions.length;
    }

    public float getDimension(int index) {
        Preconditions.checkPositionIndex(index, this.dimensionSize());

        return dimensions[index];
    }

    public float getDistance(TimeSeriesPoint timeSeriesPoint) {
        return getDistance(timeSeriesPoint.getDimensions());
    }

    public float getDistance(float[] points) {
        Preconditions.checkArgument(this.dimensionSize() == points.length, "Can't compare points with different number of dimensions");
        Preconditions.checkNotNull(this.dimensions);
        float sum = 0.0f;
        for (int i = 0; i < points.length; ++i) {
            sum += Math.pow(this.dimensions[i] - points[i], 2);
        }
        return (float) Math.sqrt(sum);
    }

    @Override
    public String toString() {
        String formatted = "(";
        for (int i = 0; i < this.dimensionSize(); ++i) {
            formatted.concat(dimensions[i] + ",");
        }
        return formatted + this.time + ")";
    }
}
