package com.tsysinfo.oneabove;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.Adapters.DietAdapter;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.WorkoutTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RemarkActivity extends AppCompatActivity {
    DataBaseAdapter dba;
    Models mod;
    TextView note;
    private RemarkAdapter mAdapter;
    private DietAdapter mAdapter1;
    ListView listView;
    private int mYear;
    private int mMonth;
    private int mDay;
    Calendar calendar;
    private TextView textView, tim, warms, wat, timecardcooldown, cooldownText, cooldownedit;

    private TextView bodypart, remark, exercise, sets, repitations;
    String bodypart1, remark1, exercise1, sets1, repitations1;
    private ArrayList<RemarkDetails> mList;
    String FromDate,ToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        listView = findViewById(R.id.listView);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        mList = new ArrayList<>();

        FromDate = getIntent().getStringExtra("FromDate");
        ToDate = getIntent().getStringExtra("ToDate");
        ArrayList<RemarkDetails> ReceiptSearchResults = Geatresultsdisplay();
        RemarkAdapter dietAdapter = new RemarkAdapter(RemarkActivity.this, ReceiptSearchResults);
        listView.setAdapter(dietAdapter);

    }


        ArrayList<RemarkDetails> Geatresultsdisplay() {
        ArrayList<RemarkDetails> results = new ArrayList<RemarkDetails>();


        Date date = new Date();  // to get the date
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(date.getTime());
        RemarkDetails sr = new RemarkDetails();
        try {
            dba.open();
            String sql = "select distinct  dietetian,time from "+ WorkoutTable.DATABASE_TABLE+" Where workout_from='"+FromDate+"' and workout_to='"+ToDate+"'";
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new RemarkDetails();
                    sr.setRemark(cur.getString(0).trim());
                    sr.setDay(cur.getString(1).trim());
                    sr.setFromDate(FromDate);
                    sr.setToDate(ToDate);
                    results.add(sr);
                }
            }  else {
//                tim.setText("");
//                warms.setText("");
//                wat.setText("");
//                cooldownText.setText(" ");
//                cooldownedit.setText("");
//                timecardcooldown.setText("");
//                listView.setAdapter(null);
                Toast.makeText(RemarkActivity.this, "No Data", Toast.LENGTH_LONG).show();

            }
            dba.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return results;
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
