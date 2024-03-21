package com.tsysinfo.oneabove;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.Adapters.DietAdapterOr;
import com.tsysinfo.oneabove.Utils.DietEntity;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.DietTable;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FoodReminder extends AppCompatActivity {
    private ListView listView;
    DataBaseAdapter dba;
    Models mod;
    private ArrayList<DietEntity> mList;
    private DietAdapterOr mAdapter;
    private SessionManager sessionManager;
    TextView textView,note;

    private JSONArray serverResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_reminder);
        sessionManager= new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView=(ListView)findViewById(R.id.listView);
        textView=(TextView)findViewById(R.id.textView6);
        note=(TextView)findViewById(R.id.noti);
        setTitle("Diet");
        dba= new DataBaseAdapter(this);
        mod= new Models();
        mList = new ArrayList<>();
        plan();
        dba.open();
        Date date = new Date();  // to get the date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US); // getting date in this format
        String formattedDate = df.format(date.getTime());
        String sq="Select * from "+ DietTable.DATABASE_TABLE+" where "+DietTable.KEY_Date+"='"+formattedDate+"'";
        Cursor cursor=DataBaseAdapter.ourDatabase.rawQuery(sq,null);
        if (cursor.getCount()>0)
        {
            while (cursor.moveToNext()) {
                DietEntity l = new DietEntity();
                l.setId(cursor.getString(0));
                l.setDiet(cursor.getString(2));
                l.setTime(cursor.getString(1));
                mList.add(l);
            }
            mAdapter= new DietAdapterOr(FoodReminder.this,mList);
            listView.setAdapter(mAdapter);
        }else {
            Toast.makeText(this, "No Diet Found for Today....Please Sync Diet or Ask for Dietitian", Toast.LENGTH_LONG).show();
        }
        dba.close();

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

        Webutil.getResponse(FoodReminder.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetPlanDetails" , param.toString() , new MyHand());
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
