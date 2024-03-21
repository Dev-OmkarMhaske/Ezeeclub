package com.tsysinfo.oneabove;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.tsysinfo.oneabove.Adapters.NotificationAdapter;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.NotificationTable;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class NotificationActivity extends AppCompatActivity {
    private ListView listView;
    DataBaseAdapter dba;
    Models mod;
    private ArrayList<NotoEntity> mList;
    private NotificationAdapter mAdapter;
    private SessionManager sessionManager;
    TextView textView,note;
    private JSONArray serverResponse;
    private Models models;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        sessionManager= new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView=(ListView)findViewById(R.id.listView);
        note=(TextView)findViewById(R.id.noti);
        textView=(TextView)findViewById(R.id.textView6);
        dba= new DataBaseAdapter(this);
        models= new Models();
        mod= new Models();
        mList = new ArrayList<>();
        setTitle("Notification");

        plan();
        setList();


    }
    private void notification(){
        JSONObject param = new JSONObject();
        try{
            param.put("MemberNo",sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("test" , "inn thread method...");

        Webutil.getResponse(NotificationActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext()) +"GetNotification" , param.toString() , new MyNotifyHandler());
    }
    class MyNotifyHandler extends Handler {

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

                        if (!serverResponse.getJSONObject(0).getString("SendTo").equalsIgnoreCase("No"))
                        {
                            for (int i=0;i<serverResponse.length();i++)
                            {
                                ContentValues contentValues= new ContentValues();
                                contentValues.put(NotificationTable.KEY_Date,serverResponse.getJSONObject(i).getString("Date"));
                                contentValues.put(NotificationTable.KEY_Noti,serverResponse.getJSONObject(i).getString("Notifi"));
                                contentValues.put(NotificationTable.KEY_STATUS,"Unread");
                                dba.open();
                                models.insertdata(NotificationTable.DATABASE_TABLE,contentValues);
                                dba.close();

                            }
                            setList();

                        }else {


                        }

                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
 public void setList()
 {
    dba.open();
    mList.clear();
    Date date = new Date();  // to get the date
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
    String formattedDate = df.format(date.getTime());
    String sq="Select * from "+ NotificationTable.DATABASE_TABLE+ " order by "+NotificationTable.KEY_Date+" DESC";
    Cursor cursor=DataBaseAdapter.ourDatabase.rawQuery(sq,null);
    if (cursor.getCount()>0)
    {

        while (cursor.moveToNext()) {
            NotoEntity l = new NotoEntity();
            l.setId(cursor.getString(0));
            l.setNoti(cursor.getString(1));
            l.setStatus(cursor.getString(2));
            l.setTime(cursor.getString(3));

            mList.add(l);
        }
        mAdapter= new NotificationAdapter(NotificationActivity.this,mList);
        listView.setAdapter(mAdapter);
    }else {
        listView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Notification Not Available..", Toast.LENGTH_SHORT).show();
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

        Webutil.getResponse(NotificationActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetPlanDetails" , param.toString() , new MyHand());
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
        getMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
         if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.refresh) {
            if (Connection.isOnline(this)) {
                notification();

            }else {
                Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
