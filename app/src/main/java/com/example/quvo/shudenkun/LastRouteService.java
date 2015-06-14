package com.example.quvo.shudenkun;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shikichi_takuya on 15/06/13.
 */
public class LastRouteService {
    public static LastRoute getLastRoute(Double latitude, Double longitude, Context c){
        SharedPreferences data = c.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        String depatureName = getNearestStationName(latitude,longitude);
        Integer userId = data.getInt("UserId",0 );

        String url = "shudenkun.herokuapp.com/api/last_train.json?user_id=" + userId.toString() + "&departure=" + depatureName;

        JSONObject lastTrainJSON = JsonManager.jsonObjects(url).get(0);
        try {
            LastRoute lastRoute = new LastRoute(depatureName,lastTrainJSON.getString("destination"),lastTrainJSON.getString("depature_at"));
            return lastRoute;
        }catch (JSONException je){
            Log.e("json取得失敗",je.toString());
            return null;
        }
    }

    private static String getNearestStationName(Double latitude, Double longitude){
        String url = "http://express.heartrails.com/api/json?method=getStations&x="
                + latitude.toString() + "&y=" + longitude.toString();
        JSONObject nearStationJSON = JsonManager.jsonObjects(url).get(0);
        try {
           JSONArray stationNames = nearStationJSON.getJSONObject("response").getJSONArray("station");
           return stationNames.get(0).toString();
        }catch (JSONException je){
           Log.e("json取得失敗",je.toString());
        }
        return null;

    }


}
