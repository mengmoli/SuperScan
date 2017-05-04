package com.google.zxing.client.android;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 默认的 ScanActivity。
 * 需要自定义的话请继承 SuperScanActivity。
 */
public class SimpleScanActivity extends SuperScanActivity {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_scan);
        context = this;
    }

    @Override
    public void handlerResult(CharSequence result) {
        Toast.makeText(context, "result = " + result, Toast.LENGTH_SHORT).show();
        finish();
    }
}
