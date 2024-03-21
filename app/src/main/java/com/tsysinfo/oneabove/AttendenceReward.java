package com.tsysinfo.oneabove;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AttendenceReward extends AppCompatActivity {
    DataBaseAdapter dba;
    SessionManager session;
    ProgressDialog waitDialog;
    JSONArray serverResponse;
    // ListView listView;
    GridView grid;
    TextView textView6;
    private ArrayList<Rewards> mList;
    private RewardAdapter mAdapter;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_reward);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        grid = findViewById(R.id.grid);
        textView6 = findViewById(R.id.textView6);

        dba = new DataBaseAdapter(this);
        waitDialog = new ProgressDialog(this);
        waitDialog.setMessage("Please Wait...");
        session = new SessionManager(this);
        mList = new ArrayList<>();

        setTitle("Attendence Rewards");

        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);

        if (Connection.isOnline(this)) {
            threadGetBranch();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void threadGetBranch() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", session.getAuthority());
            param.put("BranchNo", session.getBr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(AttendenceReward.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetAttendanceRewards", param.toString(), new BranchHandler());
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
                        Date date = new Date();  // to get the date
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
                        String formattedDate = df.format(date.getTime());

                        if (!serverResponse.getJSONObject(0).getString("Attachment").equalsIgnoreCase("No")) {
                            grid.setVisibility(View.VISIBLE);
                            for (int i = 0; i < serverResponse.length(); i++) {
                                Rewards rewards = new Rewards();
                                rewards.setMessage(serverResponse.getJSONObject(i).getString("Message"));
                                rewards.setMessageNo(serverResponse.getJSONObject(i).getString("MessageNo"));
                                rewards.setReplyMessage(serverResponse.getJSONObject(i).getString("ReplyMessage"));
                                rewards.setStatus(serverResponse.getJSONObject(i).getString("Status"));

                                mList.add(rewards);
                            }
                            mAdapter = new RewardAdapter(AttendenceReward.this, mList);
                            grid.setAdapter(mAdapter);
                        } else {
                            grid.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
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
