/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.util;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.SysOutAgentBootLogger;

public class JavaVersionUtil {
    private static final int SEVEN = 7;
    private static final JavaVersion DEFAULT_VERSION = JavaVersion.JAVA_23;
    private static final IAgentBootLogger logger = new SysOutAgentBootLogger();
    private static final JavaVersion runtimeVersion = JavaVersionUtil.getJavaVersion(System.getProperty("java.version"));

    protected static JavaVersion getJavaVersion(String version) {
        if (version != null) {
            if (version.length() >= 7) {
                version = version.substring(0, 7);
            }
            if (version.startsWith("1.")) {
                return JavaVersionUtil.parsePreJava9Version(version);
            }
            return JavaVersionUtil.parseJava9AndAbove(version);
        }
        return JavaVersionUtil.logWarningWithDefaultVersion();
    }

    private static JavaVersion logWarningWithDefaultVersion() {
        logger.warn("Warning: Java agent could not determine the version of java. Assuming 23.0");
        return DEFAULT_VERSION;
    }

    private static JavaVersion parsePreJava9Version(String version) {
        if (version.startsWith("1.8")) {
            return JavaVersion.JAVA_8;
        }
        return JavaVersionUtil.logWarningWithDefaultVersion();
    }

    private static JavaVersion parseJava9AndAbove(String version) {
        if (version.startsWith("9")) {
            return JavaVersion.JAVA_9;
        }
        if (version.startsWith("10")) {
            return JavaVersion.JAVA_10;
        }
        if ("11".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.0".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.1".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.2".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.3".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.4".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.5".equals(version)) {
            return JavaVersion.JAVA_11;
        }
        if ("11.0.6".equals(version)) {
            return JavaVersion.JAVA_11_0_6_PLUS;
        }
        if (version.startsWith("11.")) {
            return JavaVersion.JAVA_11_0_6_PLUS;
        }
        if (version.startsWith("12")) {
            return JavaVersion.JAVA_12;
        }
        if (version.startsWith("13")) {
            return JavaVersion.JAVA_13;
        }
        if (version.startsWith("14")) {
            return JavaVersion.JAVA_14;
        }
        if (version.startsWith("15")) {
            return JavaVersion.JAVA_15;
        }
        if (version.startsWith("16")) {
            return JavaVersion.JAVA_16;
        }
        if (version.startsWith("17")) {
            return JavaVersion.JAVA_17;
        }
        if (version.startsWith("18")) {
            return JavaVersion.JAVA_18;
        }
        if (version.startsWith("19")) {
            return JavaVersion.JAVA_19;
        }
        if (version.startsWith("20")) {
            return JavaVersion.JAVA_20;
        }
        if (version.startsWith("21")) {
            return JavaVersion.JAVA_21;
        }
        if (version.startsWith("22")) {
            return JavaVersion.JAVA_22;
        }
        if (version.startsWith("23")) {
            return JavaVersion.JAVA_23;
        }
        return JavaVersionUtil.logWarningWithDefaultVersion();
    }

    public static boolean isRuntimeJavaLessThan(String expectedVersion) {
        if (expectedVersion == null || expectedVersion.isEmpty()) {
            return false;
        }
        int same = runtimeVersion.compareTo(JavaVersionUtil.getJavaVersion(expectedVersion));
        return same < 0;
    }

    public static boolean isJava8() {
        return runtimeVersion == JavaVersion.JAVA_8;
    }

    public static boolean isJava9orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_9) >= 0;
    }

    public static boolean isJava10orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_10) >= 0;
    }

    public static boolean isJava11orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_11) >= 0;
    }

    public static boolean isJava11_0_6_orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_11_0_6_PLUS) >= 0;
    }

    public static boolean isJava12orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_12) >= 0;
    }

    public static boolean isJava13orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_13) >= 0;
    }

    public static boolean isJava14orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_14) >= 0;
    }

    public static boolean isJava15orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_15) >= 0;
    }

    public static boolean isJava16orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_16) >= 0;
    }

    public static boolean isJava17orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_17) >= 0;
    }

    public static boolean isJava18orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_18) >= 0;
    }

    public static boolean isJava19orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_19) >= 0;
    }

    public static boolean isJava20orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_20) >= 0;
    }

    public static boolean isJava21orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_21) >= 0;
    }

    public static boolean isJava22orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_22) >= 0;
    }

    public static boolean isJava23orHigher() {
        return runtimeVersion.compareTo(JavaVersion.JAVA_23) >= 0;
    }

    protected static enum JavaVersion {
        JAVA_8,
        JAVA_9,
        JAVA_10,
        JAVA_11,
        JAVA_11_0_6_PLUS,
        JAVA_12,
        JAVA_13,
        JAVA_14,
        JAVA_15,
        JAVA_16,
        JAVA_17,
        JAVA_18,
        JAVA_19,
        JAVA_20,
        JAVA_21,
        JAVA_22,
        JAVA_23;

    }
}

