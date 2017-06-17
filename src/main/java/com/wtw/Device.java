package com.wtw;

import com.wtw.compression.TimeSeriesCompressor;
import com.wtw.filters.Filter;
import com.wtw.timeseries.TimeSeriesPoint;

import java.util.ArrayList;

public class Device {

    private ArrayList<Filter> filters = new ArrayList<Filter>();
    private ArrayList<TimeSeriesCompressor> compressors = new ArrayList<TimeSeriesCompressor>();

    public void newPoint(float[] points, long time) {

    }

    public void newPoint(TimeSeriesPoint timeSeriesPoint) {

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
