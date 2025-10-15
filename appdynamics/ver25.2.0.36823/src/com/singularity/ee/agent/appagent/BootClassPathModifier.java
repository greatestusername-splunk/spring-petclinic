/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.ModifiedClassDefinition;
import com.singularity.ee.agent.util.JavaVersionUtil;
import com.singularity.ee.agent.util.io.Console;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class BootClassPathModifier {
    private static final String APPEND_METHOD_NAME = "appendToBootstrapClassLoaderSearch";
    private static final String BOOT_CLASS_PATH_PROP_NAME = "sun.boot.class.path";
    private static final String BOOT_CLASS_PATH_HOLDER_CLASS_NAME = "sun.misc.Launcher$BootClassPathHolder";
    private static final String LAUNCHER_CLASS_NAME = "sun.misc.Launcher";
    private static final String BOOT_CLASS_PATH_HOLDER_FIELD_NAME = "bcp";
    private static final String ADD_URL_METHOD_NAME = "addURL";
    private static final String LAUNCHER_BOOT_CLASS_PATH_HOLDER_FIELD_NAME = "bootstrapClassPath";
    private Method appendMethod;
    private final Instrumentation instrumentation;
    private final boolean isJava9;

    public BootClassPathModifier(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.isJava9 = JavaVersionUtil.isJava9orHigher();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addURLsToBootClassPath(List<URL> classPath) {
        boolean modifiedOK;
        try {
            String bootClassPathProperty = System.getProperty(BOOT_CLASS_PATH_PROP_NAME);
            modifiedOK = true;
            ACachedBootClassPathModifier cachedBootClassPathModifier = !this.isJava9 ? this.createCachedBootClassPathModifier() : null;
            try {
                for (URL url : classPath) {
                    File file;
                    if (!url.getProtocol().equals("file") || !(file = new File(url.getFile())).exists() || !file.getName().endsWith(".jar")) continue;
                    if (!this.addToBootClassPath(file)) {
                        modifiedOK = false;
                        break;
                    }
                    if (bootClassPathProperty != null) {
                        bootClassPathProperty = bootClassPathProperty + File.pathSeparator + file.getAbsolutePath();
                    }
                    if (cachedBootClassPathModifier == null) continue;
                    cachedBootClassPathModifier.addURL(url);
                }
            }
            finally {
                if (cachedBootClassPathModifier != null) {
                    cachedBootClassPathModifier.cleanup();
                }
            }
            if (modifiedOK && bootClassPathProperty != null) {
                System.setProperty(BOOT_CLASS_PATH_PROP_NAME, bootClassPathProperty);
            }
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
            modifiedOK = false;
        }
        return modifiedOK;
    }

    private boolean addToBootClassPath(File file) {
        boolean bReturn = false;
        try {
            if (this.appendMethod == null) {
                this.appendMethod = this.instrumentation.getClass().getMethod(APPEND_METHOD_NAME, JarFile.class);
            }
            JarFile jarFile = new JarFile(file);
            this.appendMethod.invoke(this.instrumentation, jarFile);
            bReturn = true;
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
        }
        return bReturn;
    }

    public static boolean createTempJar(File fileToCreate, Collection<ModifiedClassDefinition> classesToDefine) {
        boolean bReturn = false;
        try {
            if (fileToCreate.exists() && !fileToCreate.delete()) {
                return bReturn;
            }
            Manifest manifest = new Manifest();
            JarOutputStream target = new JarOutputStream((OutputStream)new FileOutputStream(fileToCreate), manifest);
            if (classesToDefine != null) {
                for (ModifiedClassDefinition nextResource : classesToDefine) {
                    String resourceName = nextResource.getClassName().endsWith(".class") ? nextResource.getClassName() : nextResource.getClassName() + ".class";
                    BootClassPathModifier.copyToJarFile(target, resourceName, new ByteArrayInputStream(nextResource.getClassBytes()));
                }
            }
            target.close();
            fileToCreate.deleteOnExit();
            bReturn = true;
        }
        catch (IOException e) {
            e.printStackTrace(Console.out());
        }
        return bReturn;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void copyToJarFile(JarOutputStream jos, String jarFileEntryName, InputStream inputStream) throws IOException {
        try {
            int available;
            ZipEntry zipEntry = new ZipEntry(jarFileEntryName);
            jos.putNextEntry(zipEntry);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            while ((available = bis.available()) > 0) {
                byte[] buffer = new byte[available];
                int bytesRead = bis.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                if (bytesRead <= 0) continue;
                jos.write(buffer, 0, bytesRead);
            }
        }
        finally {
            inputStream.close();
        }
    }

    private ACachedBootClassPathModifier createCachedBootClassPathModifier() {
        ACachedBootClassPathModifier returnObject = null;
        try {
            returnObject = new Java17CachedBootClassPathModifier();
        }
        catch (Exception e) {
            try {
                returnObject = new Java16CachedBootClassPathModifier();
            }
            catch (Exception e1) {
                Console.out().println("Unable to find method to update cached boot class path");
                e1.printStackTrace(Console.out());
            }
        }
        return returnObject;
    }

    private static class Java16CachedBootClassPathModifier
    extends ACachedBootClassPathModifier {
        Java16CachedBootClassPathModifier() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
            super(BootClassPathModifier.LAUNCHER_CLASS_NAME, BootClassPathModifier.LAUNCHER_BOOT_CLASS_PATH_HOLDER_FIELD_NAME);
        }
    }

    private static class Java17CachedBootClassPathModifier
    extends ACachedBootClassPathModifier {
        Java17CachedBootClassPathModifier() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
            super(BootClassPathModifier.BOOT_CLASS_PATH_HOLDER_CLASS_NAME, BootClassPathModifier.BOOT_CLASS_PATH_HOLDER_FIELD_NAME);
        }
    }

    static abstract class ACachedBootClassPathModifier {
        protected Class<?> bootClassPathHolderClass;
        protected Field bcpField;
        protected boolean accessibleFlag;
        protected Object urlClassPath;
        protected Method addURLMethod;

        protected ACachedBootClassPathModifier(String holdingClassName, String fieldName) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
            this.bootClassPathHolderClass = Class.forName(holdingClassName);
            this.bcpField = this.bootClassPathHolderClass.getDeclaredField(fieldName);
            this.accessibleFlag = this.bcpField.isAccessible();
            if (!this.accessibleFlag) {
                this.bcpField.setAccessible(true);
            }
            this.urlClassPath = this.bcpField.get(null);
            this.addURLMethod = this.urlClassPath.getClass().getMethod(BootClassPathModifier.ADD_URL_METHOD_NAME, URL.class);
        }

        void addURL(URL url) throws Exception {
            if (this.urlClassPath != null) {
                this.addURLMethod.invoke(this.urlClassPath, url);
            }
        }

        void cleanup() {
            if (!this.accessibleFlag) {
                this.bcpField.setAccessible(false);
            }
        }
    }
}

