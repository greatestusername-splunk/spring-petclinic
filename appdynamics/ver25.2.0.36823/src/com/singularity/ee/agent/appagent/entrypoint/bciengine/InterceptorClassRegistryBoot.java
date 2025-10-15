/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.AMethodInterceptor;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IInterceptorClassRegistry;

public abstract class InterceptorClassRegistryBoot {
    private static IInterceptorClassRegistry registeredInterceptorClassRegistry;

    public static void register(IInterceptorClassRegistry registeredInterceptorClassRegistry) {
        InterceptorClassRegistryBoot.registeredInterceptorClassRegistry = registeredInterceptorClassRegistry;
    }

    public static IInterceptorClassRegistry getRegisteredImplementation() {
        return registeredInterceptorClassRegistry;
    }

    public static void bindClass(String className, Class<? extends AMethodInterceptor> clazz) {
        try {
            if (registeredInterceptorClassRegistry != null) {
                registeredInterceptorClassRegistry.bindClass(className, clazz);
            }
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

    public static String getInterceptorName(Class<? extends AMethodInterceptor> interceptorClass) {
        try {
            return registeredInterceptorClassRegistry.getInterceptorName(interceptorClass);
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

    public static String getInterceptorDescription(Class<? extends AMethodInterceptor> interceptorClass) {
        try {
            return registeredInterceptorClassRegistry.getInterceptorDescription(interceptorClass);
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

    public static int getInterceptorPriority(Class<? extends AMethodInterceptor> interceptorClass) {
        try {
            return registeredInterceptorClassRegistry.getInterceptorPriority(interceptorClass);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {}
            finally {
                return 0;
            }
        }
    }

    public static String getInterceptorClassName(String className) {
        try {
            return registeredInterceptorClassRegistry.getInterceptorClassName(className);
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
}

