package com.lanyuan.superscan.Pojo;

import java.util.List;

public class Rule {

    private String name;
    private String packageName;
    private List<String> regexs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getRegexs() {
        return regexs;
    }

    public void setRegexs(List<String> regexs) {
        this.regexs = regexs;
    }
}
