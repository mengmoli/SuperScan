package com.google.zxing.client.android.core;

import android.content.Context;
import android.os.Vibrator;

final class VibrateManager {
    private static final boolean VIBRATE = true;

    private static final long VIBRATE_DURATION = 200L;

    private final Context context;

    VibrateManager(Context context) {
        this.context = context;
    }

    synchronized void vibrate() {
        if (VIBRATE) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
}
