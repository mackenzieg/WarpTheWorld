package com.wtw.detectors;

import com.wtw.distance.DistanceCalculator;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesPoint;

public class DefaultGestureDetector extends GestureDetector {

    public final float SLOPE_THRESHOLD;
    public final long TIME_THRESHOLD;
    private TimeSeries timeSeries;
    private boolean triggered = false;

    private long lastTimeBelowThreshold = System.nanoTime();

    private float[] previous = null;

    public DefaultGestureDetector(DistanceCalculator distanceCalculator) {
        this(distanceCalculator, 3.8f, 500000000);
    }

    public DefaultGestureDetector(DistanceCalculator distanceCalculator, float SLOPE_THRESHOLD, long TIME_THRESHOLD) {
        super(distanceCalculator);
        this.timeSeries = new TimeSeries();
        this.SLOPE_THRESHOLD = SLOPE_THRESHOLD;
        this.TIME_THRESHOLD = TIME_THRESHOLD;
    }

    @Override
    public void newMeasurement(float[] values, long time) {
        long currentTime = System.nanoTime();
        if(this.previous != null) {
            float distance = this.getDistanceCalculator().distance(values, this.previous);
            if(this.triggered) {
                if(this.lastTimeBelowThreshold + this.TIME_THRESHOLD <= currentTime) {
                    this.getDevice().measuredSeries(new TimeSeries(this.timeSeries));
                    this.timeSeries = new TimeSeries();
                    this.triggered = false;
                } else {
                    this.timeSeries.addPoint(new TimeSeriesPoint(values, time));
                }
            } else if(distance >= this.SLOPE_THRESHOLD) {
                this.triggered = true;
                this.timeSeries.addPoint(new TimeSeriesPoint(values, currentTime));
            }

            if(distance >= this.SLOPE_THRESHOLD) {
                this.lastTimeBelowThreshold = currentTime;
            }
        }

        this.previous = values.clone();
    }
}
