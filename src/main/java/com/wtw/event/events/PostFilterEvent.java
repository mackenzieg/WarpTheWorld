package com.wtw.event.events;

import com.wtw.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PostFilterEvent extends Event {

    @Getter
    private final float[] before;

    @Getter
    private final float[] after;
}