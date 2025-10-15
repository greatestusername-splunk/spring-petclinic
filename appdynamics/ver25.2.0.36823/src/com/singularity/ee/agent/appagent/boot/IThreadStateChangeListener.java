/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot;

public interface IThreadStateChangeListener {
    public void setInterceptorRunningOnThread(boolean var1);

    public void setTransactionStateForThread(boolean var1);
}

