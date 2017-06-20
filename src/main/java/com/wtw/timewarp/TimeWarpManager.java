package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostTimeWarpEvent;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesComparison;
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

    @Getter
    @Setter
    private boolean started = false;

    private TimeSeriesDistanceCalculator timeSeriesDistanceCalculator = null;

    private LinkedList<TimeWarpComparisonResults> queued = new LinkedList<>();

    public void addTimeWarpComp(TimeWarpComparisonResults timeWarpComparisons) {
        Preconditions.checkNotNull(timeWarpComparisons);
        queued.add(timeWarpComparisons);
    }

    public void setDistanceCalculator(TimeSeriesDistanceCalculator timeSeriesDistanceCalculator) {
        Preconditions.checkNotNull(timeSeriesDistanceCalculator);
        this.timeSeriesDistanceCalculator = timeSeriesDistanceCalculator;
    }

    @Override
    public void run() {
        while (this.started) {
            if (queued.size() == 0) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            Preconditions.checkNotNull(timeSeriesDistanceCalculator);

            final TimeWarpComparisonResults timeWarpComparisonResults = queued.removeFirst();

            for (final TimeSeriesComparison timeSeriesComparison : timeWarpComparisonResults.getResults()) {
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        timeSeriesDistanceCalculator.distance(timeSeriesComparison);
                        if (timeWarpComparisonResults.isComplete()) {
                            PostTimeWarpEvent postTimeWarpEvent = new PostTimeWarpEvent(timeWarpComparisonResults);
                            eventBus.post(postTimeWarpEvent);
                        }
                    }
                });
            }
        }
    }
}
