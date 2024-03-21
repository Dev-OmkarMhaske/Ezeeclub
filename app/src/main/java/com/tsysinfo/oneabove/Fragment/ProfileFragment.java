package com.tsysinfo.oneabove.Fragment;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.Mst_User;
import com.tsysinfo.oneabove.database.SessionManager;

/**
 * Created by tsysinfo on 5/22/2017.
 */
public class ProfileFragment extends Fragment {
    TextView textViewMobile,textViewPhone,textViewEmail1,textViewEmail2,textViewAddress;

    SessionManager sessionManager;
    Models models;
    DataBaseAdapter dba;
    private TextView textViewActive;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        dba= new DataBaseAdapter(getActivity());
        models= new Models();
        sessionManager= new SessionManager(getActivity());


        textViewMobile=(TextView)view.findViewById(R.id.tvNumber1);
        textViewPhone=(TextView)view.findViewById(R.id.tvNumber2);
        textViewEmail1=(TextView)view.findViewById(R.id.tvNumber3);
        textViewEmail2=(TextView)view.findViewById(R.id.tvNumber4);
        textViewAddress=(TextView)view.findViewById(R.id.tvNumber5);
        textViewActive=(TextView)view.findViewById(R.id.tvNumber6);


        dba.open();
        Cursor cursor=models.getData(Mst_User.DATABASE_TABLE);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            {
                String no1=cursor.getString(cursor.getColumnIndex(Mst_User.KEY_Mobile));
                String no2=cursor.getString(cursor.getColumnIndex(Mst_User.KEY_EMAIL));
                String email=cursor.getString(cursor.getColumnIndex(Mst_User.KEY_GENDER));
                String email2=cursor.getString(cursor.getColumnIndex(Mst_User.KEY_BDAY));
                String address=cursor.getString(cursor.getColumnIndex(Mst_User.KEY_ADDRESS));
                String active=cursor.getString(cursor.getColumnIndex(Mst_User.KEY_STATUS));
                if (no1.equalsIgnoreCase(""))
                {
                    textViewMobile.setText("Not Provided");
                }else {
                    textViewMobile.setText("+91 "+no1);
                }
                if (no2.equalsIgnoreCase(""))
                {
                    textViewPhone.setText("Not Provided");
                }else {
                    textViewPhone.setText(no2);
                }

                if (email.equalsIgnoreCase(""))
                {
                    textViewEmail1.setText("Not Provided");


                }else {
                    textViewEmail1.setText(email);

                }
                if (email2.equalsIgnoreCase(""))
                {

                    textViewEmail2.setText("Not Provided");

                }else {

                    textViewEmail2.setText(email2);
                }

                if (address!=null)
                {
                    textViewAddress.setText(address);

                }else {
                    textViewAddress.setText("Not Provided");

                }
                textViewActive.setText(active);
            }
        }
        dba.close();
        return view;
    }
}
