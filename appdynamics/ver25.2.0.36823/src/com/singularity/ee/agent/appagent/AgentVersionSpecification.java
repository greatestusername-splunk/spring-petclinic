/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.IAppTierNodeDeterminer;
import com.singularity.ee.agent.appagent.ModifiedClassDefinition;
import com.singularity.ee.agent.util.io.Console;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class AgentVersionSpecification
implements Comparable<AgentVersionSpecification> {
    private static final String BOOTSTRAP_LOAD_ATTRIBUTE_NAME = "Bootstrap-Load";
    private static final String BOOTSTRAP_PATH_REGEXES_ATTRIBUTE_NAME = "Boot-Classpath-Append";
    private static final String UNKNOWN_APP_TIER_OR_NODE = "Unknown";
    private String[] appTierNodeNames;
    private final String versionRequired;
    private int hashValue;
    private IAppTierNodeDeterminer appTierNodeDeterminer;

    public AgentVersionSpecification(String[] appTierNodeNames, String propValue) {
        this.appTierNodeNames = appTierNodeNames;
        this.versionRequired = propValue;
        for (int i = 0; i < this.appTierNodeNames.length; ++i) {
            if (this.appTierNodeNames[i] == null) {
                this.appTierNodeNames[i] = UNKNOWN_APP_TIER_OR_NODE;
            }
            this.hashValue ^= this.appTierNodeNames[i].hashCode();
        }
    }

    AgentVersionSpecification(String versionRequired) {
        this.versionRequired = versionRequired;
    }

    public String getVersionRequired() {
        return this.versionRequired;
    }

    @Override
    public int compareTo(AgentVersionSpecification other) {
        int iReturn = 1;
        if (this.appTierNodeNames.length == other.appTierNodeNames.length) {
            iReturn = -this.versionRequired.compareTo(other.versionRequired);
        } else if (this.appTierNodeNames.length > other.appTierNodeNames.length) {
            iReturn = -1;
        }
        return iReturn;
    }

    public boolean matches(String[] appTierNodeFromAgent) {
        boolean bReturn = false;
        if (this.appTierNodeNames.length <= appTierNodeFromAgent.length) {
            bReturn = true;
            for (int i = 0; i < this.appTierNodeNames.length && bReturn; ++i) {
                bReturn = this.appTierNodeNames[i].equals(appTierNodeFromAgent[i]);
            }
        }
        return bReturn;
    }

    public String[] getAppTierNodeNames() {
        return this.appTierNodeNames;
    }

    public boolean equals(Object o) {
        boolean bReturn;
        boolean bl = bReturn = this == o;
        if (!bReturn && o instanceof AgentVersionSpecification) {
            AgentVersionSpecification other = (AgentVersionSpecification)o;
            bReturn = this.hashValue == other.hashValue && this.appTierNodeNames.length == other.appTierNodeNames.length && this.matches(other.appTierNodeNames);
        }
        return bReturn;
    }

    public int hashCode() {
        return this.hashValue;
    }

    IAppTierNodeDeterminer getAppTierNodeDeterminer() {
        return this.appTierNodeDeterminer;
    }

    void setAppTierNodeDeterminer(IAppTierNodeDeterminer appTierNodeDeterminer) {
        this.appTierNodeDeterminer = appTierNodeDeterminer;
    }

    boolean supportsBootstrapClassPath() {
        boolean bReturn = false;
        try {
            bReturn = this.appTierNodeDeterminer.supportsBootstrapClassPath();
        }
        catch (AbstractMethodError abstractMethodError) {
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
        }
        return bReturn;
    }

    Collection<ModifiedClassDefinition> getModifiedClassDefinitions() {
        Collection<ModifiedClassDefinition> returnCollection = null;
        try {
            returnCollection = this.appTierNodeDeterminer.getModifiedClassDefinitions();
        }
        catch (Throwable t) {
            t.printStackTrace(Console.out());
        }
        return returnCollection;
    }

    public void setAppTierNodeNames(String[] appTierNodeNames) {
        this.appTierNodeNames = appTierNodeNames;
    }

    public List<URL> removeBootstrapJars(List<URL> fullLibraryList) {
        File nextLibraryFile;
        URL nextURL;
        ArrayList<URL> returnBootstrapList = new ArrayList<URL>();
        HashSet<String> bootstrapRegExes = new HashSet<String>();
        ListIterator<URL> libraryListIter = fullLibraryList.listIterator();
        while (libraryListIter.hasNext()) {
            Collection<String> regExesToAdd;
            nextURL = (URL)libraryListIter.next();
            nextLibraryFile = new File(nextURL.getFile());
            Manifest manifest = AgentVersionSpecification.getManifestFor(nextLibraryFile);
            if (manifest == null) continue;
            if (AgentVersionSpecification.getValueOfBootstrapLoadAttribute(manifest)) {
                returnBootstrapList.add(nextURL);
                libraryListIter.remove();
            }
            if ((regExesToAdd = AgentVersionSpecification.getBootRegExesFrom(manifest)) == null) continue;
            bootstrapRegExes.addAll(regExesToAdd);
        }
        libraryListIter = fullLibraryList.listIterator();
        while (libraryListIter.hasNext()) {
            nextURL = (URL)libraryListIter.next();
            nextLibraryFile = new File(nextURL.getFile());
            if (!AgentVersionSpecification.isFileOnRegExSet(bootstrapRegExes, nextLibraryFile)) continue;
            returnBootstrapList.add(nextURL);
            libraryListIter.remove();
        }
        return returnBootstrapList;
    }

    private static boolean isFileOnRegExSet(Collection<String> regExSet, File file) {
        boolean bReturn = false;
        String fileName = file.getAbsolutePath();
        for (String nextRegEx : regExSet) {
            if (!fileName.matches(nextRegEx)) continue;
            bReturn = true;
            break;
        }
        return bReturn;
    }

    private static Manifest getManifestFor(File file) {
        Manifest returnManifest = null;
        if (file.getAbsolutePath().endsWith(".jar")) {
            try {
                JarFile jarFile = new JarFile(file);
                returnManifest = jarFile.getManifest();
            }
            catch (Exception e) {
                e.printStackTrace(Console.out());
            }
        }
        return returnManifest;
    }

    private static boolean getValueOfBootstrapLoadAttribute(Manifest manifest) {
        boolean bReturn = false;
        Attributes attributes = manifest.getMainAttributes();
        String bootstrapLoadAttribute = attributes.getValue(new Attributes.Name(BOOTSTRAP_LOAD_ATTRIBUTE_NAME));
        if (bootstrapLoadAttribute != null) {
            bReturn = Boolean.parseBoolean(bootstrapLoadAttribute);
        }
        return bReturn;
    }

    private static Collection<String> getBootRegExesFrom(Manifest manifest) {
        List<String> returnRegExes = null;
        Attributes attributes = manifest.getMainAttributes();
        String bootClassPathRegExes = attributes.getValue(new Attributes.Name(BOOTSTRAP_PATH_REGEXES_ATTRIBUTE_NAME));
        if (bootClassPathRegExes != null && bootClassPathRegExes.length() > 0) {
            String[] parsedBootClassPath = bootClassPathRegExes.split(",");
            returnRegExes = Arrays.asList(parsedBootClassPath);
        }
        return returnRegExes;
    }
}

