/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.utils;

import com.singularity.ee.agent.util.io.Console;
import java.io.PrintWriter;
import java.io.StringWriter;

public class StackOperations {
    public static void printStackTrace(Throwable e) {
        e.printStackTrace(Console.out());
    }

    public static void printStackTrace(Throwable t, PrintWriter writer) {
        t.printStackTrace(writer);
    }

    public static void printStackTrace(Throwable t, StringBuilder builder) {
        PrintWriter writer = new PrintWriter(new StringWriter());
        StackOperations.printStackTrace(t, writer);
        builder.append(writer);
    }
}

