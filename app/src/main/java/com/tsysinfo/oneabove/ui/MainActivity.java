package com.tsysinfo.oneabove.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.tsysinfo.oneabove.AboutActivity;
import com.tsysinfo.oneabove.ChangePasswordActivity;
import com.tsysinfo.oneabove.Connection;
import com.tsysinfo.oneabove.Fragment.MenuFragment;
import com.tsysinfo.oneabove.Fragment.ProfileFragment;
import com.tsysinfo.oneabove.IPAddress;
import com.tsysinfo.oneabove.ImageStorage;
import com.tsysinfo.oneabove.NotificationActivity;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.DietTable;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.Mst_User;
import com.tsysinfo.oneabove.database.NotificationTable;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.database.WorkoutTable;
import com.tsysinfo.oneabove.receiver.MyBroadcastReceiver;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView prof_name, footertext;
    TextView fromworkout;
    TextView toWorkout;
    TextView PlanStart;
    CircleImageView profImage;
    DataBaseAdapter dba;
    SessionManager sessionManager;
    private Bitmap b;
    Models models;
    private JSONArray serverResponse;
    private PendingIntent pendingIntent, pendingIntent1;
    private AlarmManager manager;
    private int msgCount = 0;

    /*  public void startAlarm() {
          manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
          int interval = 10000; // 1 min
          Log.w("Tag", "Start Alarm");

          manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
      }*/
    public void startAlert() {
        Intent alarmIntent = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent1 = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE //setting the mutability flag
        );
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.w("Tag", "Start Alarm");
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dba = new DataBaseAdapter(this);
        models = new Models();

        //PopupWindow popupWindow=new PopupWindow(MainActivity.this);
        //popupWindow.showAtLocation(findViewById(R.id.root_view), Gravity.CENTER, 0, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        footertext = (TextView) findViewById(R.id.footertext);
        startAlert();
        sessionManager = new SessionManager(this);
        prof_name = (TextView) findViewById(R.id.profile_name);
        profImage = (CircleImageView) findViewById(R.id.profile_image);
        prof_name.setText(sessionManager.getName());
        toolbar.setTitle("Dashboard");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        PlanStart = (TextView) findViewById(R.id.plan);
        fromworkout = (TextView) findViewById(R.id.fromWorkout);
        toWorkout = (TextView) findViewById(R.id.toWorkout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        if (ImageStorage.checkifImageExists(sessionManager.getAuthority())) {
            File file = ImageStorage.getImage("/" + sessionManager.getAuthority() + ".jpg");
            String path = file.getAbsolutePath();
            if (path != null) {
                b = BitmapFactory.decodeFile(path);
                profImage.setImageBitmap(b);
                Log.w("Image", "Local");
            }
        } else {
            GetImages getImages = new GetImages(IPAddress.IP + "/Photo/" + sessionManager.getBr() + "/" + sessionManager.getAuthority() + ".jpg", profImage, sessionManager.getAuthority());
            getImages.execute();
            Log.w("Image", "server");
        }
        chechOfflineNoification();
        if (Connection.isOnline(this)) {
            notification();
            plan();
        } else {
            Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();
        }

       /* if (Connection.isOnline(this))
        {
            threadWorkoutDetail();
        }else {
            Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

        }*/

        footertext.setText("Tsysinfo Technologies Pvt. Ltd.");
        footertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tsysinfo.com"));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onResume() {
//        chechOfflineNoification();
        threadWorkOutDetail();
        super.onResume();
    }

    private void notification() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponseWithoutProgress(MainActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetNotification", param.toString(), new MyNotifyHandler());
    }

    private void chechOfflineNoification() {
        dba.open();
        String sql = "Select * from " + NotificationTable.DATABASE_TABLE + " where " + NotificationTable.KEY_STATUS + "='Unread'";
        Cursor cursor = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            setCount(cursor.getCount());
        }
        cursor.close();
        dba.close();
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

        Webutil.getResponse(MainActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetDiet", param.toString(), new MyHandler());
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
                    dba.open();
                    models.clearDatabase(DietTable.DATABASE_TABLE);
                    dba.close();
                    serverResponse = new JSONArray(response);

                    if (serverResponse != null) {
                        Date date = new Date();  // to get the date
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
                        String formattedDate = df.format(date.getTime());

                        if (!serverResponse.getJSONObject(0).getString("DsNo").equalsIgnoreCase("No")) {
                            for (int i = 0; i < serverResponse.length(); i++) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DietTable.KEY_Date, formattedDate);
                                contentValues.put(DietTable.KEY_DIET, serverResponse.getJSONObject(i).getString("DietFood"));
                                contentValues.put(DietTable.KEY_TIME, serverResponse.getJSONObject(i).getString("DietTime"));
                                contentValues.put(DietTable.KEY_KEYID, String.valueOf(i));
                                contentValues.put(DietTable.KEY_FROMDATE, serverResponse.getJSONObject(i).getString("DietFromDate"));
                                contentValues.put(DietTable.KEY_TODATE, serverResponse.getJSONObject(i).getString("DietToDate"));
                                dba.open();
                                models.insertdata(DietTable.DATABASE_TABLE, contentValues);
                                dba.close();
                                serverResponse.getJSONObject(i).getString("Dietician");

                            }


                        } else {
                            Toast.makeText(MainActivity.this, "No Diet Found", Toast.LENGTH_SHORT).show();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyNotifyHandler extends Handler {
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
                        if (!serverResponse.getJSONObject(0).getString("SendTo").equalsIgnoreCase("No")) {
                            for (int i = 0; i < serverResponse.length(); i++) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(NotificationTable.KEY_Date, serverResponse.getJSONObject(i).getString("Date"));
                                contentValues.put(NotificationTable.KEY_Noti, serverResponse.getJSONObject(i).getString("Notifi"));
                                contentValues.put(NotificationTable.KEY_STATUS, "Unread");

                                dba.open();
                                models.insertdata(NotificationTable.DATABASE_TABLE, contentValues);
                                dba.close();

                            }
                            chechOfflineNoification();
                        } else {


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

        Webutil.getResponse(MainActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetPlanDetails", param.toString(), new MainActivity.MyHand());
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


                                //PlanEntity planEntity= new PlanEntity();
                                fromworkout.setText(serverResponse.getJSONObject(i).getString("StartDt").concat("-"));

                                toWorkout.setText(serverResponse.getJSONObject(i).getString("EndDt"));

                                PlanStart.setText(serverResponse.getJSONObject(i).getString("PlanName"));


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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_notifi);
        menuItem.setIcon(buildCounterDrawable(msgCount, R.mipmap.ic_notify));
        return true;
    }

    public static boolean checkifImageExists(String imagename) {
        Bitmap b = null;
        File file = ImageStorage.getImage("/" + imagename + ".jpg");
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if (b == null || b.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_sync) {
            if (Connection.isOnline(this)) {
                threadLogin();

            } else {
                Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.action_notifi) {

            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            dba.open();
            models.clearDatabase(Mst_User.DATABASE_TABLE);
            models.clearDatabase(DietTable.DATABASE_TABLE);
            models.clearDatabase(NotificationTable.DATABASE_TABLE);
            sessionManager.logoutUser();
            SharedPreferenceUtil.clearSharedPreference(getApplicationContext());
            finish();
            dba.close();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCount(int Cou) {
        msgCount = Cou;
        invalidateOptionsMenu();

    }

    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private ImageView view;
        private Bitmap bitmap;
        private FileOutputStream fos;

        private GetImages(String requestUrl, ImageView view, String _imagename_) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.imagename_ = _imagename_;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                if (bitmap == null) {
                    view.setImageResource(R.drawable.user);
                }
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!ImageStorage.checkifImageExists(imagename_)) {
                if (bitmap != null) {
                    view.setImageBitmap(bitmap);
                    ImageStorage.saveToSdCard(bitmap, imagename_);
                } else {
                    view.setImageResource(R.drawable.user);
                }
            } else {

            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.changePass) {

            Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id == R.id.viewQr) {
            Intent intent = new Intent(MainActivity.this, ViewQRActivity.class);
            startActivity(intent);

        } else if (id == R.id.help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MenuFragment(), "Menu");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    public Drawable buildCounterDrawable(int Cartcount, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);
        if (Cartcount == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + Cartcount);
        }
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return new BitmapDrawable(getResources(), bitmap);
    }


    private void threadWorkOutDetail() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", sessionManager.getAuthority());
            param.put("BranchNo", sessionManager.getBr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(MainActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetWorkout", param.toString(), new MyHandlerWorkOutDetail());
    }

    class MyHandlerWorkOutDetail extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity", "Response1: " + response);
                dba.open();
                models.clearDatabase(WorkoutTable.DATABASE_TABLE);
                dba.close();

                try {
                    serverResponse = new JSONArray(response);

                    if (serverResponse != null) {

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
                                        models.insertdata(WorkoutTable.DATABASE_TABLE, contentValues);
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

}
