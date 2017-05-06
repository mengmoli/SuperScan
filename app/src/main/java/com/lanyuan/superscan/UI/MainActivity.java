package com.lanyuan.superscan.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.client.android.SuperScanActivity;
import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.CommandUtil;
import com.lanyuan.superscan.Util.FileUtil;

public class MainActivity extends SuperScanActivity {

    private final static String TAG = "MainActivity";

    private ImageButton button;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (isFirstOpen()) {
            doFirst();
            editor.putBoolean("isFirst", false);
            editor.commit();
        }

        button = (ImageButton) findViewById(R.id.setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        GoToApp.setRules(FileUtil.loadRules(this));
        CommandUtil.setSwitch_on(this, preferences.getBoolean("switch_on", false));
    }

    private void doFirst() {
        FileUtil.saveRules(getApplicationContext(), "[{\"activity\":\"com.tencent.mm.plugin.scanner.ui.BaseScanUI\",\"name\":\"微信\",\"packageName\":\"com.tencent.mm\",\"regexs\":[\"http://weixin.qq.com/\\\\S*\",\"https://wx.tenpay.com/\\\\S*\"]},{\"activity\":\"com.alipay.mobile.scan.as.main.MainCaptureActivity\",\"name\":\"支付宝\",\"packageName\":\"com.eg.android.AlipayGphone\",\"regexs\":[\"HTTPS://QR.ALIPAY.COM/\\\\S*\",\"https://qr.alipay.com/\\\\S*\"]}]");
    }

    private boolean isFirstOpen() {
        boolean flag = preferences.getBoolean("isFirst", true);
        return flag;
    }

    @Override
    public void handlerResult(CharSequence result) {
        Log.e(TAG, result.toString());
        GoToApp.go(this, result);
        restartPreviewAfterDelay(1000);
    }
}