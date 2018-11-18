package com.example.mkruz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mkruz.receiver.MyNotificationService;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ReceiverTest", "otrzymano intencję: "+intent.getAction());
        long id = intent.getLongExtra("id", -1);
        Log.i("ReceiverTest", "id = " + id);
        Intent serviceIntent = new Intent(context, MyNotificationService.class);
        serviceIntent.putExtra("id", id);
        context.startService(serviceIntent);
    }
}