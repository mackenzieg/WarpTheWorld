package com.wtw.detectors;

import com.wtw.Device;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesPoint;

public class DefaultGestureDetector extends GestureDetector {
    private TimeSeries timeSeries;

    public DefaultGestureDetector(Device device) {
        super(device);
        this.timeSeries = new TimeSeries();
    }

    @Override
    public void newMeasurement(float[] values, long time) {

    }
}
