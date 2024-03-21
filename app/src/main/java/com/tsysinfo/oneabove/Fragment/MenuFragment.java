package com.tsysinfo.oneabove.Fragment;
/**
 * Created by tsysinfo on 5/22/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.tsysinfo.oneabove.Adapters.CustomAndroidGridViewAdapter;
import com.tsysinfo.oneabove.AttendanceActivity;
import com.tsysinfo.oneabove.AttendenceReward;
import com.tsysinfo.oneabove.CalenderActivity;
import com.tsysinfo.oneabove.ChatActivity;
import com.tsysinfo.oneabove.DietDetails;
import com.tsysinfo.oneabove.DietPdfActivity;
import com.tsysinfo.oneabove.DietPlan;
import com.tsysinfo.oneabove.FoodReminder;
import com.tsysinfo.oneabove.HealthActivity;
import com.tsysinfo.oneabove.MesurementActivity;
import com.tsysinfo.oneabove.NotificationActivity;
import com.tsysinfo.oneabove.PTRecords;
import com.tsysinfo.oneabove.PTRecordsActivity;
import com.tsysinfo.oneabove.PalnDetailsActivity;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.RulesActivity;
import com.tsysinfo.oneabove.SlotBookingActivity;
import com.tsysinfo.oneabove.SocialActivity;
import com.tsysinfo.oneabove.Workout;
import com.tsysinfo.oneabove.WorkoutDetailsAct;
import com.tsysinfo.oneabove.WorkoutPlan;


import java.util.ArrayList;
public class MenuFragment extends Fragment {
    public static String[] gridViewStrings = {
            "Notification",
            "Diet Suggestion",
            "Workout",
           // "Measurement",
            "Health Details",
            "Plan Details",
            "Calender",
//            "Chats",
            "Social Media",
            "Rules",
            "Rewards",
            "PT Records",
            "Slot Booking",
    };
    public static int[] gridViewImages = {
            R.drawable.notification11,
            R.drawable.diet,
            R.drawable.workout,
           // R.drawable.measuring1tape,
            R.drawable.health,
            R.drawable.plandetails,
            R.drawable.ic_calender,
//            R.drawable.chat512,
            R.drawable.socialmedia,
            R.drawable.rules,
            R.drawable.reward,
            R.drawable.ic_pt,
            R.drawable.ic_slot_booking,


    };
    GridView gridView;
    Context context;
    ArrayList arrayList;
    public MenuFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setAdapter(new CustomAndroidGridViewAdapter(getActivity(), gridViewStrings, gridViewImages));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent= new Intent(getActivity(), NotificationActivity.class);
                    startActivity(intent);
                }else if (position == 1) {
                    Intent intent= new Intent(getActivity(), DietDetails.class);
                    startActivity(intent);
                } else if (position == 2) {

                    Intent intent= new Intent(getActivity(), Workout.class);
                    //Intent intent= new Intent(getActivity(), WorkoutDetailsAct.class);

                    startActivity(intent);
                }

                else if (position == 3) {

                    Intent intent= new Intent(getActivity(), HealthActivity.class);
                    startActivity(intent);

                }
//                else if (position == 3) {
//
//                    Intent intent= new Intent(getActivity(), HealthActivity.class);
//                    startActivity(intent);
//                }
                else if (position == 4) {

                    Intent intent= new Intent(getActivity(), PalnDetailsActivity.class);
                    startActivity(intent);
                }
                else if (position == 5) {

                    Intent intent= new Intent(getActivity(), CalenderActivity.class);
                    startActivity(intent);
                }
                else if (position == 6) {

                    Intent intent= new Intent(getActivity(), SocialActivity.class);
                    startActivity(intent);
                }
                else if (position == 7) {

                    Intent intent= new Intent(getActivity(), RulesActivity.class);
                    startActivity(intent);
                }
                else if (position == 8) {

                    Intent intent= new Intent(getActivity(), AttendenceReward.class);
                    startActivity(intent);
                }
                else if (position == 9) {

                    Intent intent= new Intent(getActivity(), PTRecordsActivity.class);
                    startActivity(intent);
                }
                else if (position == 10) {

                    Intent intent= new Intent(getActivity(), SlotBookingActivity.class);
                    startActivity(intent);
                }
//                else if (position == 10) {
//
//                    Intent intent= new Intent(getActivity(), DietPdfActivity.class);
//                    startActivity(intent);
//                }
            }
        });
        return view;
    }



}
