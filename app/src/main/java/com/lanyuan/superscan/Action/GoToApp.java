package com.lanyuan.superscan.Action;

import android.util.Log;

import com.lanyuan.superscan.Pojo.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GoToApp {

    private final static String TAG = "GoToApp";

    private static ArrayList<Rule> rules;

    static {
        rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setName("微信");
        rule.setPackageName("com.mm.wechat");
        List<String> list = new ArrayList<>();
        list.add("http://weixin.qq.com\\S*");
        rule.setRegexs(list);
        rules.add(rule);
    }

    public static void go(CharSequence result) {
        String url = (String) result;
        for (Rule rule : rules) {
            for (String regex : rule.getRegexs()) {
                if (Pattern.matches(regex, url)) {
                    Log.e(TAG, rule.getName());
                }
            }
        }
    }

}
