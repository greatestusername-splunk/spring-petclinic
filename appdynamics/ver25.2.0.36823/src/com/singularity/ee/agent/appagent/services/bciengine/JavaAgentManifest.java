/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.services.bciengine;

import com.singularity.ee.agent.appagent.AgentEntryPoint;
import com.singularity.ee.agent.util.io.Console;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JavaAgentManifest {
    public static final String DISABLE_RETRANSFORMATION = "appdynamics.agent.disable.retransformation";
    private static final String CAN_REDEFINE_CLASSES_ATTRIBUTE = "Can-Redefine-Classes";
    private static final String CAN_RETRANFORM_CLASSES_ATTRIBUTE = "Can-Retransform-Classes";
    private static final String JAVA_AGENT_VERSION_ATTRIBUTE = "Javaagent-Version";
    private static final String DEFAULT_APP_AGENT_VERSION_ATTRIBUTE = "Default-appagent-version";
    private static final String BOOT_CLASS_PATH_ATTRIBUTE = "Boot-Class-Path";
    private static JavaAgentManifest _instance;
    private final boolean canRedefineClasses;
    private final boolean canRetransformClasses;
    private final File jarFile;
    private String javaAgentVersion;
    private String defaultAppAgentVersion;
    private String bootClassPathAttributeValue;
    private Attributes attrs;

    public JavaAgentManifest(File pjarFile) {
        boolean l_canRedefineClasses = false;
        boolean l_canRetransformClasses = false;
        this.jarFile = pjarFile;
        try {
            JarFile javaAgentJar = new JarFile(this.jarFile);
            Manifest manifest = javaAgentJar.getManifest();
            if (manifest != null) {
                String canRetransformValue;
                this.attrs = manifest.getMainAttributes();
                String canRedefineValue = this.attrs.getValue(CAN_REDEFINE_CLASSES_ATTRIBUTE);
                if (canRedefineValue != null) {
                    l_canRedefineClasses = Boolean.parseBoolean(canRedefineValue);
                }
                if ((canRetransformValue = this.attrs.getValue(CAN_RETRANFORM_CLASSES_ATTRIBUTE)) != null) {
                    l_canRetransformClasses = Boolean.parseBoolean(canRetransformValue);
                }
                this.javaAgentVersion = this.attrs.getValue(JAVA_AGENT_VERSION_ATTRIBUTE);
                this.defaultAppAgentVersion = this.attrs.getValue(DEFAULT_APP_AGENT_VERSION_ATTRIBUTE);
                this.bootClassPathAttributeValue = this.attrs.getValue(BOOT_CLASS_PATH_ATTRIBUTE);
            }
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
        }
        this.canRedefineClasses = l_canRedefineClasses;
        this.canRetransformClasses = l_canRetransformClasses;
    }

    public static JavaAgentManifest parseManifest(File jarFile) {
        JavaAgentManifest cached = _instance;
        if (cached == null || !cached.jarFile.getAbsolutePath().equals(jarFile.getAbsolutePath())) {
            _instance = cached = new JavaAgentManifest(jarFile);
        }
        return cached;
    }

    public static JavaAgentManifest parseManifest(String agentDirectory, String jarName) {
        File jarFile = new File(new File(agentDirectory), jarName);
        return JavaAgentManifest.parseManifest(jarFile);
    }

    public static JavaAgentManifest parseManifest(String agentDirectory) {
        File jarFile = new File(new File(agentDirectory), AgentEntryPoint.AGENT_ENTRY_POINT_JAR);
        return JavaAgentManifest.parseManifest(jarFile);
    }

    public boolean canRedefineClasses() {
        return this.canRedefineClasses;
    }

    public boolean canRetransformClasses() {
        return this.canRetransformClasses;
    }

    public boolean isRetransformationSupported() {
        boolean disableRetransformation = Boolean.getBoolean(DISABLE_RETRANSFORMATION);
        if (disableRetransformation) {
            return false;
        }
        return this.canRedefineClasses || this.canRetransformClasses;
    }

    public String getJavaAgentVersion() {
        return this.javaAgentVersion;
    }

    public String getDefaultAppAgentVersion() {
        return this.defaultAppAgentVersion;
    }

    public String getAttribute(String attributeName) {
        return this.attrs.getValue(attributeName);
    }

    public List<URL> getBootClassPathAsURLList(File rootDirectory) {
        List<URL> returnList;
        if (this.bootClassPathAttributeValue != null && this.bootClassPathAttributeValue.length() > 0) {
            String[] parsedFileNames;
            returnList = new ArrayList<URL>();
            for (String nextFile : parsedFileNames = this.bootClassPathAttributeValue.split(" ")) {
                File file = !nextFile.startsWith(File.separator) ? new File(rootDirectory, nextFile) : new File(nextFile);
                if (!file.exists() || file.isDirectory()) continue;
                try {
                    URL url = file.toURI().toURL();
                    returnList.add(url);
                }
                catch (MalformedURLException e) {
                    e.printStackTrace(Console.out());
                }
            }
        } else {
            returnList = Collections.emptyList();
        }
        return returnList;
    }

    public static void main(String[] args) {
        System.out.println(new JavaAgentManifest(new File(args[0])).isRetransformationSupported());
    }
}

