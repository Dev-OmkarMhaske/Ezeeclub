package com.tsysinfo.oneabove;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class MesurementActivity extends AppCompatActivity {
    static boolean errored = false;
    String bNo, MeasuNo;
    DataBaseAdapter dba;
    String uid, GlobalUser;
    Models mod;
    Button back, Save;
    TextView DOB;
    TextView counsellor;
    String Data, Remark = "", date, mname = "", Counsellor, nCounseller, Weight, Hieght, Neck, Shoulde, ChestNormal, ChestExpanded, UpperArm, Forearm, MuscleMass, Bmr, UpperAbdoment, Waist, LowerAbdoment, Hip, Thing, Calf, Whr, Bmi, BodyFat, IdealBodyWeight, ViceralFat, BodyWater, BoneMass, WaistCircumferance;
    EditText remark, Date, weight, hieght, neck, shoulde, chestNormal, chestExpanded, upperArm, forearm, muscleMass, bmr, upperAbdoment, waist, lowerAbdoment, hip, thing, calf, whr, bmi, bodyFat, idealBodyWeight, viceralFat, bodyWater, boneMass, waistCircumferance;
    TextView actionUser, actionDate, actionBranch;
    JSONArray serverResponse;
    LinearLayout llbutton;
    ProgressDialog waitDialog;
    int enqFlag = 0;
    SessionManager sessionManager;
    private String identifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesurement);
        sessionManager= new SessionManager(this);
        llbutton = (LinearLayout) findViewById(R.id.LLbutton);
        Date = (EditText) findViewById(R.id.textDate);
        counsellor = (TextView) findViewById(R.id.spinner);
        weight = (EditText) findViewById(R.id.textWeight);
        hieght = (EditText) findViewById(R.id.textHeight);
        DOB = (TextView) findViewById(R.id.textView2);
        DOB.setVisibility(View.GONE);
        neck = (EditText) findViewById(R.id.textNeck);
        shoulde = (EditText) findViewById(R.id.textShoulder);
        chestNormal = (EditText) findViewById(R.id.textNormal);
        chestExpanded = (EditText) findViewById(R.id.textChestExpand);
        upperArm = (EditText) findViewById(R.id.textUpperArms);
        forearm = (EditText) findViewById(R.id.textForearm);
        muscleMass = (EditText) findViewById(R.id.textMusclemass);
        bmr = (EditText) findViewById(R.id.textBMR);
        upperAbdoment = (EditText) findViewById(R.id.textUpperAbdomen);
        waist = (EditText) findViewById(R.id.textWaist);
        lowerAbdoment = (EditText) findViewById(R.id.textLowerAbdomen);
        hip = (EditText) findViewById(R.id.textHip);
        thing = (EditText) findViewById(R.id.textThigh);
        calf = (EditText) findViewById(R.id.textCalf);
        whr = (EditText) findViewById(R.id.textWHR);
        bmi = (EditText) findViewById(R.id.textBMI);
        bodyFat = (EditText) findViewById(R.id.textBodyFat);
        idealBodyWeight = (EditText) findViewById(R.id.textIdealbodyweight);
        viceralFat = (EditText) findViewById(R.id.textVisceralfat);
        bodyWater = (EditText) findViewById(R.id.textBodywater);
        boneMass = (EditText) findViewById(R.id.textBonemass);
        waistCircumferance = (EditText) findViewById(R.id.textWaistCircumference);
        remark = (EditText) findViewById(R.id.textRemark);
        Save = (Button) findViewById(R.id.buttonMesurSave);
        back = (Button) findViewById(R.id.buttonBack);
        dba = new DataBaseAdapter(this);
        mod = new Models();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        waitDialog = new ProgressDialog(this);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.setMessage("Please Wait");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (Connection.isOnline(this))
        {
            threadLogin();
        }else {
            Toast.makeText(this, "No Internet Connection Detected.", Toast.LENGTH_SHORT).show();

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

        Webutil.getResponse(MesurementActivity.this , SharedPreferenceUtil.getLoginUrl(getApplicationContext())+"GetMeasureDetails" , param.toString() , new MyHandler());
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
        {finish();}
        return super.onOptionsItemSelected(item);

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


                        counsellor.setText(serverResponse.getJSONObject(0).getString("EmpName").trim());
                        remark.setText(serverResponse.getJSONObject(0).getString("Remark").trim());
                        Date.setText(serverResponse.getJSONObject(0).getString("MeasurementDate").trim());
                        weight.setText(serverResponse.getJSONObject(0).getString("Weight").trim());
                        hieght.setText(serverResponse.getJSONObject(0).getString("HeightInches").trim());
                        neck.setText(serverResponse.getJSONObject(0).getString("Neck").trim());
                        shoulde.setText(serverResponse.getJSONObject(0).getString("shoulder").trim());
                        chestNormal.setText(serverResponse.getJSONObject(0).getString("ChestNormal").trim());
                        chestExpanded.setText(serverResponse.getJSONObject(0).getString("ChstExpanded").trim());
                        upperArm.setText(serverResponse.getJSONObject(0).getString("Arms").trim());
                        forearm.setText(serverResponse.getJSONObject(0).getString("Forearm").trim());
                        muscleMass.setText(serverResponse.getJSONObject(0).getString("MuscleMass").trim());
                        bmr.setText(serverResponse.getJSONObject(0).getString("BMR").trim());
                        upperAbdoment.setText(serverResponse.getJSONObject(0).getString("UpperAbdomen").trim());
                        waist.setText(serverResponse.getJSONObject(0).getString("Waist").trim());
                        lowerAbdoment.setText(serverResponse.getJSONObject(0).getString("LowerAbdomen").trim());
                        hip.setText(serverResponse.getJSONObject(0).getString("Hip").trim());
                        thing.setText(serverResponse.getJSONObject(0).getString("Thigh").trim());
                        calf.setText(serverResponse.getJSONObject(0).getString("Calf").trim());
                        whr.setText(serverResponse.getJSONObject(0).getString("WHR").trim());
                        bmi.setText(serverResponse.getJSONObject(0).getString("BMI").trim());
                        bodyFat.setText(serverResponse.getJSONObject(0).getString("BodyFat").trim());
                        idealBodyWeight.setText(serverResponse.getJSONObject(0).getString("IdealBodyFat").trim());
                        viceralFat.setText(serverResponse.getJSONObject(0).getString("VisceralFat").trim());
                        bodyWater.setText(serverResponse.getJSONObject(0).getString("BodyWater").trim());
                        boneMass.setText(serverResponse.getJSONObject(0).getString("BoneMass").trim());
                        waistCircumferance.setText(serverResponse.getJSONObject(0).getString("Waistcircum").trim());


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
