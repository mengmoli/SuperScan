package com.lanyuan.superscan.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.CommandUtil;
import com.lanyuan.superscan.Util.FileUtil;
import com.lanyuan.superscan.Util.ImageUtil;

import java.util.Hashtable;
import java.util.List;

public class MainActivity extends BaseCaptureActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";

    public static final int REQUEST_IMAGE = 112;
    private static boolean is_flash_on = false;

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;

    private ImageButton button, flash, picture;
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

        picture = (ImageButton) findViewById(R.id.scan_picture);
        picture.setOnClickListener(this);

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
                openFlash();
                break;
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.scan_picture:
                openPicture();
                break;
        }
    }

    private void openPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 选择系统图片并解析
         */
        if (requestCode == REQUEST_IMAGE) {
            Log.e(TAG, "5");
            if (data != null) {
                Uri uri = data.getData();
                try {
                    ImageUtil.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new ImageUtil.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, Result rawResult) {
                            if (!GoToApp.go(MainActivity.this, rawResult.getText())) {
                                doWhenScanFail(rawResult.getText());
                            }
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void openFlash() {
        if (is_flash_on) {
            cameraManager.setTorch(false);
            //Log.e(TAG,"TorchOff");
            flash.setImageResource(R.drawable.flash_off);
            is_flash_on = false;
        } else {
            cameraManager.setTorch(true);
            //Log.e(TAG,"TorchOn");
            flash.setImageResource(R.drawable.flash_on);
            is_flash_on = true;
        }
    }

    @Override
    protected void onPause() {
        is_flash_on = false;
        flash.setImageResource(R.drawable.flash_off);
        super.onPause();
    }
}