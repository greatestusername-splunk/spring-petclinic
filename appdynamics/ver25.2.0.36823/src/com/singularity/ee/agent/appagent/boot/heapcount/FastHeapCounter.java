/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.heapcount;

import com.singularity.ee.agent.appagent.boot.heapcount.AHeapCounter;

class FastHeapCounter
extends AHeapCounter {
    private volatile AHeapCounter.FastThreadStateAndCount[] fastCountPerThread;

    FastHeapCounter(int maxThreadId) {
        this.fastCountPerThread = new AHeapCounter.FastThreadStateAndCount[maxThreadId];
        for (int i = 0; i < this.fastCountPerThread.length; ++i) {
            this.fastCountPerThread[i] = new AHeapCounter.FastThreadStateAndCount();
        }
        FastHeapCounter.registerAsThreadStateListener();
    }

    @Override
    public AHeapCounter.Mode getCurrentMode() {
        return AHeapCounter.Mode.FAST;
    }

    @Override
    void setInterceptorRunningOnThread(boolean isRunningInterceptor) {
        long threadId = Thread.currentThread().getId();
        if (threadId <= Integer.MAX_VALUE) {
            int iThreadId = (int)threadId;
            AHeapCounter.FastThreadStateAndCount[] fastCountPerThread = this.fastCountPerThread;
            if (fastCountPerThread != null) {
                if (iThreadId < fastCountPerThread.length) {
                    fastCountPerThread[iThreadId].state = this.setNewInterceptorState(fastCountPerThread[iThreadId].state, isRunningInterceptor);
                } else if (threadId > (long)this.maxThreadId) {
                    this.maxThreadId = (int)threadId;
                }
            }
        }
    }

    @Override
    protected void resetForThreadId() {
        long threadId = Thread.currentThread().getId();
        AHeapCounter.FastThreadStateAndCount[] fastCountPerThread = this.fastCountPerThread;
        if (fastCountPerThread != null) {
            if (threadId < (long)fastCountPerThread.length) {
                fastCountPerThread[(int)threadId].allocationCount = 0L;
            } else if (threadId > (long)this.maxThreadId) {
                this.maxThreadId = (int)threadId;
            }
        }
    }

    @Override
    protected void setTransactionStateForThread(boolean tranRunningOnThread) {
        long threadId = Thread.currentThread().getId();
        if (threadId <= Integer.MAX_VALUE) {
            int iThreadId = (int)threadId;
            AHeapCounter.FastThreadStateAndCount[] fastCountPerThread = this.fastCountPerThread;
            if (fastCountPerThread != null) {
                if (iThreadId < fastCountPerThread.length) {
                    fastCountPerThread[iThreadId].state = this.setNewThreadState(fastCountPerThread[iThreadId].state, tranRunningOnThread);
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
    public long getForCurrentThread() {
        long lReturnValue = this.getForThreadId(Thread.currentThread().getId());
        return lReturnValue;
    }

    @Override
    public long getForThreadId(long threadId) {
        long lReturnValue = 0L;
        AHeapCounter.FastThreadStateAndCount[] fastCountPerThread = this.fastCountPerThread;
        if (fastCountPerThread != null && threadId < Integer.MAX_VALUE) {
            if (threadId < (long)fastCountPerThread.length) {
                lReturnValue = fastCountPerThread[(int)threadId].allocationCount;
            } else if (threadId > (long)this.maxThreadId) {
                this.maxThreadId = (int)threadId;
            }
        }
        return lReturnValue;
    }

    @Override
    protected void reallocateCounterSpecificArrays(int newSize) {
        if (newSize > this.fastCountPerThread.length) {
            AHeapCounter.FastThreadStateAndCount[] newFastCountPerThread = new AHeapCounter.FastThreadStateAndCount[newSize];
            System.arraycopy(this.fastCountPerThread, 0, newFastCountPerThread, 0, this.fastCountPerThread.length);
            for (int i = this.fastCountPerThread.length; i < newSize; ++i) {
                newFastCountPerThread[i] = new AHeapCounter.FastThreadStateAndCount();
            }
            this.fastCountPerThread = newFastCountPerThread;
        }
    }

    @Override
    public AHeapCounter.FastThreadStateAndCount[] getFastThreadStateAndCount() {
        return this.fastCountPerThread;
    }

    @Override
    int getMaximumThreadId() {
        return this.fastCountPerThread.length;
    }

    @Override
    public void incrementCount(Object obj) {
    }
}

