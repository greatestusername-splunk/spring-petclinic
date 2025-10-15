/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NoOpInvocationHandler
implements InvocationHandler {
    private static final String TO_STRING_METHOD = "toString";
    private static final String NO_OP_AGENT_DELEGATE = "No Op Agent Delegate";

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if (method.getName().equals(TO_STRING_METHOD)) {
            return NO_OP_AGENT_DELEGATE;
        }
        String returnTypeClassName = method.getReturnType().getName();
        return NoOpInvocationHandler.getSafeReturnType(returnTypeClassName);
    }

    public static Object getSafeReturnType(String returnType) {
        if (returnType.equals(Boolean.TYPE.getName()) || returnType.equals(Boolean.class.getName())) {
            return false;
        }
        if (returnType.equals(String.class.getName())) {
            return "";
        }
        if (returnType.equals(Float.TYPE.getName()) || returnType.equals(Float.class.getName())) {
            return 0;
        }
        if (returnType.equals(Character.TYPE.getName()) || returnType.equals(Character.class.getName())) {
            return 0;
        }
        if (returnType.equals(Integer.TYPE.getName()) || returnType.equals(Integer.class.getName())) {
            return 0;
        }
        if (returnType.equals(Short.TYPE.getName()) || returnType.equals(Short.class.getName())) {
            return 0;
        }
        if (returnType.equals(Long.TYPE.getName()) || returnType.equals(Long.class.getName())) {
            return 0;
        }
        if (returnType.equals(Double.TYPE.getName()) || returnType.equals(Double.class.getName())) {
            return 0;
        }
        return null;
    }
}

