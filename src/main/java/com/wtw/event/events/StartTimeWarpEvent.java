package com.wtw.event.events;

import com.wtw.event.Cancellable;
import com.wtw.event.Event;
import com.wtw.timeseries.TimeSeries;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class StartTimeWarpEvent extends Event implements Cancellable {

    private boolean cancelled = false;

    @Getter
    private List<TimeSeries> comparisons = new ArrayList<>();

    public void addComparison(TimeSeries timeSeries) {
        this.comparisons.add(timeSeries);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
