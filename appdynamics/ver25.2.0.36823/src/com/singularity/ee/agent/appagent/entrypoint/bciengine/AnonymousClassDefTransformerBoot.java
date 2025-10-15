/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAnonymousClassDefTransformer;
import java.security.ProtectionDomain;

public class AnonymousClassDefTransformerBoot {
    private static IAnonymousClassDefTransformer anonymousClassDefTransformer = new IAnonymousClassDefTransformer(){

        @Override
        public byte[] classDefTrap(Class<?> hostClass, byte[] data, Object[] cpPatches) {
            return data;
        }

        @Override
        public byte[] classDefTrap(ClassLoader classLoader, Class lookup, String className, byte[] data, ProtectionDomain pd) {
            return data;
        }
    };

    public static byte[] classDefTrap(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        byte[] result = null;
        try {
            result = anonymousClassDefTransformer.classDefTrap(hostClass, data, cpPatches);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return result == null ? data : result;
    }

    public static byte[] classDefTrap(ClassLoader classLoader, Class lookup, String className, byte[] data, ProtectionDomain protectionDomain) {
        byte[] result = null;
        try {
            result = anonymousClassDefTransformer.classDefTrap(classLoader, lookup, className, data, protectionDomain);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return result == null ? data : result;
    }

    public static void register(IAnonymousClassDefTransformer anonymousClassDefTransformer) {
        AnonymousClassDefTransformerBoot.anonymousClassDefTransformer = anonymousClassDefTransformer;
    }
}

