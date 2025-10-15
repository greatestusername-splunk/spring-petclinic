/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.boot.util;

import com.singularity.ee.agent.util.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityManagerPolicyHelper
extends SecurityManager {
    private final Map<String, List<String>> recordedPermissionHash = new ConcurrentHashMap<String, List<String>>();
    public static final String RECORDED_POLICY_FILE_NAME = "appdynamics-agent.policy";
    private static final String RECORDED_PERMISSIONS_FILE_NAME = "recordedPermissions.log";
    private static final long RECLAIM_SECURITY_MANAGER_WAIT_TIME = Long.parseLong(System.getProperty("appdynamics.security.manager.reclaim.interval", "100"));
    private boolean isUnitTest = false;
    private final boolean doGrantCodebaseProperty = Boolean.getBoolean("com.appdynamics.grant.codebase.policy");

    public SecurityManagerPolicyHelper() {
        this.setupRecorderFriendlySecurityPolicyImplementation();
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                SecurityManagerPolicyHelper.this.writeRecordedSecurityPolicyInInstallDir();
            }
        });
    }

    public SecurityManagerPolicyHelper(boolean isUnitTest) {
        this.isUnitTest = isUnitTest;
    }

    public void setupRecorderFriendlySecurityPolicyImplementation() {
        Policy.setPolicy(new Policy(){

            @Override
            public boolean implies(ProtectionDomain domain, Permission permission) {
                return true;
            }
        });
    }

    private void writeRecordedSecurityPolicyInInstallDir() {
        String installDir = null;
        try {
            Class<?> aepClass = Class.forName("com.singularity.ee.agent.appagent.AgentEntryPoint", false, ClassLoader.getSystemClassLoader());
            installDir = (String)aepClass.getDeclaredMethod("getAgentInstallDirectory", new Class[0]).invoke(null, new Object[0]);
        }
        catch (Exception e) {
            this.writeStatus("" + e);
            return;
        }
        this.writeRecordedSecurityPolcy(installDir);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void writeRecordedSecurityPolcy(String targetDir) {
        FileOutputStream policyfos = null;
        FileOutputStream logfos = null;
        try {
            boolean doGrantCodebase;
            URL targetURL = new File(targetDir).toURL();
            String logFile = targetDir + File.separator + RECORDED_PERMISSIONS_FILE_NAME;
            String policyFile = targetDir + File.separator + RECORDED_POLICY_FILE_NAME;
            policyfos = new FileOutputStream(policyFile);
            logfos = new FileOutputStream(logFile);
            this.writeStatus("Writing recorded security permissions to " + logFile);
            this.writeStatus("Writing agent security policy to " + policyFile);
            for (String perm : this.recordedPermissionHash.keySet()) {
                this.writeStream(">>>> Permission: " + perm + "\n", logfos);
            }
            this.writeStream("\n", logfos);
            boolean bl = doGrantCodebase = this.doGrantCodebaseProperty && !this.isUnitTest;
            if (doGrantCodebase) {
                this.writeStream("grant codebase \"" + targetURL + "-\"  {\n", policyfos);
            } else {
                this.writeStream("grant {\n", policyfos);
            }
            this.writeRecordedPermissions(logfos, policyfos);
            this.writeStream(this.getAllFilePermissions(), policyfos);
            this.writeStream(this.getAllSocketPermissions(), policyfos);
            this.writeStream("};\n", policyfos);
            logfos.close();
            logfos = null;
            policyfos.close();
            if (!new File(logFile).exists()) {
                throw new Exception("ERROR: The permissions log file (" + logFile + ") does not exist...");
            }
            if (!new File(policyFile).exists()) {
                throw new Exception("ERROR: The permissions policy file (" + policyFile + ") does not exist...");
            }
        }
        catch (Exception e) {
            this.writeStatus("Exception trying to create the policy " + e);
        }
        finally {
            try {
                if (logfos != null) {
                    logfos.close();
                }
                if (policyfos != null) {
                    policyfos.close();
                }
            }
            catch (IOException ioe) {
                this.writeStatus("Could not close permissions log or policy file " + ioe);
            }
        }
    }

    private String getAllFilePermissions() {
        return "          permission java.io.FilePermission \"<<ALL FILES>>\",\"read,write,delete\";";
    }

    private String getAllSocketPermissions() {
        return "          permission java.net.SocketPermission \"*\",\"resolve,connect\";";
    }

    private void writeRecordedPermissions(FileOutputStream logfos, FileOutputStream policyfos) throws IOException {
        for (String perm : this.recordedPermissionHash.keySet()) {
            List<String> permList = this.recordedPermissionHash.get(perm);
            this.writeStream(">>>> Requests For Permission: " + perm, logfos);
            for (String inst : permList) {
                this.writeStream("        " + inst + "\n", logfos);
                if (perm.startsWith("java.io.FilePermission") || perm.startsWith("java.net.SocketPermission")) continue;
                String permission = inst.trim().replace(")", "").replace("(\"", "").replace("Permission\" ", "Permission ").replace("\" \"", "\",\"");
                this.writeStream("          permission " + permission + ";", policyfos);
            }
        }
    }

    private void writeStream(String msg, FileOutputStream fos) throws IOException {
        fos.write((msg + "\n").getBytes());
    }

    private void writeStatus(String msg) {
        Console.out().println(msg);
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        this.recordPermission(perm);
    }

    @Override
    public void checkPermission(Permission perm) {
        this.recordPermission(perm);
    }

    private void reclaimSecurityManagerForRecording() {
        final SecurityManagerPolicyHelper securityManager = this;
        new Thread(){

            @Override
            public void run() {
                try {
                    Thread.sleep(RECLAIM_SECURITY_MANAGER_WAIT_TIME);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                System.setSecurityManager(securityManager);
            }
        }.start();
    }

    private void recordPermission(Permission perm) {
        StackTraceElement[] stes;
        if (perm.toString().contains("setSecurityManager")) {
            this.reclaimSecurityManagerForRecording();
        }
        boolean isOurAgent = false;
        for (StackTraceElement ste : stes = Thread.currentThread().getStackTrace()) {
            if (!ste.getClassName().startsWith("com.appdynamics") && !ste.getClassName().startsWith("com.singularity")) continue;
            isOurAgent = true;
            break;
        }
        if (isOurAgent) {
            this.addRecordedAgentPermission(perm);
        }
    }

    public void addRecordedAgentPermission(Permission perm) {
        String permClass = perm.getClass().getName();
        List<String> permList = this.recordedPermissionHash.get(permClass);
        if (permList == null) {
            permList = new ArrayList<String>();
            this.recordedPermissionHash.put(permClass, permList);
        }
        if (!permList.contains(perm.toString())) {
            permList.add(perm.toString());
        }
    }
}

