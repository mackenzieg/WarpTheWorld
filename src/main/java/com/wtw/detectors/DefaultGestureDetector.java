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
        if (this.previous != null) {
            float distance = this.getDistanceCalculator().distance(values, previous);
            if (triggered) {
                if (lastTimeBelowThreshold >= TIME_THRESHOLD + time) {
                    this.getDevice().measuredSeries(this.timeSeries);
                    this.timeSeries = new TimeSeries();
                    triggered = false;
                } else {
                    this.timeSeries.addPoint(new TimeSeriesPoint(values, time));
                }
            } else {
                if (distance >= SLOPE_THRESHOLD) {
                    triggered = true;
                    this.timeSeries.addPoint(new TimeSeriesPoint(values, time));
                }
            }

            if (distance >= SLOPE_THRESHOLD) {
                lastTimeBelowThreshold = time;
            }
        }
        previous = values;
    }
}
