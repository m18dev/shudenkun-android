package com.example.quvo.shudenkun;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by shikichi_takuya on 15/06/13.
 */
public class LastRouteService extends Service {
    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("alart:", isLastTrainSoon(139.766084, 35.681382).toString());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    //サービスに接続するためのBinder
    public class MyServiceLocalBinder extends Binder {
        //サービスの取得
        LastRouteService getService() {
            return LastRouteService.this;
        }
    }

    //Binderの生成
    private final IBinder mBinder = new MyServiceLocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("alart:", isLastTrainSoon(139.766084, 35.681382).toString());
        Toast.makeText(this, "MyService#onBind" + ": " + intent, Toast.LENGTH_SHORT).show();
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Toast.makeText(this, "MyService#onRebind" + ": " + intent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "MyService#onUnbind" + ": " + intent, Toast.LENGTH_SHORT).show();
        //onUnbindをreturn trueでoverrideすると次回バインド時にonRebildが呼ばれる
        return true;
    }

    public static LastRoute getLastRoute(Double longitude, Double latitude) {
        Context context = ApplicationController.getInstance().getApplicationContext();
        SharedPreferences data = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String depatureName = getNearestStationName(longitude, latitude);
        Integer userId = data.getInt("UserId", 1);

        String url = "https://shudenkun.herokuapp.com/api/last_train.json?user_id=" + userId.toString() + "&depature=" + depatureName;

        JSONObject lastTrainJSON = getJsonObject(url);
        try {
            LastRoute lastRoute = new LastRoute(depatureName, lastTrainJSON.getString("destination"), castDate(lastTrainJSON.getString("depature_at")));
            return lastRoute;
        } catch (JSONException je) {
            Log.e("json取得失敗", je.toString());
            return null;
        }
    }

    private static String getNearestStationName(Double longitude, Double latitude) {
        String url = "http://express.heartrails.com/api/json?method=getStations&x="
                + longitude.toString() + "&y=" + latitude.toString();
        JSONObject nearStationJSON = getJsonObject(url);
        try {
            JSONArray stations = nearStationJSON.getJSONObject("response").getJSONArray("station");
            return stations.getJSONObject(0).getString("name");
        } catch (JSONException je) {
            Log.e("json取得失敗", je.toString());
        }
        return null;

    }

    public static JSONObject getJsonObject(String url) {
        final int RETRY_NUM = 3;
        final long RETRY_INTERVAL = 5000;
        JsonManager jsonManager = new JsonManager(url);
        jsonManager.execute(new Uri.Builder());
        JSONObject jo = null;
        Integer retryNum = 0;
        while (jo == null && (retryNum < RETRY_NUM)) {
            try {
                Thread.sleep(RETRY_INTERVAL);
                jo = jsonManager.getJsonObject();
                retryNum++;
            } catch (Exception e) {
                Log.e("except", e.toString());
            }
        }
        return jo;
    }

    private static Date castDate(String time) {
        Date depatureAt = new Date();
        String[] timeArray = time.split(":", 0);
        int hour = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        Log.d("hour:", ((Integer) hour).toString());
        depatureAt.setHours(hour);
        depatureAt.setMinutes(minute);
        return depatureAt;
    }

    private static Boolean isLastTrainSoon(double longitude, double latitude) {
        LastRoute lastRoute = getLastRoute(longitude, latitude);
        Integer arrangedHour = lastRoute.getDepatureAt().getHours();
        if (lastRoute.getDepatureAt().getHours() < 5) {
            arrangedHour += 24;
        }
        Log.d("hour:", ((Integer) lastRoute.getDepatureAt().getMinutes()).toString());
        Date now = new Date();
        int diffHours = arrangedHour - now.getHours();
        int diffMinutes = lastRoute.getDepatureAt().getMinutes() - now.getMinutes();
        Integer diff = diffHours * 60 + diffMinutes;
        Log.d("diff:", diff.toString());
        diff = 61;
        if (diff < 6000) {
            return true;

        }
        return false;
    }
}
