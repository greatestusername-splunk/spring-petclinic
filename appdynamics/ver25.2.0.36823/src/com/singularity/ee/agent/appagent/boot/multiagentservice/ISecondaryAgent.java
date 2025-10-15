/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.multiagentservice;

import com.singularity.ee.agent.appagent.boot.multiagentservice.IBrokenConnectionListener;
import com.singularity.ee.agent.appagent.boot.multiagentservice.ISecondaryAgentConnection;
import com.singularity.ee.agent.appagent.boot.multiagentservice.UnableToConnectException;

public interface ISecondaryAgent {
    public String getAppName();

    public String getTierName();

    public String getNodeName();

    public ISecondaryAgentConnection connectTo(String var1, String var2, String var3, long var4) throws UnableToConnectException;

    public ISecondaryAgentConnection connectTo(String var1, String var2, String var3, long var4, IBrokenConnectionListener var6) throws UnableToConnectException;
}

