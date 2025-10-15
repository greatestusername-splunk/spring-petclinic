/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

public class ModifiedClassDefinition {
    private final String className;
    private final byte[] classBytes;

    public ModifiedClassDefinition(String className, byte[] classBytes) {
        this.className = className;
        this.classBytes = classBytes;
    }

    public byte[] getClassBytes() {
        return this.classBytes;
    }

    public String getClassName() {
        return this.className;
    }
}

