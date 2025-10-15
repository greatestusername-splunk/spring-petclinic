/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.java9;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public interface IJava9ClassFileTransformer {
    public byte[] transform(ClassLoader var1, String var2, Class<?> var3, ProtectionDomain var4, byte[] var5, Object var6);

    public byte[] transform(ClassLoader var1, String var2, Class<?> var3, ProtectionDomain var4, byte[] var5) throws IllegalClassFormatException;
}

