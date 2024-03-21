package com.tsysinfo.oneabove;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

public class HealthActivity extends AppCompatActivity {
    DataBaseAdapter dba;
    Models mod;
    TextView counsellor, note;
    JSONArray serverResponse;
    ProgressDialog waitDialog;
    SessionManager sessionManager;
    EditText textDate;
    TextView spinner;
    EditText textBPPulse;
    EditText textSugar;
    EditText textCardiacHistory;
    EditText textThyroidFunction;
    EditText textGynaecologicalProblems;
    EditText textSurgeries;
    EditText textAlimentary;
    EditText textDuetoInjury;
    EditText textWhen;
    EditText textMajorInjuries;
    EditText textMedical;
    EditText textMedicalWhen;
    EditText textSurgical;
    EditText textSurgicalWhen;
    EditText textPast;
    EditText textPresent;
    EditText textFollowUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        counsellor = (TextView) findViewById(R.id.spinner);
        note = (TextView) findViewById(R.id.noti);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        waitDialog = new ProgressDialog(this);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.setMessage("Please Wait");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /** modified omkar 16.01.24 START */

        textDate = findViewById(R.id.textDate);
        spinner = findViewById(R.id.spinner);
        textBPPulse = findViewById(R.id.textBPPulse);
        textSugar = findViewById(R.id.textSugar);
        textCardiacHistory = findViewById(R.id.textCardiacHistory);
        textThyroidFunction = findViewById(R.id.textThyroidFunction);
        textGynaecologicalProblems = findViewById(R.id.textGynaecologicalProblems);
        textSurgeries = findViewById(R.id.textSurgeries);
        textAlimentary = findViewById(R.id.textAlimentary);
        textDuetoInjury = findViewById(R.id.textDuetoInjury);
        textWhen = findViewById(R.id.textWhen);
        textMajorInjuries = findViewById(R.id.textMajorInjuries);
        textMedical = findViewById(R.id.textMedical);
        textMedicalWhen = findViewById(R.id.textMedicalWhen);
        textSurgical = findViewById(R.id.textSurgical);
        textSurgicalWhen = findViewById(R.id.textSurgicalWhen);
        textPast = findViewById(R.id.textPast);
        textPresent = findViewById(R.id.textPresent);
        textFollowUp = findViewById(R.id.textFollowUp);

        /**  END */

        setTitle("Health");
        if (Connection.isOnline(this)) {
            plan();
            threadLogin();
        } else {
            Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

        }

    }

    private void threadLogin() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(HealthActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetHealthDetails", param.toString(), new MyHandler());
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
                    serverResponse = new JSONArray(response);

                    if (serverResponse != null) {


                        if (!serverResponse.getJSONObject(0).getString("HealthDetailNo").equalsIgnoreCase("No")) {

                            textDate.setText(serverResponse.getJSONObject(0).getString("HealthDetailDate"));
                            spinner.setText(serverResponse.getJSONObject(0).getString("Trainer"));
                            textBPPulse.setText(serverResponse.getJSONObject(0).getString("BPPulse"));
                            textSugar.setText(serverResponse.getJSONObject(0).getString("Sugar"));
                            textCardiacHistory.setText(serverResponse.getJSONObject(0).getString("CardiacHistory"));
                            textThyroidFunction.setText(serverResponse.getJSONObject(0).getString("ThyroidFunction"));
                            textGynaecologicalProblems.setText(serverResponse.getJSONObject(0).getString("GynaecologicalProblems"));
                            textSurgeries.setText(serverResponse.getJSONObject(0).getString("Surgeries"));
                            textAlimentary.setText(serverResponse.getJSONObject(0).getString("AlimentaryTestAilments"));
                            textDuetoInjury.setText(serverResponse.getJSONObject(0).getString("DuetoInjury"));
                            textWhen.setText(serverResponse.getJSONObject(0).getString("DuetoInjuryWhen"));
                            textMajorInjuries.setText(serverResponse.getJSONObject(0).getString("MajorInjuries"));
                            textMedical.setText(serverResponse.getJSONObject(0).getString("Medical"));
                            textMedicalWhen.setText(serverResponse.getJSONObject(0).getString("MedicalWhen"));
                            textSurgical.setText(serverResponse.getJSONObject(0).getString("Surgical"));
                            textSurgicalWhen.setText(serverResponse.getJSONObject(0).getString("SurgicalWhen"));
                            textPast.setText(serverResponse.getJSONObject(0).getString("Past"));
                            textPresent.setText(serverResponse.getJSONObject(0).getString("Present"));
                            textFollowUp.setText(serverResponse.getJSONObject(0).getString("FollowUp"));
                        } else {
                            Toast.makeText(HealthActivity.this, "No Health Details Found", Toast.LENGTH_SHORT).show();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void plan() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(HealthActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetPlanDetails", param.toString(), new MyHand());
    }

    class MyHand extends Handler {

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

                        if (!serverResponse.getJSONObject(0).getString("MemberNo").equalsIgnoreCase("No")) {

                            // listView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < serverResponse.length(); i++) {
                                note.setText(serverResponse.getJSONObject(i).getString("PlanName").concat("  ").concat(serverResponse.getJSONObject(i).getString("StartDt")).concat("-").concat(serverResponse.getJSONObject(i).getString("EndDt")));
                                //PlanEntity planEntity= new PlanEntity();
                              /*  fromworkout.setText(serverResponse.getJSONObject(i).getString("StartDt").concat("-"));

                                toWorkout.setText(serverResponse.getJSONObject(i).getString("EndDt"));

                                PlanStart.setText(serverResponse.getJSONObject(i).getString("PlanName"));
*/

                                // mList.add(planEntity);

                            }


                            //mAdapter= new PlanAdapter(PalnDetailsActivity.this,mList);
                            //listView.setAdapter(mAdapter);


                        } else {
                            //listView.setVisibility(View.GONE);
                            //textView.setVisibility(View.VISIBLE);

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
