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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.WorkoutTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Workout extends AppCompatActivity {
    private int mYear;
    private int mMonth;
    private int mDay;
    DataBaseAdapter dba;
    Models mod;
    TextView note;
    private WorkAdapter  mAdapter;
    private String mmonth = "",Clientid;
    Button b1,b2;
    TextView Datetext;
    Calendar calendar;
    ListView listView;
    private ArrayList<WorkoutDetails> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        listView=findViewById(R.id.listView);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        mList = new ArrayList<>();

        ArrayList<WorkoutDetails> ReceiptSearchResults = Geatresultsdisplay();
        WorkAdapter dietAdapter = new WorkAdapter(Workout.this, ReceiptSearchResults);
        listView.setAdapter(dietAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RemarkActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    ArrayList<WorkoutDetails> Geatresultsdisplay() {
        ArrayList<WorkoutDetails> results = new ArrayList<WorkoutDetails>();

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

        //Datetext.setText(formattedDate);

        WorkoutDetails sr = new WorkoutDetails();
        try {
            dba.open();
            //String sql = "select distinct workout_from +' - '+ workout_to from "+ WorkoutTable.DATABASE_TABLE;
            String sql = "select distinct workout_from , workout_to from "+ WorkoutTable.DATABASE_TABLE;
            Cursor cur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            Log.w("CA", "SQL1 :" + sql);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    sr = new WorkoutDetails();
                    //sr.setDate(formattedDate);
                    sr.setDate(cur.getString(0).trim());
                    sr.setType(cur.getString(1).trim());
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
                Toast.makeText(Workout.this, "No Data", Toast.LENGTH_LONG).show();

            }
            dba.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  results;
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
