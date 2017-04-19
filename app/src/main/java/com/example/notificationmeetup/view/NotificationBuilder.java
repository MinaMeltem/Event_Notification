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

    public NotificationBuilder(Context context) {
        this.context = context;
    }

    public void ShowNotification(String event_name, long t, String link) {

        remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.noti_layout);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this.context)
                .setSmallIcon(R.drawable.meetup_logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContent(remoteViews);

        remoteViews.setImageViewResource(R.id.icon_iv, R.drawable.meetup_logo);
        remoteViews.setTextViewText(R.id.title_tv, context.getResources().getString(R.string.title));

        if (event_name.length() > 20){
            event_name = event_name.substring(0, 19) + "  ";
        }

        remoteViews.setTextViewText(R.id.event_name_tv, event_name);
        remoteViews.setTextViewText(R.id.event_date_tv, changeToDate(t));

        NotificationManager notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestID, notificationBuilder.build());
    }

    public String changeToDate(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        return month + "/" + day +" , "+ hour+":"+((minutes == 0) ? "00" : minutes);
    }
}
