/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template;

import com.appdynamics.instrumentation.sdk.ASDKPlugin;
import com.appdynamics.instrumentation.sdk.contexts.ISDKDataContext;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

public abstract class ADataCollector
extends ASDKPlugin {
    public abstract void storeData(Object var1, String var2, String var3, Object[] var4, Throwable var5, Object var6, ISDKDataContext var7) throws ReflectorException;

    public abstract boolean addToSnapshot();

    public abstract boolean addToAnalytics();
}

