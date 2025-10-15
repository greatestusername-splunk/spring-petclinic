/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

public interface IInlineInterceptor {
    public void methodStart(Object var1, String var2, String var3, Object[] var4, int var5, Object var6);

    public void methodEnd(Object var1, String var2, String var3, Object[] var4, Throwable var5, Object var6, int var7, Object var8);
}

