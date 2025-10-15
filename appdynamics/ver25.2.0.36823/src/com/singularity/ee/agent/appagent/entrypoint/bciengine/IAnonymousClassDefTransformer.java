/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import java.security.ProtectionDomain;

public interface IAnonymousClassDefTransformer {
    public byte[] classDefTrap(Class<?> var1, byte[] var2, Object[] var3);

    public byte[] classDefTrap(ClassLoader var1, Class var2, String var3, byte[] var4, ProtectionDomain var5);
}

