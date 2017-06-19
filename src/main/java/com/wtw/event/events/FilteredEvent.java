package com.wtw.event.events;

import com.wtw.event.Event;
import com.wtw.filters.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FilteredEvent extends Event {

    @Getter
    private Filter filter;

    @Getter
    private float[] before;

    @Getter
    private float[] after;
}
