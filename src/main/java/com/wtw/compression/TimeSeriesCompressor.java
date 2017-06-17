package com.wtw.compression;

import com.google.common.base.Preconditions;
import com.wtw.timeseries.TimeSeries;
import lombok.Getter;
import lombok.Setter;

public abstract class TimeSeriesCompressor {

    @Getter
    @Setter
    private int threshold;

    public TimeSeriesCompressor(int threshold) {
        Preconditions.checkArgument(threshold >= 0, "Can't have negative threshold for compression.");
        this.threshold = threshold;
    }

    public abstract TimeSeries compress(TimeSeries timeSeries);

}
