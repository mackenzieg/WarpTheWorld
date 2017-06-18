package com.wtw.timewarp;

import com.google.common.base.Preconditions;
import com.wtw.distance.DistanceCalculator;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostTimeWarpEvent;
import com.wtw.timeseries.TimeSeries;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
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

    @Getter
    @Setter
    private DistanceCalculator distanceCalculator;

    public TimeWarpManager(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public void addTimeWarpComp(TimeSeries original, TimeSeries compareTo) {
        Preconditions.checkNotNull(original);
        Preconditions.checkNotNull(compareTo);
        Preconditions.checkArgument(this.isStarted(), "Must start thread before adding comparisons.");
        this.queuedSeries.add(original);
        this.queuedReferenceSeries.add(compareTo);
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

            try {
                pool.submit(new TimeWarpCalculator(recorded, compareTo)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public class TimeWarpCalculator implements Runnable {

        @Getter
        private TimeSeries recorded;
        @Getter
        private TimeSeries compareTo;

        public TimeWarpCalculator(TimeSeries recorded, TimeSeries compareTo) {
            Preconditions.checkNotNull(recorded);
            Preconditions.checkNotNull(compareTo);
            this.recorded = recorded;
            this.compareTo = compareTo;
        }

        private float[][][] matrix;
        private float[][][] distance;
        private float sum[];

        @Override
        public void run() {

            // TODO this is DTW not FastDTW just to do some testing
            // TODO this is very bad bad bad

            this.matrix = new float[recorded.getPoint(0).size()][recorded.size() + 1][compareTo.size() + 1];
            this.distance = new float[recorded.getPoint(0).size()][recorded.size()][compareTo.size()];

            int numDimensions = recorded.getPoint(0).size();


            for (int i = 0; i < numDimensions; ++i) {
                for (int x = 0; x <= recorded.size(); ++x) {
                    for (int y = 0; y <= compareTo.size(); ++y) {
                        this.matrix[i][x][y] = -1.0f;
                    }
                }
            }
            for (int i = 0; i < numDimensions; ++i) {
                for (int x = 0; x < recorded.size(); ++x) {
                    for (int y = 0; y < compareTo.size(); ++y) {
                        this.distance[i][x][y] = distanceCalculator.distance(recorded.getPoint(x).getDimension(i), compareTo.getPoint(x).getDimension(i));
                    }
                }
            }
            for (int i = 0; i < numDimensions; ++i) {
                for (int x = 1; x < recorded.size(); ++x) {
                    matrix[i][x][0] = Float.POSITIVE_INFINITY;
                }
            }

            for (int i = 0; i < numDimensions; ++i) {
                for (int x = 1; x < compareTo.size(); ++x) {
                    matrix[i][0][x] = Float.POSITIVE_INFINITY;
                }
            }

            for (int i = 0; i < numDimensions; ++i) {
                matrix[i][0][0] = 0.0f;
            }

            sum = new float[numDimensions];

            for (int i = 0; i < numDimensions; ++i) {
                sum[i] = computeBackward(i, recorded.size() - 1, compareTo.size() - 1);
            }

            float singleSum = 0.0f;

            for (int i = 0; i < numDimensions; ++i) {
                singleSum += sum[i];
            }

            PostTimeWarpEvent postTimeWarpEvent = new PostTimeWarpEvent(recorded, compareTo, singleSum);
            eventBus.post(postTimeWarpEvent);
        }

        public float computeBackward(int dimension, int i, int j) {
            // If greater than window return distance
            if (!(matrix[dimension][i][j] < 0.0f)) {
                return matrix[dimension][i][j];
            }

            // Compute path in insertion manner
            if (computeBackward(dimension, i - 1, j) <= computeBackward(dimension, i, j - 1) &&
                    computeBackward(dimension, i - 1, j) <= computeBackward(dimension, i - 1, j - 1) &&
                    computeBackward(dimension, i - 1, j) < Float.POSITIVE_INFINITY) {

                matrix[dimension][i][j] = distance[dimension][i - 1][j - 1] + computeBackward(dimension, i - 1, j);
                // Compute path in deletion manner
            } else if (computeBackward(dimension, i, j - 1) <= computeBackward(dimension, i - 1, j) &&
                    computeBackward(dimension, i, j - 1) <= computeBackward(dimension, i - 1, j - 1) &&
                    computeBackward(dimension, i, j - 1) < Float.POSITIVE_INFINITY) {

                matrix[dimension][i][j] = distance[dimension][i - 1][j - 1] + computeBackward(dimension, i, j - 1);
                // Compute path in match manner
            } else if (computeBackward(dimension, i - 1, j - 1) <= computeBackward(dimension, i - 1, j) &&
                    computeBackward(dimension, i - 1, j - 1) <= computeBackward(dimension, i, j - 1) &&
                    computeBackward(dimension, i - 1, j - 1) < Float.POSITIVE_INFINITY) {

                matrix[dimension][i][j] = distance[dimension][i - 1][j - 1] + computeBackward(dimension, i - 1, j - 1);
            }
            return matrix[dimension][i][j];
        }
    }
}
