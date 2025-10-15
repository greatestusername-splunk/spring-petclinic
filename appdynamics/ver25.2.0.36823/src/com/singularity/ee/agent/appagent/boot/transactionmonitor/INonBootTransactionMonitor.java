/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.transactionmonitor;

import com.singularity.ee.agent.appagent.boot.transactionmonitor.IBootTransactionContext;

public interface INonBootTransactionMonitor {
    public IBootTransactionContext getBootCurrentTransaction();
}

