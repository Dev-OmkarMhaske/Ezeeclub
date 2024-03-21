package com.tsysinfo.oneabove;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.Adapters.DietAdapter;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.database.WorkoutTable;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkoutPlan extends AppCompatActivity {
    private ListView listView;
    DataBaseAdapter dba;
    Models mod;
    private ArrayList<RemarkDetails> mList;
    private DietAdapter mAdapter;

    private SessionManager sessionManager;
    private JSONArray serverResponse;
    private String FromDate,remark,ToDate,Day;
    private TextView bodypart1,remark1,exercise1,sets1,repitations1;
    static EditText DateEdit;
    private int mYear;
    private int mMonth;
    private int mDay;
    TextView note;
    private String mmonth = "",Clientid;
    Button b1,b2;
    TextView Datetext;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);
        listView=findViewById(R.id.listView);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        mList = new ArrayList<>();


        FromDate = getIntent().getStringExtra("FromDate");
        remark = getIntent().getStringExtra("Remark");
        ToDate = getIntent().getStringExtra("ToDate");
        Day = getIntent().getStringExtra("Day");


        sessionManager = new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        ArrayList<RemarkDetails> ReceiptSearchResults = Geatresultsdisplay();
        PlanAdapter dietAdapter = new PlanAdapter(WorkoutPlan.this, ReceiptSearchResults);
        listView.setAdapter(dietAdapter);


    }
    ArrayList<RemarkDetails> Geatresultsdisplay() {
        ArrayList<RemarkDetails> results = new ArrayList<RemarkDetails>();
        RemarkDetails sr = new RemarkDetails();
        try {
            dba.open();
            //String sql = "select distinct workout_from +' - '+ workout_to from "+ WorkoutTable.DATABASE_TABLE;
            String sql = "select distinct  exercise,reps,status from "+ WorkoutTable.DATABASE_TABLE+" Where workout_from='"+FromDate+"' and workout_to='"+ToDate+"' and time='"+Day+"' and dietetian='"+remark+"'";
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new RemarkDetails();
                    //sr.setDate(formattedDate);
                    sr.setExercise(cur.getString(0).trim());
                    sr.setRepetations(cur.getString(1).trim());
                    sr.setSets(cur.getString(2).trim());
                    results.add(sr);
                }
            }  else {
//
                Toast.makeText(WorkoutPlan.this, "No Data", Toast.LENGTH_LONG).show();

            }
            dba.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  results;
    }

}
