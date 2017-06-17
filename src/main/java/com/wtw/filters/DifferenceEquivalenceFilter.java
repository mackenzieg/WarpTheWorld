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

    public DifferenceEquivalenceFilter() {
        this(0.2f);
    }

    public DifferenceEquivalenceFilter(double sensitivity) {
        super();
        this.sensitivity = sensitivity;
        this.reset();
    }

    @Override
    public float[] filterAlgorithm(float[] vector) {
        boolean accept = false;

        for (int i = 0; i < vector.length; ++i) {
            if (vector[i] < previous[i] - this.sensitivity
                    || vector[i] > previous[i] + this.sensitivity) {
                accept = true;
            }
        }

        if (accept) {
            this.previous = vector;
            return vector;
        }

        return new float[]{0.0f, 0.0f, 0.0f};
    }

    @Override
    public void reset() {
        this.previous = new float[this.getDimensions()];
        for (int i = 0; i < this.getDimensions(); ++i) {
            this.previous[i] = 0.0f;
        }
    }
}