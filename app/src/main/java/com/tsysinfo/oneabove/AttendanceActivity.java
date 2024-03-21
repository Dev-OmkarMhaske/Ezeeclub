package com.tsysinfo.oneabove;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceActivity  extends AppCompatActivity {
    DataBaseAdapter dba;
    Models mod;
    private SessionManager sessionManager;
    private TextView textAttendance,note,profile_name,membernumber,TextDate;
    private JSONArray serverResponse;
    CircleImageView profImage;
    private Bitmap b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_attendance_activity);
        sessionManager= new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Attendance");

        Date date = new Date();  // to get the date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm"); // getting date in this format
        String formattedDate = df.format(date.getTime());

        note=(TextView)findViewById(R.id.noti);
        textAttendance=(TextView)findViewById(R.id.textAttendance);
        TextDate=(TextView)findViewById(R.id.TextDate);
        profile_name=(TextView)findViewById(R.id.profile_name);
        profile_name.setText(sessionManager.getName());
        membernumber=(TextView)findViewById(R.id.membernumber);
        membernumber.setText(sessionManager.getAuthority());
        TextDate.setText(formattedDate);
        profImage= (CircleImageView) findViewById(R.id.profile_image);
        dba= new DataBaseAdapter(this);
        mod= new Models();
        plan();
        textAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attendance();
            }
        });


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
                if(bitmap==null){
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
                    view.setImageResource(R.drawable.icprofile);
                }
            } else {

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

        Webutil.getResponse(AttendanceActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"SaveAttendance" , param.toString() , new MyHand());
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
    public void Attendance()
    {
        JSONObject param = new JSONObject();
        try{
            param.put("MemberNo",sessionManager.getAuthority());
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("test" , "inn thread method...");

        Webutil.getResponse(AttendanceActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"SaveAttendance" , param.toString() , new MyHandAttendance());
    }
    class MyHandAttendance extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg!= null){
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity","Response1: "+response);

                try {
                    serverResponse = new JSONArray(response);

                    if(serverResponse != null) {
                        Date date = new Date();  // to get the date
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // getting date in this format
                        String formattedDate = df.format(date.getTime());

                        if (serverResponse.getJSONObject(0).getString("ChangePassword").equalsIgnoreCase("Success")) {
                            Toast.makeText(AttendanceActivity.this, "Attendance Marked successfully", Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
