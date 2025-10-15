/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api.bootstrap;

import com.appdynamics.agent.api.EventPublisher;
import com.appdynamics.agent.api.MetricPublisher;
import com.appdynamics.agent.api.bootstrap.IApiTransactionDelegate;

public interface IApiBootstrapFacade {
    public MetricPublisher getMetricPublisher();

    public EventPublisher getEventPublisher();

    public IApiTransactionDelegate getTransactionDelegate();

    public boolean isAgentCompatible(int var1);
}

