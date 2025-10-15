/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api;

import java.util.Map;

public interface EventPublisher {
    public void publishErrorEvent(String var1, Map<String, String> var2, boolean var3);

    public void publishErrorEvent(String var1, Map<String, String> var2, String var3, boolean var4);

    public void publishInfoEvent(String var1, Map<String, String> var2);

    public void publishInfoEvent(String var1, Map<String, String> var2, String var3);

    public void publishEvent(String var1, String var2, String var3, Map<String, String> var4);
}

