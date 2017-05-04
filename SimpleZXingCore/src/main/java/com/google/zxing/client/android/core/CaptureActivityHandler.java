package com.google.zxing.client.android.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.camera.CameraManager;

import java.util.Collection;

public final class CaptureActivityHandler extends Handler {

    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    CaptureActivityHandler(CaptureActivity activity,
                           Collection<BarcodeFormat> decodeFormats,
                           String characterSet,
                           CameraManager cameraManager) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet, null);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();

        } else if (message.what == R.id.decode_succeeded) {
            state = State.SUCCESS;
            activity.handleDecode((Result) message.obj);

        } else if (message.what == R.id.decode_failed) {// We're decoding as fast as possible, so when one decode fails, start another.
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);

        } else if (message.what == R.id.return_scan_result) {
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();

        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            activity.drawViewfinder();
        }
    }
}
