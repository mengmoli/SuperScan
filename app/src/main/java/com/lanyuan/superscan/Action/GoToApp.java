package com.lanyuan.superscan.Action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.Util.CommandUtil;
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

    public static boolean go(Context context, CharSequence result) {
        String url = (String) result;
        for (Rule rule : rules) {
            for (String regex : rule.getRegexs()) {
                if (Pattern.matches(regex, url)) {
                    if (CommandUtil.isSwitch_on()) {
                        CommandUtil.BootAppByRoot(rule.getPackageName(), rule.getActivity());
                        return true;
                    } else {
                        PackageUtil.lunchPkg(context, rule);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void go(Context context, Rule rule) {

        if (CommandUtil.isSwitch_on()) {
            CommandUtil.BootAppByRoot(rule.getPackageName(), rule.getActivity());
            return;
        } else {
            PackageUtil.lunchPkg(context, rule);
            return;
        }

    }

}
