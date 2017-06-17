package com.wtw.timeseries;

import com.google.common.base.Preconditions;
import lombok.Getter;

public class TimeSeriesPoint {

    @Getter
    private float[] dimensions;

    @Getter
    private long time;

    public TimeSeriesPoint() {
        this.dimensions = new float[0];
        this.time = System.nanoTime();
    }

    public TimeSeriesPoint(float[] dimensions) {
        this.dimensions = dimensions;
        this.time = System.nanoTime();
    }

    public TimeSeriesPoint(float[] dimensions, long time) {
        this.dimensions = dimensions;
        this.time = time;
    }

    public TimeSeriesPoint(TimeSeriesPoint point) {
        this.dimensions = point.getDimensions();
        this.time = point.getTime();
    }

    public int size() {
        Preconditions.checkNotNull(this.dimensions);

        return this.dimensions.length;
    }

    public float getDimension(int index) {
        Preconditions.checkPositionIndex(index, this.size());

        return dimensions[index];
    }

    public TimeSeriesPoint averagePoints(TimeSeriesPoint timeSeriesPoint) {
        Preconditions.checkArgument(timeSeriesPoint.size() == this.size(), "TimeSeriesPoint must have same number of dimensions to average.");
        float[] newPoints = new float[this.size()];
        for (int i = 0; i < this.size(); ++i) {
            newPoints[i] = (this.dimensions[i] + timeSeriesPoint.getDimension(i)) / 2;
        }
        long newTime = (this.time + timeSeriesPoint.getTime()) / 2;
        return new TimeSeriesPoint(newPoints, newTime);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("(");

        for (int i = 0; i < this.size(); ++i) {
            stringBuilder.append(dimensions[i])
                    .append(",");
        }
        stringBuilder.append(this.time);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
