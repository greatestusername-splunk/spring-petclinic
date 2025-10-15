/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api.debug;

public interface ITransformationMatch {
    public boolean onTransformation(String var1, String var2, String var3);

    public boolean onSkippedTransformation(String var1);
}

