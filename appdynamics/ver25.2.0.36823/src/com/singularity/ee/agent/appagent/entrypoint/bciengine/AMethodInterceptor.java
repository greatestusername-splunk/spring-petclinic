/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.MethodExecutionEnvironment;

public abstract class AMethodInterceptor {
    private int transformationId;

    public abstract void onMethodBegin(MethodExecutionEnvironment var1);

    public abstract void onMethodEnd(MethodExecutionEnvironment var1);

    public abstract String getName();

    public abstract String getDescription();

    public int getPriority() {
        return 0;
    }

    public void setTransformationId(int transformationId) {
        this.transformationId = transformationId;
    }

    public int getTransformationId() {
        return this.transformationId;
    }
}

