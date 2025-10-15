/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.AgentVersionSpecification;
import com.singularity.ee.agent.util.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AgentVersionSpecificationFileParser {
    public static final String START_OF_STANZA = "[config]";
    private String appName = null;
    private String tierName = null;
    private String nodeName = null;
    private String version = null;
    private boolean currentStanzaInError;
    private boolean gotConfig;
    private LineNumberReader lnr;
    private final File file;
    private String mostCurrentVersion;

    public AgentVersionSpecificationFileParser(File file, String mostCurrentVersion) {
        this.file = file;
        this.mostCurrentVersion = mostCurrentVersion;
    }

    public List<AgentVersionSpecification> getAgentVersionSpecifications() {
        List<AgentVersionSpecification> returnList = Collections.EMPTY_LIST;
        try {
            if (this.file.exists()) {
                returnList = this.parseVersionSpecificationContents(new FileInputStream(this.file));
            } else {
                Console.out().println(String.format("File %s does not exist", this.file.getAbsolutePath()));
            }
        }
        catch (Exception e) {
            e.printStackTrace(Console.out());
        }
        Collections.sort(returnList);
        return returnList;
    }

    private void init() {
        this.version = null;
        this.nodeName = null;
        this.tierName = null;
        this.appName = null;
        this.currentStanzaInError = false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<AgentVersionSpecification> parseVersionSpecificationContents(InputStream is) throws Exception {
        ArrayList<AgentVersionSpecification> returnList = new ArrayList<AgentVersionSpecification>();
        try {
            AgentVersionSpecification nextAgentVersionSpecification;
            String nextLine;
            this.lnr = new LineNumberReader(new InputStreamReader(is));
            while ((nextLine = this.lnr.readLine()) != null) {
                if ((nextLine = nextLine.trim()).length() <= 0 || nextLine.startsWith(";")) continue;
                if (nextLine.equals(START_OF_STANZA)) {
                    nextAgentVersionSpecification = this.createAgentVersionSpecification();
                    if (nextAgentVersionSpecification != null) {
                        returnList.add(nextAgentVersionSpecification);
                    }
                    this.gotConfig = true;
                    this.init();
                    continue;
                }
                if (this.gotConfig) {
                    this.parseLine(nextLine);
                    continue;
                }
                this.printIllegalInput(nextLine);
            }
            nextAgentVersionSpecification = this.createAgentVersionSpecification();
            if (nextAgentVersionSpecification != null) {
                returnList.add(nextAgentVersionSpecification);
            }
        }
        finally {
            if (this.lnr != null) {
                this.lnr.close();
                this.lnr = null;
            }
        }
        return returnList;
    }

    private AgentVersionSpecification createAgentVersionSpecification() {
        AgentVersionSpecification returnObject = null;
        if (!this.currentStanzaInError && this.gotConfig) {
            if (this.version != null && this.appName != null) {
                if (this.nodeName != null) {
                    if (this.tierName != null) {
                        returnObject = new AgentVersionSpecification(new String[]{this.appName, this.tierName, this.nodeName}, this.version);
                    } else {
                        Console.out().println(String.format("%s specified but %s is missing in prior stanza at line %d.  Stanza ignored", KeywordType.NODE.name(), KeywordType.TIER.name(), this.lnr.getLineNumber()));
                    }
                } else {
                    returnObject = this.tierName != null ? new AgentVersionSpecification(new String[]{this.appName, this.tierName}, this.version) : new AgentVersionSpecification(new String[]{this.appName}, this.version);
                }
            } else {
                Console.out().println(String.format("Either %s or %s is missing in prior stanza at line %d.  Stanza ignored", KeywordType.APP.name(), KeywordType.VERSION.name(), this.lnr.getLineNumber()));
            }
        }
        return returnObject;
    }

    private void printIllegalInput(String inputLine) {
        Console.out().println(String.format("Illegal line in version specifications file: %s at line %d.  Stanza ignored until next %s", inputLine, this.lnr.getLineNumber(), START_OF_STANZA));
        this.currentStanzaInError = true;
    }

    private void parseLine(String readLine) {
        int indexOfEqual = readLine.indexOf(61);
        if (indexOfEqual > 0) {
            String keyword = readLine.substring(0, indexOfEqual).trim();
            String value = readLine.substring(indexOfEqual + 1).trim();
            try {
                KeywordType keywordType = Enum.valueOf(KeywordType.class, keyword.toUpperCase(Locale.ENGLISH));
                switch (keywordType) {
                    case APP: {
                        if (this.appName == null) {
                            this.appName = value;
                            break;
                        }
                        this.printIllegalInput(readLine);
                        break;
                    }
                    case TIER: {
                        if (this.tierName == null) {
                            this.tierName = value;
                            break;
                        }
                        this.printIllegalInput(readLine);
                        break;
                    }
                    case NODE: {
                        if (this.nodeName == null) {
                            this.nodeName = value;
                            break;
                        }
                        this.printIllegalInput(readLine);
                        break;
                    }
                    case VERSION: {
                        if (this.version == null) {
                            if (value.equals("latest") && this.mostCurrentVersion != null) {
                                this.version = this.mostCurrentVersion;
                                break;
                            }
                            this.version = value;
                            break;
                        }
                        this.printIllegalInput(readLine);
                    }
                }
            }
            catch (IllegalArgumentException e) {
                this.printIllegalInput(readLine);
            }
        } else {
            this.printIllegalInput(readLine);
        }
    }

    public static enum KeywordType {
        APP,
        TIER,
        NODE,
        VERSION;

    }
}

