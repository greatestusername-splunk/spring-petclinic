/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.BaseMethodInterceptorDelegatorBoot;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IInlineInterceptor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InlineInterceptorDelegator
extends BaseMethodInterceptorDelegatorBoot {
    private static final Map<Integer, IInlineInterceptor> transformationMap = new ConcurrentHashMap<Integer, IInlineInterceptor>();

    public static final void safeOnMethodBegin(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Object additionalArgument) {
        block5: {
            InlineInterceptorDelegator.verifyStackSpaceAvailable();
            try {
                IInlineInterceptor interceptor = transformationMap.get(transformationId);
                if (interceptor != null) {
                    interceptor.methodStart(invokedObject, className, methodName, paramValues, transformationId, additionalArgument);
                }
            }
            catch (Throwable t) {
                try {
                    InlineInterceptorDelegator.safeError(t);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                if (!InlineInterceptorDelegator.shouldPropagateErrorToApplication(t)) break block5;
                throw (Error)t;
            }
        }
    }

    public static final void safeOnMethodEnd(int fastInterceptorId, int transformationId, Object invokedObject, String className, String methodName, Object[] paramValues, Throwable thrownException, Object returnValue, Object additionalArgument) {
        block5: {
            try {
                IInlineInterceptor interceptor = transformationMap.get(transformationId);
                if (interceptor != null) {
                    interceptor.methodEnd(invokedObject, className, methodName, paramValues, thrownException, returnValue, transformationId, additionalArgument);
                }
            }
            catch (Throwable t) {
                try {
                    InlineInterceptorDelegator.safeError(t);
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                if (!InlineInterceptorDelegator.shouldPropagateErrorToApplication(t)) break block5;
                throw (Error)t;
            }
        }
    }

    public static final void registerInterceptor(int transformationId, IInlineInterceptor interceptor) {
        transformationMap.put(transformationId, interceptor);
    }
}

