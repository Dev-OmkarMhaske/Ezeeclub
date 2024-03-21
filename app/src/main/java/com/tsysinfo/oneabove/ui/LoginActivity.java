package com.tsysinfo.oneabove.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tsysinfo.oneabove.Connection;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.RegistrationActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.Mst_User;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.database.SessionManagerLoginBrach;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


public class LoginActivity extends AppCompatActivity {
    EditText pswd, usrusr;
    TextView lin;
    TextView textViewLogin, ForGotPassword;
    int flag = 0;
    private ProgressDialog dialog;
    private SessionManager session;
    private SessionManagerLoginBrach sessionManagerLoginBrach;
    private DataBaseAdapter dba;
    private Models mod;
    private String user;
    private String pass;
    private JSONArray loginStatus;
    private Bundle bundle;
    String memberNo;
    JSONArray serverResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewLogin = (TextView) findViewById(R.id.textLogin);

        session = new SessionManager(this);
        sessionManagerLoginBrach = new SessionManagerLoginBrach(this);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        ForGotPassword = (TextView) findViewById(R.id.ForGotPassword);
        lin = (TextView) findViewById(R.id.lin);
        usrusr = (EditText) findViewById(R.id.usrusr);
        pswd = (EditText) findViewById(R.id.pswrdd);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        lin.setTypeface(custom_font1);
        usrusr.setTypeface(custom_font);
        pswd.setTypeface(custom_font);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Please Wait...");
        final Toast success = Toast.makeText(getApplicationContext(), "Login Successful...!!!", Toast.LENGTH_LONG);
        final Toast incorrect = Toast.makeText(LoginActivity.this, "User Name or Password Incorrect", Toast.LENGTH_LONG);
        final Toast empty = Toast.makeText(getApplicationContext(), "Enter User Name And Password", Toast.LENGTH_LONG);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dba.open();
                mod.clearDatabase("branch");
                dba.close();
                user = usrusr.getText().toString().trim().toLowerCase();
                pass = pswd.getText().toString().trim();
                if (user.length() > 0 && pass.length() > 0) {


                    threadLogin();
                    /*AsyncCallWS task = new AsyncCallWS();
                    // Call execute
                    task.execute();
*/
                } else {
                    Toast.makeText(getApplicationContext(), "Enter UserName and Password...!!! ", Toast.LENGTH_LONG).show();
                }
            }
        });

        ForGotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection.isOnline(LoginActivity.this)) {

                    if (!usrusr.getText().toString().equalsIgnoreCase("")) {
                        memberNo = usrusr.getText().toString();

                        if (Connection.isOnline(LoginActivity.this)) {
                            threadForgotPassword();
                        } else {
                            Toast.makeText(LoginActivity.this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Please Enter User Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void threadLogin() {
        JSONObject param = new JSONObject();
        try {
            param.put("UserName", user);
            param.put("Password", pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");

        Webutil.getResponse(LoginActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "UserLogin", param.toString(), new LoginHandler());
    }

    class LoginHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity", "Response1: " + response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray != null) {

                        dba.open();


                        /*{
                            "Active": "No",
                                "Address": "Test",
                                "AnniversaryDate": "",
                                "BirthDate": "10\/04\/2017",
                                "BranchName": null,
                                "Branchno": "1",
                                "Email": "Abc@xzy.cm",
                                "EmployeeNo": 0,
                                "Gender": "Female",
                                "JoiningDate": "",
                                "Location": "",
                                "MemberName": " Rushu",
                                "MemberNo": "6",
                                "MemberNoBr": "20004",
                                "Membershipstatus": "Expired",
                                "MobileNo": "2986352589",
                                "Password": "1234"
                        }

*/
                        if (!jsonArray.getJSONObject(0).getString("MemberNo").equalsIgnoreCase("No")) {

                            if (!jsonArray.getJSONObject(0).getString("Active").equalsIgnoreCase("No")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(Mst_User.KEY_MEMBER_NO, jsonArray.getJSONObject(0).getString("MemberNo"));
                                contentValues.put(Mst_User.KEY_NAME, jsonArray.getJSONObject(0).getString("MemberName"));
                                contentValues.put(Mst_User.KEY_Mobile, jsonArray.getJSONObject(0).getString("MobileNo"));
                                contentValues.put(Mst_User.KEY_MEMBER_NO_BR, jsonArray.getJSONObject(0).getString("MemberNoBr"));
                                contentValues.put(Mst_User.KEY_GENDER, jsonArray.getJSONObject(0).getString("Gender"));
                                contentValues.put(Mst_User.KEY_BDAY, jsonArray.getJSONObject(0).getString("BirthDate"));
                                Log.w("BDAY", jsonArray.getJSONObject(0).getString("BirthDate"));
                                contentValues.put(Mst_User.KEY_STATUS, jsonArray.getJSONObject(0).getString("Active"));
                                contentValues.put(Mst_User.KEY_BRANCH, jsonArray.getJSONObject(0).getString("Branchno"));
                                contentValues.put(Mst_User.KEY_Password, jsonArray.getJSONObject(0).getString("Password"));
                                contentValues.put(Mst_User.KEY_EMAIL, jsonArray.getJSONObject(0).getString("Email"));
                                mod.insertdata(Mst_User.DATABASE_TABLE, contentValues);
                                dba.close();
                                session.logoutUser();
                                session.createLoginSession(jsonArray.getJSONObject(0).getString("MemberNo"), jsonArray.getJSONObject(0).getString("MemberName"), jsonArray.getJSONObject(0).getString("Branchno"), pass);
                                sessionManagerLoginBrach.createLoginSession(jsonArray.getJSONObject(0).getString("MemberNo"), jsonArray.getJSONObject(0).getString("MemberName"), jsonArray.getJSONObject(0).getString("Branchno"), pass);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "You are a InActive User", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed...Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void threadForgotPassword() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", memberNo);
            param.put("NewPassword", UUID.randomUUID().toString().substring(0, 6));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");
        Webutil.getResponse(LoginActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "ForgotPassword", param.toString(), new MyHandler());
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
                    serverResponse = new JSONArray(response);
                    if (serverResponse != null) {
                        if (serverResponse.getJSONObject(0).getString("ChangePassword").equalsIgnoreCase("Failure")) {
                            Toast.makeText(LoginActivity.this, "Member is already registered.", Toast.LENGTH_SHORT).show();
                        } else if (serverResponse.getJSONObject(0).getString("ChangePassword").equalsIgnoreCase("Invalid")) {
                            Toast.makeText(LoginActivity.this, "Mobile Number not Available to Send Password. Ask Admin to Update Password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Password is sent on registered mobile number.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
