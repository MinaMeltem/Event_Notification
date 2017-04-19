package com.example.notificationmeetup.network;

import com.example.notificationmeetup.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meltemyildirim on 4/18/17.
 */


/**
 * This is Api connection interface
 */

public interface Api {
    @GET("self/events")
    Call<List<Event>> getEvents(@Query("key") String key);
}
