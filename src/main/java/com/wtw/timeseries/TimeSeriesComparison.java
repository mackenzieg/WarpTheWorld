package com.wtw.timeseries;


import lombok.Getter;
import lombok.Setter;

public class TimeSeriesComparison {

    @Getter
    private final TimeSeries recorded;

    @Getter
    private final TimeSeries reference;

    @Getter
    @Setter
    private Float distance = null;

    public TimeSeriesComparison(TimeSeries recorded, TimeSeries reference, Float distance) {
        this.recorded = recorded;
        this.reference = reference;
        this.distance = distance;
    }

    public TimeSeriesComparison(TimeSeries recorded, TimeSeries reference) {
        this.recorded = recorded;
        this.reference = reference;
    }
}
