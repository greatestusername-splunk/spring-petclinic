/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.BaseMethodInterceptorDelegatorBoot;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IMethodInterceptorDelegator;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IMethodInterceptorDelegatorFactory;

public final class MethodInterceptorDelegatorBoot
extends BaseMethodInterceptorDelegatorBoot {
    private static IMethodInterceptorDelegator registeredDelegator;
    private static IMethodInterceptorDelegatorFactory delegatorFactory;

    public static void register(IMethodInterceptorDelegator registeredDelegator) {
        MethodInterceptorDelegatorBoot.registeredDelegator = registeredDelegator;
    }

    public static void registerFactory(IMethodInterceptorDelegatorFactory registeredDelegatorFactory) {
        delegatorFactory = registeredDelegatorFactory;
    }

    public static int getTransformationId() {
        if (registeredDelegator == null) {
            return 0;
        }
        try {
            return registeredDelegator.getTransformationId();
        }
        catch (Throwable t) {
            try {
                MethodInterceptorDelegatorBoot.safeError(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (MethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) {
                throw (Error)t;
            }
            return 0;
        }
    }

    public static IMethodInterceptorDelegator createMethodDelegatorInterceptor(String[] interceptorClassNames, int[] transformationIds) {
        if (delegatorFactory == null) {
            return null;
        }
        MethodInterceptorDelegatorBoot.verifyStackSpaceAvailable();
        try {
            return delegatorFactory.createMethodDelegatorInterceptor(interceptorClassNames, transformationIds);
        }
        catch (Throwable t) {
            try {
                MethodInterceptorDelegatorBoot.safeError(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (MethodInterceptorDelegatorBoot.shouldPropagateErrorToApplication(t)) {
                throw (Error)t;
            }
            return null;
        }
    }
}

