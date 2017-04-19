package com.example.notificationmeetup.network;

import android.app.Service;
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

/**
 * In BackgroundService we pull the events that user sighup from meetup api.
 */

public class BackgroundService extends Service {
    /**
     * @events is list of events that user signup
     */
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

        /**
         * loop will keep running on Runnable Thread and calling apiConnection Method
         *
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        apiConnection();
                        Thread.sleep(((1000 * 60) * 60) * 2); // It checks api every 2 hours to see if there is any event coming up with in 24hr
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // It restarts the service when it closes
    }

    /**
     * This method calls api connection by using retrofit
     * Callback returns a list of events (Response) which gets from api, by using the specified api key
     *
     * */
    public void apiConnection() {
        //invoking this API call
        Retrofit retrofit = ApiClient.getClient();
        Api service = retrofit.create(Api.class);
        Call<List<Event>> call = service.getEvents(BuildConfig.apiKey);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                events = response.body(); // list of events from meetup api
                for (int i = 0; i < events.size(); i++){ // loop through each event in events list
                    long currentTime = new Date().getTime(); // getting  current time from the system
                    long timeLeft = currentTime -  events.get(i).getTime();  // Subtract the event time from current Time, so we can get the time left for the event
                    if (Math.abs(timeLeft) < (((1000 * 60) * 60) * 24)){ // if time left is less than 24 hours, it's upcoming event
                        if (timeLeft <= 0){ // if time left is positive then the event passed already
                            // Show notifications for upcoming events
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
