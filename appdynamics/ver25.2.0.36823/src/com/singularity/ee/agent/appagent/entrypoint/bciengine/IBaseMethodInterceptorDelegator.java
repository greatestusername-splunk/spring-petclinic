/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;

public interface IBaseMethodInterceptorDelegator {
    public boolean isDisabled();

    public void disable();

    public void enable();

    public void setAgentDisabled(boolean var1);

    public void safeWarn(String var1, Throwable var2);

    public void setUsePrivilegedAction(boolean var1);

    public void safeError(Throwable var1);

    public void setLogger(IAgentBootLogger var1);
}

