/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.utils;

import com.singularity.ee.agent.util.io.Console;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public abstract class WindowsRegistryReader {
    public static String getRegistryValue(String keyName, String attributeName) {
        String returnString = null;
        List<String> commands = Arrays.asList("reg", "query", keyName, "/v", attributeName);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            Process process = processBuilder.start();
            process.waitFor();
            if (process.exitValue() == 0) {
                InputStream is = process.getInputStream();
                returnString = WindowsRegistryReader.parseRegCommandOutput(is, attributeName);
            }
        }
        catch (Exception e) {
            e.printStackTrace(Console.out());
        }
        return returnString;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String parseRegCommandOutput(InputStream is, String attributeName) throws IOException {
        String returnString = null;
        try (BufferedReader br = null;){
            String nextLine;
            br = new BufferedReader(new InputStreamReader(is));
            while ((nextLine = br.readLine()) != null) {
                String[] splitString;
                if (!(nextLine = nextLine.trim()).startsWith(attributeName) || (splitString = nextLine.split("\\s+", 3)).length != 3 || !splitString[0].equals(attributeName) || !splitString[1].equals("REG_SZ")) continue;
                returnString = splitString[2];
            }
        }
        return returnString;
    }
}

