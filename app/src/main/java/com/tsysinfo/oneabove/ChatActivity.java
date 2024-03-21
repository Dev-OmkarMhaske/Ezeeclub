package com.tsysinfo.oneabove;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.Chat.AndroidUtilities;
import com.tsysinfo.oneabove.Chat.App;
import com.tsysinfo.oneabove.Chat.ChatListAdapter;
import com.tsysinfo.oneabove.Chat.NotificationCenter;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.MessageTable;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.model.ChatMessage;
import com.tsysinfo.oneabove.model.Status;
import com.tsysinfo.oneabove.model.UserType;
import com.tsysinfo.oneabove.webUtil.Webutil;
import com.tsysinfo.oneabove.widgets.SizeNotifierRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements SizeNotifierRelativeLayout.SizeNotifierRelativeLayoutDelegate, NotificationCenter.NotificationCenterDelegate {
    private ListView chatListView;
    private EditText chatEditText1;
    private ArrayList<ChatMessage> chatMessages;
    private ImageView enterChatView1, emojiButton;
    private ChatListAdapter listAdapter;
    private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    private boolean showingEmoji;
    private int keyboardHeight;
    private boolean keyboardVisible;
    private WindowManager.LayoutParams windowLayoutParams;
    DataBaseAdapter dba;
    SessionManager sessionManager;
    private JSONArray serverResponse;
    private boolean errored = false;
    Models models;
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 10;

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                EditText editText = (EditText) v;
                if (v == chatEditText1) {
                    if (Connection.isOnline(ChatActivity.this)) {
                        sendMessage(editText.getText().toString(), UserType.SELF);
                    } else {
                        Toast.makeText(ChatActivity.this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
                    }
                }
                chatEditText1.setText("");
                return true;
            }
            return false;
        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == enterChatView1) {
                if (Connection.isOnline(ChatActivity.this)) {
                    sendMessage(chatEditText1.getText().toString(), UserType.SELF);
                } else {
                    Toast.makeText(ChatActivity.this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
                }
            }
            chatEditText1.setText("");
        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (chatEditText1.getText().toString().equals("")) {

            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                enterChatView1.setImageResource(R.drawable.ic_menu_send);
            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };

    private SizeNotifierRelativeLayout rootView;
    private String Member = "";
    private String msgnew = "";
    private String formattedDate = "";
    ChatMessage chtmsg;
    private TextView textViewDateHome;
    private String fdate = "";
    private SimpleDateFormat df;

    public void skype(String number, Context ctx) {
        try {
            //Intent sky = new Intent("android.intent.action.CALL_PRIVILEGED");
            //the above line tries to create an intent for which the skype app doesn't supply public api
            Intent skype_intent = new Intent("android.intent.action.VIEW");
            skype_intent.setData(Uri.parse("skype:" + number + ""));
            startActivity(skype_intent);
        } catch (ActivityNotFoundException e) {
            Log.e("SKYPE CALL", "Skype failed", e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        PopupWindow popupWindow = new PopupWindow(ChatActivity.this);
        popupWindow.showAtLocation(findViewById(R.id.root_view), Gravity.CENTER, 0, 0);
        Date date = new Date();  // to get the date
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat dfor = new SimpleDateFormat("EEEE - dd MMM");// getting date in this format
        formattedDate = df.format(date.getTime());
        fdate = dfor.format(date.getTime());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        AndroidUtilities.statusBarHeight = getStatusBarHeight();
        dba = new DataBaseAdapter(this);
        sessionManager = new SessionManager(this);
        Member = sessionManager.getAuthority();
        getSupportActionBar().setTitle(sessionManager.getName());
        models = new Models();
        chatMessages = new ArrayList<>();
        emojiButton = (ImageView) findViewById(R.id.emojiButton);
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        sizeNotifierRelativeLayout = (SizeNotifierRelativeLayout) findViewById(R.id.root_view);
        chatEditText1 = (EditText) findViewById(R.id.chat_edit_text1);
        enterChatView1 = (ImageView) findViewById(R.id.enter_chat1);
        textViewDateHome = (TextView) findViewById(R.id.textViewDate);
        textViewDateHome.setText("   " + fdate + "   ");
        threadGetMessage();
        // startTimer();
        //startAlert();
        callAsynchronousTask();

     /*   emojIcon = new EmojIconActions(this, sizeNotifierRelativeLayout, chatEditText1, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("", "Keyboard closed");
            }
        });
        // Hide the emoji on click of edit text
        chatEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        /*emojIcon.setUseSystemEmoji(false);*/
        listAdapter = new ChatListAdapter(chatMessages, this);
        chatListView.setAdapter(listAdapter);
        chatEditText1.setOnKeyListener(keyListener);
        enterChatView1.setOnClickListener(clickListener);
        chatEditText1.addTextChangedListener(watcher1);


        NotificationCenter.getInstance().addObserver(this, NotificationCenter.emojiDidLoaded);
        showMsg();
        dba.open();
        String sql = "update " + MessageTable.DATABASE_TABLE + " set " + MessageTable.KEY_STATUS + "='read' where " + MessageTable.KEY_STATUS + "='unread'";
        DataBaseAdapter.ourDatabase.execSQL(sql);
        sizeNotifierRelativeLayout.delegate = this;
        dba.close();
    }

    public void setDate(String date) {
        textViewDateHome.setText("   " + date + "   ");
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

    private void sendMessage(final String messageText, final UserType userType) {
        if (userType == UserType.SELF) {
            if (messageText.trim().length() == 0)
                return;
            final ChatMessage message = new ChatMessage();
            msgnew = messageText;
            chtmsg = message;
            message.setMessageStatus(Status.PENDING);
            message.setMessageText(messageText);
            message.setUserType(userType);
            Date dates = new Date();
            formattedDate = df.format(dates.getTime());
            message.setMessageTime(new Date().getTime());
            chatMessages.add(message);
            if (listAdapter != null)
                listAdapter.notifyDataSetChanged();
            //new AsyncCallWSSendMSG().execute();
            threadLogin();
        }
        // Mark message as delivered after one second

/*        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        exec.schedule(new Runnable(){
            @Override
            public void run(){
               message.setMessageStatus(Status.DELIVERED);

                final ChatMessage message = new ChatMessage();
                message.setMessageStatus(Status.SENT);
                message.setMessageText(messageText);
                message.setUserType(UserType.SELF);
                message.setMessageTime(new Date().getTime());
                chatMessages.add(message);

                ChatActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });


            }
        }, 1, TimeUnit.SECONDS);*/

    }

    private Activity getActivity() {
        return this;
    }

    /**
     * Check if the emoji popup is showing
     *
     * @return
     */
    public boolean isEmojiPopupShowing() {
        return showingEmoji;
    }

    /**
     * Updates emoji views when they are complete loading
     *
     * @param id
     * @param args
     */
    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.emojiDidLoaded) {
            if (chatListView != null) {
                chatListView.invalidateViews();
            }
        }
    }

    @Override
    public void onSizeChanged(int height) {
        Rect localRect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Activity.WINDOW_SERVICE);
        if (wm == null || wm.getDefaultDisplay() == null) {
            return;
        }
        if (height > AndroidUtilities.dp(50) && keyboardVisible) {
            keyboardHeight = height;
            App.getInstance().getSharedPreferences("emoji", 0).edit().putInt("kbd_height", keyboardHeight).commit();
        }
        if (showingEmoji) {
            int newHeight = 0;
            newHeight = keyboardHeight;
            if (windowLayoutParams.width != AndroidUtilities.displaySize.x || windowLayoutParams.height != newHeight) {
                windowLayoutParams.width = AndroidUtilities.displaySize.x;
                windowLayoutParams.height = newHeight;
                if (!keyboardVisible) {
                    sizeNotifierRelativeLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            if (sizeNotifierRelativeLayout != null) {
                                sizeNotifierRelativeLayout.setPadding(0, 0, 0, windowLayoutParams.height);
                                sizeNotifierRelativeLayout.requestLayout();
                            }
                        }
                    });
                }
            }
        }
        boolean oldValue = keyboardVisible;
        keyboardVisible = height > 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stoptimertask();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }

    /**
     * Get the system status bar height
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    private void threadLogin() {
        try {
            dba.open();
            JSONObject param = new JSONObject();
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
            param.put("EmployeeNo", "1");
            param.put("Message", msgnew);
            param.put("Date_Time", formattedDate);
            Webutil.getResponse(ChatActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "SaveMessage", param.toString(), new MyHandler());
            dba.close();
        } catch (Exception e) {
            Log.w("Chat Activity", "Timeout ");
        }
    }

    class MyHandler extends Handler {
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
                        dba.open();
                        Cursor cursor2 = DataBaseAdapter.ourDatabase.rawQuery("Select * from " + MessageTable.DATABASE_TABLE + " where time='" + formattedDate + "' and msgtext='" + msgnew + "'", null);
                        if (cursor2.getCount() == 0) {
                            if (serverResponse.getJSONObject(0).getString("ChangePassword").equals("Success")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MessageTable.KEY_MSG_TEXT, msgnew);
                                contentValues.put(MessageTable.KEY_DIETETIAN, 1);
                                contentValues.put(MessageTable.KEY_TIME, formattedDate);
                                contentValues.put(MessageTable.KEY_STATUS, "sent");
                                contentValues.put(MessageTable.KEY_USER_TYPE, "self");
                                dba.open();
                                models.insertdata(MessageTable.DATABASE_TABLE, contentValues);
                                chtmsg.setMessageStatus(Status.SENT);
                                listAdapter.notifyDataSetChanged();
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

    public void threadGetMessage() {
        try {
            dba.open();
            JSONObject param = new JSONObject();
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
            Webutil.getResponse(ChatActivity.this, "http://.ezeeclub.net/MobileAppService.svc/GetMessage", param.toString(), new MyHandlerGetMessage());
            showMsg();
            dba.close();
        } catch (Exception e) {
            Log.w("Chat Activity", "Timeout ");
        }
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
                            // Toast.makeText(ChatActivity.this, "No New Message", Toast.LENGTH_SHORT).show();
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
                                    listAdapter.notifyDataSetChanged();
                                    dba.close();
                                    showMsg();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*private class AsyncCallWSSendMSG extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // Call Web Method
            try {

                dba.open();
                serverResponse = WebService.SaveMessage(Member,"1",msg,formattedDate,sessionManager.getAuthority(),"SaveMessage","POST");
                dba.close();

            } catch (Exception e) {
                Log.w("Renew Plan Activity", "Timeout ");
            }
            return null;
        }

        @SuppressLint("ResourceAsColor")
        @SuppressWarnings("deprecation")
        @Override
        // Once WebService returns response
        protected void onPostExecute(Void result) {


            Log.w("DietActivity", "TimeOutFlag : " + WebService.timeoutFlag);
            Log.w("DietActivity", "ResponseString : "
                    + WebService.responseString);

            if (WebService.timeoutFlag == 1) {



            } else {
                try {

                    if (!errored) {
                        // Based on Boolean value returned from WebService
                        if (serverResponse != null) {
                            if (serverResponse.getJSONObject(0).getString("Status").equalsIgnoreCase("Success")) {
                                ContentValues contentValues= new ContentValues();
                                contentValues.put(MessageTable.KEY_MSG_TEXT,msg);
                                contentValues.put(MessageTable.KEY_DIETETIAN," ");
                                contentValues.put(MessageTable.KEY_TIME,formattedDate);
                                contentValues.put(MessageTable.KEY_STATUS,"sent");
                                contentValues.put(MessageTable.KEY_USER_TYPE,"self");
                                dba.open();
                                models.insertdata(MessageTable.DATABASE_TABLE,contentValues);
                                chtmsg.setMessageStatus(com.ezeeclub.ezeeclubmemberapp.model.Status.SENT);
                                listAdapter.notifyDataSetChanged();
                                dba.close();
                            }else {
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error Occured", Toast.LENGTH_LONG)
                                    .show();
                        }
                        // Error status is true
                    } else {
                        Toast.makeText(ChatActivity.this,
                                "Server Error", Toast.LENGTH_LONG).show();
                        // statusTV.setText("Error occured in invoking webservice");
                    }
                    // Re-initialize Error Status to False
                    errored = false;


                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        // Make Progress Bar visible
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }*/

    /*private class AsyncCallWSgetMSG extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // Call Web Method
            try {

                dba.open();


                serverResponse = WebService.SaveMessage(Member,"1",msg,formattedDate,sessionManager.getAuthority(),"GetMessage","POST");


                dba.close();

            } catch (Exception e) {
                Log.w("Renew Plan Activity", "Timeout ");
            }
            return null;
        }

        @SuppressLint("ResourceAsColor")
        @SuppressWarnings("deprecation")
        @Override
        // Once WebService returns response
        protected void onPostExecute(Void result) {


            Log.w("DietActivity", "TimeOutFlag : " + WebService.timeoutFlag);
            Log.w("DietActivity", "ResponseString : "
                    + WebService.responseString);

            if (WebService.timeoutFlag == 1) {



            } else {
                try {

                    if (!errored) {
                        // Based on Boolean value returned from WebService
                        if (serverResponse != null) {
                            if (serverResponse.getJSONObject(0).getString("MessageNo").equalsIgnoreCase("No")) {
                                Toast.makeText(ChatActivity.this, "No New Message", Toast.LENGTH_SHORT).show();

                            }else {
                                for (int i=0;i<serverResponse.length();i++) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(MessageTable.KEY_MSG_TEXT, serverResponse.getJSONObject(i).getString("Message"));
                                    contentValues.put(MessageTable.KEY_DIETETIAN,serverResponse.getJSONObject(i).getString("EmpName"));
                                    contentValues.put(MessageTable.KEY_TIME, serverResponse.getJSONObject(i).getString("SendDt"));
                                    contentValues.put(MessageTable.KEY_STATUS, "read");
                                    contentValues.put(MessageTable.KEY_USER_TYPE, "other");
                                    dba.open();
                                    models.insertdata(MessageTable.DATABASE_TABLE, contentValues);
                                    listAdapter.notifyDataSetChanged();
                                    dba.close();
                                    showMsg();
                                }
                            }


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error Occured", Toast.LENGTH_LONG)
                                    .show();
                        }
                        // Error status is true
                    } else {
                        Toast.makeText(ChatActivity.this,
                                "Server Error", Toast.LENGTH_LONG).show();
                        // statusTV.setText("Error occured in invoking webservice");
                    }
                    // Re-initialize Error Status to False
                    errored = false;


                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        // Make Progress Bar visible
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }*/

             /* public void startAlert(){


                     Intent intent = new Intent(this, MyBroadcastReceiver.class);
                     PendingIntent pendingIntent = PendingIntent.getBroadcast(
                             this.getApplicationContext(), 234324243, intent, 0);
                     AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                     alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                             + ( 1000), pendingIntent);
                     threadGetMessage();    

                 }
*/


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //threadGetMessage();
                            showMsg();
                            //Toast.makeText(ChatActivity.this, "Response ", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 10000, 10000); //execute in every 50000 ms
    }

   /* final Handler handler = new Handler();
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 10000, Your_X_SECS * 1000); //
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
                        //threadGetMessage();
                        showMsg();
                    }
                });
            }
        };
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            showMsg();
        }
        return super.onOptionsItemSelected(item);
    }
}