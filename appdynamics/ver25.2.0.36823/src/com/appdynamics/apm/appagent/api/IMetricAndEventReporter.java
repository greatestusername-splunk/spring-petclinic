/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api;

import com.appdynamics.apm.appagent.api.DataScope;
import java.util.Map;
import java.util.Set;

public interface IMetricAndEventReporter {
    public void reportAverageMetric(String var1, long var2);

    public void reportSumMetric(String var1, long var2);

    public void reportObservedMetric(String var1, long var2);

    public void reportMetric(String var1, long var2, String var4, String var5, String var6);

    public void reportMetric(String var1, long var2, long var4, long var6, long var8, String var10, String var11, String var12);

    public void publishErrorEvent(String var1, Map<String, String> var2, boolean var3);

    public void publishErrorEvent(String var1, Map<String, String> var2, String var3, boolean var4);

    public void publishInfoEvent(String var1, Map<String, String> var2);

    public void publishInfoEvent(String var1, Map<String, String> var2, String var3);

    public void publishEvent(String var1, String var2, String var3, Map<String, String> var4);

    public boolean addSnapshotData(String var1, Object var2, Set<DataScope> var3);
}

