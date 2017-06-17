package com.wtw;

import com.wtw.compression.TimeSeriesCompressor;
import com.wtw.event.Event;
import com.wtw.event.EventBus;
import com.wtw.event.events.StartFilteringEvent;
import com.wtw.filters.Filter;
import com.wtw.timeseries.TimeSeries;

import java.util.ArrayList;

public class Device {

    private ArrayList<Filter> filters = new ArrayList<Filter>();
    private ArrayList<TimeSeriesCompressor> compressors = new ArrayList<TimeSeriesCompressor>();

    private final EventBus eventBus = new EventBus();

    public void newMeasurement(float[] values, long time) {
        StartFilteringEvent startFilteringEvent = new StartFilteringEvent();
        this.eventBus.post(startFilteringEvent);
        if (!startFilteringEvent.isCancelled()) {
            // do filtering
        }
    }

    public void measuredSeries(TimeSeries timeSeries) {

    }

    public Device registerListener(Object listener) {
        this.eventBus.register(listener);
        return this;
    }

    public Device addFilter(Filter filter) {
        this.filters.add(filter);
        return this;
    }

    public Device addCompressor(TimeSeriesCompressor compressor) {
        this.compressors.add(compressor);
        return this;
    }

}
