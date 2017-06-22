package com.wtw.filters;

/*
 * Integrate to smooth out high frequency
 */
public class LowPassFilter extends Filter {

    private double TIME_CONSTANT = 0.10;
    private int COUNT_BEFORE_UPDATE = 1;
    private float[] previous;
    private double alpha = 0.95;
    private double deltaTime = 0.0;

    private double startTime = 0.0;

    private int count = 0;

    public LowPassFilter(int dimensions) {
        super(dimensions);
        this.reset();
    }

    public LowPassFilter(int dimensions, double TIME_CONSTANT, int COUNT_BEFORE_UPDATE) {
        super(dimensions);
        this.TIME_CONSTANT = TIME_CONSTANT;
        this.COUNT_BEFORE_UPDATE = COUNT_BEFORE_UPDATE;
    }

    @Override
    public float[] filterAlgorithm(float[] vector) {
        if (startTime == 0.0) {
            startTime = System.nanoTime();
        }

        deltaTime = 1.0 / (count++ / ((System.nanoTime() - startTime) / 1000000000.0));
        alpha = TIME_CONSTANT / (TIME_CONSTANT + deltaTime);

        if (count > COUNT_BEFORE_UPDATE) {
            for (int i = 0; i < this.getDimensions(); ++i) {
                previous[i] = (float) (alpha * previous[i] + (1 - alpha) * vector[i]);
            }
        }

        return previous;
    }

    @Override
    public void reset() {
        this.previous = new float[this.getDimensions()];
        for (int i = 0; i < this.getDimensions(); ++i) {
            this.previous[i] = 0.0f;
        }
    }
}