package com.example.quvo.shudenkun;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by shikichi_takuya on 15/03/22.
 */
public class JsonManager extends AsyncTask<Uri.Builder, Void, String> {
    private Activity mainActivity;
    private String url;
    private JSONObject jsonObject;

    public JsonManager(String url) {
        // 呼び出し元のアクティビティ
        this.url = url;
    }
    @Override
    protected String doInBackground(Uri.Builder... builder){
        jsonObject = jsonObject(url);
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        // 取得した結果をテキストビューに入れちゃったり
    }
    private JSONObject jsonObject(String url)
    {
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        Log.d("url!!!!:",url);
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
            return new JSONObject(jsonData);
        }catch(JSONException e){
            Log.e("JsonManager",e.toString());
            return null;
        }
    }

    public JSONObject getJsonObject(){
       return jsonObject;
    }
}

