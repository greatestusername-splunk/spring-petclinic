/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IBaseMethodInterceptorDelegator;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.MethodExecutionEnvironment;

public interface IMethodInterceptorDelegator
extends IBaseMethodInterceptorDelegator {
    public int getTransformationId();

    public void safeOnMethodBegin(MethodExecutionEnvironment var1);

    public void safeOnMethodEndNormal(MethodExecutionEnvironment var1);

    public void safeOnMethodEndException(MethodExecutionEnvironment var1);
}

