package com.lanyuan.superscan.Action;

import android.content.Context;
import android.util.Log;

import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.Util.PackageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GoToApp {

    private final static String TAG = "GoToApp";

    private static List<Rule> rules;

    public static List<Rule> getRules() {
        return rules;
    }

    public static void setRules(List<Rule> rules) {
        GoToApp.rules = rules;
    }

    public static void go(Context context, CharSequence result) {
        String url = (String) result;
        for (Rule rule : rules) {
            for (String regex : rule.getRegexs()) {
                if (Pattern.matches(regex, url)) {
                    //Log.e(TAG, "go");
                    PackageUtil.lunchPkg(context, rule);
                }
            }
        }
    }

}
