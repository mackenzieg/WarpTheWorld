package com.wtw.timewarp;

import com.wtw.timeseries.TimeSeriesComparison;
import lombok.Getter;

import java.util.ArrayList;

public class TimeWarpComparisonResults {

    @Getter
    private ArrayList<TimeSeriesComparison> results = new ArrayList<>();

    public void addComparison(TimeSeriesComparison result) {
        this.results.add(result);
    }

    public boolean isComplete() {
        for (TimeSeriesComparison timeSeriesComparison : results) {
            if (!timeSeriesComparison.isDistanceSet()) {
                return false;
            }
        }
        return true;
    }

}
