package com.wtw;

import com.google.common.base.Preconditions;
import com.wtw.compression.CompressionManager;
import com.wtw.compression.TimeSeriesCompressor;
import com.wtw.detectors.GestureDetector;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostFilterEvent;
import com.wtw.event.events.RecordedTimeSeriesEvent;
import com.wtw.event.events.StartCompressionEvent;
import com.wtw.event.events.StartFilteringEvent;
import com.wtw.filters.Filter;
import com.wtw.timeseries.TimeSeries;

import java.util.ArrayList;

public class Device {

    private final EventBus eventBus = new EventBus();
    private ArrayList<Filter> filters = new ArrayList<Filter>();
    private GestureDetector gestureDetector = null;
    private CompressionManager compressionManager = new CompressionManager(eventBus);

    public Device() {
        this.compressionManager.start();
    }

    public void newMeasurement(float[] values, long time) {
        StartFilteringEvent startFilteringEvent = new StartFilteringEvent();
        this.eventBus.post(startFilteringEvent);
        float[] originalValues = values.clone();
        float[] filteredValues = values.clone();
        if (!startFilteringEvent.isCancelled()) {
            for (Filter filter : this.filters) {
                filteredValues = filter.filter(filteredValues);
            }
            PostFilterEvent postFilterEvent = new PostFilterEvent(originalValues, filteredValues);
            this.eventBus.post(postFilterEvent);
        }

        this.gestureDetector.newMeasurement(values, time);
    }

    public void measuredSeries(TimeSeries timeSeries) {
        this.eventBus.post(new RecordedTimeSeriesEvent(timeSeries));
    }

    public Device setGestureDetector(GestureDetector gestureDetector) {
        Preconditions.checkNotNull(gestureDetector);
        gestureDetector.setDevice(this);
        this.gestureDetector = gestureDetector;
        return this;
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
        this.compressionManager.addCompressor(compressor);
        return this;
    }

}
