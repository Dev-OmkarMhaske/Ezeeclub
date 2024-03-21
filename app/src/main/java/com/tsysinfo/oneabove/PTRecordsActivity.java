package com.tsysinfo.oneabove;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.ui.MainActivity;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PTRecordsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    TextView tvDate, tvMemberName, tvPlanName, tvSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_records);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("PT Records");
        sessionManager = new SessionManager(this);

        Button btAccept = findViewById(R.id.btAccept);
        Button btReject = findViewById(R.id.btReject);
        tvDate = findViewById(R.id.tvDate);
        tvMemberName = findViewById(R.id.tvMemberName);
        tvSessions = findViewById(R.id.tvSessions);
        tvPlanName = findViewById(R.id.tvPlanName);

        Date date = new Date();  // to get the date
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy"); // getting date in this format
        String formattedDate = df.format(date.getTime());

        tvDate.setText(formattedDate);
        tvMemberName.setText(sessionManager.getName());
        tvSessions.setText("1");

        btAccept.setOnClickListener(view -> {
            makeCall("Approved");
        });

        btReject.setOnClickListener(view -> {
            makeCall("Rejected");
        });
        if (Connection.isOnline(this)) {
            getPlan();
        } else {
            Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

        }

    }

    private void getPlan() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(PTRecordsActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetPlanDetails", param.toString(), new MyHandler());
    }

    String status = "Approved";
    private void makeCall(String status) {
        JSONObject param = new JSONObject();
        try {
            this.status = status;
            param.put("RecNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
            param.put("Status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(PTRecordsActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "ApprovePTSessions",
                param.toString(), new PTHandler());
    }

    class PTHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Log.d("test", "response in handler PT....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("PTACTIVITY", "Response1: " + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.getJSONObject(0).getString("ChangePassword").equals("Success")) {
                        new AlertDialog.Builder(PTRecordsActivity.this)
                                .setTitle("Alert")
                                .setMessage("Your Request "+status+" Successfully")
                                .setCancelable(false)
                                .setPositiveButton("ok", (dialog, which) -> {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }).show();

                    } else {
                        Toast.makeText(PTRecordsActivity.this, "Action failed.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity", "Response1: " + response);

                try {
                    JSONArray serverResponse = new JSONArray(response);

                    if (serverResponse != null) {
                        if (!serverResponse.getJSONObject(0).getString("MemberNo").equalsIgnoreCase("No")) {
                            int i = 0;
                            tvPlanName.setText(serverResponse.getJSONObject(i).getString("PlanName"));
                        }                    }

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
