package com.greendashPH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    static MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mp == null) {
            mp = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.setLooping(true);
        }
        String status = intent.getStringExtra("status");
        if (status!=null && status.equals("on")) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "test:test");
            wl.acquire();
            wl.release();
            mp.start();
            Toast.makeText(context, "Alarm", Toast.LENGTH_LONG).show();
        }
        else{
            mp.pause();
        }
    }
}