package com.example.quvo.shudenkun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;
import java.util.Date;
import java.util.HashMap;

import butterknife.InjectView;

/**
 * Created by shikichi_takuya on 15/06/13.
 */
public class AlarmNortificationActivity extends Activity  {
    private LocationManager mLocationManager;
    HashMap<String,Double> locationMap;

    @InjectView(R.id.limit_time)
    TextView textLimiTime;
    @InjectView(R.id.name)
    TextView textName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject jsonObject =  LastRouteService.getJsonObject("http://express.heartrails.com/api/json?method=getStations&x=135&y=35");
        try {
            Log.d("json:", jsonObject.getJSONObject("response").getJSONArray("station").toString());
            Log.d("json:", jsonObject.getJSONObject("response").getJSONArray("station").getJSONObject(0).getString("x").toString());

        }catch (Exception e){
            Log.d("error",e.toString());
        }
        //checkLastTrain(139.766084,35.681382);
//        getLocation();

    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "アラームスタート！", Toast.LENGTH_LONG).show();
        // 音を鳴らす

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndRelaese();
    }

    private void stopAndRelaese() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // mam.stopAlarm();
    }

    private void getLocation(){
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = null;
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }

        Log.d("PROVIDER:", provider);
        if(provider != null) {
            locationMap = new HashMap<String,Double>();
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, //LocationManager.NETWORK_PROVIDER,
                    10, // 通知のための最小時間間隔（ミリ秒）
                    1, // 通知のための最小距離間隔（メートル）
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mLocationManager.removeUpdates(this);
                            String msg = "Lat=" + location.getLatitude() + "\nLng=" + location.getLongitude();
                            Log.d("GPS", msg);
                            checkLastTrain(location.getLongitude(), location.getLatitude());
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }
                    }

            );
        }
    }

    private void checkLastTrain(double longitude,double latitude){

        LastRoute lastRoute = LastRouteService.getLastRoute(longitude, latitude);
        if(lastRoute != null){
            // スクリーンロックを解除する
            // 権限が必要
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            Toast.makeText(this, "アラーム！", Toast.LENGTH_SHORT).show();
           noticeLastRoute(lastRoute);
        }else{
            Log.e("AlarmNortification:", "終電情報取得失敗");
        }
    }

    private void noticeLastRoute(LastRoute lastRoute){
        Integer arrangedHour = lastRoute.getDepatureAt().getHours() ;
        if(lastRoute.getDepatureAt().getHours() < 5){
            arrangedHour += 24;
        }
        Log.d("hour:",((Integer) lastRoute.getDepatureAt().getMinutes()).toString());
        Date now = new Date();
        int diffHours = arrangedHour - now.getHours();
        int diffMinutes = lastRoute.getDepatureAt().getMinutes() - now.getMinutes();
        Integer diff = diffHours*60 + diffMinutes;
        Log.d("diff:", diff.toString());
        diff = 61;
        if(diff < 6000) {
            textLimiTime.setText("終電時刻:"+lastRoute.getDepatureAt().toString());
            textName.setText("出発駅:"+lastRoute.getDepature());
            setContentView(R.layout.alarm_nortification);
        }
    }
}
