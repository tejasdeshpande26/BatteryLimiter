package com.example.android.batterylimiter;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView statusImage;
    TextView statusText;
    TextView percent;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusImage = findViewById(R.id.status_image);
        statusText = findViewById(R.id.status);
        percent = findViewById(R.id.percent);

        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryLevel();
                boolean status =  batteryStatus();
                percent.setText("Battery is at: "+level+"%");
                if(level >= 80){
                    statusImage.setImageResource(R.drawable.battery_green);
                    if(level>95){
                        statusText.setText("Please do not charge anymore");
                        statusText.setTextColor(4);
                    }
                }
                if(level >50 && level <= 80){
                    statusImage.setImageResource(R.drawable.battery_yellow);
                }
                if(level >25 && level <= 50 ){
                    statusImage.setImageResource(R.drawable.battery_marron);
                }
                if(level > 5 && level <= 25){
                    statusImage.setImageResource(R.drawable.battery_red);
                }
                if (status){
                    statusText.setText("Charging");
                }
                else {
                    statusText.setText("Not Charging");
                }
                handler.postDelayed(runnable, 5000);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 0);
    }

    public float batteryLevel(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        if(level == -1 || scale == -1){
            return 50.0f;
        }
        return ((float) level / (float) scale) *100.0f;
    }

    public boolean batteryStatus(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }
}
