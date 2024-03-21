package com.tsysinfo.oneabove;

import static com.tsysinfo.oneabove.R.drawable.green;
import static com.tsysinfo.oneabove.R.drawable.orange;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    private int mYear;
    private int mMonth;
    private int mDay;
    Calendar calendar;
    CalendarView calendarView;
    private SessionManager sessionManager;
    private JSONArray serverResponse;
    private RewardAdapter mAdapter;
    List<EventDay> events;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sessionManager = new SessionManager(this);
        note = (TextView) findViewById(R.id.noti);
        calendarView = findViewById(R.id.calendarView);
        // green color for the week separator line
        events = new ArrayList<>();
        setTitle("Calender");
        if (Connection.isOnline(this)) {
           // threadGetBranch();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void threadGetBranch() {

        Date date = new Date();  // to get the date
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(date.getTime());
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(mYear, mMonth, mDay);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
            param.put("Month", mMonth);
            param.put("Year", mYear);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");
        Webutil.getResponse(CalenderActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetCalendarDetails", param.toString(), new BranchHandler());
    }

    class BranchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {

                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity", "Response1: " + response);
                try {
                    serverResponse = new JSONArray(response);
                    if (serverResponse != null) {
                        if (!serverResponse.getJSONObject(0).getString("Attachment").equalsIgnoreCase("No")) {
                            for (int i = 0; i < serverResponse.length(); i++) {
                                calendar = Calendar.getInstance();
                                mYear = Integer.parseInt(serverResponse.getJSONObject(i).getString("MessageNo").trim().substring(6,10));
                                mMonth = Integer.parseInt(serverResponse.getJSONObject(i).getString("MessageNo").trim().substring(0,2))-1;
                                mDay = Integer.parseInt(serverResponse.getJSONObject(i).getString("MessageNo").trim().substring(3,5));
                                calendar.set(mYear, mMonth, mDay);
                                if(serverResponse.getJSONObject(i).getString("Message").toString()=="Yes"){
                                    events.add(new EventDay(calendar,orange));
                                }else{
                                    events.add(new EventDay(calendar, green));
                                }
                            }
                            calendarView.setEvents(events);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}