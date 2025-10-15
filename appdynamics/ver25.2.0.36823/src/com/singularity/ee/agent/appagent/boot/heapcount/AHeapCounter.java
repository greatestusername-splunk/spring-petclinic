/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.heapcount;

import com.singularity.ee.agent.appagent.boot.IThreadStateChangeListener;
import com.singularity.ee.agent.appagent.boot.ThreadInterceptorStateChangeNotifier;
import com.singularity.ee.agent.appagent.boot.heapcount.FastHeapCounter;
import com.singularity.ee.agent.appagent.boot.heapcount.HeapTracer;
import com.singularity.ee.agent.appagent.boot.heapcount.IBootHeapAllocationTracer;
import com.singularity.ee.agent.appagent.boot.heapcount.NullHeapCounter;
import com.singularity.ee.agent.appagent.boot.heapcount.SlowHeapCounter;
import java.util.Collection;

public abstract class AHeapCounter {
    public static final byte THREAD_STATE_NO_TRAN_ON_THREAD = 1;
    public static final byte THREAD_STATE_NO_TRAN_ON_THREAD_RUNNING_INTERCEPTOR = 2;
    public static final byte THREAD_STATE_TRAN_ON_THREAD = 3;
    public static final byte THREAD_STATE_TRAN_ON_THREAD_RUNNING_INTERCEPTOR = 4;
    public volatile int maxThreadId;
    protected static final int DEFAULT_THREAD_ARRAY_SIZE = 500;
    protected static final long MAX_ARRAY_SIZE = 10000L;
    protected volatile boolean maxArraySizeReached;
    protected volatile boolean countAllocationsInInterceptors;
    private Collection<String> classNamePrefixes;
    protected volatile boolean[] traceActiveOnThread;
    protected static volatile IBootHeapAllocationTracer tracer;
    public static volatile AHeapCounter currentHeapCounter;
    private static volatile IThreadStateChangeListener threadStateChangeListener;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static void registerAsThreadStateListener() {
        if (threadStateChangeListener != null) return;
        Class<AHeapCounter> clazz = AHeapCounter.class;
        synchronized (AHeapCounter.class) {
            if (threadStateChangeListener != null) return;
            threadStateChangeListener = new IThreadStateChangeListener(){

                @Override
                public void setInterceptorRunningOnThread(boolean isRunningInterceptor) {
                    currentHeapCounter.setInterceptorRunningOnThread(isRunningInterceptor);
                }

                @Override
                public void setTransactionStateForThread(boolean tranRunningOnThread) {
                    currentHeapCounter.setTransactionStateForThread(tranRunningOnThread);
                }
            };
            ThreadInterceptorStateChangeNotifier.getInstance().registerNewListener(threadStateChangeListener);
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static void deregisterAsThreadStateListener() {
        if (threadStateChangeListener == null) return;
        Class<AHeapCounter> clazz = AHeapCounter.class;
        synchronized (AHeapCounter.class) {
            if (threadStateChangeListener == null) return;
            ThreadInterceptorStateChangeNotifier.getInstance().deregisterListener(threadStateChangeListener);
            threadStateChangeListener = null;
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    protected AHeapCounter() {
    }

    public abstract Mode getCurrentMode();

    public boolean isEnabled() {
        return this.getCurrentMode() != Mode.OFF;
    }

    public static void init() {
        AHeapCounter.reset(Mode.OFF, null, false);
    }

    public static synchronized void reset(Mode newMode, Collection<String> classNamePrefixes, boolean selfCounting) {
        if (newMode != currentHeapCounter.getCurrentMode()) {
            int size = AHeapCounter.getMaximumThreadIDForNewMode();
            switch (newMode) {
                case OFF: {
                    currentHeapCounter = new NullHeapCounter(size);
                    break;
                }
                case FAST: {
                    currentHeapCounter = new FastHeapCounter(size);
                    break;
                }
                case SLOW: {
                    currentHeapCounter = new SlowHeapCounter(classNamePrefixes, size);
                    break;
                }
                case TRACE: {
                    currentHeapCounter = new HeapTracer(classNamePrefixes, size);
                }
            }
        } else {
            currentHeapCounter.setNewClassNamePrefixes(classNamePrefixes);
        }
        AHeapCounter.currentHeapCounter.countAllocationsInInterceptors = selfCounting;
    }

    protected void setNewClassNamePrefixes(Collection<String> classNamePrefixes) {
    }

    private static int getMaximumThreadIDForNewMode() {
        int returnSize = currentHeapCounter.getMaximumThreadId();
        if (AHeapCounter.currentHeapCounter.maxThreadId > returnSize && (long)(returnSize = AHeapCounter.currentHeapCounter.maxThreadId * 2) > 10000L) {
            returnSize = 10000;
        }
        return returnSize;
    }

    public void incrementCount(Object obj) {
    }

    abstract void setInterceptorRunningOnThread(boolean var1);

    protected byte setNewInterceptorState(byte currentState, boolean isRunningInterceptor) {
        int returnState = currentState;
        if (isRunningInterceptor) {
            switch (currentState) {
                case 1: {
                    returnState = 2;
                    break;
                }
                case 3: {
                    returnState = 4;
                }
            }
        } else {
            switch (currentState) {
                case 2: {
                    returnState = 1;
                    break;
                }
                case 4: {
                    returnState = 3;
                }
            }
        }
        return (byte)returnState;
    }

    abstract void setTransactionStateForThread(boolean var1);

    protected void resetForThreadId() {
    }

    protected byte setNewThreadState(byte currentState, boolean tranRunningOnThread) {
        int returnState = currentState;
        if (tranRunningOnThread) {
            switch (currentState) {
                case 1: {
                    returnState = 3;
                    this.resetForThreadId();
                    break;
                }
                case 2: {
                    returnState = 4;
                    this.resetForThreadId();
                }
            }
        } else {
            switch (currentState) {
                case 3: {
                    returnState = 1;
                    break;
                }
                case 4: {
                    returnState = 2;
                }
            }
        }
        return (byte)returnState;
    }

    public abstract long getForCurrentThread();

    public abstract long getForThreadId(long var1);

    public AllocationCountByClassPrefix[] getAllAllocationCountsForCurrentThread() {
        return null;
    }

    public AllocationCountByClassPrefix[] getAllAllocationCountsForThreadId(long threadId) {
        return null;
    }

    public synchronized int reallocateCountArrayIfNecessary() {
        long newThreadArraySize = 0L;
        if (this.maxThreadId > 0 && !this.maxArraySizeReached) {
            newThreadArraySize = (long)this.maxThreadId * 2L;
            this.maxThreadId = 0;
            if (newThreadArraySize > 10000L) {
                newThreadArraySize = 10000L;
                this.maxArraySizeReached = true;
            }
            this.reallocateCounterSpecificArrays((int)newThreadArraySize);
        }
        return (int)newThreadArraySize;
    }

    protected abstract void reallocateCounterSpecificArrays(int var1);

    public boolean isCountAllocationsInInterceptors() {
        return this.countAllocationsInInterceptors;
    }

    public void traceAllocation(Object obj) {
    }

    public static synchronized void registerAllocationTracer(IBootHeapAllocationTracer tracer) {
        AHeapCounter.tracer = tracer;
    }

    public static synchronized void deregisterAllocationTracer(IBootHeapAllocationTracer tracer) {
        if (AHeapCounter.tracer == tracer) {
            AHeapCounter.tracer = null;
        }
    }

    public FastThreadStateAndCount[] getFastThreadStateAndCount() {
        return null;
    }

    public void setNewMaxThreadId(int newMaxThreadId) {
        if (newMaxThreadId > this.maxThreadId) {
            this.maxThreadId = newMaxThreadId;
        }
    }

    abstract int getMaximumThreadId();

    static {
        currentHeapCounter = new NullHeapCounter(500);
    }

    public static enum Mode {
        OFF,
        FAST,
        SLOW,
        TRACE;

    }

    public static class AllocationCountByClassPrefix
    implements Cloneable {
        final String classPrefix;
        long allocationCount;

        AllocationCountByClassPrefix() {
            this(null);
        }

        AllocationCountByClassPrefix(String classPrefix) {
            this.classPrefix = classPrefix;
        }

        boolean matches(String className) {
            return this.classPrefix == null ? true : className.startsWith(this.classPrefix);
        }

        public Object clone() {
            AllocationCountByClassPrefix returnObject = new AllocationCountByClassPrefix(this.classPrefix);
            returnObject.allocationCount = this.allocationCount;
            return returnObject;
        }

        public String getClassPrefix() {
            return this.classPrefix;
        }

        public long getAllocationCount() {
            return this.allocationCount;
        }
    }

    public static class FastThreadStateAndCount {
        public byte state = 1;
        public long allocationCount;

        FastThreadStateAndCount() {
        }
    }
}

