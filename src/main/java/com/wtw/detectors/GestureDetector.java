package com.wtw.detectors;

import com.wtw.Device;
import com.wtw.event.EventBus;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesPoint;
import lombok.AllArgsConstructor;
import lombok.Setter;

public abstract class GestureDetector {

    @Setter
    private Device device;

    public GestureDetector(Device device) {
        this.device = device;
    }

    public abstract void newMeasurement(float[] values, long time);

    public final void recordedGesture(TimeSeries timeSeries) {
        this.device.measuredSeries(timeSeries);
    }

}
