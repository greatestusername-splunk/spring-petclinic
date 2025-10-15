/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IBaseMethodInterceptorDelegator;

public interface IFastMethodInterceptorDelegator
extends IBaseMethodInterceptorDelegator {
    public int getTransformationId();

    @Override
    public void setAgentDisabled(boolean var1);

    public Object safeOnMethodBegin(int var1, int var2, Object var3, String var4, String var5, Object[] var6);

    public Object safeOnMethodBeginNoReentrantCheck(int var1, int var2, Object var3, String var4, String var5, Object[] var6);

    public void safeOnMethodEnd(int var1, int var2, Object var3, String var4, String var5, Object[] var6, Object var7, Throwable var8, Object var9);

    public void safeOnMethodEndNoReentrantCheck(int var1, int var2, Object var3, String var4, String var5, Object[] var6, Object var7, Throwable var8, Object var9);

    public void safeOnMethodMiddle(int var1, int var2, int var3, Object var4, String var5, String var6, Object[] var7, Object[] var8, int var9);
}

