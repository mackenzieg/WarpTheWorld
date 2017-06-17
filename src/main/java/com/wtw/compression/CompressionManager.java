package com.wtw.compression;

import com.google.common.base.Preconditions;
import com.wtw.event.EventBus;
import com.wtw.event.events.PostCompressionEvent;
import com.wtw.event.events.StartCompressionEvent;
import com.wtw.timeseries.TimeSeries;

import java.util.ArrayList;
import java.util.LinkedList;

public class CompressionManager extends Thread {

    private LinkedList<TimeSeries> queuedCompressions = new LinkedList<>();
    private ArrayList<TimeSeriesCompressor> compressors = new ArrayList<TimeSeriesCompressor>();

    private EventBus eventBus;

    public CompressionManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        if (queuedCompressions.size() == 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        StartCompressionEvent startCompressionEvent = new StartCompressionEvent();
        this.eventBus.post(startCompressionEvent);

        TimeSeries after = queuedCompressions.removeFirst();

        TimeSeries before = new TimeSeries(after);

        if (!startCompressionEvent.isCancelled()) {
            for (TimeSeriesCompressor timeSeriesCompressor : compressors) {
                after = timeSeriesCompressor.compress(after);
            }
        }

        PostCompressionEvent postCompressionEvent = new PostCompressionEvent(before, after);
        this.eventBus.post(postCompressionEvent);
    }

    public void addCompressor(TimeSeriesCompressor compressor) {
        Preconditions.checkNotNull(compressor);
        this.compressors.add(compressor);
    }
}
