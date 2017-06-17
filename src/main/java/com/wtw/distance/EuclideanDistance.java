package com.wtw.distance;

import com.google.common.base.Preconditions;

public class EuclideanDistance extends DistanceCalculator {

    @Override
    public float distance(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public float[] distancePerAxis(float[] x, float[] y) {
        Preconditions.checkNotNull(x);
        Preconditions.checkNotNull(y);
        Preconditions.checkArgument(x.length == y.length, "Vectors must be same length.");

        float[] newVector = new float[x.length];
        for (int i = 0; i < x.length; ++i) {
            newVector[i] = (float) Math.sqrt(Math.pow(x[i] - y[i], 2));
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
            distance += Math.pow(x[i] - y[i], 2);
        }
        return (float) Math.sqrt(distance);
    }
}
