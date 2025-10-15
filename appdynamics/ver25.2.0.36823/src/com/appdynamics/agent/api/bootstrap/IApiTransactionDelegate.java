/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api.bootstrap;

import com.appdynamics.agent.api.ExitCall;
import com.appdynamics.agent.api.ServletContext;
import com.appdynamics.agent.api.Transaction;
import com.appdynamics.apm.appagent.api.DataScope;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public interface IApiTransactionDelegate {
    public Transaction getTransaction();

    public Transaction getTransaction(String var1);

    public Map<String, String> getBackendIdentifyingPropertiesBasedOnConfiguration(Object var1);

    public String getBTNameBasedOnConfigurationForEntryType(Object var1);

    public String getSEPNameBasedOnConfigurationForEntryType(Object var1);

    public Transaction startApiTransaction(String var1, String var2, String var3, boolean var4);

    public Transaction startApiTransaction(ServletContext var1, String var2, String var3, boolean var4);

    public Transaction startApiTransactionAndSep(String var1, String var2, String var3, String var4, boolean var5, long var6, long var8);

    public boolean endApiTransaction(Transaction var1);

    public void markTransactionAsError(String var1);

    public void markTransactionAsError(String var1, String var2);

    public ExitCall startApiExitCall(boolean var1, Map<String, String> var2, String var3, String var4);

    public ExitCall startApiHttpExitCall(Map<String, String> var1, boolean var2);

    public void endApiExitCall(Object var1, Map<String, String> var2);

    public void markHandoff(Object var1, String var2);

    public void cancelHandoff(Object var1);

    public Transaction startSegment(Object var1);

    public Transaction startSegmentNoHandoff(String var1);

    public boolean endSegment(Transaction var1);

    public void ignoreSegment(String var1);

    public ExitCall fetchExitCall(Object var1);

    public boolean stashExitCall(Object var1, ExitCall var2);

    public boolean collectData(String var1, String var2, String var3, Set<DataScope> var4);

    public boolean collectHttpData(String var1, Object var2);

    public Object createApiHttpNamingContext(URL var1, String var2, String var3, Map<String, String> var4, Map<String, String[]> var5, Map<String, Object> var6);

    public boolean handleOpenTracingBootStrap(Set<String> var1);

    public Transaction setCurrentTransactionName(Transaction var1, String var2);
}

