/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.java9;

import com.singularity.ee.agent.appagent.java9.IJava9ClassFileTransformer;
import java.lang.instrument.ClassFileTransformer;

public interface IJava9ClassFileTransformerFactory {
    public ClassFileTransformer build(IJava9ClassFileTransformer var1, ClassLoader var2);
}

