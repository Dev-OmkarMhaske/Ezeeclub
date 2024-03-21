package com.tsysinfo.oneabove.alarm;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.tsysinfo.oneabove.FoodReminder;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.DietTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class AlarmReceiver extends BroadcastReceiver  {

    public static Context conn = null;
    DataBaseAdapter dba;
    String fullDate="";

    @Override
    public void onReceive(Context context, Intent arg1) {
        //	Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        this.conn = context;
        String formattedTime="",startdt="",nextdate="";
        Date date = new Date();  // to get the date
        SimpleDateFormat dfull = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        fullDate = dfull.format(date.getTime());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // getting date in this format
        formattedTime = df.format(date.getTime());
        try {
            Date datee = df.parse(formattedTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datee);
            calendar.add(Calendar.MINUTE, 15);
            startdt=df.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, 15);
            nextdate=df.format(calendar.getTime());
            System.out.println("Time here "+formattedTime+" newxt tme "+nextdate);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        String m="";
        final Date dateObj;
        dba= new DataBaseAdapter(conn);
        dba.open();
        String sql="select * from "+ DietTable.DATABASE_TABLE+ " where "+DietTable.KEY_TIME+" between '"+startdt+"' and '"+nextdate+"' and date='"+fullDate+"'";
        Cursor  cursor=DataBaseAdapter.ourDatabase.rawQuery(sql,null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
           @SuppressLint("Range") String datetime=cursor.getString(cursor.getColumnIndex("time"));
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                dateObj = sdf.parse(datetime);
                System.out.println(dateObj);
                m="Your Next Diet "+new SimpleDateFormat("K:mm a").format(dateObj)+" is "+cursor.getString(cursor.getColumnIndex("diet"))+" ";
                buildNotification(conn,m);
            } catch (final ParseException e) {
                e.printStackTrace();
            }



        }
        dba.close();


    }


    private void buildNotification(Context context,String msg) {


        Intent intent = new Intent(context, FoodReminder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        intent = new Intent(String.valueOf(FoodReminder.class));
        intent.putExtra("message", msg);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(FoodReminder.class);
        stackBuilder.addNextIntent(intent);
// PendingIntent pendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

// android.support.v4.app.NotificationCompat.BigTextStyle bigStyle = new     NotificationCompat.BigTextStyle();
// bigStyle.bigText((CharSequence) context);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Diet Alert")
                .setContentText(msg)
                .setAutoCancel(true)
                // .setStyle(new Notification.BigTextStyle().bigText(th_alert)  ตัวเก่า
                // .setStyle(new NotificationCompat.BigTextStyle().bigText(th_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentIntent(pendingIntent)
                .setNumber(1)
                .build();

        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.ring);
        notificationManager.notify(1000, notification);
















      /*  NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        Intent intent = new Intent(context, FoodReminder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setLights(Color.RED, 3000, 3000);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Diet Alert")
                .setContentText(msg)
                // .setContentInfo("ContentInfo")
                .setTicker("GMNC").setLights(0xFFFF0000, 500, 500)
                .setContentIntent(pendingIntent).setAutoCancel(true);

        Notification notification = builder.getNotification();

        notificationManager.notify(R.mipmap.ic_launcher, notification);*/
    }







}


