package com.wtw.distance;

public abstract class DistanceCalculator {

    public abstract float distance(float x, float y);

    public abstract float[] distancePerAxis(float[] x, float[] y);

    public abstract float distance(float[] x, float[] y);

}
