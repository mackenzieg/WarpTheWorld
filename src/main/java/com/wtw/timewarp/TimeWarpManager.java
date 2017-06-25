package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostTimeWarpEvent;
import com.wtw.timeseries.TimeSeriesComparison;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

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
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            Preconditions.checkNotNull(timeSeriesDistanceCalculator);

            final TimeWarpComparisonResults timeWarpComparisonResults = queued.removeFirst();


            // TODO clean this up where Callable returns a TimeSeriesComparison instead
            final Collection<Callable<TimeSeriesComparison>> tasks = new ArrayList<>();
            for (int i = 0; i < timeWarpComparisonResults.getResults().size(); i++) {
                final TimeSeriesComparison timeSeriesComparison = timeWarpComparisonResults.getResults().get(i);
                tasks.add(new Callable<TimeSeriesComparison>() {
                    @Override
                    public TimeSeriesComparison call() throws Exception {
                        return timeSeriesDistanceCalculator.calculateDistance(timeSeriesComparison);
                    }
                });
            }

            TimeWarpComparisonResults completedWarp = new TimeWarpComparisonResults();

            try {
                List<Future<TimeSeriesComparison>> futures = pool.invokeAll(tasks);
                for (Future future : futures) {
                    completedWarp.addComparison((TimeSeriesComparison) future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            PostTimeWarpEvent postTimeWarpEvent = new PostTimeWarpEvent(completedWarp);
            eventBus.post(postTimeWarpEvent);
        }
    }
}
