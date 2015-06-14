package com.example.quvo.shudenkun;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    SharedPreferences data;
    @InjectView(R.id.alert)
    Button alertBtn;
    @InjectView(R.id.edit_station)
    EditText editStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                ButterKnife.inject(this);
        data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        editStation.setText(data.getString("NearestStation",""));
        initAlert();
    }

    @OnClick(R.id.save_btn)
    void onClickButton(){
        createUserData("quvo4","kubo.cota1017@gmail.com","赤坂見附");
        SharedPreferences.Editor editor = data.edit();
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveMyNearestStation(String station){
        SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString("NearestStation", station);
        editor.apply();
    }

    private void createUserData(String name, String email, String nearestStation){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            String url = "http://shudenkun.herokuapp.com/users.json";

            HttpPost httpPost = new HttpPost(url);
            DefaultHttpClient client = new DefaultHttpClient();
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("name", name);
                jsonObject.put("email", email);
                jsonObject.put("nearest_station", nearestStation);
                StringEntity se = new StringEntity(jsonObject.toString());
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");
                HttpResponse response = client.execute(httpPost);
                Log.d("response", EntityUtils.toString(response.getEntity()));
            }catch (JSONException je){
                je.printStackTrace();

            }catch (IOException ie){
                 ie.printStackTrace();
            }
        }

    }
    private void initAlert(){
        Boolean aleartOn = data.getBoolean("AleartOn",false);
        if(aleartOn){
            alertBtn.setText("終電君OFF");
            alertBtn.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            alertBtn.setText("終電君ON");
            alertBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        MyAlarmManager mam = new MyAlarmManager(this);
        mam.addAlarm(9,10);
    }

    @OnClick(R.id.alert)
    void onClickAlertBtn(){
       Boolean alertOn = data.getBoolean("AlertOn",false);
        SharedPreferences.Editor editor = data.edit();
        if(alertOn) {
            editor.putBoolean("AlertOn", false);
            alertBtn.setText("終電君OFF");
            alertBtn.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            editor.putBoolean("AlertOn", true);
             alertBtn.setText("終電君ON");
                        alertBtn.setBackgroundColor(getResources().getColor(R.color.blue));
       }
       editor.apply();
    }
}
