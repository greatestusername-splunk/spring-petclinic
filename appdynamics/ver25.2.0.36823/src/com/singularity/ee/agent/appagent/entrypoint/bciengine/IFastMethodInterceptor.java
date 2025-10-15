/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.MethodExecutionEnvironment;

public interface IFastMethodInterceptor {
    public Object onMethodBegin(Object var1, String var2, String var3, Object[] var4, int var5);

    public void onMethodEnd(Object var1, Object var2, String var3, String var4, Object[] var5, Throwable var6, Object var7, int var8);

    public void onMethodMiddle(Object var1, String var2, String var3, Object[] var4, Object[] var5, int var6, int var7, int var8);

    public int getInterceptorId();

    public boolean shouldPassParamsToOnMethodBegin();

    public boolean shouldCallOnMethodEnd();

    public boolean shouldPassReturnValueToOnMethodEnd();

    @Deprecated
    public void onMethodBegin(MethodExecutionEnvironment var1);

    @Deprecated
    public void onMethodEnd(MethodExecutionEnvironment var1);

    @Deprecated
    public void setTransformationId(int var1);

    @Deprecated
    public int getTransformationId();

    public boolean skipReentrantCheck();

    public int getPriority();

    public boolean usesInlineInterceptor();
}

