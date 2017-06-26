package com.wtw;

import com.wtw.compression.MeanCompressor;
import com.wtw.detectors.DefaultGestureDetector;
import com.wtw.distance.AbsoluteDistance;
import com.wtw.distance.EuclideanDistance;
import com.wtw.event.EventHandler;
import com.wtw.event.events.PostCompressionEvent;
import com.wtw.event.events.PostTimeWarpEvent;
import com.wtw.event.events.StartTimeWarpEvent;
import com.wtw.filters.DifferenceEquivalenceFilter;
import com.wtw.filters.LowPassFilter;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesComparison;
import com.wtw.timeseries.TimeSeriesPoint;
import com.wtw.timewarp.SlowTimeWarpCalculator;

import java.util.EventListener;
import java.util.Random;

public class FullSystemTest {

    public static void main(String[] args) {

        final TimeSeries compare = new TimeSeries();

        Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            compare.addPoint(new TimeSeriesPoint(new float[]{
                    random.nextFloat()}, i));
        }

        final TimeSeries compare1 = new TimeSeries();

        for (int i = 0; i < 100; ++i) {
            compare1.addPoint(new TimeSeriesPoint(new float[]{
                    random.nextFloat()}, i));
        }

        final TimeSeries timeSeries = new TimeSeries();

        for (int i = 0; i < 200; ++i) {
            timeSeries.addPoint(new TimeSeriesPoint(new float[]{
                    random.nextFloat()}, i));
        }

        BuiltDevice builtDevice = new Device()
                .addCompressor(new MeanCompressor(5))
                .addFilter(new LowPassFilter(3))
                .addFilter(new DifferenceEquivalenceFilter(3))
                .setGestureDetector(new DefaultGestureDetector(new EuclideanDistance()))
                .setTimeWarpCalculator(new SlowTimeWarpCalculator(new AbsoluteDistance()))
                .registerListener(new EventListener() {
                    @EventHandler
                    public void postTimeWarp(PostTimeWarpEvent postTimeWarpEvent) {
                        float lowestSum = 100000.0f;
                        TimeSeriesComparison lowest = null;
                        for (TimeSeriesComparison timeSeriesComparison : postTimeWarpEvent.getTimeWarpComparisonResults().getResults()) {
                            if (lowestSum >= timeSeriesComparison.getDistance()) {
                                lowestSum = timeSeriesComparison.getDistance();
                                lowest = timeSeriesComparison;
                            }
                        }

                        System.out.println(String.format("Lowest sum: %f", lowestSum));
                    }

                    @EventHandler
                    public void compare(StartTimeWarpEvent startTimeWarpEvent) {
                        startTimeWarpEvent.addComparison(compare);
                        startTimeWarpEvent.addComparison(compare1);
                    }
                }).build().setStartCompression(true).setStartTimeWarp(true);

        builtDevice.measuredSeries(timeSeries);

        while (true) {

        }
    }

}
