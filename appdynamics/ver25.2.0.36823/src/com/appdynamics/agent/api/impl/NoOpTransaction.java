/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api.impl;

import com.appdynamics.agent.api.ExitCall;
import com.appdynamics.agent.api.Transaction;
import com.appdynamics.agent.api.impl.NoOpExitCall;
import com.appdynamics.apm.appagent.api.DataScope;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class NoOpTransaction
implements Transaction {
    private static final ExitCall NO_OP_EXIT_CALL = new NoOpExitCall();

    @Override
    public void end() {
    }

    @Override
    public ExitCall startExitCall(String uniquePropertiesIdentifier, String displayName, String exitType, boolean async) {
        return NO_OP_EXIT_CALL;
    }

    @Override
    public ExitCall startExitCall(Map<String, String> uniquePropertiesIdentifier, String displayName, String exitType, boolean async) {
        return NO_OP_EXIT_CALL;
    }

    @Override
    public ExitCall startHttpExitCall(Map<String, String> uniquePropertiesIdentifier, URL url, boolean async) {
        return NO_OP_EXIT_CALL;
    }

    @Override
    public void markHandoff(Object commonObject) {
    }

    @Override
    public void markAsError(String errorMessage) {
    }

    @Override
    public void endSegment() {
    }

    @Override
    public String getUniqueIdentifier() {
        return "";
    }

    @Override
    public boolean isAsyncTransaction() {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public void collectData(String key, String value, Set<DataScope> dataScopes) {
    }

    @Override
    public Map<String, String> getEumMetadata() {
        return Collections.emptyMap();
    }
}

