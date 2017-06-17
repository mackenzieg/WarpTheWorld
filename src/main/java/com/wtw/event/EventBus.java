package com.wtw.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private final Map<Class<?>, List<EventHandlerMethod>> listeners = new HashMap<Class<?>, List<EventHandlerMethod>>();

    public void post(Object event) {
        List<EventHandlerMethod> handlers = listeners.get(event.getClass());

        if (handlers == null) {
            return;
        }

        for (EventHandlerMethod method : handlers) {
            try {
                method.invoke(event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO eventually check that class extends EventListener and have priority
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation != null) {
                Class<?>[] params = method.getParameterTypes();

                if (params.length != 1) {
                    // TODO error out here
                    continue;
                }

                Class<?> eventType = params[0];

                this.listeners.putIfAbsent(eventType, new ArrayList<EventHandlerMethod>());

                method.setAccessible(true);

                this.listeners.get(eventType).add(new EventHandlerMethod(listener, method));
            }
        }
    }

}
