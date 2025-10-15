/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.heapcount;

import com.singularity.ee.agent.appagent.boot.heapcount.AHeapCounter;

public class NullHeapCounter
extends AHeapCounter {
    private final int maxThreadId;

    NullHeapCounter(int maxThreadId) {
        this.maxThreadId = maxThreadId;
        NullHeapCounter.deregisterAsThreadStateListener();
    }

    @Override
    public AHeapCounter.Mode getCurrentMode() {
        return AHeapCounter.Mode.OFF;
    }

    @Override
    void setInterceptorRunningOnThread(boolean isRunningInterceptor) {
    }

    @Override
    protected void reallocateCounterSpecificArrays(int newSize) {
    }

    @Override
    void setTransactionStateForThread(boolean tranRunningOnThread) {
    }

    @Override
    public long getForCurrentThread() {
        return 0L;
    }

    @Override
    public long getForThreadId(long threadId) {
        return 0L;
    }

    @Override
    public int reallocateCountArrayIfNecessary() {
        return 0;
    }

    @Override
    int getMaximumThreadId() {
        return this.maxThreadId;
    }
}

