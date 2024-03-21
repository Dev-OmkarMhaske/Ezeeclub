package com.tsysinfo.oneabove.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import androidx.core.app.NotificationCompat;

import com.tsysinfo.oneabove.Chat.ChatListAdapter;
import com.tsysinfo.oneabove.ChatActivity;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.MessageTable;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.model.ChatMessage;
import com.tsysinfo.oneabove.model.Status;
import com.tsysinfo.oneabove.model.UserType;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChatService extends Service {

    Timer timer;
    private ArrayList<ChatMessage> chatMessages;
    TimerTask timerTask;
    String TAG = "Timers";
    private ChatListAdapter listAdapter;
    private ListView chatListView;
    int Your_X_SECS = 50;
    DataBaseAdapter dba;
    SessionManager sessionManager;
    private JSONArray serverResponse;
    Models models = new Models();



    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        dba=new DataBaseAdapter(ChatService.this);
        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 100000, Your_X_SECS * 10000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        threadGetMessage();
                       // showMsg();

                    }
                });
            }
        };

    }

    private void threadGetMessage() {
        sessionManager = new SessionManager(ChatService.this);
        try {
              dba.open();
            JSONObject param = new JSONObject();
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
            Webutil.getResponse(ChatService.this,"http://janorkarsgym.ezeeclub.net/MobileAppService.svc/GetMessage", param.toString(), new MyHandlerGetMessage());
            dba.close();
        } catch (Exception e) {
            Log.w("Chat Activity", "Timeout ");
        }
    }
    public long convertDate(String datetime) {
        long dateLong = 0;
        String dtStart = datetime;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date date = format.parse(dtStart);
            dateLong = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dateLong;
    }
    public void showMsg() {
        chatMessages.clear();
        dba.open();
        String sql = "Select * from  " + MessageTable.DATABASE_TABLE + " ";
        Cursor cursor = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ChatMessage chatMessage = new ChatMessage();
                String st = cursor.getString(cursor.getColumnIndex(MessageTable.KEY_STATUS));
                if (st.equalsIgnoreCase("sent")) {
                    chatMessage.setMessageStatus(Status.SENT);
                } else {
                    chatMessage.setMessageStatus(Status.PENDING);
                }
                chatMessage.setMessageText(cursor.getString(cursor.getColumnIndex(MessageTable.KEY_MSG_TEXT)));
                chatMessage.setMessageTime(convertDate(cursor.getString(cursor.getColumnIndex(MessageTable.KEY_TIME))));
                String usert = cursor.getString(cursor.getColumnIndex(MessageTable.KEY_USER_TYPE));
                if (usert.equalsIgnoreCase("self")) {
                    chatMessage.setUserType(UserType.SELF);
                } else {
                    chatMessage.setUserType(UserType.OTHER);
                }
                chatMessages.add(chatMessage);
            }
            listAdapter = new ChatListAdapter(chatMessages, this);
            chatListView.setAdapter(listAdapter);
            chatListView.smoothScrollToPosition(listAdapter.getCount() - 1);
        }
        dba.close();
    }


    class MyHandlerGetMessage extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("ChatActivity", "Response1: " + response);
                try {
                    serverResponse = new JSONArray(response);
                    if (serverResponse != null) {
                        if (serverResponse.getJSONObject(0).getString("MessageNo").equalsIgnoreCase("No")) {
                            //Toast.makeText(ChatService.this, "No New Message", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < serverResponse.length(); i++) {
                                dba.open();
                                Cursor cursor2 = DataBaseAdapter.ourDatabase.rawQuery("Select * from " + MessageTable.DATABASE_TABLE + " where time='" + serverResponse.getJSONObject(i).getString("SendDt") + "' and msgtext='" + serverResponse.getJSONObject(i).getString("Message") + "'", null);
                                if (cursor2.getCount() == 0) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(MessageTable.KEY_MSG_TEXT, serverResponse.getJSONObject(i).getString("Message"));
                                    contentValues.put(MessageTable.KEY_DIETETIAN, serverResponse.getJSONObject(i).getString("EmpName"));
                                    contentValues.put(MessageTable.KEY_TIME, serverResponse.getJSONObject(i).getString("SendDt"));
                                    contentValues.put(MessageTable.KEY_STATUS, "read");
                                    contentValues.put(MessageTable.KEY_USER_TYPE, "other");
                                    dba.open();
                                    models.insertdata(MessageTable.DATABASE_TABLE, contentValues);
                                    dba.close();
                                    showNotification();
                                    //Toast.makeText(ChatService.this,"notification recieved",Toast.LENGTH_LONG).show();
                                    //showMsg();
                                }
                                dba.close();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

        public void showNotification() {
              dba.open();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String CHANNEL_ID = "default_channnel_id";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int NOTIFICATION_ID = 234;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                if (mChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                   // mChannel.setSound(soundUri, attributes);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(true);
                    notificationManager.createNotificationChannel(mChannel);
                }
            }
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("Message", MessageTable.KEY_MSG_TEXT);
            //intent.putExtra("Courtname", CourtName);

            final int requestCode = (int) System.currentTimeMillis() / 1000;
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ChatService.this, CHANNEL_ID)
                    .setContentTitle("Alert for " + "EzeeClubMemberApp")
                    .setContentText(MessageTable.KEY_MSG_TEXT)
                    .setSmallIcon(R.drawable.above)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.above))
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    //.setSound(soundUri)
                    .setColor(getResources().getColor(R.color.colorPrimary));

            if (notificationManager != null) {
                notificationManager.notify(requestCode, builder.build());
            }
          dba.close();
        }

    }



