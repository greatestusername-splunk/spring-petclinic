/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import com.singularity.ee.agent.util.JavaVersionUtil;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class JavaMultiReleaseLoader {
    private static final String MULTI_RELEASE_PATH = "multi-release";
    private final Instrumentation instrumentation;
    private final String agentInstallDirectory;
    private final IAgentBootLogger logger;

    public JavaMultiReleaseLoader(Instrumentation instrumentation, String agentInstallDirectory, IAgentBootLogger logger) {
        this.instrumentation = instrumentation;
        this.agentInstallDirectory = agentInstallDirectory;
        this.logger = logger;
    }

    public void performClassLoad() {
        if (JavaVersionUtil.isJava9orHigher()) {
            String filePath = this.agentInstallDirectory + File.separator + MULTI_RELEASE_PATH + File.separator + "java9-impl.jar";
            File file = new File(filePath);
            this.loadJarFileToBootstrapClassLoader(file);
        }
    }

    protected void loadJarFileToBootstrapClassLoader(File file) {
        if (file == null || !file.exists()) {
            this.logger.warn("File " + (file == null ? "is null." : file.getName() + " does not exist.") + " Please check java agent package.");
            return;
        }
        try {
            this.instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(file));
        }
        catch (IOException e) {
            this.logger.warn("Failed to load " + file.getName() + " into bootstrap class loader");
        }
    }
}

