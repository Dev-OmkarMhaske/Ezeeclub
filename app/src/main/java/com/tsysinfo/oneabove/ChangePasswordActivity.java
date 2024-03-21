package com.tsysinfo.oneabove;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Mst_User;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.ui.LoginActivity;
import com.tsysinfo.oneabove.webUtil.Webutil;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText editTextNewPass,editTextOldPass,editTextConfirmNewPass;
    ImageView btnNext;
    TextView textViewLogin;
    DataBaseAdapter dba;

    SessionManager session;
    ProgressDialog waitDialog;
    JSONArray serverResponse;
    String memberNo="";
    static boolean errored = false;
    private SecureRandom random;
    private TextView editTextRegister;
    private String newPass="";
    private String pass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        editTextNewPass=(EditText)findViewById(R.id.editTextNewPass);
        editTextOldPass=(EditText)findViewById(R.id.editTextOldPassword);
        editTextConfirmNewPass=(EditText)findViewById(R.id.editTextNewPassConfirm);
        editTextRegister=(TextView) findViewById(R.id.textView6);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        editTextRegister.setTypeface(custom_font);
        textViewLogin=(TextView)findViewById(R.id.textLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        session= new SessionManager(this);
        setTitle("Change password");

        random = new SecureRandom();
        dba= new DataBaseAdapter(this);
        waitDialog= new ProgressDialog(this);
        waitDialog.setMessage("Please Wait...");
        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);
        btnNext=(ImageView)findViewById(R.id.buttonNext);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ChangePasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        /*2245f5*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextNewPass.getText().toString().equalsIgnoreCase("") || !editTextOldPass.getText().toString().equalsIgnoreCase("")||!editTextConfirmNewPass.getText().toString().equalsIgnoreCase(""))
                {
                    if (editTextNewPass.getText().toString()==editTextConfirmNewPass.getText().toString())
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Password does not matched", Toast.LENGTH_SHORT).show();
                    }else {
                        String no=session.getPass() +"   " +editTextOldPass.getText().toString();
                        if (editTextOldPass.getText().toString().trim().equalsIgnoreCase(session.getPass().toString().trim())) {
                            memberNo = session.getAuthority();
                            newPass=editTextNewPass.getText().toString();
                            pass= newPass;
                            if (Connection.isOnline(ChangePasswordActivity.this))
                            {
                                threadLogin();
                                //new AsyncCallWS().execute();
                            }else {
                                Toast.makeText(ChangePasswordActivity.this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(ChangePasswordActivity.this, "old password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(ChangePasswordActivity.this, "Please provide all all details", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

   private void threadLogin(){
       JSONObject param = new JSONObject();
       try{
           param.put("MemberNo",session.getAuthority());
           param.put("BranchNo", session.getBr());
           param.put("NewPassword", pass);

       }catch (Exception e){
           e.printStackTrace();
       }
       Log.d("test" , "inn thread method...");

       Webutil.getResponse(ChangePasswordActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"ChangePassword" , param.toString() , new MyHandler());
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

                        if (!serverResponse.getJSONObject(0).getString("ChangePassword").equalsIgnoreCase("Failure"))
                        {
                            //String sql1 = "insert into " + MessageTable.DATABASE_TABLE+ " (" +KEY_MSG_TEXT+ ") values(" +msg+")";
                            String s="Update "+ Mst_User.DATABASE_TABLE+" set "+Mst_User.KEY_Password+"='"+pass+"'";

                            dba.open();
                            DataBaseAdapter.ourDatabase.execSQL(s);
                            session.UpdatePass(pass);
                            dba.close();
                            session.UpdatePass(pass);
                            Toast.makeText(ChangePasswordActivity.this, "Password Change Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ChangePasswordActivity.this, "Failed.... Please Try Again", Toast.LENGTH_SHORT).show();

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
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
