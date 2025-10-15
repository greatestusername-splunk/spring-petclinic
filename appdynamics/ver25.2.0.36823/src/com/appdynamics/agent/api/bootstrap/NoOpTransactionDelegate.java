/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api.bootstrap;

import com.appdynamics.agent.api.ExitCall;
import com.appdynamics.agent.api.ServletContext;
import com.appdynamics.agent.api.Transaction;
import com.appdynamics.agent.api.bootstrap.IApiTransactionDelegate;
import com.appdynamics.agent.api.impl.NoOpExitCall;
import com.appdynamics.agent.api.impl.NoOpTransaction;
import com.appdynamics.apm.appagent.api.DataScope;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class NoOpTransactionDelegate
implements IApiTransactionDelegate {
    private static final Transaction NO_OP_TRANSACTION = new NoOpTransaction();
    private static final ExitCall NO_OP_EXIT_CALL = new NoOpExitCall();
    private static final String EMPTY_STRING = "";
    private static final Object EMPTY_OBJECT = new Object();
    public static final Map<String, String> UNKNOWN_BACKEND_PROPERTIES = Collections.singletonMap("api-exit", "UnknownBackend");

    @Override
    public Transaction getTransaction() {
        return NO_OP_TRANSACTION;
    }

    @Override
    public Transaction getTransaction(String uniqueIdentifier) {
        return NO_OP_TRANSACTION;
    }

    @Override
    public String getBTNameBasedOnConfigurationForEntryType(Object apiNamingContext) {
        return EMPTY_STRING;
    }

    @Override
    public String getSEPNameBasedOnConfigurationForEntryType(Object apiNamingContext) {
        return EMPTY_STRING;
    }

    @Override
    public Map<String, String> getBackendIdentifyingPropertiesBasedOnConfiguration(Object apiNamingContext) {
        return UNKNOWN_BACKEND_PROPERTIES;
    }

    @Override
    public Transaction startApiTransaction(String btname, String correlationHeader, String entryType, boolean isAsync) {
        return NO_OP_TRANSACTION;
    }

    @Override
    public Transaction startApiTransaction(ServletContext servletContext, String correlationHeader, String entryType, boolean isAsync) {
        return NO_OP_TRANSACTION;
    }

    @Override
    public Transaction startApiTransactionAndSep(String btName, String correlationHeader, String sepName, String entryType, boolean isAsync, long startTime, long skewAdjustedStartTime) {
        return NO_OP_TRANSACTION;
    }

    @Override
    public boolean endApiTransaction(Transaction transaction) {
        return false;
    }

    @Override
    public void markTransactionAsError(String errorDetails) {
    }

    @Override
    public void markTransactionAsError(String message, String uniqueIdentifier) {
    }

    @Override
    public ExitCall startApiExitCall(boolean async, Map<String, String> uniquePropertiesIdentifier, String invokedServiceDisplayName, String exitType) {
        return NO_OP_EXIT_CALL;
    }

    @Override
    public ExitCall startApiHttpExitCall(Map<String, String> uniquePropertiesIdentifier, boolean async) {
        return NO_OP_EXIT_CALL;
    }

    @Override
    public void endApiExitCall(Object invokedObject, Map<String, String> uniquePropertiesIdentifier) {
    }

    @Override
    public void markHandoff(Object commonObject, String uniqueIdentifier) {
    }

    @Override
    public void cancelHandoff(Object commonObject) {
    }

    @Override
    public Transaction startSegment(Object commonObject) {
        return NO_OP_TRANSACTION;
    }

    @Override
    public Transaction startSegmentNoHandoff(String uniqueIdentifier) {
        return NO_OP_TRANSACTION;
    }

    @Override
    public boolean endSegment(Transaction transaction) {
        return false;
    }

    @Override
    public void ignoreSegment(String uniqueIdentifier) {
    }

    @Override
    public ExitCall fetchExitCall(Object uniqueObject) {
        return NO_OP_EXIT_CALL;
    }

    @Override
    public boolean stashExitCall(Object uniqueObject, ExitCall exitCall) {
        return false;
    }

    @Override
    public boolean collectData(String uniqueIdentifier, String key, String value, Set<DataScope> dataScopes) {
        return false;
    }

    @Override
    public boolean collectHttpData(String uniqueIdentifier, Object apiHttpNamingContext) {
        return false;
    }

    @Override
    public Object createApiHttpNamingContext(URL url, String defaultName, String method, Map<String, String> headers, Map<String, String[]> parameters, Map<String, Object> attributes) {
        return EMPTY_OBJECT;
    }

    @Override
    public boolean handleOpenTracingBootStrap(Set<String> frameworksAffected) {
        return false;
    }

    public int getAgentApiCompatibilityLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Transaction setCurrentTransactionName(Transaction transaction, String name) {
        return NO_OP_TRANSACTION;
    }
}

