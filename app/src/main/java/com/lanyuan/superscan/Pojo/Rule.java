package com.lanyuan.superscan.Pojo;

import java.util.List;

public class Rule {

    private String name;
    private String packageName;
    private String activity;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<String> getRegexs() {
        return regexs;
    }

    public void setRegexs(List<String> regexs) {
        this.regexs = regexs;
    }

    public String getRegexs2String() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < regexs.size(); i++) {
            if (i != regexs.size() - 1) {
                sb.append(regexs.get(i) + "\n");
            } else {
                sb.append(regexs.get(i));
            }
        }
        return sb.toString();
    }
}
