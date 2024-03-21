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

public class PlanAdapter extends BaseAdapter {
    private static ArrayList<RemarkDetails> searchArrayList;
    private LayoutInflater mInflater;
    Calendar calendar;
    Context context;
    DataBaseAdapter dba;

    public PlanAdapter(Context context, ArrayList<RemarkDetails> results) {
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
        final PlanAdapter.ViewHolder holder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.plan_row11, null);
            holder = new PlanAdapter.ViewHolder();
            holder.textbody = (TextView) convertView.findViewById(R.id.bodypart);
            holder.textremark = (TextView) convertView.findViewById(R.id.textremark);
            holder.textDay = (TextView) convertView.findViewById(R.id.textDay);
            holder.root_view = (LinearLayout) convertView.findViewById(R.id.root_view);
            holder.textrepitations = (TextView) convertView.findViewById(R.id.repitations);
            holder.textsets = (TextView) convertView.findViewById(R.id.sets);
            holder.textexercise = (TextView) convertView.findViewById(R.id.exercise);
            convertView.setTag(holder);
        }
        else {
            holder = (PlanAdapter.ViewHolder) convertView.getTag();
        }

       // holder.textremark.setText(searchArrayList.get(position).getRemark());
        holder.textsets.setText(searchArrayList.get(position).getSets());
        holder.textrepitations.setText(searchArrayList.get(position).getRepetations());
        holder.textexercise.setText(searchArrayList.get(position).getExercise());

        final String Bodypart = searchArrayList.get(position).getBodyPart();
        final String Remark = searchArrayList.get(position).getRemark();
        final   String set = searchArrayList.get(position).getSets();
        final String Repitations = searchArrayList.get(position).getRepetations();
        final String Exercise = searchArrayList.get(position).getExercise();
        return convertView;
    }

    static class ViewHolder {

        TextView textbody,textremark,textDay,textrepitations,textsets,textexercise;
        LinearLayout root_view;
        ImageView imgProduct;
    }
}
