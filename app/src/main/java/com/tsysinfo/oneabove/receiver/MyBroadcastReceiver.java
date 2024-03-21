package com.tsysinfo.oneabove.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, ChatService.class);
        context.startService(i);

       /* i = new Intent(context, ChatActivity.class);
        context.startService(i);
*/


    }
}