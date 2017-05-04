package com.lanyuan.superscan.UI;

import android.os.Bundle;
import android.util.Log;

import com.google.zxing.client.android.SuperScanActivity;
import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.R;

public class MainActivity extends SuperScanActivity {

    private final static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void handlerResult(CharSequence result) {
        //Log.e(TAG, (String) result);
        GoToApp.go(result);
        restartPreviewAfterDelay(1000);
    }
}