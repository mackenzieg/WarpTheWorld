package com.wtw.event.events;

import com.wtw.event.Cancellable;
import com.wtw.event.Event;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StartCompressionEvent extends Event implements Cancellable {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
