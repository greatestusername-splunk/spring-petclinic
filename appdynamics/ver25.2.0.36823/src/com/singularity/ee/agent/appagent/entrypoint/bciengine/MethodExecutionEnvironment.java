/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.entrypoint.bciengine;

public class MethodExecutionEnvironment {
    private static final Class[] EMPTY_CLAZZ = new Class[0];
    private Object invokedObject;
    private String className;
    private String methodName;
    private Object[] paramValues;
    private Throwable thrownException;
    private Object returnValue;
    private Object crossAppBreadCrumb;

    public MethodExecutionEnvironment() {
    }

    public MethodExecutionEnvironment(Object invokedObject, String className, String methodName, Object[] paramValues) {
        this.init(invokedObject, className, methodName, paramValues);
    }

    public void init(Object invokedObject, String className, String methodName, Object[] paramValues) {
        this.invokedObject = invokedObject;
        this.className = className;
        this.methodName = methodName;
        this.paramValues = paramValues;
    }

    public void reset() {
        this.init(null, null, null, null);
        this.thrownException = null;
        this.returnValue = null;
    }

    public String getClassName() {
        return this.className;
    }

    public Object getInvokedObject() {
        return this.invokedObject;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public Object[] getParamValues() {
        return this.paramValues;
    }

    public Throwable getThrownException() {
        return this.thrownException;
    }

    public void setThrownException(Throwable thrownException) {
        this.thrownException = thrownException;
    }

    public Object getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public String safeGetClassName() {
        return this.invokedObject == null ? this.className : this.invokedObject.getClass().getName();
    }

    public String getClassAndMethodName() {
        return this.safeGetClassName() + "." + this.getMethodName();
    }

    public Class[] getParamClasses() {
        return MethodExecutionEnvironment.getParamClasses(this.paramValues);
    }

    private static Class[] getParamClasses(Object[] paramValues) {
        if (paramValues == null || paramValues.length == 0) {
            return EMPTY_CLAZZ;
        }
        Class[] clazz = new Class[paramValues.length];
        int inx = 0;
        for (Object paramValue : paramValues) {
            if (paramValue == null) continue;
            clazz[inx++] = paramValue.getClass();
        }
        return clazz;
    }

    private static String paramsAsString(Object[] paramValues) {
        if (paramValues == null || paramValues.length == 0) {
            return "[ ]";
        }
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < paramValues.length; ++i) {
            sb.append(MethodExecutionEnvironment.objectAsString(paramValues[i]));
            if (i >= paramValues.length - 1) continue;
            sb.append(",\n");
        }
        sb.append(" ]");
        return sb.toString();
    }

    private static String objectAsString(Object value) {
        StringBuilder sb = new StringBuilder();
        if (value == null) {
            sb.append("{ NULL }");
        } else {
            sb.append("{ Class='");
            sb.append(value.getClass().getName());
            sb.append("', Hash code=");
            sb.append(System.identityHashCode(value));
            sb.append(" }");
        }
        return sb.toString();
    }

    public Object getCrossAppBreadCrumb() {
        return this.crossAppBreadCrumb;
    }

    public void setCrossAppBreadCrumb(Object crossAppBreadCrumb) {
        this.crossAppBreadCrumb = crossAppBreadCrumb;
    }

    public String toString() {
        return MethodExecutionEnvironment.toString(this.invokedObject, this.className, this.methodName, this.paramValues, this.thrownException, this.returnValue);
    }

    public static String toString(Object invokedObject, String className, String methodName, Object[] paramValues) {
        return MethodExecutionEnvironment.toString(invokedObject, className, methodName, paramValues, null, null);
    }

    public static String toString(Object invokedObject, String className, String methodName, Object[] paramValues, Throwable thrownException, Object returnValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("MethodExecutionEnvironment{\n");
        sb.append(" invokedObject='");
        sb.append(MethodExecutionEnvironment.objectAsString(invokedObject));
        sb.append("',\n className='");
        sb.append(className);
        sb.append("',\n methodName='");
        sb.append(methodName);
        sb.append("',\n paramValues=");
        sb.append(MethodExecutionEnvironment.paramsAsString(paramValues));
        if (thrownException != null) {
            sb.append(",\n thrownException=");
            sb.append(thrownException);
        }
        if (returnValue != null) {
            sb.append(",\n returnValue class=");
            sb.append(MethodExecutionEnvironment.objectAsString(returnValue));
        }
        sb.append('}');
        return sb.toString();
    }
}

