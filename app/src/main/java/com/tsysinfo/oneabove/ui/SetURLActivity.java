package com.tsysinfo.oneabove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.Utils.SharedPreferenceUtil;

public class SetURLActivity extends AppCompatActivity {
    TextView setUrlBtn;
    EditText urlEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.set_url_activity);

            setUrlBtn = findViewById(R.id.setUrlBtn);
            urlEditTxt = findViewById(R.id.urlEditTxt);

            setUrlBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = urlEditTxt.getText().toString().trim();
                    if (url.contains("http://") || url.contains("www")) return;
                    String finalUrl = "http://" + url + "/";
                    if (url.length() > 0) {
                        SharedPreferenceUtil.setLoginURL(getApplicationContext(), finalUrl);
                        Log.d("URL", finalUrl);
                        Intent intent = new Intent(SetURLActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SetURLActivity.this, "Please enter valid URL to proceed !!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}