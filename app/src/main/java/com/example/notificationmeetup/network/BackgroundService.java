package com.example.notificationmeetup.network;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notificationmeetup.BuildConfig;
import com.example.notificationmeetup.model.Event;
import com.example.notificationmeetup.view.NotificationBuilder;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by meltemyildirim on 4/18/17.
 */

public class BackgroundService extends Service {
    private List<Event> events;
    private Context context;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        apiConnection();
                        Thread.sleep(((1000 * 60) * 60) * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    public void apiConnection() {

        Retrofit retrofit = ApiClient.getClient();
        Api service = retrofit.create(Api.class);
        Call<List<Event>> call = service.getEvents(BuildConfig.apiKey);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events = response.body();
                for (int i = 0; i < events.size(); i++){
                    long currentTime = new Date().getTime();
                    currentTime -= events.get(i).getTime();
                    if (Math.abs(currentTime) < (((1000 * 60) * 60) * 24)){
                        if (currentTime <= 0){
                            NotificationBuilder notificationManager = new NotificationBuilder(context);
                            notificationManager.ShowNotification(events.get(i).getName(), events.get(i).getTime(), events.get(i).getLink());
                        }else{
                            Log.e("E NOTIFICATION", "EVENT PASSED ALREADY");
                        }
                    } else {
                        Log.e("E NOTIFICATION", "NO NOTIFICATIONS");
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.d("Connection Failed", t.toString());
            }
        });

    }
}
