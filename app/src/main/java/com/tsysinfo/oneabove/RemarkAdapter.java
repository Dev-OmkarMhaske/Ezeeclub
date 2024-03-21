package com.tsysinfo.oneabove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsysinfo.oneabove.database.DataBaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class RemarkAdapter  extends BaseAdapter {

    private static ArrayList<RemarkDetails> searchArrayList;
    private LayoutInflater mInflater;
    Calendar calendar;
    Context context;
    DataBaseAdapter dba;

    public RemarkAdapter(Context context, ArrayList<RemarkDetails> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        dba = new DataBaseAdapter(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rank_row, null);
            holder = new ViewHolder();
            holder.textbody = (TextView) convertView.findViewById(R.id.textbody);
            holder.textremark = (TextView) convertView.findViewById(R.id.textremark);
            holder.textDay = (TextView) convertView.findViewById(R.id.textDay);
            holder.root_view = (LinearLayout) convertView.findViewById(R.id.root_view);
            holder.textrepitations = (TextView) convertView.findViewById(R.id.textrepitations);
            holder.textsets = (TextView) convertView.findViewById(R.id.textsets);
            holder.textexercise = (TextView) convertView.findViewById(R.id.textexercise);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textbody.setText(searchArrayList.get(position).getBodyPart());
        holder.textremark.setText(searchArrayList.get(position).getRemark());
        holder.textsets.setText(searchArrayList.get(position).getSets());
        holder.textrepitations.setText(searchArrayList.get(position).getRepetations());
        holder.textDay.setText(searchArrayList.get(position).getDay());
        holder.textexercise.setText(searchArrayList.get(position).getExercise());

        holder.textDay.setText("Day: "+searchArrayList.get(position).getDay());
        holder.textbody.setText("Bodypart: "+searchArrayList.get(position).getBodyPart());
//        holder.textsets.setText("set: "+searchArrayList.get(position).getSets());
//        holder.textrepitations.setText("set: "+searchArrayList.get(position).getRepetations());


        final String Remark = searchArrayList.get(position).getRemark();
        final   String FromDate = searchArrayList.get(position).getFromDate();
        final String ToDate = searchArrayList.get(position).getToDate();
        final String Day = searchArrayList.get(position).getDay();



        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,WorkoutPlan.class);

                i.putExtra("Remark" , Remark  );
                i.putExtra("FromDate" , FromDate );
                i.putExtra("ToDate" , ToDate  );
                i.putExtra("Day" , Day  );
                context.startActivity(i);

            }
        });



        return convertView;
    }

    static class ViewHolder {

        TextView textbody,textremark,textDay,textrepitations,textsets,textexercise;
        LinearLayout root_view;
        ImageView imgProduct;
    }
}
