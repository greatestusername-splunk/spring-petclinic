/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.heapcount;

import com.singularity.ee.agent.appagent.boot.heapcount.AHeapCounter;
import com.singularity.ee.agent.appagent.boot.heapcount.ASlowOrTraceHeapCounter;
import java.util.Collection;
import java.util.Iterator;

public class SlowHeapCounter
extends ASlowOrTraceHeapCounter {
    private volatile AHeapCounter.AllocationCountByClassPrefix[][] allocationCountByClassPrefixes;

    SlowHeapCounter(Collection<String> classNamePrefixes, int maxThreadId) {
        super(classNamePrefixes);
        this.allocationCountByClassPrefixes = SlowHeapCounter.createCountByThreadIdMatrix(classNamePrefixes, maxThreadId);
        this.initThreadStates(maxThreadId);
    }

    @Override
    public AHeapCounter.Mode getCurrentMode() {
        return AHeapCounter.Mode.SLOW;
    }

    private static AHeapCounter.AllocationCountByClassPrefix[][] createCountByThreadIdMatrix(Collection<String> classNamePrefixes, int majorArrayLength) {
        AHeapCounter.AllocationCountByClassPrefix[][] returnMatrix = new AHeapCounter.AllocationCountByClassPrefix[majorArrayLength][];
        int minorArrayLength = classNamePrefixes != null ? classNamePrefixes.size() + 1 : 1;
        for (int i = 0; i < majorArrayLength; ++i) {
            returnMatrix[i] = new AHeapCounter.AllocationCountByClassPrefix[minorArrayLength];
            int j = 0;
            if (classNamePrefixes != null) {
                Iterator<String> iter = classNamePrefixes.iterator();
                while (iter.hasNext()) {
                    returnMatrix[i][j++] = new AHeapCounter.AllocationCountByClassPrefix(iter.next());
                }
            }
            returnMatrix[i][j] = new AHeapCounter.AllocationCountByClassPrefix();
        }
        return returnMatrix;
    }

    @Override
    public void incrementCount(Object obj) {
        long threadId;
        AHeapCounter.AllocationCountByClassPrefix[][] allocationCountByClassPrefixes = this.allocationCountByClassPrefixes;
        byte[] threadStates = this.threadStates;
        if (allocationCountByClassPrefixes != null && threadStates != null && (threadId = Thread.currentThread().getId()) <= Integer.MAX_VALUE) {
            int iThreadId = (int)threadId;
            if (iThreadId < allocationCountByClassPrefixes.length && iThreadId < threadStates.length) {
                if (!this.countAllocationsInInterceptors && threadStates[iThreadId] == 3 || this.countAllocationsInInterceptors && threadStates[iThreadId] == 4) {
                    SlowHeapCounter.incrementBucketForThreadId(iThreadId, allocationCountByClassPrefixes, obj.getClass().getName());
                }
            } else if (iThreadId > this.maxThreadId) {
                this.maxThreadId = iThreadId;
            }
        }
    }

    private static void incrementBucketForThreadId(int threadId, AHeapCounter.AllocationCountByClassPrefix[][] allocationCountByClassPrefixes, String className) {
        AHeapCounter.AllocationCountByClassPrefix[] arrayOfBuckets = allocationCountByClassPrefixes[threadId];
        int arrayOfBucketsLength = arrayOfBuckets.length;
        if (arrayOfBucketsLength == 1) {
            ++arrayOfBuckets[0].allocationCount;
        } else {
            for (int i = 0; i < arrayOfBucketsLength; ++i) {
                AHeapCounter.AllocationCountByClassPrefix nextBucket = arrayOfBuckets[i];
                if (!nextBucket.matches(className)) continue;
                ++nextBucket.allocationCount;
                break;
            }
        }
    }

    @Override
    protected void resetForThreadId() {
        long threadId = Thread.currentThread().getId();
        AHeapCounter.AllocationCountByClassPrefix[][] allocationCountByClassPrefixes = this.allocationCountByClassPrefixes;
        if (allocationCountByClassPrefixes != null) {
            if (threadId < (long)allocationCountByClassPrefixes.length) {
                for (AHeapCounter.AllocationCountByClassPrefix nextBucket : allocationCountByClassPrefixes[(int)threadId]) {
                    nextBucket.allocationCount = 0L;
                }
            } else if (threadId > (long)this.maxThreadId) {
                this.maxThreadId = (int)threadId;
            }
        }
    }

    @Override
    public long getForCurrentThread() {
        long lReturnValue = 0L;
        lReturnValue = this.getForThreadId(Thread.currentThread().getId());
        return lReturnValue;
    }

    @Override
    public long getForThreadId(long threadId) {
        long lReturnValue = 0L;
        AHeapCounter.AllocationCountByClassPrefix[][] allocationCountByClassPrefixes = this.allocationCountByClassPrefixes;
        if (allocationCountByClassPrefixes != null && threadId <= Integer.MAX_VALUE) {
            if (threadId < (long)allocationCountByClassPrefixes.length) {
                AHeapCounter.AllocationCountByClassPrefix[] allocationCountsForThreadId = allocationCountByClassPrefixes[(int)threadId];
                if (allocationCountsForThreadId.length == 1) {
                    lReturnValue = allocationCountsForThreadId[0].allocationCount;
                } else {
                    for (AHeapCounter.AllocationCountByClassPrefix nextBucket : allocationCountsForThreadId) {
                        lReturnValue += nextBucket.allocationCount;
                    }
                }
            } else if (threadId > (long)this.maxThreadId) {
                this.maxThreadId = (int)threadId;
            }
        }
        return lReturnValue;
    }

    @Override
    public AHeapCounter.AllocationCountByClassPrefix[] getAllAllocationCountsForCurrentThread() {
        return this.getAllAllocationCountsForThreadId(Thread.currentThread().getId());
    }

    @Override
    public AHeapCounter.AllocationCountByClassPrefix[] getAllAllocationCountsForThreadId(long threadId) {
        AHeapCounter.AllocationCountByClassPrefix[] returnArray = null;
        AHeapCounter.AllocationCountByClassPrefix[][] countByThreadId = this.allocationCountByClassPrefixes;
        if (countByThreadId != null && threadId <= Integer.MAX_VALUE) {
            if (threadId < (long)countByThreadId.length) {
                AHeapCounter.AllocationCountByClassPrefix[] allocationCountsForThreadId = countByThreadId[(int)threadId];
                returnArray = new AHeapCounter.AllocationCountByClassPrefix[allocationCountsForThreadId.length];
                for (int i = 0; i < returnArray.length; ++i) {
                    returnArray[i] = (AHeapCounter.AllocationCountByClassPrefix)allocationCountsForThreadId[i].clone();
                }
            } else if (threadId > (long)this.maxThreadId) {
                this.maxThreadId = (int)threadId;
            }
        }
        return returnArray;
    }

    @Override
    protected void reallocateCounterSpecificArrays(int newSize) {
        if (newSize > this.allocationCountByClassPrefixes.length) {
            int minorArraySize = this.allocationCountByClassPrefixes[0].length;
            AHeapCounter.AllocationCountByClassPrefix[][] newCountByThreadId = new AHeapCounter.AllocationCountByClassPrefix[newSize][];
            System.arraycopy(this.allocationCountByClassPrefixes, 0, newCountByThreadId, 0, this.allocationCountByClassPrefixes.length);
            for (int i = this.allocationCountByClassPrefixes.length; i < newSize; ++i) {
                newCountByThreadId[i] = new AHeapCounter.AllocationCountByClassPrefix[minorArraySize];
                for (int j = 0; j < minorArraySize; ++j) {
                    newCountByThreadId[i][j] = new AHeapCounter.AllocationCountByClassPrefix(this.allocationCountByClassPrefixes[0][j].classPrefix);
                }
            }
            this.allocationCountByClassPrefixes = newCountByThreadId;
            this.initThreadStates(newSize);
        }
        super.reallocateCounterSpecificArrays(newSize);
    }

    @Override
    int getMaximumThreadId() {
        return this.allocationCountByClassPrefixes.length;
    }

    @Override
    protected void setNewClassNamePrefixes(Collection<String> classNamePrefixes) {
        this.allocationCountByClassPrefixes = SlowHeapCounter.createCountByThreadIdMatrix(classNamePrefixes, this.allocationCountByClassPrefixes.length);
        super.setNewClassNamePrefixes(classNamePrefixes);
    }
}

