package com.wtw.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Filter {

    @Getter
    private final int dimensions;

    public float[] filter(float[] vector) {
        if (vector == null) {
            return null;
        }
        return filterAlgorithm(vector);
    }

    abstract public float[] filterAlgorithm(float[] vector);

    abstract public void reset();

}
