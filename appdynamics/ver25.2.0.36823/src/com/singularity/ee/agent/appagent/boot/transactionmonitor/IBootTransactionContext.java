/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.transactionmonitor;

public interface IBootTransactionContext {
    public int startOfSocketRead();

    public int endOfSocketRead();

    public int startOfSocketWrite();

    public int endOfSocketWrite();

    public void socketBytesRead(long var1, long var3);

    public void socketBytesWritten(long var1, long var3);
}

