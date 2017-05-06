package com.lanyuan.superscan.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.DataOutputStream;

public class CommandUtil {

    private static boolean switch_on = false;

    public static boolean isSwitch_on() {
        return switch_on;
    }

    public static void setSwitch_on(Context context, boolean switch_on) {
        CommandUtil.switch_on = switch_on;
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switch_on", switch_on);
        editor.commit();
    }

    public static boolean BootAppByRoot(String packageName, String activityName) {
        Process process = null;
        DataOutputStream os = null;
        //Log.d("RootInfo", "Method excuted");
        try {
            String s = "am start -n " + packageName + "/" + activityName;
            Log.e("Hey", s);
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.write(s.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static boolean isRoot() {
        int flag = 0;
        Process process = null;
        DataOutputStream os = null;
        //Log.d("RootInfo", "Method excuted");
        try {
            String s = "echo test";
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.write(s.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            flag = process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        //Log.e("hey",String.valueOf(flag));
        if (flag == 0)
            return true;
        else
            return false;
    }
}
