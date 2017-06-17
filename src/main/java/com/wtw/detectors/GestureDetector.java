package com.wtw.detectors;

import com.wtw.Device;
import com.wtw.distance.DistanceCalculator;
import lombok.Getter;
import lombok.Setter;

public abstract class GestureDetector {

    @Getter
    @Setter
    private Device device;

    @Getter
    private DistanceCalculator distanceCalculator;

    public GestureDetector(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public abstract void newMeasurement(float[] values, long time);
}
