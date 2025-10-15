/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.transactionmonitor;

import com.singularity.ee.agent.appagent.boot.transactionmonitor.IBootTransactionContext;
import com.singularity.ee.agent.appagent.boot.transactionmonitor.INonBootTransactionMonitor;

public class TransactionMonitorBoot
implements INonBootTransactionMonitor {
    public static String CONSTANT1 = "methodExitTemplate - start";
    public static String CONSTANT2 = "methodExitTemplate - got CTC";
    public static String CONSTANT3 = "methodExitTemplate - Start time is ";
    public static String CONSTANT4 = "methodEntryTemplate - returned from getBootCurrentTransaction()";
    public static String CONSTANT5 = "methodEntryTemplate - got CTC ";
    public static String CONSTANT6 = "methodEntryTemplate - Start time is ";
    public static String CONSTANT7 = "Caught exception: ";
    private static volatile TransactionMonitorBoot instance;
    private INonBootTransactionMonitor nonBootTransactionMonitor;

    public static TransactionMonitorBoot getInstance() {
        if (instance == null) {
            TransactionMonitorBoot.getInstanceSync();
        }
        return instance;
    }

    private static synchronized TransactionMonitorBoot getInstanceSync() {
        if (instance == null) {
            instance = new TransactionMonitorBoot();
        }
        return instance;
    }

    private TransactionMonitorBoot() {
    }

    public void setNonBootTransactionMonitor(INonBootTransactionMonitor nonBootTransactionMonitor) {
        this.nonBootTransactionMonitor = nonBootTransactionMonitor;
    }

    @Override
    public IBootTransactionContext getBootCurrentTransaction() {
        if (this.nonBootTransactionMonitor != null) {
            IBootTransactionContext returnObject = this.nonBootTransactionMonitor.getBootCurrentTransaction();
            return returnObject;
        }
        return null;
    }
}

