/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.template;

import com.appdynamics.instrumentation.sdk.AOutOfProcessCorrelator;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

public abstract class AEntry
extends AOutOfProcessCorrelator {
    public abstract String unmarshalTransactionContext(Object var1, String var2, String var3, Object[] var4, ISDKUserContext var5) throws ReflectorException;

    public abstract String getBusinessTransactionName(Object var1, String var2, String var3, Object[] var4, ISDKUserContext var5) throws ReflectorException;
}

