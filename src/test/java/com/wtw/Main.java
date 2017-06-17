package com.wtw;

import com.wtw.compression.MeanCompressor;
import com.wtw.detectors.DefaultGestureDetector;
import com.wtw.distance.EuclideanDistance;
import com.wtw.event.EventHandler;
import com.wtw.event.events.PostCompressionEvent;
import com.wtw.filters.DifferenceEquivalenceFilter;
import com.wtw.filters.LowPassFilter;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesPoint;

import java.util.EventListener;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BuiltDevice builtDevice = new Device()
                .addCompressor(new MeanCompressor())
                .addFilter(new LowPassFilter(3))
                .addFilter(new DifferenceEquivalenceFilter(3))
                .setGestureDetector(new DefaultGestureDetector(new EuclideanDistance()))
                .registerListener(new EventListener() {
                    @EventHandler public void getCompressed(PostCompressionEvent postCompressionEvent) {
                        System.out.println(postCompressionEvent.getAfter().toString());
                    }
                })
                .build();

        TimeSeries timeSeries = new TimeSeries();

        Random random = new Random();
        for (int i = 0; i < 200; ++i) {
            timeSeries.addPoint(new TimeSeriesPoint(new float[] {
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat()}));
        }

        builtDevice.measuredSeries(timeSeries);

        while(true) {

        }
    }

}
