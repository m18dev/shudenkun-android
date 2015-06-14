package com.example.quvo.shudenkun;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by shikichi_takuya on 15/06/13.
 */
public class AlarmNortificationActivity extends Activity  {
    private LocationManager mLocationManager;
    HashMap<String,Double> locationMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_nortification);
        getLocation();
        LastRoute lastRoute = LastRouteService.getLastRoute(locationMap.get("latitude"), locationMap.get("longitude"),this);

        if(lastRoute != null){
                    // スクリーンロックを解除する
        // 権限が必要
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(this, "アラーム！", Toast.LENGTH_SHORT).show();
        }else{
            Log.e("AlarmNortificationActivity Error:", "終電情報取得失敗");
        }

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
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, //LocationManager.NETWORK_PROVIDER,
                    3000, // 通知のための最小時間間隔（ミリ秒）
                    10, // 通知のための最小距離間隔（メートル）
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            locationMap = new HashMap<String,Double>();
                            locationMap.put("latitude", location.getLatitude());
                            locationMap.put("longitude", location.getLongitude());
                            String msg = "Lat=" + location.getLatitude()
                                    + "\nLng=" + location.getLongitude();
                            Log.d("GPS", msg);
                            mLocationManager.removeUpdates(this);
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

}
