/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.api;

import com.appdynamics.agent.api.EventPublisher;
import com.appdynamics.agent.api.MetricPublisher;
import com.appdynamics.agent.api.bootstrap.IApiBootstrapFacade;
import com.appdynamics.agent.api.bootstrap.IApiTransactionDelegate;
import com.appdynamics.agent.api.bootstrap.NoOpTransactionDelegate;
import com.appdynamics.apm.appagent.api.NoOpInvocationHandler;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ApiBootstrapFacade
implements IApiBootstrapFacade {
    private static NoOpInvocationHandler noOpInvocationHandler = new NoOpInvocationHandler();
    private MetricPublisher metricPublisher = (MetricPublisher)Proxy.newProxyInstance(ApiBootstrapFacade.class.getClassLoader(), new Class[]{MetricPublisher.class}, (InvocationHandler)noOpInvocationHandler);
    private EventPublisher eventPublisher = (EventPublisher)Proxy.newProxyInstance(ApiBootstrapFacade.class.getClassLoader(), new Class[]{EventPublisher.class}, (InvocationHandler)noOpInvocationHandler);
    private IApiTransactionDelegate transactionDelegate = new NoOpTransactionDelegate();
    private static final int AGENT_API_COMPATIBILITY_LEVEL = 3;
    private static final ApiBootstrapFacade INSTANCE = new ApiBootstrapFacade();

    private ApiBootstrapFacade() {
    }

    @Override
    public MetricPublisher getMetricPublisher() {
        return this.metricPublisher;
    }

    @Override
    public EventPublisher getEventPublisher() {
        return this.eventPublisher;
    }

    @Override
    public IApiTransactionDelegate getTransactionDelegate() {
        return this.transactionDelegate;
    }

    @Override
    public boolean isAgentCompatible(int apiLevel) {
        return 3 >= apiLevel;
    }

    public void init(MetricPublisher metricPublisher, EventPublisher eventPublisher, IApiTransactionDelegate transactionDelegate) {
        this.metricPublisher = metricPublisher;
        this.eventPublisher = eventPublisher;
        this.transactionDelegate = transactionDelegate;
    }

    public static ApiBootstrapFacade getInstance() {
        return INSTANCE;
    }
}

