/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.sdk.impl;

import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;

public class NoOpLogger
implements ISDKLogger {
    @Override
    public void debug(String msg) {
    }

    @Override
    public void debug(String msg, Throwable t) {
    }

    @Override
    public void debugParams(String msg, Object ... objects) {
    }

    @Override
    public void error(String msg) {
    }

    @Override
    public void error(String msg, Throwable t) {
    }

    @Override
    public void fatal(String msg) {
    }

    @Override
    public void fatal(String msg, Throwable t) {
    }

    @Override
    public void info(String msg) {
    }

    @Override
    public void info(String msg, Throwable t) {
    }

    @Override
    public void trace(String msg) {
    }

    @Override
    public void trace(String msg, Throwable t) {
    }

    @Override
    public void traceParams(String msg, Object ... objects) {
    }

    @Override
    public void warn(String msg) {
    }

    @Override
    public void warn(String msg, Throwable t) {
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }
}

