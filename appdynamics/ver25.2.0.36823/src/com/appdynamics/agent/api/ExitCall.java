/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api;

public interface ExitCall {
    public static final String API_EXIT_CALL_IDENTIFIER = "api-exit";

    public String getCorrelationHeader();

    public void end();

    public void stash(Object var1);
}

