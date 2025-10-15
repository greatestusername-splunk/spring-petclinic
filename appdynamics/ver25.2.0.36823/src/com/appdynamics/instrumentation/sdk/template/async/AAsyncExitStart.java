/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template.async;

import com.appdynamics.instrumentation.sdk.AOutOfProcessCorrelator;
import com.appdynamics.instrumentation.sdk.contexts.ISDKDataContext;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;
import java.util.Map;

public abstract class AAsyncExitStart
extends AOutOfProcessCorrelator {
    public abstract void marshalTransactionContext(String var1, Object var2, String var3, String var4, Object[] var5, Throwable var6, Object var7, ISDKUserContext var8) throws ReflectorException;

    public abstract Map<String, String> identifyBackend(Object var1, String var2, String var3, Object[] var4, Throwable var5, Object var6, ISDKUserContext var7) throws ReflectorException;

    public abstract Object getAsyncObject(Object var1, Object[] var2, Object var3);

    public boolean resolveToNode() {
        return true;
    }

    public boolean identifyOnEnd() {
        return true;
    }

    public boolean getAsyncObjectOnEnd() {
        return true;
    }

    public boolean getAsyncObjectOnExeption() {
        return false;
    }

    public void addSnapshotData(Object invokedObject, String className, String methodName, Object[] paramValues, Throwable thrownException, Object returnValue, ISDKUserContext context, ISDKDataContext dataContext) throws ReflectorException {
    }
}

