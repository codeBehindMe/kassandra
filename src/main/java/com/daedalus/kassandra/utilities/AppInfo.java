package com.daedalus.kassandra.utilities;


public final class AppInfo {
    private final String appName;
    private final int versionMajor;
    private final int versionMinor;
    private final int buildNumber;

    private AppInfo() {
        this.appName = "kassandra";
        this.versionMajor = 0;
        this.versionMinor = 1;
        this.buildNumber = 0;
    }

    /**
     * Prints out the application information to standard out.
     */
    public void printApplicationInfo() {
       throw new Error("Not implemented");
    }
}
