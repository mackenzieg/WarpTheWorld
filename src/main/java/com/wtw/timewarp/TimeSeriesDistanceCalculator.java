package com.wtw.timewarp;

import com.wtw.distance.DistanceCalculator;
import com.wtw.timeseries.TimeSeriesComparison;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class TimeSeriesDistanceCalculator {

    @Getter
    @Setter
    private DistanceCalculator distanceCalculator;

    public abstract void distance(TimeSeriesComparison timeSeriesComparison);

}
