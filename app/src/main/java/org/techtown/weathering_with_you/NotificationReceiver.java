package org.techtown.weathering_with_you;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    String TAG = "NotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Arrive");
        Intent notiIntent = new Intent(context, NotificationService.class);

        //NotificationService notificationService = new NotificationService();
        //notificationService.startService(notiIntent);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(notiIntent);
        } else {
            context.startService(notiIntent);
        }

    }
}
