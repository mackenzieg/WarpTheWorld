package com.wtw.compression;

import com.google.common.base.Preconditions;
import com.wtw.timeseries.TimeSeries;

public class MeanCompressor extends TimeSeriesCompressor {

    public MeanCompressor() {
        this(40);
    }

    public MeanCompressor(int threshold) {
        super(threshold);
    }

    public TimeSeries compress(TimeSeries timeSeries) {
        Preconditions.checkNotNull(timeSeries);
        if (timeSeries.size() <= this.getThreshold()) {
            return timeSeries;
        }

        TimeSeries compressedSeries = new TimeSeries();

        for (int i = 0; i < timeSeries.size(); i += 2) {
            if (i < timeSeries.size() - 1) {
                compressedSeries.addPoint(timeSeries.getPoint(i).averagePoints(timeSeries.getPoint(i + 1)));
            }
        }

        if ((timeSeries.size() / 2) % 2 == 1) {
            compressedSeries.addPoint(timeSeries.getPoint(timeSeries.size() - 1));
        }

        return compressedSeries;
    }
}
