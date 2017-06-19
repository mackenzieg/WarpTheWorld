package com.wtw.event.events;

import com.wtw.timeseries.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PostTimeWarpEvent {

    @Getter
    private final TimeSeries recorded;

    @Getter
    private final TimeSeries reference;

    @Getter
    private final float distance;
}
