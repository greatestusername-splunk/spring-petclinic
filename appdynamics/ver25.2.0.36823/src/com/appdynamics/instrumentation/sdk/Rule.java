/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk;

import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import java.util.Arrays;

public class Rule {
    private final SDKClassMatchType classMatchType;
    private final SDKStringMatchType classStringMatchType;
    private final String classMatchString;
    private final SDKStringMatchType methodStringMatchType;
    private final String methodMatchString;
    private final String[] paramClasses;
    private final String localVarNameToMatch;
    private final String fieldNameToMatch;
    private final String invokeMethodToMatch;
    private final boolean read;
    private final boolean before;
    private final int occurrences;
    private final int lineNum;
    private final boolean isAbsLineNum;
    private final String[] localVarNameToCollect;

    private Rule(Builder builder) {
        this.classMatchType = builder.classMatchType;
        this.classStringMatchType = builder.classStringMatchType;
        this.classMatchString = builder.classMatchString;
        this.methodStringMatchType = builder.methodStringMatchType;
        this.methodMatchString = builder.methodMatchString;
        this.lineNum = builder.lineNum;
        this.paramClasses = builder.paramClasses;
        this.isAbsLineNum = builder.isAbsLineNum;
        this.localVarNameToMatch = builder.localVarNameToMatch;
        this.fieldNameToMatch = builder.fieldNameToMatch;
        this.invokeMethodToMatch = builder.invokeMethodToMatch;
        this.read = builder.read;
        this.before = builder.before;
        this.occurrences = builder.occurrence;
        this.localVarNameToCollect = builder.localVarNameToCollect;
    }

    public SDKClassMatchType getClassMatchType() {
        return this.classMatchType;
    }

    public SDKStringMatchType getClassStringMatchType() {
        return this.classStringMatchType;
    }

    public String getClassMatchString() {
        return this.classMatchString;
    }

    public SDKStringMatchType getMethodStringMatchType() {
        return this.methodStringMatchType;
    }

    public String getMethodMatchString() {
        return this.methodMatchString;
    }

    public int getLineNumber() {
        return this.lineNum;
    }

    public String getLocalVarNameToMatch() {
        return this.localVarNameToMatch;
    }

    public String getFieldNameToMatch() {
        return this.fieldNameToMatch;
    }

    public String getInvokeMethodToMatch() {
        return this.invokeMethodToMatch;
    }

    public int getOccurrences() {
        return this.occurrences;
    }

    public boolean isRead() {
        return this.read;
    }

    public boolean isBefore() {
        return this.before;
    }

    public String[] getLocalVarNameToCollect() {
        return this.localVarNameToCollect;
    }

    public boolean isAbsoluteLineNumber() {
        return this.isAbsLineNum;
    }

    public String[] getParamClasses() {
        return this.paramClasses;
    }

    public boolean hasMidMethodInfo() {
        return this.lineNum > -1 || this.localVarNameToMatch != null || this.fieldNameToMatch != null || this.invokeMethodToMatch != null;
    }

    public String toString() {
        return "Rule{classMatchType=" + (Object)((Object)this.classMatchType) + ", classStringMatchType=" + (Object)((Object)this.classStringMatchType) + ", classMatchString='" + this.classMatchString + '\'' + ", methodStringMatchType=" + (Object)((Object)this.methodStringMatchType) + ", methodMatchString='" + this.methodMatchString + '\'' + ", paramClasses=" + Arrays.toString(this.paramClasses) + ", localVarNameToMatch='" + this.localVarNameToMatch + '\'' + ", invokeMethodToMatch='" + this.invokeMethodToMatch + '\'' + ", read=" + this.read + ", before=" + this.before + ", occurrences=" + this.occurrences + ", lineNum=" + this.lineNum + ", isAbsLineNum=" + this.isAbsLineNum + ", localVarNameToCollect=" + Arrays.toString(this.localVarNameToCollect) + '}';
    }

    public static class Builder {
        private SDKClassMatchType classMatchType = SDKClassMatchType.MATCHES_CLASS;
        private SDKStringMatchType classStringMatchType = SDKStringMatchType.EQUALS;
        private String classMatchString;
        private SDKStringMatchType methodStringMatchType = SDKStringMatchType.EQUALS;
        private String methodMatchString = "";
        private String[] paramClasses;
        private int lineNum = -1;
        private boolean isAbsLineNum = false;
        private String localVarNameToMatch = null;
        private String fieldNameToMatch = null;
        private String invokeMethodToMatch = null;
        private boolean read = false;
        private boolean before = false;
        private int occurrence = 1;
        private String[] localVarNameToCollect = null;

        public Builder(String classMatchString) {
            this.classMatchString = classMatchString;
        }

        public Builder classMatchType(SDKClassMatchType type) {
            this.classMatchType = type;
            return this;
        }

        public Builder classStringMatchType(SDKStringMatchType type) {
            this.classStringMatchType = type;
            return this;
        }

        public Builder methodStringMatchType(SDKStringMatchType type) {
            this.methodStringMatchType = type;
            return this;
        }

        public Builder methodMatchString(String string) {
            this.methodMatchString = string;
            return this;
        }

        public Builder withParams(String ... classNames) {
            this.paramClasses = classNames;
            return this;
        }

        public Builder atLineNumber(int lineNum) {
            this.lineNum = lineNum;
            return this;
        }

        public Builder isAbsoluteLineNumber(boolean isAbsLineNum) {
            if (this.lineNum == -1) {
                return this;
            }
            this.isAbsLineNum = isAbsLineNum;
            return this;
        }

        public Builder atLocalVariable(String name, boolean read, int occurrence) {
            this.localVarNameToMatch = name;
            this.occurrence = occurrence;
            this.read = read;
            return this;
        }

        public Builder atField(String name, boolean read, int occurrence) {
            this.fieldNameToMatch = name;
            this.read = read;
            this.occurrence = occurrence;
            return this;
        }

        public Builder atMethodInvocation(String name, int occurrence) {
            this.invokeMethodToMatch = name;
            this.occurrence = occurrence;
            return this;
        }

        public Builder isInstrumentBefore(boolean before) {
            this.before = before;
            return this;
        }

        public Builder collectLocalVariables(String[] vars) {
            this.localVarNameToCollect = vars;
            return this;
        }

        public Rule build() {
            Rule rule = new Rule(this);
            return rule;
        }
    }
}

