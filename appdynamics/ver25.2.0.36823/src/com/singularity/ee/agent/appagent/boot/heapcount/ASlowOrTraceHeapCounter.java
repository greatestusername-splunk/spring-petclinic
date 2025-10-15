/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.heapcount;

import com.singularity.ee.agent.appagent.boot.heapcount.AHeapCounter;
import java.util.Arrays;
import java.util.Collection;

abstract class ASlowOrTraceHeapCounter
extends AHeapCounter {
    protected Collection<String> classNamePrefixes;
    public volatile byte[] threadStates;

    protected ASlowOrTraceHeapCounter(Collection<String> classNamePrefixes) {
        this.classNamePrefixes = classNamePrefixes;
        ASlowOrTraceHeapCounter.registerAsThreadStateListener();
    }

    @Override
    void setInterceptorRunningOnThread(boolean isRunningInterceptor) {
        long threadId = Thread.currentThread().getId();
        if (threadId <= Integer.MAX_VALUE) {
            int iThreadId = (int)threadId;
            byte[] threadStates = this.threadStates;
            if (threadStates != null) {
                if (threadId < (long)threadStates.length) {
                    threadStates[iThreadId] = this.setNewInterceptorState(threadStates[iThreadId], isRunningInterceptor);
                } else if (threadId > (long)this.maxThreadId) {
                    this.maxThreadId = (int)threadId;
                }
            }
        }
    }

    @Override
    void setTransactionStateForThread(boolean tranRunningOnThread) {
        long threadId = Thread.currentThread().getId();
        if (threadId <= Integer.MAX_VALUE) {
            int iThreadId = (int)threadId;
            byte[] threadStates = this.threadStates;
            if (threadStates != null) {
                if (threadId < (long)threadStates.length) {
                    threadStates[iThreadId] = this.setNewThreadState(threadStates[iThreadId], tranRunningOnThread);
                } else if (threadId > (long)this.maxThreadId) {
                    this.maxThreadId = (int)threadId;
                }
            }
            if (tranRunningOnThread) {
                this.resetForThreadId();
            }
        }
    }

    @Override
    protected void setNewClassNamePrefixes(Collection<String> classNamePrefixes) {
        this.classNamePrefixes = classNamePrefixes;
    }

    protected void initThreadStates(int threadArraySize) {
        byte[] newThreadStates = new byte[threadArraySize];
        Arrays.fill(newThreadStates, (byte)1);
        this.threadStates = newThreadStates;
    }

    @Override
    protected void reallocateCounterSpecificArrays(int newSize) {
        if (newSize > this.threadStates.length) {
            byte[] newThreadStates = new byte[newSize];
            Arrays.fill(newThreadStates, (byte)1);
            System.arraycopy(this.threadStates, 0, newThreadStates, 0, this.threadStates.length);
            this.threadStates = newThreadStates;
        }
    }
}

