package com.lanyuan.superscan.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.SuperScanActivity;
import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends SuperScanActivity {

    private final static String TAG = "MainActivity";

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        //test();
        //test1();
    }

    private void test() {
        ArrayList<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setName("微信");
        rule.setPackageName("com.mm.wechat");
        List<String> list = new ArrayList<>();
        list.add("http://weixin.qq.com\\S*");
        rule.setRegexs(list);
        rules.add(rule);
        String s = JsonUtil.gson.toJson(rules);
        Log.e(TAG, s);
        try {
            FileOutputStream outputStream = openFileOutput("rules.ini", MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void test1() {
        String s = null;
        try {
            FileInputStream inputStream = openFileInput("rules.ini");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Rule> rules = JsonUtil.gson.fromJson(s, new TypeToken<ArrayList<Rule>>() {
        }.getType());
        Log.e(TAG, String.valueOf(rules.size()));
        Rule rule = rules.get(0);
        Log.e(TAG, rule.getPackageName());
    }

    @Override
    public void handlerResult(CharSequence result) {
        //Log.e(TAG, (String) result);
        //GoToApp.go(result);
        restartPreviewAfterDelay(1000);
    }
}