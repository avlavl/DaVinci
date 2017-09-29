package com.aioros.investor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aizhang on 2017/9/25.
 */

public class PushService extends Service {

    public static final String TAG = "PushService";
    static Timer timer = null;

    //清除通知
    public static void cleanAllNotification() {
        NotificationManager notificationManager = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //添加通知
    public static void addNotification(String dateTime, String title, String content) {
        Intent intent = new Intent(MainActivity.getContext(), PushService.class);
        intent.putExtra("dateTime", dateTime);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        MainActivity.getContext().startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "==========onCreate=======");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e(TAG, "==========onStartCommand=======");
        long period = 24 * 3600 * 1000; //24小时一个周期
        String dateTime = intent.getStringExtra("dateTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (null == timer) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(PushService.this);
                builder.setSmallIcon(R.mipmap.ic_app_logo);
                builder.setContentTitle(intent.getStringExtra("title"));//下拉通知栏标题
                builder.setContentText(intent.getStringExtra("content")); //下拉通知栏内容
                builder.setAutoCancel(true);
                builder.setSound(Uri.fromFile(new File("/system/media/audio/notifications/CrystalRing.ogg")));
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                Intent intent = new Intent(PushService.this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(PushService.this, 0, intent, 0);
                builder.setContentIntent(contentIntent);//点击跳转的intent
                Notification notification = builder.build();
                notificationManager.notify((int) System.currentTimeMillis(), notification);
            }
        }, date, period);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "==========onDestroy=======");
    }
}