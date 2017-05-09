package com.lanyuan.superscan.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.CommandUtil;
import com.lanyuan.superscan.Util.FileUtil;

import java.util.List;

public class MainActivity extends BaseCaptureActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;

    private ImageButton button, flash;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (isFirstOpen()) {
            doFirst();
            editor.putBoolean("isFirst", false);
            editor.commit();
        }

        flash = (ImageButton) findViewById(R.id.flashlight);
        flash.setOnClickListener(this);

        button = (ImageButton) findViewById(R.id.setting);
        button.setOnClickListener(this);

        GoToApp.setRules(FileUtil.loadRules(this));
        CommandUtil.setSwitch_on(this, preferences.getBoolean("switch_on", false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        if (!GoToApp.go(this, rawResult.getText())) {
            doWhenScanFail(rawResult.getText());
        }
    }

    private void doWhenScanFail(String result) {
        List<Rule> rulelist = GoToApp.getRules();
        FailDialog dialog = new FailDialog(MainActivity.this, result, rulelist);
        dialog.show();
    }

    private void doFirst() {
        FileUtil.saveRules(getApplicationContext(), "[{\"activity\":\"com.tencent.mm.plugin.scanner.ui.BaseScanUI\",\"name\":\"微信\",\"packageName\":\"com.tencent.mm\",\"regexs\":[\"http://weixin.qq.com/\\\\S*\",\"https://wx.tenpay.com/\\\\S*\"]},{\"activity\":\"com.alipay.mobile.scan.as.main.MainCaptureActivity\",\"name\":\"支付宝\",\"packageName\":\"com.eg.android.AlipayGphone\",\"regexs\":[\"HTTPS://QR.ALIPAY.COM/\\\\S*\",\"https://qr.alipay.com/\\\\S*\"]}]");
    }

    private boolean isFirstOpen() {
        boolean flag = preferences.getBoolean("isFirst", true);
        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flashlight:

                break;
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
    }
}