/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template;

import com.appdynamics.instrumentation.sdk.ASDKPlugin;

public abstract class AGenericInterceptor
extends ASDKPlugin {
    public abstract Object onMethodBegin(Object var1, String var2, String var3, Object[] var4);

    public abstract void onMethodEnd(Object var1, Object var2, String var3, String var4, Object[] var5, Throwable var6, Object var7);

    public int getInterceptorId() {
        return -1;
    }
}

