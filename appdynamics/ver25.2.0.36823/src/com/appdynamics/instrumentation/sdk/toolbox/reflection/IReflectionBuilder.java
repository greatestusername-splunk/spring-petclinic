/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.toolbox.reflection;

import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;

public interface IReflectionBuilder {
    public IReflectionBuilder loadClass(String var1);

    public IReflectionBuilder createObject(String var1, String ... var2);

    public IReflectionBuilder invokeStaticMethod(String var1, boolean var2, String ... var3);

    public IReflectionBuilder invokeInstanceMethod(String var1, boolean var2, String ... var3);

    public IReflectionBuilder accessFieldValue(String var1, boolean var2);

    public IReflector build();
}

