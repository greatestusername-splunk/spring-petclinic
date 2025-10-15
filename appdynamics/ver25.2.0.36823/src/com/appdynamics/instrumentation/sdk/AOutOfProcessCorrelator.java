/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk;

import com.appdynamics.instrumentation.sdk.ASDKPlugin;

public abstract class AOutOfProcessCorrelator
extends ASDKPlugin {
    public abstract boolean isCorrelationEnabled();

    public abstract boolean isCorrelationEnabledForOnMethodBegin();
}

