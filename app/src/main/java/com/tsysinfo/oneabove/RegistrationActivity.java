package com.tsysinfo.oneabove;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.SessionManager;
import com.tsysinfo.oneabove.ui.LoginActivity;
import com.tsysinfo.oneabove.webUtil.Webutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class RegistrationActivity extends AppCompatActivity {

    EditText editTextMemberNo;
    ImageView btnNext;
    String BranchNo="";
    //TextView textViewLogin;
    DataBaseAdapter dba;
    SessionManager session;
    ProgressDialog waitDialog;
    JSONArray serverResponse;
    String memberNo = "";
    List<String> branch;
    static boolean errored = false;
    Spinner spinner;
    private SecureRandom random;
    private TextView editTextRegister;
    private String brNo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_registration);

            getSupportActionBar().setTitle("Tsysinfo Gym");

            editTextMemberNo = (EditText) findViewById(R.id.editText);
            editTextRegister = (TextView) findViewById(R.id.textView6);
            spinner= (Spinner)findViewById(R.id.spinnerBr);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
            editTextRegister.setTypeface(custom_font);
            //textViewLogin = (TextView) findViewById(R.id.textLogin);
            random = new SecureRandom();
            dba = new DataBaseAdapter(this);
            waitDialog = new ProgressDialog(this);
            waitDialog.setMessage("Please Wait...");

            waitDialog.setCancelable(false);
            waitDialog.setCanceledOnTouchOutside(false);
            btnNext = (ImageView) findViewById(R.id.buttonNext);
           /* textViewLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });*/
            /*2245f5*/
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Connection.isOnline(RegistrationActivity.this)) {
                        brNo=spinner.getSelectedItem().toString();
                        if (!brNo.equalsIgnoreCase("Select Branch")|| !brNo.equalsIgnoreCase(""))
                       {
                            if (!editTextMemberNo.getText().toString().equalsIgnoreCase("")) {
                               memberNo = editTextMemberNo.getText().toString();

                                String[] ar=brNo.split("-");
                                BranchNo=ar[0];

                                if (Connection.isOnline(RegistrationActivity.this))
                                {
                                    threadLogin();

                                }else {
                                    Toast.makeText(RegistrationActivity.this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

                                }


                            }
                            else {
                                Toast.makeText(RegistrationActivity.this, "Please Enter Member Number", Toast.LENGTH_SHORT).show();
                            }}else {
                            Toast.makeText(RegistrationActivity.this, "Select Your branch.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "No Internet Connection Detected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        if (Connection.isOnline(this))
        {
            threadGetBranch();
        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }


    private void threadGetBranch() {
        JSONObject param = new JSONObject();

        Webutil.getResponse(RegistrationActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "GetBranchDetails", param.toString(), new BranchHandler());
    }
    class BranchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                branch = new ArrayList<String>();
                branch.add("Select Branch");
                Log.d("test", "response in handler login....." + msg.toString());
                String response = (String) msg.obj;
                Log.w("LoginActivity", "Response1: " + response);
                try {
                    serverResponse = new JSONArray(response);
                    if (serverResponse != null) {

                        for (int i=0;i<serverResponse.length();i++)
                        {
                            branch.add(serverResponse.getJSONObject(i).getString("Branchno")+"-"+serverResponse.getJSONObject(i).getString("Branchname"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, branch);


                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                        spinner.setAdapter(dataAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void threadLogin() {
        JSONObject param = new JSONObject();
        try {
            param.put("MemberNo", memberNo);
            param.put("NewPassword", UUID.randomUUID().toString().substring(0, 6));
            param.put("BranchNo", BranchNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "inn thread method...");
        Webutil.getResponse(RegistrationActivity.this, SharedPreferenceUtil.getLoginUrl(getApplicationContext()) + "Registration", param.toString(), new MyHandler());
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
                            Toast.makeText(RegistrationActivity.this, "Member is already registered.", Toast.LENGTH_SHORT).show();
                        } else if (serverResponse.getJSONObject(0).getString("ChangePassword").equalsIgnoreCase("Invalid")) {
                            Toast.makeText(RegistrationActivity.this, "Mobile Number not Available to Send Password. Ask Admin to Update Password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Password is sent on registered mobile number.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
