/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.api.impl;

import com.appdynamics.agent.api.ExitCall;

public class NoOpExitCall
implements ExitCall {
    @Override
    public String getCorrelationHeader() {
        return null;
    }

    @Override
    public void end() {
    }

    @Override
    public void stash(Object uniqueObject) {
    }
}

