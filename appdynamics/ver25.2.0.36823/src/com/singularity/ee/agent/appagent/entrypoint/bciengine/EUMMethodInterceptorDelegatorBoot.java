/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IEUMMethodInterceptorDelegator;

public abstract class EUMMethodInterceptorDelegatorBoot {
    private static IEUMMethodInterceptorDelegator delegatorImpl;

    public static void register(IEUMMethodInterceptorDelegator delegatorImpl) {
        EUMMethodInterceptorDelegatorBoot.delegatorImpl = delegatorImpl;
    }

    public static final void safeParseAndInjectHeaderJS(Object writer, String message, Object invokedObject, int uniqueId) {
        try {
            delegatorImpl.safeParseAndInjectHeaderJS(writer, message, invokedObject, uniqueId);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static final void safeParseAndInjectFooterJS(Object writer, String message, Object invokedObject, int uniqueId) {
        try {
            delegatorImpl.safeParseAndInjectFooterJS(writer, message, invokedObject, uniqueId);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static final void safeParseAndInjectHeaderJS(Object writer, byte[] bytes, String html, Object invokedObject, int uniqueId) {
        try {
            delegatorImpl.safeParseAndInjectHeaderJS(writer, bytes, html, invokedObject, uniqueId);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static final void safeParseAndInjectFooterJS(Object writer, byte[] bytes, String html, Object invokedObject, int uniqueId) {
        try {
            delegatorImpl.safeParseAndInjectFooterJS(writer, bytes, html, invokedObject, uniqueId);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static Object register(Injector injector) {
        try {
            return delegatorImpl.register(injector);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {}
            finally {
                return null;
            }
        }
    }

    public static interface Injector {
        public void injectHeader(Object var1, byte[] var2, String var3, Object var4);

        public void injectFooter(Object var1, byte[] var2, String var3, Object var4);
    }
}

