/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.AMethodInterceptor;

public interface IInterceptorClassRegistry {
    public void bindClass(String var1, Class<? extends AMethodInterceptor> var2);

    public String getInterceptorName(Class<? extends AMethodInterceptor> var1);

    public String getInterceptorDescription(Class<? extends AMethodInterceptor> var1);

    public int getInterceptorPriority(Class<? extends AMethodInterceptor> var1);

    public String getInterceptorClassName(String var1);
}

