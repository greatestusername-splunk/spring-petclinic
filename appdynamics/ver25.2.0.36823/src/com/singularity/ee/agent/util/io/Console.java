/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.util.io;

import java.io.IOException;
import java.io.PrintStream;

public class Console {
    private static final String PROPERTY = "appdynamics.agent.use.stderr";
    private static final Redirection redirect;
    private static PrintStream file;

    private static Redirection selectFromProperty(String propValue) {
        if (propValue.startsWith("file:")) {
            String fileName = propValue.substring(5);
            try {
                PrintStream ps = new PrintStream(fileName);
                ps.println("AppDynamics Java Agent console output:");
                ps.flush();
                file = ps;
                return Redirection.FILE;
            }
            catch (IOException e) {
                e.printStackTrace();
                return Redirection.STDERR;
            }
        }
        if ("false".equals(propValue)) {
            return Redirection.STDOUT;
        }
        return Redirection.STDERR;
    }

    public static PrintStream out() {
        switch (redirect) {
            case FILE: {
                return file;
            }
            case STDOUT: {
                return System.out;
            }
        }
        return System.err;
    }

    static {
        String propValue = System.getProperty(PROPERTY);
        redirect = propValue != null ? Console.selectFromProperty(propValue) : Redirection.STDOUT;
    }

    private static enum Redirection {
        STDERR,
        STDOUT,
        FILE;

    }
}

