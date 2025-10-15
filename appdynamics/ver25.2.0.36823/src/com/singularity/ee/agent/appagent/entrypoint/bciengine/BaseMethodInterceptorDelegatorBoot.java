/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IBaseMethodInterceptorDelegator;
import com.singularity.ee.agent.appagent.utils.StackOperations;

public abstract class BaseMethodInterceptorDelegatorBoot {
    protected static IBaseMethodInterceptorDelegator registeredInterceptorDelegator;
    private static final boolean STACK_BANGING_ENABLED;
    private static final int DEFAULT_BANGING_CALL_DEPTH = 40;
    private static int callDepth;
    public static long dummyValue;

    protected static void verifyStackSpaceAvailable() {
        if (STACK_BANGING_ENABLED) {
            try {
                dummyValue = BaseMethodInterceptorDelegatorBoot.recursiveFunction(callDepth, 8L, -1L, 5L, dummyValue, 4L, 42L, 12L);
            }
            catch (StackOverflowError e) {
                throw new StackOverflowError("Not enough stack space left for agent to operate (" + callDepth + ")");
            }
        }
    }

    private static long recursiveFunction(int depth, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8) {
        if (depth <= 0) {
            return arg8;
        }
        return BaseMethodInterceptorDelegatorBoot.recursiveFunction(depth - 1, arg3, arg4, arg5, arg6, arg7, arg8, arg2) + arg2;
    }

    public static void register(IBaseMethodInterceptorDelegator registeredInterceptorDelegator) {
        BaseMethodInterceptorDelegatorBoot.registeredInterceptorDelegator = registeredInterceptorDelegator;
    }

    public static boolean isDisabled() {
        if (registeredInterceptorDelegator == null) {
            return true;
        }
        try {
            return registeredInterceptorDelegator.isDisabled();
        }
        catch (Throwable t) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            return false;
        }
    }

    public static void disable() {
        if (registeredInterceptorDelegator == null) {
            return;
        }
        try {
            registeredInterceptorDelegator.disable();
        }
        catch (Throwable t) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static void enable() {
        if (registeredInterceptorDelegator == null) {
            return;
        }
        try {
            registeredInterceptorDelegator.enable();
        }
        catch (Throwable t) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static void setAgentDisabled(boolean value) {
        if (registeredInterceptorDelegator == null) {
            return;
        }
        try {
            registeredInterceptorDelegator.setAgentDisabled(value);
        }
        catch (Throwable t) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static void safeWarn(String msg, Throwable t) {
        if (registeredInterceptorDelegator == null) {
            return;
        }
        try {
            registeredInterceptorDelegator.safeWarn(msg, t);
        }
        catch (Throwable t1) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static void setUsePrivilegedAction(boolean usePrivilegedAction) {
        if (registeredInterceptorDelegator == null) {
            return;
        }
        try {
            registeredInterceptorDelegator.setUsePrivilegedAction(usePrivilegedAction);
        }
        catch (Throwable t) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static void safeError(Throwable t) {
        if (registeredInterceptorDelegator != null) {
            try {
                registeredInterceptorDelegator.safeError(t);
                return;
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        try {
            t.printStackTrace(System.err);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void setLogger(IAgentBootLogger newLogger) {
        if (registeredInterceptorDelegator == null) {
            return;
        }
        try {
            registeredInterceptorDelegator.setLogger(newLogger);
        }
        catch (Throwable t) {
            try {
                StackOperations.printStackTrace(t);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static boolean shouldPropagateErrorToApplication(Throwable t) {
        if (t instanceof Error) {
            if (t instanceof VirtualMachineError) {
                return true;
            }
            if (t instanceof ThreadDeath) {
                return true;
            }
        }
        return false;
    }

    static {
        String s = System.getProperty("appdynamics.agent.interceptor.verify.stack.depth");
        if (s == null) {
            STACK_BANGING_ENABLED = false;
        } else {
            callDepth = 40;
            if (s.length() > 0) {
                try {
                    callDepth = Integer.parseInt(s);
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
            STACK_BANGING_ENABLED = true;
        }
    }
}

