package org.techtown.weathering_with_you;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    String TAG = "ScreenReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "On Receive");
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.e(TAG, "폰 꺼짐");
            Intent i = new Intent(context, LockScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
