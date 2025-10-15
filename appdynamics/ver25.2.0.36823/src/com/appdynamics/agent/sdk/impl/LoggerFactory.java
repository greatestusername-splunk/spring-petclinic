/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.agent.sdk.impl;

import com.appdynamics.agent.sdk.impl.NoOpLogger;
import com.appdynamics.instrumentation.sdk.logging.ISDKLogger;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorFactory;

public class LoggerFactory {
    private IReflector reflector;
    private ClassLoader sdkLoggerClassLoader;
    private static LoggerFactory INSTANCE = null;
    private static ISDKLogger NO_OP_LOGGER = new NoOpLogger();

    private LoggerFactory(IReflector reflector, ClassLoader sdkLoggerClassLoader) {
        this.reflector = reflector;
        this.sdkLoggerClassLoader = sdkLoggerClassLoader;
    }

    public ISDKLogger getNewLogger(String loggerNamespace) throws ReflectorException {
        if (this.reflector != null) {
            Object[] params = new Object[]{loggerNamespace};
            return (ISDKLogger)this.reflector.execute(this.sdkLoggerClassLoader, null, new Object[][]{params});
        }
        return NO_OP_LOGGER;
    }

    public static LoggerFactory getInstance() {
        return INSTANCE;
    }

    public static void initialize(Class sdkLoggerClass) throws Exception {
        if (INSTANCE == null) {
            IReflector reflector = ReflectorFactory.getInstance().getNewReflectionBuilder().createObject(sdkLoggerClass.getName(), String.class.getName()).build();
            INSTANCE = new LoggerFactory(reflector, sdkLoggerClass.getClassLoader());
        }
    }
}

