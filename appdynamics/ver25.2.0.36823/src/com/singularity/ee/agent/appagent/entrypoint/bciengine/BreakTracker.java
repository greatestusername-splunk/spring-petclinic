/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

public class BreakTracker {
    public static final int TRANSFORMATION_LIMIT = 5000;
    private static final boolean[] BREAKER_ARRAY = new boolean[5001];

    public static boolean[] getBreakerArray() {
        return BREAKER_ARRAY;
    }

    protected boolean isValidTransformationId(int transformationId) {
        return transformationId > 0 && transformationId <= 5000;
    }

    public void throwBreaker(int transformationId) {
        if (!this.isValidTransformationId(transformationId)) {
            return;
        }
        BreakTracker.BREAKER_ARRAY[transformationId] = true;
    }

    public void resetBreaker(int transformationId) {
        if (!this.isValidTransformationId(transformationId)) {
            return;
        }
        BreakTracker.BREAKER_ARRAY[transformationId] = false;
    }

    public static boolean isDisabled(int transformationId) {
        return BREAKER_ARRAY[transformationId];
    }

    public void reset() {
        for (int i = 0; i < BREAKER_ARRAY.length; ++i) {
            BreakTracker.BREAKER_ARRAY[i] = false;
        }
    }

    public void onTransactionMonitorHotDisable() {
        this.reset();
    }

    public void onTransactionMonitorHotEnable() {
    }
}

