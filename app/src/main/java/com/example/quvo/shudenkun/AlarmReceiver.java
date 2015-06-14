package com.example.quvo.shudenkun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shikichi_takuya on 15/06/13.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // アラームを受け取って起動するActivityを指定、起動
        context.startService(new Intent(context, LastRouteService.class));
        Log.d("check","!!!!!!!!");
        /*
        Intent notification = new Intent(context, AlarmNortificationActivity.class);
        // 画面起動に必要
        notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notification);
        */
    }

}
