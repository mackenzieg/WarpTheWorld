package com.wtw.event.events;

import com.wtw.event.Event;
import com.wtw.timewarp.TimeWarpComparisonResults;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PostTimeWarpEvent extends Event {

    @Getter
    private final TimeWarpComparisonResults timeWarpComparisonResults;
}
