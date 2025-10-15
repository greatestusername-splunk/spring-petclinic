/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.toolbox.reflection;

import com.appdynamics.instrumentation.sdk.toolbox.reflection.OperationParams;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

public interface IReflector {
    public <E> E execute(ClassLoader var1, Object var2, Object[] ... var3) throws ReflectorException;

    public <E> E execute(ClassLoader var1, Object var2, OperationParams var3) throws ReflectorException;
}

