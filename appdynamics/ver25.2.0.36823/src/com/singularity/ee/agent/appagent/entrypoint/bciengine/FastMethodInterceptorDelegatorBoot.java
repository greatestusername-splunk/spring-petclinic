/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.BaseMethodInterceptorDelegatorBoot;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IFastMethodInterceptorDelegator;

public final class FastMethodInterceptorDelegatorBoot
extends BaseMethodInterceptorDelegatorBoot {
    public static void register(IFastMethodInterceptorDelegator registeredDelegator) {
    }

    public static int getTransformationId() {
        if (registeredInterceptorDelegator == null) {
            return 0;
        }
        try {
            return ((IFastMethodInterceptorDelegator)registeredInterceptorDelegator).getTransformationId();
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return 0;
        }
    }

    public static final Object safeOnMethodBegin(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues) {
        if (registeredInterceptorDelegator == null) {
            return null;
        }
        FastMethodInterceptorDelegatorBoot.verifyStackSpaceAvailable();
        try {
            return ((IFastMethodInterceptorDelegator)registeredInterceptorDelegator).safeOnMethodBegin(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues);
        }
        catch (Throwable t) {
            try {
                FastMethodInterceptorDelegatorBoot.safeError(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (FastMethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) {
                throw (Error)t;
            }
            return null;
        }
    }

    public static final Object safeOnMethodBeginNoReentrantCheck(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues) {
        if (registeredInterceptorDelegator == null) {
            return null;
        }
        try {
            return ((IFastMethodInterceptorDelegator)registeredInterceptorDelegator).safeOnMethodBeginNoReentrantCheck(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues);
        }
        catch (Throwable t) {
            try {
                FastMethodInterceptorDelegatorBoot.safeError(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (FastMethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) {
                throw (Error)t;
            }
            return null;
        }
    }

    public static final void safeOnMethodEndNormal(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object state, Object returnValue) {
        FastMethodInterceptorDelegatorBoot.safeOnMethodEnd(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues, state, null, returnValue);
    }

    public static final void safeOnMethodEndException(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object state, Throwable thrownException) {
        FastMethodInterceptorDelegatorBoot.safeOnMethodEnd(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues, state, thrownException, null);
    }

    private static void safeOnMethodEnd(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object state, Throwable thrownException, Object returnValue) {
        block5: {
            if (registeredInterceptorDelegator == null) {
                return;
            }
            try {
                ((IFastMethodInterceptorDelegator)registeredInterceptorDelegator).safeOnMethodEnd(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues, state, thrownException, returnValue);
            }
            catch (Throwable t) {
                try {
                    FastMethodInterceptorDelegatorBoot.safeError(t);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                if (!FastMethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) break block5;
                throw (Error)t;
            }
        }
    }

    public static void safeOnMethodEndNoReentrantCheckNormal(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object state, Object returnValue) {
        FastMethodInterceptorDelegatorBoot.safeOnMethodEndNoReentrantCheck(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues, state, null, returnValue);
    }

    public static void safeOnMethodEndNoReentrantCheckException(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object state, Throwable thrownException) {
        FastMethodInterceptorDelegatorBoot.safeOnMethodEndNoReentrantCheck(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues, state, thrownException, null);
    }

    private static void safeOnMethodEndNoReentrantCheck(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object state, Throwable thrownException, Object returnValue) {
        block5: {
            if (registeredInterceptorDelegator == null) {
                return;
            }
            try {
                ((IFastMethodInterceptorDelegator)registeredInterceptorDelegator).safeOnMethodEndNoReentrantCheck(fastInterceptorId, transformationId, invokedObject, className, methodName, paramValues, state, thrownException, returnValue);
            }
            catch (Throwable t) {
                try {
                    FastMethodInterceptorDelegatorBoot.safeError(t);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                if (!FastMethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) break block5;
                throw (Error)t;
            }
        }
    }

    public static void safeOnMethodMiddle(int fastInterceptorId, int transformationId, int midMethodId, Object invokedObject, String className, String methodName, Object[] paramValues, Object[] localVars, int lineNum) {
        block5: {
            if (registeredInterceptorDelegator == null) {
                return;
            }
            try {
                ((IFastMethodInterceptorDelegator)registeredInterceptorDelegator).safeOnMethodMiddle(fastInterceptorId, transformationId, midMethodId, invokedObject, className, methodName, paramValues, localVars, lineNum);
            }
            catch (Throwable t) {
                try {
                    FastMethodInterceptorDelegatorBoot.safeError(t);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                if (!FastMethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) break block5;
                throw (Error)t;
            }
        }
    }
}

