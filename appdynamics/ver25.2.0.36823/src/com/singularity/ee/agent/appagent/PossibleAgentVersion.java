/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent;

import com.singularity.ee.agent.appagent.IAppTierNodeDeterminer;

class PossibleAgentVersion {
    final String version;
    final String[] appTierNodeArray;
    IAppTierNodeDeterminer appTierNodeDeterminer;

    PossibleAgentVersion(String version, String[] appTierNodeArray, IAppTierNodeDeterminer appTierNodeDeterminer) {
        this.version = version;
        this.appTierNodeArray = appTierNodeArray;
        this.appTierNodeDeterminer = appTierNodeDeterminer;
    }
}

