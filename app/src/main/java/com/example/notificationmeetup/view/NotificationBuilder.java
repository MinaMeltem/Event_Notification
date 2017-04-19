package com.example.notificationmeetup.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.notificationmeetup.R;

import java.util.Calendar;

/**
 * Created by meltemyildirim on 4/18/17.
 */

public class NotificationBuilder {

    int requestID = (int) System.currentTimeMillis();
    private Context context;
    private String eventName;
    private long time;
    private RemoteViews remoteViews;


    //constructor
    public NotificationBuilder(Context context) {
        this.context = context;
    }

    /**
     *
     * @param event_name  Event name we are reding from meetupApi
     * @param t event time
     * @param link is the link of the event that redirect the users when they click on notification
     *
     *             ShowNotification Method creats custom notification ( by using remoteView) and display it
     */
    public void ShowNotification(String event_name, long t, String link) {


        remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.noti_layout); //this is where creating remote obj instintiating remote view
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link)); //defining the intent to trigger when notification is selected

        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT); //turning the intent we created into a PendingIntent

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this.context) //Building the notification by using remoteView obj
                .setSmallIcon(R.drawable.meetup_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContent(remoteViews);

        remoteViews.setImageViewResource(R.id.icon_iv, R.drawable.meetup_logo); //setting the icon from drawable
        remoteViews.setTextViewText(R.id.title_tv, context.getResources().getString(R.string.title)); // setting tiitle from Values

        if (event_name.length() > 40){ //if event name is too long, show only first 20 character so notification displays it properly
            event_name = event_name.substring(0, 39) + "  ";
        }

        // update event_name view
        remoteViews.setTextViewText(R.id.event_name_tv, event_name);
        // update event_date view
        remoteViews.setTextViewText(R.id.event_date_tv, changeToDate(t));

        //Get the notification manager system service
        NotificationManager notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestID, notificationBuilder.build()); //update the notification with unique ID
    }

    //With this method we change the time format from timestamp to readable "mm/dd , tt:mm" format
    public String changeToDate(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        //In api, when the minutes is 00 ( sucah as 1:00pm) it displays only 0 ( 1:0 pm)
        //So i added the line below to show the minutes in proper format for better user experience
        return month + "/" + day +" , "+ hour+":"+((minutes == 0) ? "00" : minutes);
    }
}
