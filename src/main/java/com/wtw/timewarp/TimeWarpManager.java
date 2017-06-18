package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.distance.DistanceCalculator;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostTimeWarpEvent;
import com.wtw.timeseries.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO eventually generalize this
public class TimeWarpManager extends Thread {

    @Getter @Setter
    private EventBus eventBus;

    private ExecutorService pool = Executors.newCachedThreadPool();

    // TODO break into separate object
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
                continue;
            }

            TimeSeries recorded = queuedSeries.removeFirst();
            TimeSeries compareTo = queuedReferenceSeries.removeFirst();

            pool.submit(new TimeWarpCalculator(recorded, compareTo));
        }
    }

    @AllArgsConstructor
    public class TimeWarpCalculator implements Runnable {

        @Getter
        private TimeSeries recorded;
        @Getter
        private TimeSeries compareTo;

        @Override
        public void run() {
            // TODO calculate it and post an event
            PostTimeWarpEvent postTimeWarpEvent = new PostTimeWarpEvent(recorded, compareTo, 10);
            eventBus.post(postTimeWarpEvent);
        }
    }

}
