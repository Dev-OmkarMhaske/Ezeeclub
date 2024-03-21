package com.tsysinfo.oneabove.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.database.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewQRActivity extends AppCompatActivity {
    SessionManager sessionManager;
    ImageView qrImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view_qr_activity);
            sessionManager = new SessionManager(this);
            qrImg = findViewById(R.id.qrImg);
            generateQR();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateQR() {
        // QR format memberId@@branchNo@@date@@time

        String qrStr = new StringBuilder(sessionManager.getAuthority()).append("@@").append(sessionManager.getBr()).append("@@").append(getDateInDDMMYY()).toString();
        Log.d("Qr_STR", qrStr);

        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            BitMatrix mMatrix = mWriter.encode(qrStr, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            qrImg.setImageBitmap(mBitmap);//Setting generated QR code to imageView
            qrImg.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static String getDateInDDMMYY() {

        String PATTERN = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(PATTERN);
        String date1 = dateFormat.format(Calendar.getInstance().getTime());
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);
        date1 = date1 + "@@" + hour + ":" + min;
        return date1;
    }

}