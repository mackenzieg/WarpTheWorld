package com.wtw.detectors;

import com.wtw.BuiltDevice;
import com.wtw.distance.DistanceCalculator;
import lombok.Getter;
import lombok.Setter;

public abstract class GestureDetector {

    @Getter
    @Setter
    private BuiltDevice device;

    @Getter
    private DistanceCalculator distanceCalculator;

    public GestureDetector(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public abstract void newMeasurement(float[] values, long time);
}
