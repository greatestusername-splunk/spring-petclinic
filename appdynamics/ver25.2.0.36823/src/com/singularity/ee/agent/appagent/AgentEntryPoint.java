/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.AgentVersionSpecification;
import com.singularity.ee.agent.appagent.AgentVersionSpecificationFileParser;
import com.singularity.ee.agent.appagent.BootClassPathModifier;
import com.singularity.ee.agent.appagent.IAgentClassLoader;
import com.singularity.ee.agent.appagent.IAppTierNodeDeterminer;
import com.singularity.ee.agent.appagent.JavaMultiReleaseLoader;
import com.singularity.ee.agent.appagent.ModifiedClassDefinition;
import com.singularity.ee.agent.appagent.PossibleAgentVersion;
import com.singularity.ee.agent.appagent.UnableToFindAppagentException;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.IAgentBootLogger;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.SysOutAgentBootLogger;
import com.singularity.ee.agent.appagent.java9.IJava9UtilBoot;
import com.singularity.ee.agent.appagent.java9.exception.ExportsAccessRuntimeException;
import com.singularity.ee.agent.appagent.services.bciengine.JavaAgentManifest;
import com.singularity.ee.agent.util.JavaVersionUtil;
import com.singularity.ee.agent.util.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

public class AgentEntryPoint {
    public static final String APP_AGENT_INSTALL_DIR = "appagent.install.dir";
    public static final String APP_AGENT_NODE_DIR = "appdynamics.agent.node.dir";
    public static final String AGENT_HOST_CLASS_PROPERTY = "appagent.host.class.name";
    public static final String DEFAULT_AGENT_HOST_CLASS_NAME = "com.singularity.ee.agent.appagent.kernel.JavaAgent";
    public static final String AGENT_JAR = "appagent.jar";
    public static final String NON_IBM_JAVAAGENT_NAME = "javaagent.jar";
    public static final String IBM_JAVAAGENT_JAR = "javaagent-ibm.jar";
    public static String AGENT_ENTRY_POINT_JAR = "javaagent.jar";
    public static final String NAMESPACE_SAFE_LOG4J_JAR = "singularity-log4j.jar";
    public static final String AGENT_INIT_METHOD = "initialize";
    public static final String USE_SYSTEM_CLASS_LOADER_PARENT = "appagent.usesystemclassloader";
    public static final String USER_WORKING_DIRECTORY = "user.dir";
    private static final String APPDYNAMICS_READ_MODULES = "appdynamics.read.modules";
    private static final String APPDYNAMICS_EXPORT_PACKAGES = "appdynamics.agent.export.packages";
    private static final String USE_BOOT_CLASS_LOADER_PROP_NAME = "appdynamics.use.boot.class.loader";
    private static boolean useBootClassLoader;
    public static final String USE_BOOTSTRAP_CLASS_LOADER_PARENT = "appagent.usebootstrap.as.parent";
    public static final String AGENT_TYPE = "agentType";
    public static final String APP_AGENT_DISABLE = "appagent.disable";
    public static final String LIB_DIR = "lib";
    public static final String VERSION_DIR_PREFIX = "ver";
    public static final String CONFIG_DIR = "conf";
    public static final String THIRD_PARTY_LIB_DIR = "tp";
    private static final Class[] agentInitArgs;
    public static final String APP_TIER_NODE_DETERMINER_CLASS_NAME = "com.singularity.ee.agent.appagent.kernel.AppTierNodeDeterminer";
    public static final String CONTROLLER_CONFIG_FILE = "controller-info.xml";
    public static final String AGENT_VERSION_DEFINITION_FILE_NAME = "agent-version.ini";
    private static final String AGENT_PROPERTIES_FILE_NAME = "agent.properties";
    private static ClassLoader agentHostClassLoader;
    private static Object javaAgent;
    public static boolean reTransformSupported;
    public static boolean reDefinitionSupported;
    protected static final char UNIX_SEPARATOR = '/';
    protected static final char WINDOWS_SEPARATOR = '\\';
    static final String CURRENT_VERSION = "latest";
    private static String mostCurrentVersion;
    private static final String AGENT_CLASS_LOADER_CLASS_NAME = "com.singularity.ee.agent.appagent.kernel.classloader.AgentClassLoader";
    private static final String GET_AGENT_CLASS_LOADER_METHOD_NAME = "getAgentClassLoader";
    public static final String DYNAMIC_ATTACH = "appdynamics.dynamic.attach";
    private static String agentVersionDirectory;
    private static String baseInstallDirectory;
    private static String javaAgentVersion;
    private static String defaultAppAgentVersion;
    private static JavaAgentManifest manifest;
    private static Map<String, File> mapOfAllVersionSubDirectories;
    static String nodeDir;
    static String agentInstallDir;
    private static WeakReference<Thread> refToMainThread;
    private static File tempBootClassJarFile;
    private static List<URL> libraries;
    private static List<URL> librariesAddedToBootClassPath;
    private static boolean appAgentJarsAddedToBootClassPath;
    private static volatile boolean inPreMain;
    private static volatile boolean runningAtJVMInit;
    private static volatile String javaAgentName;
    private static final String JAVA_LANG_PACKAGE = "java.lang";
    private static final String JAVA_SECURITY_PACKAGE = "java.security";
    private static final String SUN_INSTRUMENT_PACKAGE = "sun.instrument";
    private static final String ORG_APACHE_XERCES_JAXP = "org.apache.xerces.jaxp";
    private static final String JAVA_XML_MODULE_NAME = "java.xml";
    private static final String[] JAVA9_NON_CRITICAL_EXPORT_REQUIREMENTS;
    private static final String[] JAVA9_CRITICAL_EXPORT_REQUIREMENTS;
    private static final String[] JAVA9_USES_REQUIREMENTS;
    private static final String[] JAVA9_READ_MODULE_BY_NAME;
    private static final String APPAGENT_MODULE_NAME = "com.appdynamics.appagent";
    private static final String[] APPAGENT_REQUIRED_MODULE_NAMES;
    private static final String[] APPAGENT_EXPORTS_NAME;
    private static final String[] agentClassLoaderRequiredPackages;
    private static volatile boolean isLog4j1FactoryPropertySet;
    private static final int CHECK_LOG4J_PROPERTY_UNSET_INTERVAL = 10000;
    private static int MAX_LOG4J_PROPERTY_CHECK_COUNT;
    private static final NonBootPackageAccess[] NON_BOOT_PACKAGE_ACCESS;
    private static volatile IJava9UtilBoot java9Util;
    private static final IAgentBootLogger logger;

    public static void agentmain(String agentArgs, Instrumentation inst) throws Exception {
        if (javaAgent != null) {
            throw new Exception("Attempting to attach to a JVM that is already instrumented by Java agent");
        }
        agentArgs = agentArgs == null ? "appdynamics.dynamic.attach=" + Boolean.toString(Boolean.TRUE) : agentArgs + ",appdynamics.dynamic.attach=" + Boolean.toString(Boolean.TRUE);
        AgentEntryPoint.setSystemProperties(agentArgs);
        if (agentArgs != null) {
            AgentEntryPoint.setSystemProperties(agentArgs);
        }
        AgentEntryPoint.premain(agentArgs, inst);
        try {
            Method initMethod = javaAgent.getClass().getMethod("redefineAllClassesNeeded", new Class[0]);
            initMethod.invoke(javaAgent, new Object[0]);
        }
        catch (Throwable e) {
            e.printStackTrace(Console.out());
        }
    }

