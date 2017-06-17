package com.wtw;

import com.google.common.base.Preconditions;
import com.wtw.compression.CompressionManager;
import com.wtw.detectors.GestureDetector;
import com.wtw.event.EventBus;
import com.wtw.event.EventHandler;
import com.wtw.event.events.PostCompressionEvent;
import com.wtw.event.events.PostFilterEvent;
import com.wtw.event.events.RecordedTimeSeriesEvent;
import com.wtw.event.events.StartFilteringEvent;
import com.wtw.filters.Filter;
import com.wtw.timeseries.TimeSeries;
import lombok.Getter;

import java.util.ArrayList;

public class BuiltDevice {
    @Getter
    private final EventBus eventBus;
    private ArrayList<Filter> filters = new ArrayList<>();
    @Getter
    private GestureDetector gestureDetector = null;
    @Getter
    private CompressionManager compressionManager;

    public BuiltDevice(EventBus eventBus, ArrayList<Filter> filters, GestureDetector gestureDetector) {
        Preconditions.checkNotNull(gestureDetector, "Must define a gesture detector.");
        this.filters = filters;
        this.gestureDetector = gestureDetector;
        this.eventBus = eventBus;
        this.compressionManager = new CompressionManager(this.eventBus);
        this.eventBus.register(this);
        this.gestureDetector.setDevice(this);
        this.compressionManager.start();
    }

    @EventHandler
    public void postCompressionListener(PostCompressionEvent event) {

    }

    public BuiltDevice measuredSeries(TimeSeries timeSeries) {
        this.eventBus.post(new RecordedTimeSeriesEvent(timeSeries));
        this.compressionManager.addSeries(timeSeries);
        return this;
    }

    public BuiltDevice newMeasurement(float[] values, long time) {
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
        return this;
    }
}