/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.ModifiedClassDefinition;
import java.util.Collection;

public interface IAppTierNodeDeterminer {
    public static final String SET_CONTROLLER_CONFIG_FILES_FUNCTION_NAME = "setControllerConfigFiles";

    public void setControllerConfigFile(String var1);

    public String[] getAppTierNode();

    public boolean isCompatibleWithJavaAgent(String var1);

    public boolean supportsDottedJavaAgentVersion();

    public Object executeGenericFunction(String var1, Object ... var2) throws UnsupportedOperationException;

    public boolean supportsBootstrapClassPath();

    public Collection<ModifiedClassDefinition> getModifiedClassDefinitions();
}

