package com.wtw.timewarp;

import com.wtw.distance.DistanceCalculator;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesComparison;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class TimeSeriesDistanceCalculator {

    @Getter
    @Setter
    private DistanceCalculator distanceCalculator;

    public final TimeSeriesComparison calculateDistance(TimeSeriesComparison timeSeriesComparison) {
        timeSeriesComparison.setDistance(this.distance(timeSeriesComparison.getRecorded(), timeSeriesComparison.getReference()));
        return timeSeriesComparison;
    }

    public abstract float distance(TimeSeries recorded, TimeSeries reference);

}
