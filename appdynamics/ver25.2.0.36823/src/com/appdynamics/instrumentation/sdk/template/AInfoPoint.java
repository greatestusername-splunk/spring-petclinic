/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template;

import com.appdynamics.instrumentation.sdk.ASDKPlugin;
import com.appdynamics.instrumentation.sdk.contexts.ISDKMetricContext;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

public abstract class AInfoPoint
extends ASDKPlugin {
    public abstract void storeMetrics(Object var1, String var2, String var3, Object[] var4, Throwable var5, Object var6, ISDKMetricContext var7) throws ReflectorException;
}

