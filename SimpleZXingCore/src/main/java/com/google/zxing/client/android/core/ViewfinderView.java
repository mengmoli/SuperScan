package com.google.zxing.client.android.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.client.android.camera.CameraManager;

public final class ViewfinderView extends View {
    /** 黑色背景 */
    private static final int COLOR_BACKGROUND = 0x60000000;
    /** 黄色边框 */
    private static final int COLOR_SIDE = 0xFFF1C40F;
    /** 红色激光 */
    private static final int COLOR_LASER = 0xFFE74C3C;
    /** 激光透明度循环 */
    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    /** 多长时间刷新一次 */
    private static final long ANIMATION_DELAY = 100L;
    /** 刷新的区域距离frame的距离 */
    private static final int POINT_SIZE = 6;

    private Context context;

    private CameraManager cameraManager;
    private final Paint paint;
    private int scannerAlphaIndex;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scannerAlphaIndex = 0;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources()
                                   .getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }

        // 缩小边框
        int padding = dp2px(context, 50);
        frame = new Rect(frame.left + padding, frame.top + padding, frame.right - padding, frame.bottom - padding);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 画黑色背景
        paint.setColor(COLOR_BACKGROUND);
        canvas.drawRect(0, 0, width, frame.top, paint);// 上边
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);// 左边
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);// 右边
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);// 下边

        // 画黄色边框
        paint.setColor(COLOR_SIDE);
        canvas.drawLine(frame.left, frame.top, frame.right, frame.top, paint);
        canvas.drawLine(frame.right, frame.top, frame.right, frame.bottom, paint);
        canvas.drawLine(frame.right, frame.bottom, frame.left, frame.bottom, paint);
        canvas.drawLine(frame.left, frame.bottom, frame.left, frame.top, paint);

        // 画激光线
        paint.setColor(COLOR_LASER);
        paint.setAlpha(SCANNER_ALPHA[scannerAlphaIndex]);
        scannerAlphaIndex = (scannerAlphaIndex + 1) % SCANNER_ALPHA.length;
        int middle = frame.height() / 2 + frame.top;
        canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

        // 区域刷新
        postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE, frame.top - POINT_SIZE, frame.right + POINT_SIZE, frame.bottom + POINT_SIZE);
    }

    public void drawViewfinder() {
        invalidate();
    }
}
