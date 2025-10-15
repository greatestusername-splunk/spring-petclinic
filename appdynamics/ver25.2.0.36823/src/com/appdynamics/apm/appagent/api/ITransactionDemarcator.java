/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api;

import java.util.Map;

public interface ITransactionDemarcator {
    public static final String APPDYNAMICS_TRANSACTION_CORRELATION_HEADER = "singularityheader";

    public String beginExternalCall(String var1, String var2, boolean var3) throws IllegalArgumentException;

    public String beginExternalCall(Map<String, String> var1, String var2, boolean var3);

    public void endExternalCall(boolean var1, String var2);

    public String beginContinuingTransactionAndAddCurrentThread(String var1, String var2);

    public boolean beginOrContinueTransactionAndAddCurrentThread(String var1, String var2, String var3, String var4);

    public boolean endContinuingTransactionAndRemoveCurrentThread();

    public boolean endContinuingTransactionAndRemoveCurrentThread(Throwable var1);

    public String getUniqueIdentifierForTransaction();

    public boolean setUniqueIdentifierForTransaction(String var1) throws IllegalArgumentException;

    public boolean addCurrentThreadToTransaction(String var1, String var2, Map<String, String> var3);

    public boolean removeCurrentThreadFromTransaction(Throwable var1);

    public boolean removeCurrentThreadFromTransaction(String var1, Throwable var2);

    public boolean setActivityNameForCurrentThread(String var1);

    public boolean addPropertyForCurrentThread(String var1, String var2);

    public boolean addPropertiesForCurrentThread(Map<String, String> var1);

    public String extractTransactionHeaderFromJMSMessage(Object var1) throws IllegalArgumentException;

    public String beginContinuingTransaction(String var1, String var2);

    public boolean endContinuingTransaction(String var1);

    public String beginOriginatingTransaction(String var1, String var2) throws IllegalArgumentException;

    public boolean endOriginatingTransaction(String var1);

    public void markCurrentTransactionAsError(String var1);

    public String beginOriginatingTransactionAndAddCurrentThread(String var1, String var2);

    public String beginOriginatingTransactionAndAddCurrentThread(String var1, String var2, String var3);

    public boolean endOriginatingTransactionAndRemoveCurrentThread();

    public boolean endOriginatingTransactionAndRemoveCurrentThread(String var1, Throwable var2);
}

