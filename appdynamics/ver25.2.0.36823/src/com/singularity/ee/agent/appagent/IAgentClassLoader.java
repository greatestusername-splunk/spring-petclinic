/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.ProtectionDomain;

public interface IAgentClassLoader {
    public Class<?> findBootstrapClassWithReflection(String var1) throws ClassNotFoundException;

    public InputStream getBootstrapResource(String var1) throws Throwable;

    public boolean tryToLock();

    public void releaseLock();

    public void preLoadAgentClasses();

    public void setLogger(IAgentBootLogger var1);

    public void defineGeneratedClass(String var1, byte[] var2, ProtectionDomain var3);

    public void addURLToClassPath(URL var1);

    public boolean isTestForClassLoadsInTransform();

    public void setTestForClassLoadsInTransform(boolean var1);

    public void setTransformOnCurrentThread(boolean var1);

    public String getAgentVersion(Class<?> var1) throws IOException;
}

