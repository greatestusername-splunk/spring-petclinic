/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.logging;

public interface ISDKLogger {
    public void debug(String var1);

    public void debug(String var1, Throwable var2);

    public void debugParams(String var1, Object ... var2);

    public void error(String var1);

    public void error(String var1, Throwable var2);

    public void fatal(String var1);

    public void fatal(String var1, Throwable var2);

    public void info(String var1);

    public void info(String var1, Throwable var2);

    public void trace(String var1);

    public void trace(String var1, Throwable var2);

    public void traceParams(String var1, Object ... var2);

    public void warn(String var1);

    public void warn(String var1, Throwable var2);

    public boolean isDebugEnabled();

    public boolean isInfoEnabled();

    public boolean isTraceEnabled();
}

