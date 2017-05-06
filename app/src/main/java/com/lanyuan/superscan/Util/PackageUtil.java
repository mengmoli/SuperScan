package com.lanyuan.superscan.Util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.lanyuan.superscan.Pojo.Rule;


public class PackageUtil {
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void lunchPkg(Context context, Rule rule) {
        if (PackageUtil.isPkgInstalled(context, rule.getPackageName())) {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(rule.getPackageName());
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "没有安装 " + rule.getName() + " 应用", Toast.LENGTH_SHORT).show();
        }
    }

}