/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.instrumentation.sdk;

import com.appdynamics.instrumentation.sdk.Rule;
import java.util.List;

public interface IRuleAppliable {
    public List<Rule> initializeRules();

    public List<Rule> getRules();
}

