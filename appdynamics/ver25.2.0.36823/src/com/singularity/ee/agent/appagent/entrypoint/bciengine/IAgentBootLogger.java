/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

public interface IAgentBootLogger {
    public void warn(String var1, Throwable var2);

    public void warn(String var1);

    public void info(Object var1);

    public void debugParams(String var1, Object ... var2);
}

