package com.example.mkruz.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.mkruz.receiver.R;


public class MyNotificationService extends Service {
    public MyNotificationService() {
    }
    private boolean isRunning  = false;
    private long id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ServiceTest", "rozpoczeto serwis");
        id = intent.getLongExtra("id", -1);
        Log.i("ServiceTest", "id = " + id);
        Log.i("ServiceTest", "zrobiono serwis");
        Intent intent1 = new Intent(getApplicationContext(), ListActivity.class); //brak akcji
        intent1.putExtra("id", id);
        Log.i("intenet", intent1.toString());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
        Log.i("pendingIntenet", pendingIntent.toString());
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Item added to shopping list")
                .setContentText("Tap to view")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        Log.i("ServiceTest", "zrobiono serwis 1");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("ServiceTest", "zrobiono serwis 2");
        notificationManager.notify(0, notification);
        Log.i("ServiceTest", "zrobiono serwis 3");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ServiceTest", "zbindowano serwis");
        return null;
    }
    @Override
    public void onCreate(){
        isRunning = true;
    }
    @Override
    public void onDestroy(){
        isRunning = false;
        Log.i("ServiceTest", "usunięto serwis");
    }
}
