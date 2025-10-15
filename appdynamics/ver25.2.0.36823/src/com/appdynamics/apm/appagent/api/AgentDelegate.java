/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api;

import com.appdynamics.apm.appagent.api.IEndUserMonitoringDelegate;
import com.appdynamics.apm.appagent.api.IMetricAndEventReporter;
import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.apm.appagent.api.NoOpInvocationHandler;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Deprecated
public class AgentDelegate {
    private static ITransactionDemarcator transactionDelegateProxyInstance;
    private static IMetricAndEventReporter metricAndEventPublisherProxyInstance;
    private static IEndUserMonitoringDelegate endUserMonitoringDelegateProxyInstance;
    private static Object javaAgent;
    private static ClassLoader agentClassLoader;
    private static List<AgentDelegateSDKProxy> proxyInstanceList;
    private static LinkedHashMap<String, Integer> mappingADAppAgentMethodsHash;
    private static final int REPORT_ERROR_EVENT = 0;
    private static final int REPORT_INFO_EVENT = 1;
    private static final int REPORT_METRIC = 2;
    private static final int REPORT_COUNT_METRIC = 3;
    private static final int REPORT_AVERAGE_METRIC = 4;
    private static final int REPORT_OBSERVED_METRIC = 5;

    private AgentDelegate() {
    }

    private static void getAgentReferences() {
        try {
            Class<?> cl = Class.forName("com.singularity.ee.agent.appagent.AgentEntryPoint", false, ClassLoader.getSystemClassLoader());
            Field field = cl.getDeclaredField("javaAgent");
            field.setAccessible(true);
            javaAgent = field.get(null);
            if (javaAgent == null) {
                throw new Exception("Java Agent is not defined...");
            }
            field = cl.getDeclaredField("agentHostClassLoader");
            field.setAccessible(true);
            agentClassLoader = (ClassLoader)field.get(null);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to get Agent References from AgentEntryPoint: " + e.getMessage());
        }
    }

    public static void clearSDKReferences() {
        javaAgent = null;
        agentClassLoader = null;
        AgentDelegate.clearAgentReferencesInProxy();
    }

    public static List<AgentDelegateSDKProxy> getProxyInstances() throws Exception {
        return proxyInstanceList;
    }

    public static void resetSDKReferences() throws Exception {
        AgentDelegate.getAgentReferences();
        for (AgentDelegateSDKProxy proxy : proxyInstanceList) {
            proxy.resetRealInstances();
        }
    }

    public static void clearAgentReferencesInProxy() {
        for (AgentDelegateSDKProxy proxy : proxyInstanceList) {
            proxy.clearRealInstance();
        }
    }

    public static void setReturnNoopInProxy(boolean onOff) {
        for (AgentDelegateSDKProxy proxy : proxyInstanceList) {
            proxy.setReturnNoop(onOff);
        }
    }

    public static AgentDelegateSDKProxy getAgentDelegateSDKProxy(String method) throws AgentDelegateException {
        return new AgentDelegateSDKProxy(method);
    }

    public static ITransactionDemarcator getTransactionDemarcator() {
        if (transactionDelegateProxyInstance == null) {
            transactionDelegateProxyInstance = AgentDelegate.getTransactionDelegateProxyInstance();
        }
        return transactionDelegateProxyInstance;
    }

    public static IMetricAndEventReporter getMetricAndEventPublisher() {
        if (metricAndEventPublisherProxyInstance == null) {
            metricAndEventPublisherProxyInstance = AgentDelegate.getMetricAndEventPublisherProxyInstance();
        }
        return metricAndEventPublisherProxyInstance;
    }

    public static IEndUserMonitoringDelegate getEndUserMonitoringDelegate() {
        if (endUserMonitoringDelegateProxyInstance == null) {
            endUserMonitoringDelegateProxyInstance = AgentDelegate.getEndUserMonitoringDelegateProxyInstance();
        }
        return endUserMonitoringDelegateProxyInstance;
    }

