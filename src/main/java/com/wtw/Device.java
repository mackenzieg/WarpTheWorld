package com.wtw;

import com.google.common.base.Preconditions;
import com.wtw.compression.TimeSeriesCompressor;
import com.wtw.detectors.GestureDetector;
import com.wtw.event.Event;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostFilterEvent;
import com.wtw.event.events.StartFilteringEvent;
import com.wtw.filters.Filter;
import com.wtw.timeseries.TimeSeries;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Device {

    private ArrayList<Filter> filters = new ArrayList<Filter>();
    private ArrayList<TimeSeriesCompressor> compressors = new ArrayList<TimeSeriesCompressor>();
    private GestureDetector gestureDetector;

    private final EventBus eventBus = new EventBus();

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
    }

    public void measuredSeries(TimeSeries timeSeries) {

    }

    public Device setGestureDetector(Class<? extends GestureDetector> detector) {
        Preconditions.checkNotNull(detector);
        try {
            Constructor constructor = detector.getConstructor(this.getClass());
            constructor.setAccessible(true);
            this.gestureDetector = (GestureDetector) constructor.newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
        this.compressors.add(compressor);
        return this;
    }

}
