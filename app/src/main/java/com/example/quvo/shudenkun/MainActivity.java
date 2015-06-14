package com.example.quvo.shudenkun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

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
        /*
        Intent intent = new Intent(this,AlarmNortificationActivity.class);
        startActivity(intent);
        */

    }

    @OnClick(R.id.save_btn)
    void onClickButton(){
        UserService.create("quvo4", "kubo.cota1017@gmail.com", "赤坂見附");
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
        Date now = new Date();
        mam.addAlarm(now.getHours(), now.getMinutes() + 1);
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
