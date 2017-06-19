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
import com.wtw.timeseries.TimeSeriesPoint;
import com.wtw.timewarp.SlowTimeWarpCalculator;
import com.wtw.timewarp.TimeWarpManager;

import java.util.EventListener;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class FullSystemTest {

    public static void main(String[] args) {

        final TimeSeries compare = new TimeSeries();

        Random random = new Random();
        for (int i = 0; i < 1000; ++i) {
            compare.addPoint(new TimeSeriesPoint(new float[]{
                    random.nextFloat()}, i));
        }

        TimeSeries timeSeries = new TimeSeries();

        for (int i = 0; i < 200; ++i) {
            timeSeries.addPoint(new TimeSeriesPoint(new float[]{
                    random.nextFloat()}, i));
        }

        final AtomicReference<TimeSeries> cache = new AtomicReference<>();

        cache.set(timeSeries);

        BuiltDevice builtDevice = new Device()
                .addCompressor(new MeanCompressor(5))
                .addFilter(new LowPassFilter(1))
                .addFilter(new DifferenceEquivalenceFilter(3))
                .setGestureDetector(new DefaultGestureDetector(new EuclideanDistance()))
                .addTimeWarpCalculator(new SlowTimeWarpCalculator(new AbsoluteDistance()))
                .registerListener(new EventListener() {
                    @EventHandler
                    public void getCompressed(PostCompressionEvent postCompressionEvent) {
                        System.out.println("After");
                        cache.set(postCompressionEvent.getAfter());
                        System.out.println(postCompressionEvent.getAfter().toString());
                    }

                    @EventHandler
                    public void postTimeWarp(PostTimeWarpEvent postTimeWarpEvent) {
                        System.out.println(postTimeWarpEvent.getDistance());
                    }

                    @EventHandler
                    public void compare(StartTimeWarpEvent startTimeWarpEvent) {
                        startTimeWarpEvent.addComparison(compare);
                    }
                }).build().setStartCompression(true).setStartTimeWarp(true);

        System.out.println("Before");
        System.out.println(timeSeries.toString());

        builtDevice.measuredSeries(timeSeries);

        while (true) {

        }
    }

}
