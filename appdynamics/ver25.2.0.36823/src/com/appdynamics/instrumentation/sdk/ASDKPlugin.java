/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk;

import com.appdynamics.agent.sdk.impl.LoggerFactory;
import com.appdynamics.instrumentation.sdk.IRuleAppliable;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflectionBuilder;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorFactory;
import com.appdynamics.instrumentation.sdk.util.AgentInfoUtil;
import java.util.List;

public abstract class ASDKPlugin
implements IRuleAppliable {
    private final List<Rule> rules = this.initializeRules();
    private final ISDKLogger logger = this.createLogger();
    private static final String ASDK_PLUGIN_LOGGER_PREFIX = "com.singularity.instrumentation.sdk.";

    @Override
    public abstract List<Rule> initializeRules();

    @Override
    public List<Rule> getRules() {
        return this.rules;
    }

    public ISDKLogger getLogger() {
        return this.logger;
    }

    public IReflectionBuilder getNewReflectionBuilder() {
        try {
            return ReflectorFactory.getInstance().getNewReflectionBuilder();
        }
        catch (Exception e) {
            if (this.logger != null) {
                this.logger.error(String.format("\"%s\" caught in ReflectorFactory", e.toString()), e);
            }
            return null;
        }
    }

    public String getAgentPluginDirectory() {
        AgentInfoUtil agentInfoUtil = AgentInfoUtil.getInstance();
        return agentInfoUtil != null ? agentInfoUtil.getPluginDir() : null;
    }

    private ISDKLogger createLogger() {
        try {
            return LoggerFactory.getInstance().getNewLogger(ASDK_PLUGIN_LOGGER_PREFIX + this.getClass().getSimpleName());
        }
        catch (Exception e) {
            if (this.logger != null) {
                this.logger.error(String.format("\"%s\" caught in LoggerFactory", e.toString()), e);
            }
            return null;
        }
    }
}

