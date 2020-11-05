package org.techtown.weathering_with_you;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {

    String TAG = "NotificationService";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    IBinder iBinder = new NotificationBinder();

    MainActivity mainActivity;
    Weather weather;

    int timeIndex1, timeIndex2;



    class NotificationBinder extends Binder{
        NotificationService getService(){
            return NotificationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            Log.e(TAG, "intent null");
            return Service.START_STICKY;
        }
        else{
            Log.e(TAG, "intent not null");
            NotificationSomethings();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Destroy");
        NotificationSomethings();

        unregisterReceiver(new NotificationReceiver());
    }

    @SuppressLint("WrongConstant")
    public void NotificationSomethings() {

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("refresh", 1);

        PendingIntent actionIntent = PendingIntent.getBroadcast( this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mainActivity = new MainActivity();
        mainActivity.getWeatherInfo(1);

        Log.e(TAG, "1");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), NotificationData.getInstance().iconId)) //BitMap 이미지 요구
                //.setContentTitle(skyInfo(1))
                .setContentTitle(NotificationData.getInstance().nowSkyText)
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                .setStyle(new NotificationCompat.BigTextStyle().bigText("현재기온: "+NotificationData.getInstance().nowT3h+"    미세먼지: "+getMicroDustInfo(0)+"    "+"초미세먼지: "+getMicroDustInfo(1)+
                        "               \n"+"습도: "+NotificationData.getInstance().nowReh+"   "+"강수확률: "+NotificationData.getInstance().nowPop+"   "+"강수량: "+NotificationData.getInstance().nowR06+ "\n"))

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .addAction(R.mipmap.ic_launcher, "날씨 업데이트", actionIntent);



        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.icon); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }else builder.setSmallIcon(R.drawable.icon); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴

    }

    private String getMicroDustInfo(int type) {
        MicroDustActivity microDustActivity = new MicroDustActivity();
        MicroDust microDust = microDustActivity.setBaseData();

        String microDustGrade = "";
        if(type == 0)
            microDustGrade = microDust.getPm10Grade1h()+"";
        else
            microDustGrade = microDust.getPm25Grade1h()+"";

        Log.e(TAG, "Grade: "+microDustGrade);
        String text = "";
        switch (microDustGrade) {
            case "1":
                text = "좋음";
                break;
            case "2":
                text = "보통";
                break;
            case "3":
                text = "나쁨";
                break;
            case "4":
                text = "매무나쁨";
                break;
            default:
                text = "측정소 점검중";
                break;
        }

        return text;
    }


}
