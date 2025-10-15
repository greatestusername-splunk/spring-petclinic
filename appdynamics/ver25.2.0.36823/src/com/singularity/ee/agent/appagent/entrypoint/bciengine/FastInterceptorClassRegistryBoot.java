/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IFastInterceptorClassRegistry;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IFastMethodInterceptor;

public abstract class FastInterceptorClassRegistryBoot {
    private static IFastInterceptorClassRegistry registeredIFastInterceptorClassRegistry;

    public static void register(IFastInterceptorClassRegistry registeredIFastInterceptorClassRegistry) {
        FastInterceptorClassRegistryBoot.registeredIFastInterceptorClassRegistry = registeredIFastInterceptorClassRegistry;
    }

    public static IFastInterceptorClassRegistry getRegisteredImplementation() {
        return registeredIFastInterceptorClassRegistry;
    }

    public static int getActivityMarkerInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityMarkerInterceptorId();
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

    public static int getActivityProcessingLoopInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityProcessingLoopInterceptorId();
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

    public static int getActivityProducerInProcessCorrelationInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityProducerInProcessCorrelationInterceptorId();
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

    public static int getActivityProducerInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityProducerInterceptorId();
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

    public static int getActivityProducerMultiExitInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityProducerMultiExitInterceptorId();
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

    public static int getActivityProducerOutgoingCorrelationInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityProducerOutgoingCorrelationInterceptorId();
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

    public static void bindFastInterceptorInstance(IFastMethodInterceptor instance) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return;
        }
        try {
            registeredIFastInterceptorClassRegistry.bindFastInterceptorInstance(instance);
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

    public static IFastMethodInterceptor getFastInterceptorInstance(int fastInterceptorId) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return null;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getFastInterceptorInstance(fastInterceptorId);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return null;
        }
    }

    public static void bindFastInterceptorInstance(IFastMethodInterceptor instance, boolean replace) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return;
        }
        try {
            registeredIFastInterceptorClassRegistry.bindFastInterceptorInstance(instance, replace);
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

    public static boolean isRegistered(String interceptorClassName) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return false;
        }
        try {
            return registeredIFastInterceptorClassRegistry.isRegistered(interceptorClassName);
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {}
            finally {
                return false;
            }
        }
    }

    public static Class<? extends IFastMethodInterceptor> getFastClass(String className) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return null;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getFastClass(className);
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

    public static int getNewInterceptorId() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getNewInterceptorId();
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

    public static void unbindFastInterceptorInstance(IFastMethodInterceptor instance) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return;
        }
        try {
            registeredIFastInterceptorClassRegistry.unbindFastInterceptorInstance(instance);
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

    public static int getFastNoReentrantCheckTestInterceptorOrdinal() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getFastNoReentrantCheckTestInterceptorOrdinal();
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

    public static boolean isDisableCollectionCaptureInstrumentation() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return false;
        }
        try {
            return registeredIFastInterceptorClassRegistry.isDisableCollectionCaptureInstrumentation();
        }
        catch (Throwable t) {
            try {
                t.printStackTrace(System.err);
            }
            catch (Throwable throwable) {}
            finally {
                return false;
            }
        }
    }

    public static int getActivityTracesThatAreRunning() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getActivityTracesThatAreRunning();
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

    public static int getMemoryTrackerInterceptorOrdinal() {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.getMemoryTrackerInterceptorOrdinal();
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

    public static int incrementActivityTracesThatAreRunning(int incrBy) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return 0;
        }
        try {
            return registeredIFastInterceptorClassRegistry.incrementActivityTracesThatAreRunning(incrBy);
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

    public static void setDisableCollectionCaptureInstrumentation(boolean disable) {
        if (registeredIFastInterceptorClassRegistry == null) {
            return;
        }
        try {
            registeredIFastInterceptorClassRegistry.setDisableCollectionCaptureInstrumentation(disable);
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
}

