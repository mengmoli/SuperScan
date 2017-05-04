package com.google.zxing.client.android;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.core.CaptureActivity;
import com.google.zxing.client.android.core.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.Hashtable;

/**
 * 需要自定义的话请继承此 activity
 */
public abstract class SuperScanActivity extends CaptureActivity {
    private static final int REQUEST_CODE_GET_IMAGE = 1;

    /**
     * 获取本地图片。
     * 重写 onGetImageOk 方法监听获取到的图片path。
     */
    public void getLocalImage() {
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择图片");
        startActivityForResult(wrapperIntent, REQUEST_CODE_GET_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGE) {
            //获取选中图片的路径
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            if (cursor != null) {
                String imgPath = null;
                if (cursor.moveToFirst()) {
                    imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
                onGetImageOk(imgPath);
            }
        }
    }

    /**
     * 获取到本地图片的时候会调用此方法。
     */
    public void onGetImageOk(String imgPath) {
    }

    /**
     * 扫描二维码图片的方法
     */
    public void scanningImage(String path) {
        if (TextUtils.isEmpty(path)) return;

        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0) sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(bitmap1, hints);
            String text = result.getText();
            handlerResult(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scanBitmap != null) {
                scanBitmap.recycle();
            }
        }
    }

    /**
     * 交给子类处理扫描结果
     */
    @Override
    public abstract void handlerResult(CharSequence result);
}
