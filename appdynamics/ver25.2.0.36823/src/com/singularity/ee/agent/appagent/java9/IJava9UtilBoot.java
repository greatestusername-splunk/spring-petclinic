/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.java9;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IJava9UtilBoot {
    public boolean bootStrap(boolean var1);

    public Object getThisModule();

    public Object getBaseModule();

    public Object getModuleForClass(Class<?> var1);

    public boolean addExportsAndOpens(Object var1, Map<String, Set<Object>> var2, Map<String, Set<Object>> var3);

    public boolean addReads(Object var1, Object var2);

    public boolean addReads(Object var1, Set<String> var2);

    public Object createModuleFromClassLoader(URLClassLoader var1, String var2, String[] var3, String[] var4) throws IOException, URISyntaxException;

    public void addReadsToClassModule(Object var1, Class<?> var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    public void addOpensFromClass(Class<?> var1, String var2, Object var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    public void addExportsFromClass(Class<?> var1, String var2, Object var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    public void addExportsFromModule(Object var1, String var2, Object var3);

    public void addExportsToAll(Object var1, String[] var2);

    public Object locateModuleByName(String var1);

    public ClassLoader getPlatformClassLoader();

    public String getModuleName(Object var1);

    public boolean addUses(Object var1, Set<Class<?>> var2, Map<Class<?>, List<Class<?>>> var3);
}

