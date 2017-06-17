package com.wtw.event.events;

import com.wtw.event.Event;
import com.wtw.timeseries.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RecordedTimeSeriesEvent extends Event {

    @Getter
    private TimeSeries timeSeries;

}