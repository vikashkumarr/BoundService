package com.mahindra.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

    private final IBinder binder = new MyBinder();
    private final Random mGenerator = new Random();
    int count;

    public MyService() {
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getMyDataFromService() {
        Log.i("MM- MyService", Thread.currentThread().getId() + "");
        return count++;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
