package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostTimeWarpEvent;
import com.wtw.timeseries.TimeSeries;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO eventually generalize this
public class TimeWarpManager extends Thread {

    @Getter
    @Setter
    private EventBus eventBus;

    private ExecutorService pool = Executors.newCachedThreadPool();

    // TODO break into separate object
    private LinkedList<TimeSeries> queuedSeries = new LinkedList<>();
    private LinkedList<TimeSeries> queuedReferenceSeries = new LinkedList<>();

    @Getter
    @Setter
    private boolean started = false;

    private ArrayList<TimeSeriesDistanceCalculator> timeSeriesDistanceCalculators = new ArrayList<>();

    public void addTimeWarpComp(TimeSeries original, TimeSeries compareTo) {
        Preconditions.checkNotNull(original);
        Preconditions.checkNotNull(compareTo);
        Preconditions.checkArgument(this.isStarted(), "Must start thread before adding comparisons.");
        this.queuedSeries.add(original);
        this.queuedReferenceSeries.add(compareTo);
    }

    public void addDistanceCalculator(TimeSeriesDistanceCalculator timeSeriesDistanceCalculator) {
        Preconditions.checkNotNull(timeSeriesDistanceCalculator);
        this.timeSeriesDistanceCalculators.add(timeSeriesDistanceCalculator);
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
                continue;
            }

            TimeSeries recorded = queuedSeries.removeFirst();
            TimeSeries compareTo = queuedReferenceSeries.removeFirst();

            for (TimeSeriesDistanceCalculator timeSeriesDistanceCalculator : timeSeriesDistanceCalculators) {
                pool.submit(() -> {
                    float distance = timeSeriesDistanceCalculator.distance(recorded, compareTo);

                    PostTimeWarpEvent postTimeWarpEvent = new PostTimeWarpEvent(recorded, compareTo, distance);
                    eventBus.post(postTimeWarpEvent);
                });
            }
        }
    }
}
