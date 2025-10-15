/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api;

public interface MetricPublisher {
    public void reportAverageMetric(String var1, long var2);

    public void reportSumMetric(String var1, long var2);

    public void reportObservedMetric(String var1, long var2);

    public void reportMetric(String var1, long var2, String var4, String var5, String var6);

    public void reportMetric(String var1, long var2, long var4, long var6, long var8, String var10, String var11, String var12);
}

