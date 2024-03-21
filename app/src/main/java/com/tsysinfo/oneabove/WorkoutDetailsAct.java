package com.tsysinfo.oneabove;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.Adapters.DietAdapter;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.database.WorkoutTable;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class WorkoutDetailsAct extends AppCompatActivity {
    private ListView listView;
    DataBaseAdapter dba;
    Models mod;
    private ArrayList<entityWorkout> mList;
    private DietAdapter mAdapter;

    private SessionManager sessionManager;
    private JSONArray serverResponse;
    private TextView textView,tim,warms,wat,timecardcooldown,cooldownText,cooldownedit;
    static EditText DateEdit;
    private int mYear;
    private int mMonth;
    private int mDay;
    TextView note;
    private String mmonth = "",Clientid;
    Button b1,b2;
    TextView Datetext;
    Calendar calendar;

    //TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_act);

        timecardcooldown=(TextView)findViewById(R.id.timecardcooldown);
        cooldownText=(TextView)findViewById(R.id.cooldownText);
        cooldownedit=(TextView)findViewById(R.id.cooldownedit);

        wat=(TextView)findViewById(R.id.war);
        tim=(TextView)findViewById(R.id.time2);
        warms=(TextView)findViewById(R.id.warmup2);
        sessionManager = new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView6);
        textView.setText("No Workout Found");
        b1 = (Button) findViewById(R.id.textView2);
        note=(TextView)findViewById(R.id.noti);
        b2 = (Button) findViewById(R.id.Next);
        Datetext = (TextView) findViewById(R.id.date);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        mList = new ArrayList<>();
        setTitle("Workout");

        plan();
        //threadLogin();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mList.clear();
                if (getApplicationContext() != null) {
                    ArrayList<entityWorkout> ReceiptSearchResultsPrevious =GeatresultsPrevious();
                    DietAdapter dietAdapter = new DietAdapter(WorkoutDetailsAct.this, ReceiptSearchResultsPrevious);
                    listView.setAdapter(dietAdapter);
                    listView.setFastScrollEnabled(false);
                    listView.setVerticalScrollBarEnabled(false);
                    listView.setScrollContainer(false);

                    //final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    //int pixels = (int) (listView.getHeight() * scale + 0.5f);
                    int totalHeight = 0;
                    for (int size = 0; size < dietAdapter.getCount(); size++) {
                        View listItem = dietAdapter.getView(size, null, listView);
                        if (listItem != null) {
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                    }
                    // setting listview item in adapter
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popaplayout);
                    rl.getLayoutParams().height = totalHeight + (listView.getDividerHeight() * (dietAdapter.getCount() - 1));
                }


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.clear();

                if (getApplicationContext() != null) {
                    ArrayList<entityWorkout> ReceiptSearchResultsNext =GeatresultsNext();
                    DietAdapter dietAdapter = new DietAdapter(WorkoutDetailsAct.this, ReceiptSearchResultsNext);
                    listView.setAdapter(dietAdapter);
                    listView.setFastScrollEnabled(false);
                    listView.setVerticalScrollBarEnabled(false);
                    listView.setScrollContainer(false);

                    //final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    //int pixels = (int) (listView.getHeight() * scale + 0.5f);
                    int totalHeight = 0;
                    for (int size = 0; size < dietAdapter.getCount(); size++) {
                        View listItem = dietAdapter.getView(size, null, listView);
                        if (listItem != null) {
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                    }
                    // setting listview item in adapter
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.popaplayout);
                    rl.getLayoutParams().height = totalHeight + (listView.getDividerHeight() * (dietAdapter.getCount() - 1));
                }
            }
        });

        //threadLogin();

        if (getApplicationContext() != null) {
            ArrayList<entityWorkout> ReceiptSearchResults = Geatresultsdisplay();
            DietAdapter dietAdapter = new DietAdapter(WorkoutDetailsAct.this, ReceiptSearchResults);
            listView.setAdapter(dietAdapter);
            listView.setFastScrollEnabled(false);
            listView.setVerticalScrollBarEnabled(false);
            listView.setScrollContainer(false);

            //final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            //int pixels = (int) (listView.getHeight() * scale + 0.5f);
            int totalHeight = 0;
            for (int size = 0; size < dietAdapter.getCount(); size++) {
                View listItem = dietAdapter.getView(size, null, listView);
                if (listItem != null) {
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            // setting listview item in adapter
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.popaplayout);
            rl.getLayoutParams().height = totalHeight + (listView.getDividerHeight() * (dietAdapter.getCount() - 1));
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

        Webutil.getResponse(WorkoutDetailsAct.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetWorkout" , param.toString() , new MyHandler());
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg!= null){
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity","Response1: "+response);
                dba.open();
                mod.clearDatabase(WorkoutTable.DATABASE_TABLE);
                dba.close();

                try {
                    serverResponse = new JSONArray(response);

                    if(serverResponse != null){

                        ContentValues contentValues = new ContentValues();

                        if (!serverResponse.getJSONObject(0).getString("WorkoutNo").equalsIgnoreCase("No"))

                        if (serverResponse != null && serverResponse.length() > 0) {
                            if (serverResponse.getJSONObject(0).getString("WorkoutNo").trim()
                                    .equalsIgnoreCase("No")) {
                            } else {
                                for (int i = 0; i < serverResponse.length(); i++) {
                                    dba.open();
                                        if (!serverResponse.getJSONObject(i).getString("Day").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_TIME, serverResponse.getJSONObject(i).getString("Day").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_TIME, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("BodyPart").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_MSG_TEXT, serverResponse.getJSONObject(i).getString("BodyPart").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_MSG_TEXT, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("Remark").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_DIETETIAN, serverResponse.getJSONObject(i).getString("Remark").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_DIETETIAN, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("reps").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_REPS, serverResponse.getJSONObject(i).getString("reps").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_REPS, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("sets").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_STATUS, serverResponse.getJSONObject(i).getString("sets").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_STATUS, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("AreaOfConcern").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_AREA, serverResponse.getJSONObject(i).getString("AreaOfConcern").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_AREA, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("BranchNo").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_BRANCH, serverResponse.getJSONObject(i).getString("BranchNo").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_BRANCH, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("Cooldown").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_COOLDOWN, serverResponse.getJSONObject(i).getString("Cooldown").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_COOLDOWN, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("Instructor").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_INSRUCTOR, serverResponse.getJSONObject(i).getString("Instructor").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_INSRUCTOR, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("InstructorNo").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_INSRUCTOR_NO, serverResponse.getJSONObject(i).getString("InstructorNo").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_INSRUCTOR_NO, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("MemberNo").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_MEMBER_NO, serverResponse.getJSONObject(i).getString("MemberNo").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_MEMBER_NO, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("PlanEnd").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_PLAN_END, serverResponse.getJSONObject(i).getString("PlanEnd").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_PLAN_END, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("PlanSelectionNo").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_PLAN_SELECTION_NO, serverResponse.getJSONObject(i).getString("PlanSelectionNo").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_PLAN_SELECTION_NO, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("PlanStart").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_PLAN_START, serverResponse.getJSONObject(i).getString("PlanStart").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_PLAN_START, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("Warmup").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_WARM_UP, serverResponse.getJSONObject(i).getString("Warmup").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_WARM_UP, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("WorkoutFrom").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_WORKOUT_FROM, serverResponse.getJSONObject(i).getString("WorkoutFrom").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_WORKOUT_FROM, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("WorkoutNo").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_WORKOUT_NO, serverResponse.getJSONObject(i).getString("WorkoutNo").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_WORKOUT_NO, "");
                                        }
                                        if (!serverResponse.getJSONObject(i).getString("WorkoutTo").trim().equalsIgnoreCase("null")) {
                                            contentValues.put(WorkoutTable.KEY_WORKOUT_TO, serverResponse.getJSONObject(i).getString("WorkoutTo").trim());
                                        } else {
                                            contentValues.put(WorkoutTable.KEY_WORKOUT_TO, "");
                                        }
                                    if (!serverResponse.getJSONObject(i).getString("Excersize").trim().equalsIgnoreCase("null")) {
                                        contentValues.put(WorkoutTable.KEY_EXERCISE, serverResponse.getJSONObject(i).getString("Excersize").trim());
                                    } else {
                                        contentValues.put(WorkoutTable.KEY_EXERCISE, "");
                                    }
                                    dba.open();
                                        mod.insertdata(WorkoutTable.DATABASE_TABLE, contentValues);
                                        dba.close();

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


    ArrayList<entityWorkout> Geatresultsdisplay() {
         ArrayList<entityWorkout> results = new ArrayList<entityWorkout>();

        Date date = new Date();  // to get the date
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(date.getTime());


        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(mYear, mMonth, mDay);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Datetext.setText(formattedDate);

        entityWorkout sr = new entityWorkout();
        try {
            dba.open();
            String sql = "select * from "+ WorkoutTable.DATABASE_TABLE+ " where time='"+dayOfWeek+"'";
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new entityWorkout();
                    tim.setText(formattedDate);
                    timecardcooldown.setText(formattedDate);
                    warms.setText(cur.getString(cur.getColumnIndex("warm_up")));
                    wat.setText("Warm up :");
                    cooldownedit.setText(cur.getString(cur.getColumnIndex("cooldown")));
                    cooldownText.setText("Cool Down :");
                    sr.setTime(formattedDate);
                    sr.setBodypart(cur.getString(cur.getColumnIndex("msgtext")));
                    sr.setWarmup(cur.getString(cur.getColumnIndex("warm_up")));
                    sr.setRemark(cur.getString(cur.getColumnIndex("dietetian")));
                    sr.setExercise(cur.getString(cur.getColumnIndex("exercise")));
                    sr.setReps(cur.getString(cur.getColumnIndex("reps")));
                    sr.setSets(cur.getString(cur.getColumnIndex("status")));
                    results.add(sr);
                }
            }  else {
                tim.setText("");
                warms.setText("");
                wat.setText("");
                cooldownText.setText(" ");
                cooldownedit.setText("");
                timecardcooldown.setText("");
                listView.setAdapter(null);
                Toast.makeText(WorkoutDetailsAct.this, "No Data", Toast.LENGTH_LONG).show();
            }
            dba.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       return  results;
    }
    ArrayList<entityWorkout> GeatresultsPrevious() {
        ArrayList<entityWorkout> results = new ArrayList<entityWorkout>();

        Date date = new Date();  // to get the date
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate1 = df.format(date.getTime());

        calendar.add(Calendar.DATE, -1);
        formattedDate1 = df.format(calendar.getTime());
        Log.v("PREVIOUS DATE : ", formattedDate1);
        Datetext.setText(formattedDate1);

        calendar.setTime(calendar.getTime());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Datetext.setText(formattedDate1);

        entityWorkout sr = new entityWorkout();
        try {
            dba.open();
            String sql = "select * from "+ WorkoutTable.DATABASE_TABLE+ " where time='"+dayOfWeek+"'";
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new entityWorkout();
                    tim.setText(formattedDate1);
                    timecardcooldown.setText(formattedDate1);
                    wat.setText("Warm up :");
                    warms.setText(cur.getString(cur.getColumnIndex("warm_up")));
                    cooldownText.setText("Cool Down :");
                    cooldownedit.setText(cur.getString(cur.getColumnIndex("cooldown")));
                    sr.setTime(formattedDate1);
                    sr.setBodypart(cur.getString(cur.getColumnIndex("msgtext")));
                    sr.setWarmup(cur.getString(cur.getColumnIndex("warm_up")));
                    sr.setExercise(cur.getString(cur.getColumnIndex("exercise")));
                    sr.setRemark(cur.getString(cur.getColumnIndex("dietetian")));
                    sr.setReps(cur.getString(cur.getColumnIndex("reps")));
                    sr.setSets(cur.getString(cur.getColumnIndex("status")));
                    results.add(sr);
                }
            }  else {
                tim.setText("");
                warms.setText("");
                timecardcooldown.setText("");
                cooldownText.setText(" ");
                cooldownedit.setText("");
                wat.setText("");
                listView.setAdapter(null);
                Toast.makeText(WorkoutDetailsAct.this, "No Data", Toast.LENGTH_LONG).show();

            }
            dba.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  results;
    }

    ArrayList<entityWorkout> GeatresultsNext() {
        ArrayList<entityWorkout> results = new ArrayList<entityWorkout>();

        Date date = new Date();  // to get the date
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate1 = df.format(date.getTime());

        calendar.add(Calendar.DATE, +1);
        formattedDate1 = df.format(calendar.getTime());
        Log.v("NEXT DATE : ", formattedDate1);

        calendar.setTime(calendar.getTime());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Datetext.setText(formattedDate1);

        entityWorkout sr = new entityWorkout();
        try {
            dba.open();
            String sql = "select * from "+ WorkoutTable.DATABASE_TABLE+ " where time='"+dayOfWeek+"'";
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new entityWorkout();
                    tim.setText(formattedDate1);
                    timecardcooldown.setText(formattedDate1);
                    sr.setTime(formattedDate1);
                    warms.setText(cur.getString(cur.getColumnIndex("warm_up")));
                    wat.setText("Warm up :");
                    cooldownedit.setText(cur.getString(cur.getColumnIndex("cooldown")));
                    cooldownText.setText("Cool Down :");
                    sr.setBodypart(cur.getString(cur.getColumnIndex("msgtext")));
                    sr.setWarmup(cur.getString(cur.getColumnIndex("warm_up")));
                    sr.setRemark(cur.getString(cur.getColumnIndex("dietetian")));
                    sr.setReps(cur.getString(cur.getColumnIndex("reps")));
                    sr.setExercise(cur.getString(cur.getColumnIndex("exercise")));
                    sr.setSets(cur.getString(cur.getColumnIndex("status")));
                    results.add(sr);
                }
            }  else {
                tim.setText("");
                timecardcooldown.setText("");
                cooldownText.setText(" ");
                warms.setText("");
                cooldownedit.setText("");
                wat.setText("");
                listView.setAdapter(null);
                Toast.makeText(WorkoutDetailsAct.this, "No Data", Toast.LENGTH_LONG).show();

            }
            dba.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  results;
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

        Webutil.getResponse(WorkoutDetailsAct.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetPlanDetails" , param.toString() , new MyHand());
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
