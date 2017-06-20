package com.wtw;

import com.google.common.base.Preconditions;
import com.wtw.compression.CompressionManager;
import com.wtw.compression.TimeSeriesCompressor;
import com.wtw.detectors.GestureDetector;
import com.wtw.event.EventBus;
import com.wtw.filters.Filter;
import com.wtw.timewarp.TimeSeriesDistanceCalculator;
import com.wtw.timewarp.TimeWarpManager;

import java.util.ArrayList;

public class Device {

    private final EventBus eventBus = new EventBus();
    private ArrayList<Filter> filters = new ArrayList<>();
    private GestureDetector gestureDetector = null;
    private CompressionManager compressionManager = null;
    private TimeWarpManager timeWarpManager = new TimeWarpManager();

    public Device() {
    }

    public Device setTimeWarpManager(TimeWarpManager timeWarpManager) {
        Preconditions.checkNotNull(timeWarpManager);
        this.timeWarpManager = timeWarpManager;
        this.timeWarpManager.setEventBus(this.eventBus);
        return this;
    }

    public Device addTimeWarpCalculator(TimeSeriesDistanceCalculator timeSeriesDistanceCalculator) {
        Preconditions.checkNotNull(timeSeriesDistanceCalculator);
        this.timeWarpManager.addDistanceCalculator(timeSeriesDistanceCalculator);
        return this;
    }

    public Device setGestureDetector(GestureDetector gestureDetector) {
        Preconditions.checkNotNull(gestureDetector);
        this.gestureDetector = gestureDetector;
        return this;
    }

    public Device registerListener(Object listener) {
        Preconditions.checkNotNull(listener);
        this.eventBus.register(listener);
        return this;
    }

    public Device addFilter(Filter filter) {
        Preconditions.checkNotNull(filter);
        this.filters.add(filter);
        return this;
    }

    public Device addCompressor(TimeSeriesCompressor compressor) {
        Preconditions.checkNotNull(compressor);
        if (this.compressionManager == null) {
            this.compressionManager = new CompressionManager(this.eventBus);
        }
        this.compressionManager.addCompressor(compressor);
        return this;
    }

    public BuiltDevice build() {
        return new BuiltDevice(this.eventBus, this.filters, this.gestureDetector, this.compressionManager, this.timeWarpManager);
    }
}
