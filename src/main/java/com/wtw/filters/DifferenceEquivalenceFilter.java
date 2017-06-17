package com.wtw.filters;

import lombok.Getter;
import lombok.Setter;

/**
 * This filter removes a vector if it doesn't differ enough from the previous
 * retrieved value
 */
public class DifferenceEquivalenceFilter extends Filter {

    @Getter
    @Setter
    private double sensitivity;
    private float[] previous;

    public DifferenceEquivalenceFilter(int dimensions) {
        this(dimensions, 0.2f);
    }

    public DifferenceEquivalenceFilter(int dimensions, double sensitivity) {
        super(dimensions);
        this.sensitivity = sensitivity;
        this.reset();
    }

    @Override
    public float[] filterAlgorithm(float[] vector) {
        boolean accept = false;

        for (int i = 0; i < this.getDimensions(); ++i) {
            if (vector[i] < previous[i] - this.sensitivity
                    || vector[i] > previous[i] + this.sensitivity) {
                accept = true;
            }
        }

        if (accept) {
            this.previous = vector;
            return vector;
        }

        float[] defaultVals = new float[this.getDimensions()];

        for (int i = 0; i < this.getDimensions(); ++i) {
            defaultVals[i] = 0.0f;
        }

        return defaultVals;
    }

    @Override
    public void reset() {
        this.previous = new float[this.getDimensions()];
        for (int i = 0; i < this.getDimensions(); ++i) {
            this.previous[i] = 0.0f;
        }
    }
}