    private static void setSystemProperties(String agentArgs) {
        StringTokenizer st = new StringTokenizer(agentArgs, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String key = token.substring(0, token.indexOf("="));
            String value = token.substring(token.indexOf("=") + 1, token.length());
            Console.out().println("Setting System Property Key [" + key + "] Value [" + value + "]");
            System.setProperty(key, value);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void readAgentPropertiesFile(String agentPropertiesFileName) {
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        if (new File(agentPropertiesFileName).exists()) {
            try {
                inputStream = new FileInputStream(agentPropertiesFileName);
                properties.load(inputStream);
                for (Map.Entry<Object, Object> objectEntry : properties.entrySet()) {
                    String propertyKey = (String)objectEntry.getKey();
                    if (System.getProperty(propertyKey) != null) continue;
                    String propertyValue = (String)objectEntry.getValue();
                    System.setProperty(propertyKey, propertyValue);
                }
            }
            catch (Throwable e) {
                e.printStackTrace(Console.out());
            }
            finally {
                try {
                    if (inputStream != null) {
                        ((InputStream)inputStream).close();
                    }
                }
                catch (IOException e) {
                    Console.out().println("Cannot close file " + agentPropertiesFileName + ". Error Message: " + e.getMessage());
                }
            }
        }
    }

    protected static void configureAgentProperties(String agentInstallDir, String agentVersionDirectory) {
        String agentPropertiesFileName = agentVersionDirectory + File.separator + CONFIG_DIR + File.separator + AGENT_PROPERTIES_FILE_NAME;
        String agentPropertiesFileNameGlobal = agentInstallDir + File.separator + CONFIG_DIR + File.separator + AGENT_PROPERTIES_FILE_NAME;
        AgentEntryPoint.readAgentPropertiesFile(agentPropertiesFileName);
        AgentEntryPoint.readAgentPropertiesFile(agentPropertiesFileNameGlobal);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        inPreMain = true;
        runningAtJVMInit = AgentEntryPoint.testIfRunningAtJVMInit();
        AGENT_ENTRY_POINT_JAR = runningAtJVMInit ? AgentEntryPoint.getJavaAgentName() : NON_IBM_JAVAAGENT_NAME;
        if (javaAgent != null) {
            Console.out().println("Multiple instances of AppDynamics java agent detected in the command line. First instance takes precedence.");
            return;
        }
        if (Boolean.getBoolean(APP_AGENT_DISABLE)) {
            Console.out().println("App Agent Disabled");
            return;
        }
        refToMainThread = new WeakReference<Thread>(Thread.currentThread());
        agentInstallDir = System.getProperty(APP_AGENT_INSTALL_DIR);
        if (agentInstallDir == null && (agentInstallDir = AgentEntryPoint.getAgentInstallDirectory()) == null) {
            Console.out().println("Could not resolve Agent installation directory, please restart with system property appagent.install.dir");
            return;
        }
        baseInstallDirectory = agentInstallDir;
        try {
            File appagentJarFileOnBootClassPath;
            Class<?> agentHostClass = null;
            String javaAgentClassName = System.getProperty(AGENT_HOST_CLASS_PROPERTY, DEFAULT_AGENT_HOST_CLASS_NAME);
            nodeDir = AgentEntryPoint.getNodeDir(agentInstallDir);
            if (!nodeDir.equals(agentInstallDir)) {
                manifest = null;
            }
            AgentEntryPoint.determineJavaAgentVersion(agentInstallDir);
            AgentEntryPoint.initJavaVersion();
            JavaMultiReleaseLoader multiReleaseLoader = new JavaMultiReleaseLoader(instrumentation, baseInstallDirectory, logger);
            multiReleaseLoader.performClassLoad();
            if (JavaVersionUtil.isJava9orHigher()) {
                logger.warn("Java 9+ detected, booting with Java9Util enabled.");
                try {
                    if (java9Util == null) {
                        Class<AgentEntryPoint> clazz = AgentEntryPoint.class;
                        // MONITORENTER : com.singularity.ee.agent.appagent.AgentEntryPoint.class
                        if (java9Util == null) {
                            Class<?> java9UtilClass = Class.forName("com.singularity.ee.agent.appagent.java9.Java9Util");
                            Constructor<?> java9UtilConstructor = java9UtilClass.getConstructor(Instrumentation.class);
                            java9Util = (IJava9UtilBoot)java9UtilConstructor.newInstance(instrumentation);
                        }
                        // MONITOREXIT : clazz
                    }
                    java9Util.bootStrap(JavaVersionUtil.isJava11_0_6_orHigher());
                    AgentEntryPoint.forceOpenJava9Modules(instrumentation);
                }
                catch (ClassNotFoundException e) {
                    logger.warn(String.format("Java agent fails to initialize under Java 9 because: %s", e.toString()), e);
                    throw e;
                }
            }
            boolean agentJarAlreadyOnBootPath = false;
            AgentEntryPoint.configureAgentProperties(baseInstallDirectory, agentVersionDirectory);
            List<URL> bootClassPathFromManifest = AgentEntryPoint.getBootClassPathAttribute(agentInstallDir);
            if (bootClassPathFromManifest != null && (appagentJarFileOnBootClassPath = AgentEntryPoint.getAppAgentJarFileFrom(bootClassPathFromManifest)) != null) {
                agentJarAlreadyOnBootPath = true;
                agentVersionDirectory = appagentJarFileOnBootClassPath.getParent();
                useBootClassLoader = true;
                libraries = bootClassPathFromManifest;
                agentHostClass = Class.forName(javaAgentClassName);
            }
            if (agentJarAlreadyOnBootPath) return;
            mapOfAllVersionSubDirectories = AgentEntryPoint.createMapOfAllVersionSubDirectories(nodeDir);
            AgentVersionSpecification requiredVersion = AgentEntryPoint.determineAppAgentVersionToUse(nodeDir);
            if (requiredVersion == null) {
                requiredVersion = AgentEntryPoint.createDefaultVersionSpecification();
            }
            if (requiredVersion != null) {
                String useBootClassLoaderPropValue;
                Console.out().println("Install Directory resolved to[" + agentInstallDir + "]");
                libraries = new ArrayList<URL>();
                agentVersionDirectory = AgentEntryPoint.getAgentClassPath(agentInstallDir, requiredVersion.getVersionRequired(), libraries);
                if (agentVersionDirectory != null) {
                    agentInstallDir = agentVersionDirectory;
                }
                if ((useBootClassLoader = Boolean.parseBoolean(useBootClassLoaderPropValue = System.getProperty(USE_BOOT_CLASS_LOADER_PROP_NAME, "false"))) && requiredVersion.supportsBootstrapClassPath() && (tempBootClassJarFile = AgentEntryPoint.createTempBootClassJarFile(requiredVersion)) != null) {
                    BootClassPathModifier bootClassPathModifier = new BootClassPathModifier(instrumentation);
                    ArrayList<URL> tempBootClassJarClassPath = new ArrayList<URL>(1);
                    tempBootClassJarClassPath.add(tempBootClassJarFile.toURI().toURL());
                    if (bootClassPathModifier.addURLsToBootClassPath(tempBootClassJarClassPath)) {
                        librariesAddedToBootClassPath = requiredVersion.removeBootstrapJars(libraries);
                        if (!bootClassPathModifier.addURLsToBootClassPath(librariesAddedToBootClassPath)) {
                            libraries.addAll(librariesAddedToBootClassPath);
                            librariesAddedToBootClassPath = null;
                        } else {
                            appAgentJarsAddedToBootClassPath = true;
                        }
                    }
                }
                agentHostClassLoader = AgentEntryPoint.createClassLoader(libraries.toArray(new URL[libraries.size()]), AgentEntryPoint.getParentLoader(), baseInstallDirectory);
                if (JavaVersionUtil.isJava9orHigher()) {
                    AgentEntryPoint.createJava9Module();
                }
                agentHostClass = agentHostClassLoader.loadClass(javaAgentClassName);
                javaAgent = agentHostClass.newInstance();
                final Method initMethod = javaAgent.getClass().getMethod(AGENT_INIT_METHOD, agentInitArgs);
                final AtomicBoolean wakeupFlag = new AtomicBoolean();
                Thread t = new Thread("AD Agent init"){

                    @Override
                    public void run() {
                        try {
                            initMethod.invoke(javaAgent, agentInstallDir, agentArgs, instrumentation);
                            wakeupFlag.set(true);
                        }
                        catch (Throwable e) {
                            e.printStackTrace(Console.out());
                            javaAgent = null;
                        }
                    }
                };
                AgentEntryPoint.waitForAgentStartup(t, wakeupFlag);
                return;
            }
            Console.out().println("Unable to locate appagent version to use - Java agent disabled");
            return;
        }
        catch (Throwable e) {
            e.printStackTrace(Console.out());
            return;
        }
        finally {
            runningAtJVMInit = false;
            inPreMain = false;
        }
    }

    private static void waitForAgentStartup(Thread t, AtomicBoolean wakeupFlag) {
        boolean startupTimeoutPropSet;
        long startStamp = System.currentTimeMillis();
        t.start();
        int waitTime = Integer.parseInt(System.getProperty("appagent.start.timeout", "60000"));
        boolean bl = startupTimeoutPropSet = System.getProperty("appagent.start.timeout") != null;
        while (!wakeupFlag.get()) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            if (System.currentTimeMillis() <= startStamp + (long)waitTime) continue;
            int updateWaitTime = AgentEntryPoint.updatePremainWaitTimeLog4j1();
            if (updateWaitTime > 0 && --MAX_LOG4J_PROPERTY_CHECK_COUNT >= 0 && !startupTimeoutPropSet) {
                waitTime += updateWaitTime;
                logger.warn("Incrementing Pre-main wait time till logger property is reset by 10000ms, pendingRetryCount = " + MAX_LOG4J_PROPERTY_CHECK_COUNT);
                continue;
            }
            logger.warn("Warning: Agent startup exceeded the timeout of " + waitTime + " , application now enabled to start...");
            break;
        }
    }

    private static int updatePremainWaitTimeLog4j1() {
        boolean log4j2Disabled = Boolean.parseBoolean(System.getProperty("appdynamics.agent.log4j2.disabled"));
        if (log4j2Disabled && isLog4j1FactoryPropertySet) {
            return 10000;
        }
        return -1;
    }

    public static void log4j1FactoryPropertySet(boolean propertySet) {
        isLog4j1FactoryPropertySet = propertySet;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void clearAgentClassLoaderReferences() {
        Class<AgentEntryPoint> clazz = AgentEntryPoint.class;
        synchronized (AgentEntryPoint.class) {
            javaAgent = null;
            agentHostClassLoader = null;
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    private static void forceOpenJava9Modules(Instrumentation instrumentation) {
        try {
            HashMap<String, Set<Object>> opens = new HashMap<String, Set<Object>>();
            Object thisModule = java9Util.getThisModule();
            Object baseModule = java9Util.getBaseModule();
            HashSet<Object> thisModuleSet = new HashSet<Object>();
            thisModuleSet.add(thisModule);
            opens.put(JAVA_LANG_PACKAGE, thisModuleSet);
            opens.put(JAVA_SECURITY_PACKAGE, thisModuleSet);
            java9Util.addExportsAndOpens(baseModule, Collections.EMPTY_MAP, opens);
            Object javaInstrumentModule = java9Util.getModuleForClass(instrumentation.getClass());
            HashMap<String, Set<Object>> exports = new HashMap<String, Set<Object>>();
            exports.put(SUN_INSTRUMENT_PACKAGE, thisModuleSet);
            java9Util.addExportsAndOpens(javaInstrumentModule, exports, Collections.EMPTY_MAP);
        }
        catch (Exception e) {
            e.printStackTrace(Console.out());
        }
    }

    private static void createJava9Module() throws ExportsAccessRuntimeException {
        try {
            Object module = java9Util.createModuleFromClassLoader((URLClassLoader)agentHostClassLoader, APPAGENT_MODULE_NAME, APPAGENT_REQUIRED_MODULE_NAMES, APPAGENT_EXPORTS_NAME);
            java9Util.addReadsToClassModule(module, AgentEntryPoint.class);
            for (String nextClassName : JAVA9_NON_CRITICAL_EXPORT_REQUIREMENTS) {
                AgentEntryPoint.addExportAccessForClass(module, nextClassName, false);
            }
            for (String nextClassName : JAVA9_CRITICAL_EXPORT_REQUIREMENTS) {
                AgentEntryPoint.addExportAccessForClass(module, nextClassName, true);
            }
            HashSet usedClasses = new HashSet();
            for (String className : JAVA9_USES_REQUIREMENTS) {
                Class<?> clazz = Class.forName(className, false, agentHostClassLoader);
                usedClasses.add(clazz);
            }
            HashMap providesMap = new HashMap();
            AgentEntryPoint.addToProvides(providesMap, "io.grpc.ManagedChannelProvider", "io.grpc.okhttp.OkHttpChannelProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.grpc.NameResolverProvider", "io.grpc.internal.DnsNameResolverProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.grpc.LoadBalancerProvider", "io.grpc.internal.PickFirstLoadBalancerProvider", "io.grpc.util.SecretRoundRobinLoadBalancerProvider$Provider");
            AgentEntryPoint.addToProvides(providesMap, "io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider", "io.opentelemetry.instrumentation.resources.ContainerResourceProvider", "io.opentelemetry.instrumentation.resources.HostResourceProvider", "io.opentelemetry.instrumentation.resources.OsResourceProvider", "io.opentelemetry.instrumentation.resources.ProcessResourceProvider", "io.opentelemetry.instrumentation.resources.ProcessRuntimeResourceProvider", "io.opentelemetry.sdk.autoconfigure.internal.EnvironmentResourceProvider", "com.appdynamics.opentelemetry.resource.TelemetryResourceProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSpanExporterProvider", "io.opentelemetry.exporter.otlp.internal.OtlpSpanExporterProvider", "io.opentelemetry.exporter.logging.otlp.internal.LoggingSpanExporterProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.opentelemetry.sdk.autoconfigure.spi.metrics.ConfigurableMetricExporterProvider", "io.opentelemetry.exporter.otlp.internal.OtlpMetricExporterProvider", "io.opentelemetry.exporter.logging.otlp.internal.LoggingMetricExporterProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.opentelemetry.sdk.autoconfigure.spi.logs.ConfigurableLogRecordExporterProvider", "io.opentelemetry.exporter.otlp.internal.OtlpLogRecordExporterProvider", "io.opentelemetry.exporter.logging.otlp.internal.LoggingLogRecordExporterProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.opentelemetry.exporter.internal.grpc.GrpcSenderProvider", "io.opentelemetry.exporter.sender.okhttp.internal.OkHttpGrpcSenderProvider");
            AgentEntryPoint.addToProvides(providesMap, "io.opentelemetry.exporter.internal.http.HttpSenderProvider", "io.opentelemetry.exporter.sender.okhttp.internal.OkHttpHttpSenderProvider");
            if (!usedClasses.isEmpty()) {
                java9Util.addUses(module, usedClasses, providesMap);
            }
            java9Util.addExportsFromModule(module, ORG_APACHE_XERCES_JAXP, java9Util.locateModuleByName(JAVA_XML_MODULE_NAME));
            String readModulesString = System.getProperty(APPDYNAMICS_READ_MODULES);
            HashSet<String> readModules = new HashSet<String>();
            readModules.addAll(AgentEntryPoint.parseReadModulesProperty(readModulesString));
            readModules.addAll(Arrays.asList(JAVA9_READ_MODULE_BY_NAME));
            java9Util.addReads(module, readModules);
            String exportPackagesString = System.getProperty(APPDYNAMICS_EXPORT_PACKAGES);
            String[] exportPackages = AgentEntryPoint.parseReadModulesProperty(exportPackagesString).toArray(new String[0]);
            java9Util.addExportsToAll(module, exportPackages);
            HashSet<Object> moduleSet = new HashSet<Object>();
            moduleSet.add(module);
            for (NonBootPackageAccess nonBootPackageAccess : NON_BOOT_PACKAGE_ACCESS) {
                AgentEntryPoint.addExportsAndOpensForNonBootPackage(nonBootPackageAccess, moduleSet);
            }
        }
        catch (ExportsAccessRuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
        }
    }

    private static void addToProvides(HashMap<Class<?>, List<Class<?>>> providesMap, String intf, String ... clazz) throws ClassNotFoundException, Exception {
        ArrayList classes = new ArrayList();
        for (String c : clazz) {
            classes.add(Class.forName(c, false, agentHostClassLoader));
        }
        providesMap.put(Class.forName(intf, false, agentHostClassLoader), classes);
    }

    private static void addExportAccessForClass(Object module, String className, boolean critical) throws ExportsAccessRuntimeException {
        try {
            Class<?> clazz = Class.forName(className);
            String pkgName = clazz.getPackage().getName();
            java9Util.addReadsToClassModule(module, clazz);
            java9Util.addOpensFromClass(clazz, pkgName, module);
            java9Util.addExportsFromClass(clazz, pkgName, module);
        }
        catch (ClassNotFoundException e) {
            if (critical) {
                logger.warn("Failed to create appagent module, due to failing to add export access for class with name [" + className + "] because is not available in classpath" + e);
                throw new ExportsAccessRuntimeException("Failed to create appagent module, due to failing to add export access for class with name [" + className + "] because is not available in classpath", e);
            }
            logger.warn("Class with name [" + className + "] is not available in classpath, so will ignore export access.");
        }
        catch (Exception e) {
            if (critical) {
                logger.warn("Failed to create appagent module, due to failing to add export access for class with name [" + className + "]. Exception: " + e);
                throw new ExportsAccessRuntimeException("Failed to create appagent module, due to failing to add export access for class with name [" + className + "]. Exception: ", e);
            }
            logger.warn("Class with name [" + className + "] is not available in classpath due to exception: [" + e + "], so will ignore export access.");
        }
        catch (Throwable e) {
            if (critical) {
                logger.warn("Failed to create appagent module, due to an unknown error for class with name [" + className + "]. Some agent functionality might be disabled. Exception: " + e);
                throw new ExportsAccessRuntimeException("Failed to create appagent module, due to an unknown error for class with name [" + className + "]. Some agent functionality might be disabled.", e);
            }
            logger.warn("Class with name [" + className + "] is not available in classpath due to unknown exception: [" + e + "], so will ignore export access. Some functionality might be disabled.");
        }
    }

    private static void addExportsAndOpensForNonBootPackage(NonBootPackageAccess nonBootPackageAccess, Set<Object> moduleSet) {
        try {
            String[] packageNames;
            Object targetModule = java9Util.locateModuleByName(nonBootPackageAccess.getModuleName());
            if (targetModule == null) {
                logger.warn("Failed to locate module [" + nonBootPackageAccess.getModuleName() + "]");
                return;
            }
            HashMap<String, Set<Object>> exports = new HashMap<String, Set<Object>>();
            HashMap<String, Set<Object>> opens = new HashMap<String, Set<Object>>();
            for (String nextPackage : packageNames = nonBootPackageAccess.getPackageNames()) {
                exports.put(nextPackage, moduleSet);
                opens.put(nextPackage, moduleSet);
            }
            java9Util.addExportsAndOpens(targetModule, exports, opens);
        }
        catch (IllegalArgumentException e) {
            logger.warn("Failed to add exports and opens for module [" + nonBootPackageAccess.getModuleName() + "] with packages " + Arrays.toString(nonBootPackageAccess.getPackageNames()) + ". These packages may not exist in this module for this JVM");
        }
    }

    public static List<String> parseReadModulesProperty(String readModulesString) {
        ArrayList<String> returnModules = new ArrayList<String>();
        if (readModulesString != null) {
            String[] readModules = readModulesString.trim().split("\\s*+,\\s*");
            StringBuilder sb = new StringBuilder("Loading reads modules (or export packages) from system property: ");
            for (String readModule : readModules) {
                if (readModule.length() == 0) continue;
                sb.append("[" + readModule + "] ");
                returnModules.add(readModule);
            }
            Console.out().println(sb.toString());
        }
        return returnModules;
    }

    private static File createTempBootClassJarFile(AgentVersionSpecification requiredVersion) {
        Collection<ModifiedClassDefinition> modifiedClassDefinitions = requiredVersion.getModifiedClassDefinitions();
        String[] appTierNodeNames = requiredVersion.getAppTierNodeNames();
        File tempJarDirectory = new File(new File(agentVersionDirectory), "logs" + File.separator + String.format("%s", appTierNodeNames[2]));
        tempJarDirectory.mkdirs();
        File returnFile = new File(tempJarDirectory, String.format("temp.%s.%s.%s.jar", appTierNodeNames[0].replace(' ', '_'), appTierNodeNames[1].replace(' ', '_'), appTierNodeNames[2].replace(' ', '_')));
        if (!BootClassPathModifier.createTempJar(returnFile, modifiedClassDefinitions)) {
            returnFile = null;
        }
        return returnFile;
    }

    private static void initJavaVersion() {
        try {
            if (manifest == null) {
                manifest = JavaAgentManifest.parseManifest(nodeDir);
            }
            reDefinitionSupported = manifest.canRedefineClasses();
            reTransformSupported = manifest.canRetransformClasses();
        }
        catch (Throwable th) {
            Console.out().println("Error occurred during Java Version configuration");
            th.printStackTrace(Console.out());
        }
    }

    private static void determineJavaAgentVersion(String agentInstallDir) {
        if (manifest == null) {
            manifest = JavaAgentManifest.parseManifest(agentInstallDir);
        }
        javaAgentVersion = manifest.getJavaAgentVersion();
        defaultAppAgentVersion = manifest.getDefaultAppAgentVersion();
    }

    private static ClassLoader getParentLoader() {
        boolean useSystemCL = Boolean.getBoolean(USE_SYSTEM_CLASS_LOADER_PARENT);
        if (useSystemCL) {
            return ClassLoader.getSystemClassLoader();
        }
        return AgentEntryPoint.class.getClassLoader();
    }

    public static IAgentClassLoader getClassLoader() {
        return (IAgentClassLoader)((Object)agentHostClassLoader);
    }

    public static Object getJavaAgent() {
        return javaAgent;
    }

    private static void addThirdPartyURLs(List<URL> libURLs, File appAgentDirectory) {
        File[] libraries;
        File thirdPartyLibDir = new File(appAgentDirectory, THIRD_PARTY_LIB_DIR);
        if (!thirdPartyLibDir.exists()) {
            throw new RuntimeException("Invalid Agent Installation Directory [" + appAgentDirectory.getAbsolutePath() + "]");
        }
        for (File library : libraries = thirdPartyLibDir.listFiles()) {
            try {
                String libName = library.getName();
                if (!libName.endsWith(".jar") && !libName.endsWith(".zip")) continue;
                libURLs.add(library.toURI().toURL());
            }
            catch (MalformedURLException e) {
                e.printStackTrace(Console.out());
            }
        }
    }

    public static String getAgentInstallDirectory() {
        block4: {
            try {
                String systemClassPath = System.getProperty("java.class.path");
                String agentInstallEntry = systemClassPath.substring(systemClassPath.lastIndexOf(File.pathSeparator) + 1);
                if (!agentInstallEntry.contains(File.separator + AGENT_ENTRY_POINT_JAR)) {
                    StringTokenizer cpTokenizer = new StringTokenizer(systemClassPath, File.pathSeparator);
                    while (cpTokenizer.hasMoreTokens()) {
                        String cpEntry = cpTokenizer.nextToken();
                        if (!cpEntry.contains(File.separator + AGENT_ENTRY_POINT_JAR)) continue;
                        return AgentEntryPoint.getInstallDirPath(cpEntry);
                    }
                    break block4;
                }
                return AgentEntryPoint.getInstallDirPath(agentInstallEntry);
            }
            catch (Exception e) {
                e.printStackTrace(Console.out());
            }
        }
        String dirPath = AgentEntryPoint.getAgentDirFromCommandLine();
        return dirPath;
    }

    private static String getInstallDirPath(String agentInstallEntry) {
        if (new File(agentInstallEntry).exists()) {
            try {
                File agentInstallDir = new File(agentInstallEntry).getParentFile();
                if (agentInstallDir != null) {
                    return agentInstallDir.getCanonicalPath();
                }
                String workingDir = System.getProperty(USER_WORKING_DIRECTORY);
                if (new File(workingDir + File.separatorChar + AGENT_ENTRY_POINT_JAR).exists()) {
                    return new File(workingDir).getCanonicalPath();
                }
            }
            catch (IOException e) {
                Console.out().println("Error Message: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    private static String getNodeDir(String installDir) {
        try {
            String nodeDir = System.getProperty(APP_AGENT_NODE_DIR);
            if (nodeDir != null) {
                Console.out().println("Node Directory specified [" + nodeDir + "]");
                return nodeDir;
            }
        }
        catch (Throwable e) {
            Console.out().println("Error in getting Node directory from system property, . Error Message" + e.getMessage());
        }
        return installDir;
    }

    public static void main(String[] args) throws RuntimeException {
        if (args.length == 0) {
            Console.out().println("Invalid usage, use [list] or [pid]");
        }
        try {
            if (args[0].equals("list")) {
                Class<?> vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
                Method listMethod = vmClass.getMethod("list", new Class[0]);
                List vmList = (List)listMethod.invoke(null, new Object[0]);
                for (Object vmDesc : vmList) {
                    Method dispNameMethod = vmDesc.getClass().getMethod("displayName", new Class[0]);
                    Method idMethod = vmDesc.getClass().getMethod("id", new Class[0]);
                    String dispName = (String)dispNameMethod.invoke(vmDesc, new Object[0]);
                    String id = (String)idMethod.invoke(vmDesc, new Object[0]);
                    Console.out().println("VM Found [" + dispName + "], VM ID [" + id + "]");
                }
            } else {
                Class<?> vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
                String vmID = args[0];
                String agentArgs = null;
                if (args.length > 1) {
                    agentArgs = args[1];
                }
                Method attachMethod = vmClass.getMethod("attach", String.class);
                Console.out().println("Attaching to VM [" + vmID + "]");
                String agentPath = AgentEntryPoint.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                agentPath = AgentEntryPoint.transformFileSeparators(agentPath, File.separatorChar);
                Object vm = attachMethod.invoke(null, vmID);
                Method loadAgentMethod = vm.getClass().getMethod("loadAgent", String.class, String.class);
                Console.out().println("agent path >>>" + agentPath);
                String agentInstallDir = agentPath.substring(0, agentPath.lastIndexOf(File.separator));
                StringBuilder sb = new StringBuilder();
                sb.append(APP_AGENT_INSTALL_DIR).append("=").append(agentInstallDir);
                if (agentArgs != null) {
                    sb.append(",").append(agentArgs);
                }
                loadAgentMethod.invoke(vm, agentPath, sb.toString());
            }
        }
        catch (Throwable e) {
            e.printStackTrace(Console.out());
            throw new RuntimeException(e);
        }
    }

    public static String transformFileSeparators(String uriPath, char separatorChar) {
        if (separatorChar == '\\') {
            if (uriPath == null || uriPath.indexOf(47) == -1) {
                return uriPath;
            }
            if ((uriPath = uriPath.replace('/', '\\')).charAt(0) == '\\') {
                uriPath = uriPath.substring(1);
            }
        }
        return uriPath;
    }

    private static String getAgentClassPath(String agentInstallDir, String version, List<URL> returnClassPath) throws MalformedURLException, UnableToFindAppagentException {
        File fAgentInstallDirectory = new File(agentInstallDir);
        version = version != null ? VERSION_DIR_PREFIX + version : VERSION_DIR_PREFIX + defaultAppAgentVersion;
        String returnString = AgentEntryPoint.getAgentClassPath(fAgentInstallDirectory, version, returnClassPath);
        return returnString;
    }

    private static String getAgentClassPath(File fAgentInstallDirectory, String subDirectoryName, List<URL> returnClassPath) throws MalformedURLException, UnableToFindAppagentException {
        String version = subDirectoryName.substring(VERSION_DIR_PREFIX.length());
        File versionSubDirectory = mapOfAllVersionSubDirectories.get(version);
        File versionSpecificLibDirectory = null;
        if (versionSubDirectory != null) {
            versionSpecificLibDirectory = new File(versionSubDirectory, LIB_DIR);
        }
        if (versionSpecificLibDirectory == null || !versionSpecificLibDirectory.isDirectory()) {
            versionSpecificLibDirectory = new File(fAgentInstallDirectory, LIB_DIR);
        }
        boolean foundVersionSpecificJars = false;
        if (versionSpecificLibDirectory.isDirectory()) {
            foundVersionSpecificJars = AgentEntryPoint.addAppAgentToClasspath(versionSpecificLibDirectory, returnClassPath);
            if (foundVersionSpecificJars) {
                AgentEntryPoint.addThirdPartyURLs(returnClassPath, versionSpecificLibDirectory);
            }
            AgentEntryPoint.checkIfSingularityLog4jFound(versionSpecificLibDirectory);
        }
        if (!foundVersionSpecificJars) {
            Console.out().println(String.format("Unable to locate directory %s - java agent cannot execute", versionSpecificLibDirectory));
            throw new UnableToFindAppagentException();
        }
        return versionSpecificLibDirectory.getParentFile().getAbsolutePath();
    }

    private static List<URL> getAgentClassPath(File agentSubDirectory) throws MalformedURLException {
        ArrayList<URL> returnClassPath = new ArrayList<URL>();
        File versionSpecificLibDirectory = new File(agentSubDirectory, LIB_DIR);
        if (versionSpecificLibDirectory.isDirectory()) {
            boolean foundVersionSpecificJars = AgentEntryPoint.addAppAgentToClasspath(versionSpecificLibDirectory, returnClassPath);
            if (foundVersionSpecificJars) {
                AgentEntryPoint.addThirdPartyURLs(returnClassPath, versionSpecificLibDirectory);
            } else {
                returnClassPath = null;
            }
            AgentEntryPoint.checkIfSingularityLog4jFound(versionSpecificLibDirectory);
        }
        return returnClassPath;
    }

    private static boolean addAppAgentToClasspath(File directory, Collection<URL> classPath) throws MalformedURLException {
        File agentJar = new File(directory, AGENT_JAR);
        if (agentJar.exists()) {
            File[] allFiles = directory.listFiles();
            if (allFiles != null) {
                for (File nextFile : allFiles) {
                    if (nextFile.isDirectory() || !nextFile.getName().endsWith(".jar")) continue;
                    classPath.add(nextFile.toURI().toURL());
                }
            }
            return true;
        }
        return false;
    }

    private static void checkIfSingularityLog4jFound(File directory) {
        File log4jJar = new File(directory, NAMESPACE_SAFE_LOG4J_JAR);
        boolean exists = log4jJar.exists();
        if (!exists) {
            Console.out().println("singularity-log4j.jar is not present. Agent must use log4j 2.x");
        }
    }

    private static ClassLoader createClassLoader(URL[] classPath, ClassLoader parent, String agentInstallDir) {
        ClassLoader returnClassLoader = null;
        URLClassLoader urlClassLoader = new URLClassLoader(classPath, parent);
        boolean useBootstrapClassLoaderAsParent = Boolean.getBoolean(USE_BOOTSTRAP_CLASS_LOADER_PARENT);
        try {
            Method getAgentClassLoaderMethod;
            Class<?> classLoaderClass = urlClassLoader.loadClass(AGENT_CLASS_LOADER_CLASS_NAME);
            if (JavaVersionUtil.isJava9orHigher()) {
                AgentEntryPoint.giveClassLoaderModulePermission(classLoaderClass);
            }
            if (useBootstrapClassLoaderAsParent) {
                try {
                    getAgentClassLoaderMethod = classLoaderClass.getMethod(GET_AGENT_CLASS_LOADER_METHOD_NAME, URL[].class, String.class);
                    returnClassLoader = (ClassLoader)getAgentClassLoaderMethod.invoke(null, classPath, agentInstallDir);
                }
                catch (NoSuchMethodException e) {
                    Console.out().println("Unable to find 2 argument version of AgentClassLoader.getClassLoader()");
                }
            }
            if (returnClassLoader == null) {
                getAgentClassLoaderMethod = classLoaderClass.getMethod(GET_AGENT_CLASS_LOADER_METHOD_NAME, URL[].class, ClassLoader.class, String.class);
                returnClassLoader = (ClassLoader)getAgentClassLoaderMethod.invoke(null, classPath, parent, agentInstallDir);
            }
        }
        catch (Exception e) {
            e.printStackTrace(Console.out());
        }
        return returnClassLoader;
    }

    private static void giveClassLoaderModulePermission(Class<? extends ClassLoader> classLoaderClass) {
        Object classLoaderModule = java9Util.getModuleForClass(classLoaderClass);
        HashMap<String, Set<Object>> openMap = new HashMap<String, Set<Object>>();
        HashSet<Object> moduleSet = new HashSet<Object>();
        moduleSet.add(classLoaderModule);
        for (String pkgName : agentClassLoaderRequiredPackages) {
            openMap.put(pkgName, moduleSet);
        }
        java9Util.addExportsAndOpens(java9Util.getBaseModule(), Collections.EMPTY_MAP, openMap);
    }

    public static String getAgentLibUsed() {
        return agentVersionDirectory != null ? agentVersionDirectory : LIB_DIR;
    }

    public static String getBaseInstallDirectory() {
        return baseInstallDirectory;
    }

    private static AgentVersionSpecification determineAppAgentVersionToUse(String nodeDir) {
        ArrayList<AgentVersionSpecification> versionSpecifications;
        String agentVersionFileName = nodeDir + File.separator + CONFIG_DIR + File.separator + AGENT_VERSION_DEFINITION_FILE_NAME;
        File agentVersionFile = new File(agentVersionFileName);
        HashMap<String, PossibleAgentVersion> mapOfDirectoriesVsPossibleVersions = new HashMap<String, PossibleAgentVersion>();
        for (Map.Entry<String, File> nextEntry : mapOfAllVersionSubDirectories.entrySet()) {
            String version = nextEntry.getKey();
            File nextSubDir = nextEntry.getValue();
            String globalControllerConfigFileName = AgentEntryPoint.getBaseInstallDirectory() + File.separator + CONFIG_DIR + File.separator + CONTROLLER_CONFIG_FILE;
            try {
                String controllerConfigFileName = nextSubDir.getAbsolutePath() + File.separator + CONFIG_DIR + File.separator + CONTROLLER_CONFIG_FILE;
                PossibleAgentVersion possibleVersion = AgentEntryPoint.getAppTierNodeFromLib(version, nextSubDir, controllerConfigFileName, globalControllerConfigFileName);
                if (possibleVersion == null) continue;
                mapOfDirectoriesVsPossibleVersions.put(version, possibleVersion);
                if (version.compareTo(mostCurrentVersion) <= 0 || !AgentEntryPoint.versionIsAGABuild(version)) continue;
                mostCurrentVersion = version;
            }
            catch (Exception e) {
                e.printStackTrace(Console.out());
            }
        }
        if (agentVersionFile.exists()) {
            versionSpecifications = new AgentVersionSpecificationFileParser(agentVersionFile, mostCurrentVersion).getAgentVersionSpecifications();
        } else {
            versionSpecifications = new ArrayList();
            AgentVersionSpecification defaultVersionSpecification = AgentEntryPoint.createDefaultVersionSpecifications(mapOfDirectoriesVsPossibleVersions);
            if (defaultVersionSpecification != null) {
                versionSpecifications.add(defaultVersionSpecification);
            }
        }
        return AgentEntryPoint.resolveAgentVersion(versionSpecifications, mapOfDirectoriesVsPossibleVersions);
    }

    private static AgentVersionSpecification createDefaultVersionSpecifications(Map<String, PossibleAgentVersion> mapOfVersions) {
        AgentVersionSpecification returnObject = null;
        PossibleAgentVersion possibleAgentVersion = mapOfVersions.get(defaultAppAgentVersion);
        if (possibleAgentVersion != null) {
            returnObject = new AgentVersionSpecification(possibleAgentVersion.appTierNodeArray, defaultAppAgentVersion);
            returnObject.setAppTierNodeDeterminer(possibleAgentVersion.appTierNodeDeterminer);
        }
        return returnObject;
    }

    private static AgentVersionSpecification createDefaultVersionSpecification() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnableToFindAppagentException {
        AgentVersionSpecification agentVersionSpecification = null;
        String globalControllerConfigFileName = AgentEntryPoint.getBaseInstallDirectory() + File.separator + CONFIG_DIR + File.separator + CONTROLLER_CONFIG_FILE;
        PossibleAgentVersion onlyPossibleAgentVersion = AgentEntryPoint.getAppTierNodeFromLib(null, new File(agentInstallDir), globalControllerConfigFileName, globalControllerConfigFileName);
        if (onlyPossibleAgentVersion != null) {
            agentVersionSpecification = new AgentVersionSpecification(onlyPossibleAgentVersion.appTierNodeArray, null);
            agentVersionSpecification.setAppTierNodeDeterminer(onlyPossibleAgentVersion.appTierNodeDeterminer);
        }
        return agentVersionSpecification;
    }

    public static Thread getMainThread() {
        if (refToMainThread != null) {
            return (Thread)refToMainThread.get();
        }
        return null;
    }

    private static boolean versionIsAGABuild(String version) {
        return version.indexOf(45) < 0;
    }

    private static PossibleAgentVersion getAppTierNodeFromLib(String version, File fAgentSubDirectory, String controllerConfigFileName, String globalControllerConfigFileName) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnableToFindAppagentException {
        URL[] classPathURLArray;
        URLClassLoader classLoader;
        Class<?> clazz;
        String[] appTierNode = null;
        List<URL> classPath = AgentEntryPoint.getAgentClassPath(fAgentSubDirectory);
        IAppTierNodeDeterminer appTierNodeDeterminer = null;
        if (classPath != null && !classPath.isEmpty() && AgentEntryPoint.isCompatibleWithJavaAgent(appTierNodeDeterminer = (IAppTierNodeDeterminer)(clazz = (classLoader = new URLClassLoader(classPathURLArray = classPath.toArray(new URL[classPath.size()]), AgentEntryPoint.class.getClassLoader())).loadClass(APP_TIER_NODE_DETERMINER_CLASS_NAME)).newInstance())) {
            try {
                appTierNodeDeterminer.executeGenericFunction("setControllerConfigFiles", controllerConfigFileName, globalControllerConfigFileName);
            }
            catch (UnsupportedOperationException e) {
                Console.out().println(String.format("Received UnsuportedOperationException trying to call executeGenericFunction() for IAppTierNodeDeterminer loaded from %s.  Calling arguments: %s %s %s.  Version directory is ignored", fAgentSubDirectory, "setControllerConfigFiles", controllerConfigFileName, globalControllerConfigFileName));
                return null;
            }
            appTierNode = appTierNodeDeterminer.getAppTierNode();
        }
        if (appTierNode != null) {
            return new PossibleAgentVersion(version, appTierNode, appTierNodeDeterminer);
        }
        return null;
    }

    private static boolean isCompatibleWithJavaAgent(IAppTierNodeDeterminer appTierNodeDeterminer) {
        String[] parsedVersion;
        boolean bReturn = false;
        boolean supportsDottedVersion = false;
        try {
            supportsDottedVersion = appTierNodeDeterminer.supportsDottedJavaAgentVersion();
            if (supportsDottedVersion) {
                bReturn = appTierNodeDeterminer.isCompatibleWithJavaAgent(javaAgentVersion);
            }
        }
        catch (AbstractMethodError abstractMethodError) {
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
        }
        if (!supportsDottedVersion && (parsedVersion = javaAgentVersion.split("\\.")).length > 0) {
            bReturn = appTierNodeDeterminer.isCompatibleWithJavaAgent(parsedVersion[0]);
        }
        return bReturn;
    }

    private static AgentVersionSpecification resolveAgentVersion(List<AgentVersionSpecification> versionSpecifications, Map<String, PossibleAgentVersion> mapOfDirectoriesVsAppTierNode) {
        AgentVersionSpecification returnValue = null;
        for (AgentVersionSpecification nextVersionSpecification : versionSpecifications) {
            String versionRequired = nextVersionSpecification.getVersionRequired();
            PossibleAgentVersion possibleAgentVersion = mapOfDirectoriesVsAppTierNode.get(versionRequired);
            if (possibleAgentVersion == null || !nextVersionSpecification.matches(possibleAgentVersion.appTierNodeArray)) continue;
            returnValue = nextVersionSpecification;
            returnValue.setAppTierNodeDeterminer(possibleAgentVersion.appTierNodeDeterminer);
            returnValue.setAppTierNodeNames(possibleAgentVersion.appTierNodeArray);
            break;
        }
        return returnValue;
    }

    private static Map<String, File> createMapOfAllVersionSubDirectories(String ... dirs) {
        HashMap<String, File> returnMap = new HashMap<String, File>();
        for (String nextDir : dirs) {
            AgentEntryPoint.addVerDirectoriesToMap(nextDir, returnMap);
        }
        return returnMap;
    }

    private static void addVerDirectoriesToMap(String dirName, Map<String, File> mapOfVerDirs) {
        File[] subDirectoryArray;
        File nodeDirFile = new File(dirName);
        if (nodeDirFile.isDirectory() && (subDirectoryArray = nodeDirFile.listFiles()) != null) {
            for (File nextFile : subDirectoryArray) {
                String version;
                if (!nextFile.isDirectory() || !nextFile.getName().startsWith(VERSION_DIR_PREFIX) || !new File(nextFile, LIB_DIR).isDirectory() || mapOfVerDirs.get(version = nextFile.getName().substring(VERSION_DIR_PREFIX.length())) != null) continue;
                mapOfVerDirs.put(version, nextFile);
            }
        }
    }

    public static List<URL> getLibrariesAddedToBootClassPath() {
        return librariesAddedToBootClassPath;
    }

    public static List<URL> getLibraries() {
        return libraries;
    }

    private static List<URL> getBootClassPathAttribute(String agentInstallDir) {
        if (manifest == null) {
            manifest = JavaAgentManifest.parseManifest(agentInstallDir);
        }
        List<URL> returnList = manifest.getBootClassPathAsURLList(new File(agentInstallDir));
        return returnList;
    }

    private static File getAppAgentJarFileFrom(List<URL> libraryList) {
        File returnFile = null;
        for (URL url : libraryList) {
            String fileName;
            File file;
            if (!url.getProtocol().equals("file") || !(file = new File(fileName = url.getFile())).getName().equals(AGENT_JAR)) continue;
            returnFile = file;
            break;
        }
        return returnFile;
    }

    public static boolean isInPreMain() {
        return inPreMain;
    }

    public static boolean isRunningAtJVMInit() {
        return runningAtJVMInit;
    }

    private static boolean testIfRunningAtJVMInit() {
        boolean dynamicAttach = Boolean.getBoolean(DYNAMIC_ATTACH);
        return !dynamicAttach;
    }

    private static String getAgentDirFromCommandLine() {
        List<String> inputArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        String returnDirName = null;
        for (String nextArg : inputArgs) {
            int indexOfLastSep;
            String lastQual;
            if (!nextArg.startsWith("-javaagent:") || !(lastQual = (nextArg = nextArg.substring(11)).substring((indexOfLastSep = nextArg.lastIndexOf(File.separatorChar)) + 1)).startsWith("javaagent")) continue;
            String parentDir = AgentEntryPoint.resolveRelativeParentPath(nextArg);
            if (parentDir != null) {
                returnDirName = parentDir;
                break;
            }
            returnDirName = nextArg.substring(0, indexOfLastSep);
            break;
        }
        return returnDirName;
    }

    private static String resolveRelativeParentPath(String fileName) {
        try {
            File agentInstallDir = new File(fileName).getParentFile();
            if (agentInstallDir != null) {
                return agentInstallDir.getCanonicalPath();
            }
        }
        catch (IOException e) {
            logger.warn("Failed to resolve canonical path for agent install directory from command line", e);
        }
        return null;
    }

    public static String getJavaAgentName() {
        if (javaAgentName == null) {
            javaAgentName = NON_IBM_JAVAAGENT_NAME;
            List<String> commandLineArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
            if (commandLineArgs != null) {
                for (String nextArg : commandLineArgs) {
                    if (!nextArg.startsWith("-javaagent:")) continue;
                    int indexOfEqual = nextArg.lastIndexOf(61);
                    if (indexOfEqual >= 0) {
                        nextArg = nextArg.substring(0, indexOfEqual);
                    }
                    if (nextArg.endsWith(NON_IBM_JAVAAGENT_NAME)) break;
                    if (!nextArg.endsWith(IBM_JAVAAGENT_JAR)) continue;
                    javaAgentName = IBM_JAVAAGENT_JAR;
                    break;
                }
            }
        }
        return javaAgentName;
    }

    public static String getJavaAgentVersion() {
        return javaAgentVersion;
    }

    static {
        agentInitArgs = new Class[]{String.class, String.class, Instrumentation.class};
        reTransformSupported = false;
        reDefinitionSupported = false;
        mostCurrentVersion = "";
        JAVA9_NON_CRITICAL_EXPORT_REQUIREMENTS = new String[]{"com.ibm.lang.management.internal.ExtendedOperatingSystemMXBeanImpl", "com.sun.management.internal.OperatingSystemImpl", "java.lang.invoke.MethodType$ConcurrentWeakInternSet$WeakEntry", "jdk.internal.util.ReferencedKeySet"};
        JAVA9_CRITICAL_EXPORT_REQUIREMENTS = new String[]{"sun.instrument.InstrumentationImpl", "java.lang.management.ManagementFactory", "java.lang.ThreadLocal", "java.security.AccessControlContext", "java.util.Hashtable", "java.util.concurrent.ConcurrentHashMap", "java.net.URLConnection", "java.util.concurrent.locks.ReentrantLock", "java.util.logging.LogRecord", "java.text.SimpleDateFormat", "jdk.internal.misc.VM"};
        JAVA9_USES_REQUIREMENTS = new String[]{"org.apache.logging.log4j.spi.Provider", "org.apache.commons.logging.LogFactory", "io.opentelemetry.exporter.internal.grpc.GrpcSenderProvider", "io.opentelemetry.exporter.internal.http.HttpSenderProvider", "io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider", "io.opentelemetry.sdk.autoconfigure.spi.ConfigurablePropagatorProvider", "io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSamplerProvider", "io.opentelemetry.sdk.autoconfigure.spi.metrics.ConfigurableMetricExporterProvider", "io.opentelemetry.sdk.autoconfigure.spi.traces.SdkTracerProviderConfigurer", "io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSpanExporterProvider", "io.opentelemetry.sdk.autoconfigure.spi.logs.ConfigurableLogRecordExporterProvider", "io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider", "io.opentelemetry.context.ContextStorageProvider", "io.grpc.ManagedChannelProvider", "io.grpc.NameResolverProvider", "io.grpc.LoadBalancerProvider"};
        JAVA9_READ_MODULE_BY_NAME = new String[]{"java.sql", JAVA_XML_MODULE_NAME, "jdk.unsupported", "java.desktop", "java.scripting", "ALL-UNNAMED", "java.naming"};
        APPAGENT_REQUIRED_MODULE_NAMES = new String[]{"java.sql", "java.instrument", "java.management", "java.base", "jdk.jcmd", "jdk.attach", "java.corba", JAVA_XML_MODULE_NAME, "jdk.unsupported", "java.desktop", "org.apache.logging.log4j.core", "org.apache.logging.log4j", "grpc.api", "grpc.core", "grpc.netty"};
        APPAGENT_EXPORTS_NAME = new String[]{"com.singularity.ee.agent.appagent.kernel", "com.singularity.ee.agent.appagent.kernel.reflection.reflector"};
        agentClassLoaderRequiredPackages = new String[]{JAVA_LANG_PACKAGE, JAVA_SECURITY_PACKAGE};
        isLog4j1FactoryPropertySet = false;
        MAX_LOG4J_PROPERTY_CHECK_COUNT = 6;
        NON_BOOT_PACKAGE_ACCESS = new NonBootPackageAccess[]{new NonBootPackageAccess("jdk.jcmd", new String[]{"sun.tools.jmap"}), new NonBootPackageAccess("jdk.attach", new String[]{"sun.tools.attach"})};
        logger = new SysOutAgentBootLogger();
    }

    private static class NonBootPackageAccess {
        private final String moduleName;
        private final String[] packageNames;

        NonBootPackageAccess(String moduleName, String[] packageNames) {
            this.moduleName = moduleName;
            this.packageNames = packageNames;
        }

        String getModuleName() {
            return this.moduleName;
        }

        String[] getPackageNames() {
            return this.packageNames;
        }
    }
}

