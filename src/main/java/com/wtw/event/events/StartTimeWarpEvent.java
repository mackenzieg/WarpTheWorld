package com.wtw.event.events;

import com.wtw.event.Cancellable;
import com.wtw.event.Event;
import com.wtw.timeseries.TimeSeries;
import com.wtw.timeseries.TimeSeriesComparison;
import com.wtw.timewarp.TimeWarpComparisonResults;
import lombok.Getter;

public class StartTimeWarpEvent extends Event implements Cancellable {

    @Getter
    private TimeWarpComparisonResults comparisons = new TimeWarpComparisonResults();

    @Getter
    private TimeSeries recorded;

    private boolean cancelled = false;

    public StartTimeWarpEvent(TimeSeries recorded) {
        this.recorded = recorded;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void addComparison(TimeSeries reference) {
        this.comparisons.addComparison(new TimeSeriesComparison(recorded, reference));
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
