package com.wtw.event.events;

import com.wtw.timeseries.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PostCompressionEvent {

    @Getter
    private final TimeSeries before;

    @Getter
    private final TimeSeries after;

}
