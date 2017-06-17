package com.wtw.timeseries;

import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class TimeSeries {

    private ArrayList<TimeSeriesPoint> points = new ArrayList<TimeSeriesPoint>();

    public TimeSeries() {
    }

    public TimeSeries(TimeSeries other) {
        this.points = (ArrayList<TimeSeriesPoint>) other.points.clone();
    }

    public TimeSeriesPoint getPoint(int index) {
        Preconditions.checkPositionIndex(index, points.size());
        return points.get(index);
    }

    public void addPoint(TimeSeriesPoint point) {
        this.points.add(point);
    }

    public int size() {
        return this.points.size();
    }

}
