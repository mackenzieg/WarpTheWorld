package com.wtw.compression;

import com.google.common.base.Preconditions;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesPoint;

public class MeanCompressor extends TimeSeriesCompressor {

    private int threshold;

    public MeanCompressor() {
        this(40);
    }

    public MeanCompressor(int threshold) {
        super(threshold);
    }

    public TimeSeries compress(TimeSeries timeSeries) {
        Preconditions.checkNotNull(timeSeries);
        if (timeSeries.size() <= this.threshold) {
            return timeSeries;
        }

        TimeSeries compressedSeries = new TimeSeries();

        for (int i = 0; i < timeSeries.size(); i += 2) {
            if (i - 1 > timeSeries.size()) {
                compressedSeries.addPoint(new TimeSeriesPoint());
                break;
            } else {
                compressedSeries.addPoint(timeSeries.getPoint(i).averagePoints(timeSeries.getPoint(i + 1)));
            }
        }

        if ((timeSeries.size() / 2) % 2 == 1) {
            compressedSeries.addPoint(timeSeries.getPoint(timeSeries.size() - 1));
        }

        return compressedSeries;
    }

}