    public static ITransactionDemarcator getTransactionDelegateProxyInstance() {
        try {
            return (ITransactionDemarcator)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{ITransactionDemarcator.class}, (InvocationHandler)AgentDelegate.getAgentDelegateSDKProxy("getTransactionDelegate"));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static IMetricAndEventReporter getMetricAndEventPublisherProxyInstance() {
        try {
            metricAndEventPublisherProxyInstance = (IMetricAndEventReporter)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{IMetricAndEventReporter.class}, (InvocationHandler)AgentDelegate.getAgentDelegateSDKProxy("getMetricAndEventPublisher"));
            return metricAndEventPublisherProxyInstance;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static IEndUserMonitoringDelegate getEndUserMonitoringDelegateProxyInstance() {
        try {
            endUserMonitoringDelegateProxyInstance = (IEndUserMonitoringDelegate)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{IEndUserMonitoringDelegate.class}, (InvocationHandler)AgentDelegate.getAgentDelegateSDKProxy("getEndUserMonitoringDelegate"));
            return endUserMonitoringDelegateProxyInstance;
        }
        catch (Exception e) {
            return null;
        }
    }

    private static void initMethodMapping() {
        String[] mappingMethods = new String[]{"reportErrorEvent", "reportInfoEvent", "reportMetric", "reportCountMetric", "reportAverageMetric", "reportObservedMetric"};
        mappingADAppAgentMethodsHash = new LinkedHashMap();
        for (int i = 0; i < mappingMethods.length; ++i) {
            mappingADAppAgentMethodsHash.put(mappingMethods[i], i);
        }
    }

    private static void executeRealADAppAgentInterfaceMetricsFromInvoke(Object instance, Method method, Object[] args) throws Exception {
        Integer methodIndex = mappingADAppAgentMethodsHash.get(method.getName());
        if (methodIndex == null) {
            throw new Exception("Could not map publisher method " + method.getName());
        }
        IMetricAndEventReporter metricAndEventPublisherProxyInstance = (IMetricAndEventReporter)instance;
        switch (methodIndex) {
            case 0: {
                metricAndEventPublisherProxyInstance.publishErrorEvent((String)args[0], (Map)args[1], (Boolean)args[2]);
                break;
            }
            case 1: {
                metricAndEventPublisherProxyInstance.publishInfoEvent((String)args[0], (Map)args[1]);
                break;
            }
            case 2: {
                metricAndEventPublisherProxyInstance.reportMetric((String)args[0], (Long)args[1], (String)args[2], (String)args[3], (String)args[4]);
                break;
            }
            case 3: {
                metricAndEventPublisherProxyInstance.reportSumMetric((String)args[0], (Long)args[1]);
                break;
            }
            case 4: {
                metricAndEventPublisherProxyInstance.reportAverageMetric((String)args[0], (Long)args[1]);
                break;
            }
            case 5: {
                metricAndEventPublisherProxyInstance.reportObservedMetric((String)args[0], (Long)args[1]);
            }
        }
    }

    static {
        proxyInstanceList = null;
        mappingADAppAgentMethodsHash = null;
        proxyInstanceList = new ArrayList<AgentDelegateSDKProxy>();
        AgentDelegate.getAgentReferences();
        AgentDelegate.initMethodMapping();
    }

    public static class AgentDelegateSDKProxy
    implements InvocationHandler {
        private Object realInstance;
        private boolean setReturnNoop;
        private String method;

        public AgentDelegateSDKProxy(String method) throws AgentDelegateException {
            this.realInstance = this.getInstancesFromJavaAgent(javaAgent, method);
            this.method = method;
            proxyInstanceList.add(this);
        }

        public void clearRealInstance() {
            this.realInstance = null;
        }

        public void resetRealInstances() throws AgentDelegateException {
            this.realInstance = this.getInstancesFromJavaAgent(javaAgent, this.method);
        }

        private Object getInstancesFromJavaAgent(Object javaAgent, String methodName) throws AgentDelegateException {
            try {
                Method m = javaAgent.getClass().getDeclaredMethod(methodName, new Class[0]);
                return m.invoke(javaAgent, new Object[0]);
            }
            catch (Exception e) {
                throw new AgentDelegateException(e.getMessage(), e);
            }
        }

        public void setReturnNoop(boolean onOff) {
            this.setReturnNoop = onOff;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                if (this.realInstance == null || this.setReturnNoop) {
                    return NoOpInvocationHandler.getSafeReturnType(method.getReturnType().getClass().getName());
                }
                if (method.getDeclaringClass().getName().equals("com.appdynamics.sdk.ADAppAgent")) {
                    AgentDelegate.executeRealADAppAgentInterfaceMetricsFromInvoke(this.realInstance, method, args);
                    return null;
                }
                return method.invoke(this.realInstance, args);
            }
            catch (Throwable t) {
                throw new ExecutionException("SDK API call failed for method: " + method.getName(), t);
            }
        }
    }

    public static class AgentDelegateException
    extends Exception {
        public AgentDelegateException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}

