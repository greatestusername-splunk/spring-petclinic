/*
 * Decompiled with CFR 0.152.
 */
package com.singularity.ee.agent.appagent.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD, ElementType.FIELD})
@Retention(value=RetentionPolicy.SOURCE)
public @interface DotNetIgnore {
}

