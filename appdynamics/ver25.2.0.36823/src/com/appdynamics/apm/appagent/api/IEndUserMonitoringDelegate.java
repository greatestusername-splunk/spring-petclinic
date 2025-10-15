/*
 * Decompiled with CFR 0.152.
 */
package com.appdynamics.apm.appagent.api;

public interface IEndUserMonitoringDelegate {
    public String getHeader();

    public String getFooter();

    public boolean isValidTransaction();

    public boolean doInjectFooter();

    public boolean doInjectHeader();

    public void filterStart();

    public void filterEnd();
}

