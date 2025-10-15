/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template.async;

import com.appdynamics.instrumentation.sdk.ASDKPlugin;
import com.appdynamics.instrumentation.sdk.contexts.ISDKDataContext;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

public abstract class AAsyncExitEnd
extends ASDKPlugin {
    public abstract Object getAsyncObject(Object var1, Object[] var2, Object var3);

    public final boolean getAsyncObjectOnEnd() {
        return false;
    }

    public boolean getAsyncObjectOnExeption() {
        return true;
    }

    public void addSnapshotData(Object invokedObject, String className, String methodName, Object[] paramValues, Throwable thrownException, Object returnValue, ISDKUserContext context, ISDKDataContext dataContext) throws ReflectorException {
    }
}

