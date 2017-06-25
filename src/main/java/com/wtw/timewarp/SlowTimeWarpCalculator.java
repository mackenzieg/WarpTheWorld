package com.wtw.timewarp;

import com.wtw.distance.DistanceCalculator;
import com.wtw.timeseries.TimeSeries;

public class SlowTimeWarpCalculator extends TimeSeriesDistanceCalculator {

    private float[][][] matrix;
    private float[][][] distance;

    public SlowTimeWarpCalculator(DistanceCalculator distanceCalculator) {
        super(distanceCalculator);
    }

    public float distance(TimeSeries recorded, TimeSeries reference) {
        this.matrix = new float[recorded.getPoint(0).size()][recorded.size() + 1][reference.size() + 1];
        this.distance = new float[recorded.getPoint(0).size()][recorded.size()][reference.size()];

        int numDimensions = recorded.getPoint(0).size();

        for (int d = 0; d < numDimensions; ++d) {
            for (int x = 0; x <= recorded.size(); ++x) {
                for (int y = 0; y <= reference.size(); ++y) {
                    this.matrix[d][x][y] = -1.0f;
                }
            }
        }
        for (int d = 0; d < numDimensions; ++d) {
            for (int x = 0; x < recorded.size(); ++x) {
                for (int y = 0; y < reference.size(); ++y) {
                    this.distance[d][x][y] = this.getDistanceCalculator().distance(recorded.getPoint(x).getDimension(d), reference.getPoint(x).getDimension(d));
                }
            }
        }
        for (int i = 0; i < numDimensions; ++i) {
            for (int x = 1; x < recorded.size(); ++x) {
                matrix[i][x][0] = Float.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < numDimensions; ++i) {
            for (int x = 1; x < reference.size(); ++x) {
                matrix[i][0][x] = Float.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < numDimensions; ++i) {
            matrix[i][0][0] = 0.0f;
        }

        float[] sum = new float[numDimensions];

        for (int i = 0; i < numDimensions; ++i) {
            sum[i] = computeBackward(i, recorded.size() - 1, reference.size() - 1);
        }

        float singleSum = 0.0f;

        for (int i = 0; i < numDimensions; ++i) {
            singleSum += sum[i];
        }
        return singleSum;
    }

    public float computeBackward(int dimension, int i, int j) {
        // If greater than window return distance
        if (!(matrix[dimension][i][j] < 0.0f)) {
            return matrix[dimension][i][j];
        }

        // Compute path in insertion manner
        if (computeBackward(dimension, i - 1, j) <= computeBackward(dimension, i, j - 1) &&
                computeBackward(dimension, i - 1, j) <= computeBackward(dimension, i - 1, j - 1) &&
                computeBackward(dimension, i - 1, j) < Float.POSITIVE_INFINITY) {

            matrix[dimension][i][j] = distance[dimension][i - 1][j - 1] + computeBackward(dimension, i - 1, j);
            // Compute path in deletion manner
        } else if (computeBackward(dimension, i, j - 1) <= computeBackward(dimension, i - 1, j) &&
                computeBackward(dimension, i, j - 1) <= computeBackward(dimension, i - 1, j - 1) &&
                computeBackward(dimension, i, j - 1) < Float.POSITIVE_INFINITY) {

            matrix[dimension][i][j] = distance[dimension][i - 1][j - 1] + computeBackward(dimension, i, j - 1);
            // Compute path in match manner
        } else if (computeBackward(dimension, i - 1, j - 1) <= computeBackward(dimension, i - 1, j) &&
                computeBackward(dimension, i - 1, j - 1) <= computeBackward(dimension, i, j - 1) &&
                computeBackward(dimension, i - 1, j - 1) < Float.POSITIVE_INFINITY) {

            matrix[dimension][i][j] = distance[dimension][i - 1][j - 1] + computeBackward(dimension, i - 1, j - 1);
        }
        return matrix[dimension][i][j];
    }
}
