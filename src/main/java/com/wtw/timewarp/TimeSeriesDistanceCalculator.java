package com.wtw.timewarp;

import com.wtw.distance.DistanceCalculator;
import com.wtw.timeseries.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class TimeSeriesDistanceCalculator {

    @Getter
    @Setter
    private DistanceCalculator distanceCalculator;

    public abstract float distance(TimeSeries recorded, TimeSeries reference);

}
