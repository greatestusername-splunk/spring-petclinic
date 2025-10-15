/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.util;

public class AgentInfoUtil {
    private final String pluginDir;
    private static AgentInfoUtil INSTANCE = null;

    private AgentInfoUtil(String pluginDir) {
        this.pluginDir = pluginDir;
    }

    public static AgentInfoUtil getInstance() {
        return INSTANCE;
    }

    public String getPluginDir() {
        return this.pluginDir;
    }

    public static void initialize(String pluginDir) {
        if (INSTANCE == null) {
            INSTANCE = new AgentInfoUtil(pluginDir);
        }
    }
}

