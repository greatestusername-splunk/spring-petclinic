/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.EUMMethodInterceptorDelegatorBoot;

public interface IEUMMethodInterceptorDelegator {
    public void safeParseAndInjectHeaderJS(Object var1, String var2, Object var3, int var4);

    public void safeParseAndInjectFooterJS(Object var1, String var2, Object var3, int var4);

    public void safeParseAndInjectFooterJS(Object var1, byte[] var2, String var3, Object var4, int var5);

    public void safeParseAndInjectHeaderJS(Object var1, byte[] var2, String var3, Object var4, int var5);

    public Object register(EUMMethodInterceptorDelegatorBoot.Injector var1);
}

