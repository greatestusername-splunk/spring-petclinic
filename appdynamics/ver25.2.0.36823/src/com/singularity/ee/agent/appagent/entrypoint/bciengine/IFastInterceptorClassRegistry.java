/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IFastMethodInterceptor;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IInterceptorClassRegistry;

public interface IFastInterceptorClassRegistry
extends IInterceptorClassRegistry {
    public int getActivityMarkerInterceptorId();

    public int getActivityProcessingLoopInterceptorId();

    public int getActivityProducerInProcessCorrelationInterceptorId();

    public int getActivityProducerInterceptorId();

    public int getActivityProducerMultiExitInterceptorId();

    public int getActivityProducerOutgoingCorrelationInterceptorId();

    public void bindFastInterceptorInstance(IFastMethodInterceptor var1);

    public void bindFastInterceptorInstance(IFastMethodInterceptor var1, boolean var2);

    public boolean isRegistered(String var1);

    public Class<? extends IFastMethodInterceptor> getFastClass(String var1);

    public int getNewInterceptorId();

    public void unbindFastInterceptorInstance(IFastMethodInterceptor var1);

    public int getFastNoReentrantCheckTestInterceptorOrdinal();

    public int getOnDemandCollectionCaptureInterceptorOrdinal();

    public boolean isDisableCollectionCaptureInstrumentation();

    public int getActivityTracesThatAreRunning();

    public int getMemoryTrackerInterceptorOrdinal();

    public int incrementActivityTracesThatAreRunning(int var1);

    public void setDisableCollectionCaptureInstrumentation(boolean var1);

    public boolean shouldNotDispatchFMID(int var1);

    public IFastMethodInterceptor getFastInterceptorInstance(int var1);

    public boolean shouldIgnoreReEntrantCheckOnBegin(int var1);
}

