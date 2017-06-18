package com.wtw;

import com.google.common.base.Preconditions;
import com.wtw.compression.CompressionManager;
import com.wtw.detectors.GestureDetector;
import com.wtw.event.EventBus;
import com.wtw.event.EventHandler;
import com.wtw.event.EventListener;
import com.wtw.event.events.*;
import com.wtw.filters.Filter;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timewarp.TimeWarpManager;
import lombok.Getter;

import java.util.ArrayList;

public class BuiltDevice extends EventListener {
    @Getter
    private final EventBus eventBus;
    private ArrayList<Filter> filters = new ArrayList<>();
    @Getter
    private GestureDetector gestureDetector = null;
    @Getter
    private CompressionManager compressionManager;
    @Getter
    private TimeWarpManager timeWarpManager;

    public BuiltDevice(EventBus eventBus, ArrayList<Filter> filters, GestureDetector gestureDetector, CompressionManager compressionManager, TimeWarpManager timeWarpManager) {
        Preconditions.checkNotNull(gestureDetector, "Must define a gesture detector.");
        this.filters = filters;
        this.gestureDetector = gestureDetector;
        this.eventBus = eventBus;
        this.compressionManager = compressionManager;
        this.timeWarpManager = timeWarpManager;
        this.eventBus.register(this);
        this.gestureDetector.setDevice(this);
    }

    @EventHandler
    public void postCompression(PostCompressionEvent postCompressionEvent) {
        StartTimeWarpEvent startTimeWarpEvent = new StartTimeWarpEvent();
        this.eventBus.post(startTimeWarpEvent);
        if (!startTimeWarpEvent.isCancelled()) {
            for (TimeSeries timeSeries : startTimeWarpEvent.getComparisons()) {
                this.timeWarpManager.addTimeWarpComp(postCompressionEvent.getAfter(), timeSeries);
            }
        }
    }

    public void startTimeWarp() {
        this.timeWarpManager.setStarted(true);
        this.timeWarpManager.start();
    }

    public void startCompressing() {
        this.compressionManager.setStarted(true);
        this.compressionManager.start();
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