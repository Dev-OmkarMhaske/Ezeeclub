package com.tsysinfo.oneabove;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RulesActivity extends AppCompatActivity {
    TextView note;
    private SessionManager sessionManager;
    private JSONArray serverResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        sessionManager= new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        note=(TextView)findViewById(R.id.noti);
        setTitle("Rules");
        plan();

    }
    public void plan()
    {
        JSONObject param = new JSONObject();
        try{
            param.put("MemberNo",sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("test" , "inn thread method...");

        Webutil.getResponse(RulesActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetPlanDetails" , param.toString() , new MyHand());
    }
    class MyHand extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg!= null){
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity","Response1: "+response);

                try {
                    serverResponse = new JSONArray(response);

                    if(serverResponse != null){
                        Date date = new Date();  // to get the date
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
                        String formattedDate = df.format(date.getTime());

                        if (!serverResponse.getJSONObject(0).getString("MemberNo").equalsIgnoreCase("No")) {

                            // listView.setVisibility(View.VISIBLE);

                            for (int i=0;i<serverResponse.length();i++)
                            {
                                note.setText(serverResponse.getJSONObject(i).getString("PlanName").concat("  ").concat( serverResponse.getJSONObject(i).getString("StartDt")).concat("-").concat(serverResponse.getJSONObject(i).getString("EndDt")));
                                //PlanEntity planEntity= new PlanEntity();
                              /*  fromworkout.setText(serverResponse.getJSONObject(i).getString("StartDt").concat("-"));

                                toWorkout.setText(serverResponse.getJSONObject(i).getString("EndDt"));

                                PlanStart.setText(serverResponse.getJSONObject(i).getString("PlanName"));
*/

                                // mList.add(planEntity);

                            }



                            //mAdapter= new PlanAdapter(PalnDetailsActivity.this,mList);
                            //listView.setAdapter(mAdapter);


                        }else {
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
