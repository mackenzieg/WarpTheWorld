package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.timeseries.TimeSeries;

import java.util.LinkedList;

public class TimeWarpManager extends Thread {

    private LinkedList<TimeSeries> queuedSeries = new LinkedList<>();
    private LinkedList<TimeSeries> queuedReferenceSeries = new LinkedList<>();

    public void addTimeWarpComp(TimeSeries original, TimeSeries compareTo) {
        Preconditions.checkNotNull(original);
        Preconditions.checkNotNull(compareTo);
        this.queuedSeries.add(original);
        this.queuedReferenceSeries.add(compareTo);
    }

    @Override
    public void run() {
        TimeSeries recorded = queuedSeries.removeFirst();
        TimeSeries compareTo = queuedReferenceSeries.removeFirst();


    }

}
