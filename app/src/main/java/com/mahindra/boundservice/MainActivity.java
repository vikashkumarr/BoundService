package com.mahindra.boundservice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button bindService;
    private TextView updataData;
    private ServiceConnection serviceConnection;
    MyService myService;
    int counter = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService = findViewById(R.id.btn_bind_service);
        updataData = findViewById(R.id.tv_show_service_data);

        Log.i("MM- Oncreate", Thread.currentThread().getId() + "");
        bindService.setOnClickListener(viewClick -> {

            showData();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showData();
                    Log.i("MM- Handler", Thread.currentThread().getId() + "");
                    handler.postDelayed(this, 2000);
                }
            }, 2000);
            /*for (int i = 0; i <= 10; i++) {
                try {
                    Thread.sleep(5000);
                    updataData.setText(String.valueOf(myService.getMyDataFromService()));
                    counter++;
                    Toast.makeText(this, "Service Value " + myService.getMyDataFromService() + counter, Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }*/
        });


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
                myService = myBinder.getService();
                Log.i("MM- onServiceConnected", Thread.currentThread().getId() + "");
                //  Toast.makeText(getApplicationContext(), "Service Connected Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    private void showData() {
        int serviceData = myService.getMyDataFromService();
        updataData.setText(("Service Data : " + serviceData));
        // counter++;
        Log.i("MM- showData", Thread.currentThread().getId() + "");
        Toast.makeText(this, "Service Data " + serviceData, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,/*"com.vikash.test"*/MyService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        Log.i("Activity", serviceConnection + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity", "Activity Destroyed");
    }

    public static Intent convertImplicitIntentToExplicitIntent(Intent implicitIntent, Context context) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
