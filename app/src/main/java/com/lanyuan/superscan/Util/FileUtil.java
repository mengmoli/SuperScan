package com.lanyuan.superscan.Util;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.lanyuan.superscan.Pojo.Rule;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FileUtil {

    public static void saveRules(Context context, String s) {
        try {
            FileOutputStream outputStream = context.openFileOutput("rules.ini", MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRules(Context context, List<Rule> ruleList) {
        String json = JsonUtil.gson.toJson(ruleList);
        saveRules(context, json);
    }

    public static List<Rule> loadRules(Context context) {
        String s = null;
        try {
            FileInputStream inputStream = context.openFileInput("rules.ini");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            stream.close();
            inputStream.close();
            s = stream.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonUtil.gson.fromJson(s, new TypeToken<ArrayList<Rule>>() {
        }.getType());
    }

}
