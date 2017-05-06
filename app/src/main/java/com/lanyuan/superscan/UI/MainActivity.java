package com.lanyuan.superscan.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.client.android.SuperScanActivity;
import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.FileUtil;

public class MainActivity extends SuperScanActivity {

    private final static String TAG = "MainActivity";

    ImageButton button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (ImageButton) findViewById(R.id.setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        GoToApp.setRules(FileUtil.loadRules(this));
    }

    @Override
    public void handlerResult(CharSequence result) {
        Log.e(TAG, result.toString());
        GoToApp.go(this,result);
        restartPreviewAfterDelay(1000);
    }
}