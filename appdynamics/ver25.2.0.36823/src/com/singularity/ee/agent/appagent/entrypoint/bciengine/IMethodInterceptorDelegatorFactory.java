/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IMethodInterceptorDelegator;

public interface IMethodInterceptorDelegatorFactory {
    public IMethodInterceptorDelegator createMethodDelegatorInterceptor(String[] var1, int[] var2);
}

