/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.multiagentservice;

import com.singularity.ee.agent.appagent.boot.multiagentservice.ISecondaryAgent;
import java.util.concurrent.BlockingQueue;

public interface ISecondaryAgentConnection {
    public ISecondaryAgent getAgentConnectedTo();

    public BlockingQueue<Object> getSendQueue();

    public BlockingQueue<Object> getReceiveQueue();

    public void breakConnection();
}

