package com.nmmedit.protect.ui;

public class AppData {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String apkTitle;
    String apkPath;
    String proguardRules;
    String outputPath;
    String signPath;
    String alias;
    String signPassword;
    String storePassword;

    public AppData() {
    }

    public AppData(String apkTitle, String apkPath, String proguardRules, String outputPath, String signPath, String alias, String signPassword, String storePassword) {
        this.apkTitle = apkTitle;
        this.apkPath = apkPath;
        this.proguardRules = proguardRules;
        this.outputPath = outputPath;
        this.signPath = signPath;
        this.alias = alias;
        this.signPassword = signPassword;
        this.storePassword = storePassword;
    }

    public String getApkTitle() {
        return apkTitle;
    }

    public void setApkTitle(String apkTitle) {
        this.apkTitle = apkTitle;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getProguardRules() {
        return proguardRules;
    }

    public void setProguardRules(String proguardRules) {
        this.proguardRules = proguardRules;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getSignPath() {
        return signPath;
    }

    public void setSignPath(String signPath) {
        this.signPath = signPath;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSignPassword() {
        return signPassword;
    }

    public void setSignPassword(String signPassword) {
        this.signPassword = signPassword;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }
}
