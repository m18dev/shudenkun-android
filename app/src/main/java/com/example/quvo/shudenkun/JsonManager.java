package com.example.quvo.shudenkun;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by shikichi_takuya on 15/03/22.
 */
public class JsonManager {
    public static ArrayList<JSONObject> jsonObjects(String url)
    {
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.d("line",line);
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonData = builder.toString();
        Log.d("json",jsonData);
        try {

            JSONArray jsonArray = new JSONArray(builder.toString());
            System.out.println("Number of entries " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObjects.add(jsonObject);
            }
            return jsonObjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("JsonManager","you cant get Json object, url:" + url);
        return null;
    }
}

