package com.wtw.filters;

/*
 * Integrate to smooth out high frequency
 */
public class LowPassFilter extends Filter {

    private float[] previous;

    private final double TIME_CONSTANT = 0.10;
    private final int COUNT_BEFORE_UPDATE = 1;
    private double alpha = 0.95;
    private double deltaTime = 0.0;

    private double startTime = 0.0;

    private int count = 0;

    public LowPassFilter() {
        super();
        this.reset();
    }

    @Override
    public float[] filterAlgorithm(float[] vector) {
        if (startTime == 0.0)
        {
            startTime = System.nanoTime();
        }

        deltaTime = 1.0 / (count++ / ((System.nanoTime() - startTime) / 1000000000.0));
        alpha = TIME_CONSTANT / (TIME_CONSTANT + deltaTime);

        if (count > COUNT_BEFORE_UPDATE)
        {
            previous[0] = (float)(alpha * previous[0] + (1 - alpha) * vector[0]);
            previous[1] = (float)(alpha * previous[1] + (1 - alpha) * vector[1]);
            previous[2] = (float)(alpha * previous[2] + (1 - alpha) * vector[2]);
        }

        return previous;
    }

    @Override
    public void reset() {
        this.previous = new float[]{0.0f, 0.0f, 0.0f};
    }
}