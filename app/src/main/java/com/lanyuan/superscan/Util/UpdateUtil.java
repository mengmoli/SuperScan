package com.lanyuan.superscan.Util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class UpdateUtil {
    /*
    跳转到应用市场
    */
    public static void goToAppMarket(Context context) {
        if (PackageUtil.isPkgInstalled(context,"com.coolapk.market")) {
            Toast.makeText(context, "本应用目前仅发布在酷安市场，请前往酷安市场评分……", Toast.LENGTH_SHORT).show();
            try {
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "跳转出错……请向作者反馈", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "未检测到酷安市场客户端，正在跳转到网页……", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.coolapk.com/apk/com.lanyuan.wondergird"));
            context.startActivity(intent);
        }
    }
}
