package com.tsysinfo.oneabove;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.Adapters.PlanAdapter;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class PalnDetailsActivity extends AppCompatActivity {
    private ListView listView;
    DataBaseAdapter dba;
    Models mod;
    private ArrayList<PlanEntity> mList;
    private PlanAdapter mAdapter;
    private SessionManager sessionManager;
    private JSONArray serverResponse;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_reminder);
        sessionManager= new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        textView=(TextView)findViewById(R.id.textView6);
        listView=(ListView)findViewById(R.id.listView);
        dba= new DataBaseAdapter(this);
        mod= new Models();
        setTitle("Plan details");
        mList = new ArrayList<>();



        if (Connection.isOnline(this))
        {
            threadLogin();
        }else {
            Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

        }

    }
    private void threadLogin(){
        JSONObject param = new JSONObject();
        try{
            param.put("MemberNo",sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("test" , "inn thread method...");

        Webutil.getResponse(PalnDetailsActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetPlanDetails" , param.toString() , new MyHandler());
    }




    class MyHandler extends Handler {

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

                            listView.setVisibility(View.VISIBLE);

                            for (int i=0;i<serverResponse.length();i++)
                        {


                           PlanEntity planEntity= new PlanEntity();
                            planEntity.setActive(serverResponse.getJSONObject(i).getString("Active"));
                            planEntity.setBalanceAmount(serverResponse.getJSONObject(i).getString("BalanceAmount"));
                            planEntity.setDurationdays(serverResponse.getJSONObject(i).getString("Durationdays"));
                            planEntity.setDurationMonth(serverResponse.getJSONObject(i).getString("DurationMonth"));
                            planEntity.setEndDt(serverResponse.getJSONObject(i).getString("EndDt"));
                            planEntity.setPaidAmount(serverResponse.getJSONObject(i).getString("PaidAmount"));
                            planEntity.setPlanName(serverResponse.getJSONObject(i).getString("PlanName"));
                            planEntity.setProgramName(serverResponse.getJSONObject(i).getString("ProgramName"));
                            planEntity.setSaleAmount(serverResponse.getJSONObject(i).getString("SaleAmount"));
                            planEntity.setStartDt(serverResponse.getJSONObject(i).getString("StartDt"));

                            mList.add(planEntity);

                        }



                        mAdapter= new PlanAdapter(PalnDetailsActivity.this,mList);
                        listView.setAdapter(mAdapter);


                        }else {
                            listView.setVisibility(View.GONE);
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
