package com.wtw.timewarp;

import com.wtw.timeseries.TimeSeries;

public abstract class TimeSeriesDistanceCalculator {

    public abstract int distance(TimeSeries original, TimeSeries reference);

}
