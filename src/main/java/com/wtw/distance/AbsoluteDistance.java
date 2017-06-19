package com.wtw.distance;

import com.google.common.base.Preconditions;

public class AbsoluteDistance extends DistanceCalculator {
    @Override
    public float distance(float x, float y) {
        return Math.abs(x - y);
    }

    @Override
    public float[] distancePerAxis(float[] x, float[] y) {
        Preconditions.checkNotNull(x);
        Preconditions.checkNotNull(y);
        Preconditions.checkArgument(x.length == y.length, "Vectors must be same length.");

        float[] newVector = new float[x.length];
        for (int i = 0; i < x.length; ++i) {
            newVector[i] = Math.abs(x[i] - y[i]);
        }
        return newVector;
    }

    @Override
    public float distance(float[] x, float[] y) {
        Preconditions.checkNotNull(x);
        Preconditions.checkNotNull(y);
        Preconditions.checkArgument(x.length == y.length, "Vectors must be same length.");

        float distance = 0.0f;

        for (int i = 0; i < x.length; ++i) {
            distance += Math.abs(x[i] - y[i]);
        }
        return (float) Math.sqrt(distance);
    }
}
