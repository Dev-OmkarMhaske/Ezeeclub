package com.tsysinfo.oneabove;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.DietTable;
import com.tsysinfo.oneabove.database.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DietRemark extends AppCompatActivity {
    String FromDate,ToDate;
    ListView listView ;
    DataBaseAdapter dba;
    Models mod;
    private ArrayList<DietRemarkDetails> mList;
    private DietRemarkAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_remark);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        mList = new ArrayList<>();
        listView=findViewById(R.id.listView);

        FromDate = getIntent().getStringExtra("FromDate");
        ToDate = getIntent().getStringExtra("ToDate");

        ArrayList<DietRemarkDetails> ReceiptSearchResults = Geatresultsdisplay();
        DietRemarkAdapter dietAdapter = new DietRemarkAdapter(DietRemark.this, ReceiptSearchResults);
        listView.setAdapter(dietAdapter);

    }


    ArrayList<DietRemarkDetails> Geatresultsdisplay() {
        ArrayList<DietRemarkDetails> results = new ArrayList<>();
//
//

        Date date = new Date();  // to get the date
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(date.getTime());
        DietRemarkDetails sr = new DietRemarkDetails();
        try {
            dba.open();
           // String sql = "select distinct  dietetian,time from "+ WorkoutTable.DATABASE_TABLE+" Where workout_from='"+FromDate+"' and workout_to='"+ToDate+"'";
            String sql = "Select * from "+ DietTable.DATABASE_TABLE+" Where fromdate='"+FromDate+"' and todate='"+ToDate+"'";
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new DietRemarkDetails();
                    sr.setDiet(cur.getString(2));
                    sr.setTime(cur.getString(1));
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
                Toast.makeText(DietRemark.this, "No Data", Toast.LENGTH_LONG).show();

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
