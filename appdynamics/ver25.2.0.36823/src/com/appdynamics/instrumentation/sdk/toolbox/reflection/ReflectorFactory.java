/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk.toolbox.reflection;

import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflectionBuilder;

public class ReflectorFactory {
    private final Class reflectorBuilderClass;
    private static ReflectorFactory INSTANCE = null;

    private ReflectorFactory(Class reflectorBuilderClass) {
        this.reflectorBuilderClass = reflectorBuilderClass;
    }

    public IReflectionBuilder getNewReflectionBuilder() throws Exception {
        return (IReflectionBuilder)this.reflectorBuilderClass.newInstance();
    }

    public static ReflectorFactory getInstance() {
        return INSTANCE;
    }

    public static void initialize(Class reflectorBuilderClass) {
        if (INSTANCE == null) {
            INSTANCE = new ReflectorFactory(reflectorBuilderClass);
        }
    }
}

