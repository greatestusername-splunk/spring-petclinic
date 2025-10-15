/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api.debug;

import com.appdynamics.apm.appagent.api.debug.IConfigMatcher;
import com.appdynamics.apm.appagent.api.debug.ILogMatch;
import com.appdynamics.apm.appagent.api.debug.ITransformationMatch;
import com.appdynamics.apm.appagent.api.debug.ITransientConfigMatcher;
import java.util.concurrent.CountDownLatch;

public interface IAgentCommunicator {
    public boolean waitForConfig(IConfigMatcher var1, long var2);

    public void notifyOnConfigChange(CountDownLatch var1, IConfigMatcher var2);

    public void notifyOnTransactionConfig(CountDownLatch var1, String var2);

    public void notifyOnTransientConfigChange(CountDownLatch var1, ITransientConfigMatcher var2);

    public void notifyOnTransientTxnConfigCustomRule(CountDownLatch var1, String var2, String var3);

    public void notifyOnTransientTxnEntryPoint(CountDownLatch var1, String var2, boolean var3);

    public void notifyOnTransientTxnDiscovery(CountDownLatch var1, String var2, boolean var3);

    public void notifyOnTransientTxnConfigExcludeRule(CountDownLatch var1, String var2, String var3);

    public void notifyOnTransientFilterDetection(CountDownLatch var1, String var2, boolean var3);

    public void notifyOnTransactionConfig(CountDownLatch var1, String var2, String var3);

    public void notifyOnNodePropertyChange(CountDownLatch var1, String var2, String var3);

    public void notifyOnInstrumentationLevelChange(CountDownLatch var1, String var2);

    public void notifyOnTransformation(CountDownLatch var1, ITransformationMatch ... var2);

    public void notifyOnLog(CountDownLatch var1, ILogMatch ... var2);

    public String[] getAgentLog(int var1);
}

