package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.distance.DistanceCalculator;
import com.wtw.timeseries.TimeSeries;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

public class TimeWarpManager extends Thread {

    private LinkedList<TimeSeries> queuedSeries = new LinkedList<>();
    private LinkedList<TimeSeries> queuedReferenceSeries = new LinkedList<>();

    @Getter
    @Setter
    private boolean started = false;

    @Getter
    @Setter
    private DistanceCalculator distanceCalculator;

    public TimeWarpManager(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public void addTimeWarpComp(TimeSeries original, TimeSeries compareTo) {
        Preconditions.checkNotNull(original);
        Preconditions.checkNotNull(compareTo);
        if (this.isStarted()) {
            this.queuedSeries.add(original);
            this.queuedReferenceSeries.add(compareTo);
        }
    }

    @Override
    public void run() {

        while (this.started) {
            if (queuedSeries.size() == 0) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            TimeSeries recorded = queuedSeries.removeFirst();
            TimeSeries compareTo = queuedReferenceSeries.removeFirst();
        }

    }

}
