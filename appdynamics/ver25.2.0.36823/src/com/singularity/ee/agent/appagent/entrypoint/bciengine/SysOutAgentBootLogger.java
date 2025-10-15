/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import com.singularity.ee.agent.util.io.Console;

public class SysOutAgentBootLogger
implements IAgentBootLogger {
    @Override
    public void warn(String msg, Throwable t) {
        Console.out().println(msg);
        if (t != null) {
            t.printStackTrace(Console.out());
        }
    }

    @Override
    public void warn(String msg) {
        Console.out().println(msg);
    }

    @Override
    public void info(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debugParams(String msg, Object ... objects) {
        throw new UnsupportedOperationException();
    }
}

