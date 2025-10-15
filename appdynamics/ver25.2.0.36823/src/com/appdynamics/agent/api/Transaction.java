/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api;

import com.appdynamics.agent.api.ExitCall;
import com.appdynamics.apm.appagent.api.DataScope;
import java.io.Closeable;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public interface Transaction
extends Closeable {
    public void end();

    public ExitCall startExitCall(String var1, String var2, String var3, boolean var4);

    public ExitCall startExitCall(Map<String, String> var1, String var2, String var3, boolean var4);

    public ExitCall startHttpExitCall(Map<String, String> var1, URL var2, boolean var3);

    public void markHandoff(Object var1);

    public void markAsError(String var1);

    public void endSegment();

    public String getUniqueIdentifier();

    public boolean isAsyncTransaction();

    @Override
    public void close();

    public void collectData(String var1, String var2, Set<DataScope> var3);

    public Map<String, String> getEumMetadata();
}

