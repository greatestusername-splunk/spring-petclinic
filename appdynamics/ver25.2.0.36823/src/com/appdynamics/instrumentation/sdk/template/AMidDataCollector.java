/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template;

import com.appdynamics.instrumentation.sdk.ASDKPlugin;
import com.appdynamics.instrumentation.sdk.contexts.ISDKDataContext;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

public abstract class AMidDataCollector
extends ASDKPlugin {
    public abstract void storeData(Object var1, String var2, String var3, Object[] var4, Object[] var5, int var6, ISDKDataContext var7) throws ReflectorException;

    public abstract boolean addToSnapshot();

    public abstract boolean addToAnalytics();
}

