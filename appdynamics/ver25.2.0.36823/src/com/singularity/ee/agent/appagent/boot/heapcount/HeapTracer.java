/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.heapcount;

import com.singularity.ee.agent.appagent.boot.heapcount.AHeapCounter;
import com.singularity.ee.agent.appagent.boot.heapcount.ASlowOrTraceHeapCounter;
import com.singularity.ee.agent.appagent.boot.heapcount.IBootHeapAllocationTracer;
import java.util.Collection;

public class HeapTracer
extends ASlowOrTraceHeapCounter {
    private volatile boolean[] traceActiveOnThread;

    HeapTracer(Collection<String> classNamePrefixes, int maxThreadId) {
        super(classNamePrefixes);
        this.initThreadStates(maxThreadId);
        this.traceActiveOnThread = new boolean[maxThreadId];
        HeapTracer.registerAsThreadStateListener();
    }

    @Override
    public AHeapCounter.Mode getCurrentMode() {
        return AHeapCounter.Mode.TRACE;
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
    protected void reallocateCounterSpecificArrays(int newSize) {
        boolean[] traceActiveOnThread = this.traceActiveOnThread;
        if (traceActiveOnThread != null && newSize > traceActiveOnThread.length) {
            boolean[] newTraceActiveOnThread = new boolean[newSize];
            System.arraycopy(traceActiveOnThread, 0, newTraceActiveOnThread, 0, traceActiveOnThread.length);
            this.traceActiveOnThread = newTraceActiveOnThread;
        }
        super.reallocateCounterSpecificArrays(newSize);
    }

    @Override
    public void incrementCount(Object obj) {
        long threadId;
        boolean[] traceActiveOnThread = this.traceActiveOnThread;
        Collection classNamesToTrace = this.classNamePrefixes;
        if (traceActiveOnThread != null && (threadId = Thread.currentThread().getId()) <= Integer.MAX_VALUE) {
            int iThreadId = (int)threadId;
            if (iThreadId < traceActiveOnThread.length) {
                if (!traceActiveOnThread[iThreadId]) {
                    byte[] threadStates;
                    traceActiveOnThread[iThreadId] = true;
                    IBootHeapAllocationTracer tracer = AHeapCounter.tracer;
                    if (tracer != null && (threadStates = this.threadStates) != null && iThreadId < threadStates.length) {
                        if (classNamesToTrace == null || classNamesToTrace.size() == 0) {
                            tracer.traceAllocation(obj, threadStates[iThreadId]);
                        } else {
                            String className = obj.getClass().getName();
                            for (String nextClassName : classNamesToTrace) {
                                if (!className.startsWith(nextClassName)) continue;
                                tracer.traceAllocation(obj, threadStates[iThreadId]);
                                break;
                            }
                        }
                    }
                    traceActiveOnThread[iThreadId] = false;
                }
            } else if (iThreadId > this.maxThreadId) {
                this.maxThreadId = iThreadId;
            }
        }
    }

    @Override
    int getMaximumThreadId() {
        return this.traceActiveOnThread.length;
    }
}

