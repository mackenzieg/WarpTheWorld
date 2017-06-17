package com.wtw.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class EventHandlerMethod {

    @Getter
    private final Object listener;

    @Getter
    private final Method method;

    public void invoke(Object event) throws InvocationTargetException, IllegalAccessException {
        method.invoke(listener, event);
    }

}
