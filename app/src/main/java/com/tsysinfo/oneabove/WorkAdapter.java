package com.tsysinfo.oneabove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsysinfo.oneabove.Adapters.DietAdapter;
import com.tsysinfo.oneabove.database.DataBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkAdapter extends BaseAdapter {


        private static ArrayList<WorkoutDetails> searchArrayList;
        private LayoutInflater mInflater;
        Calendar calendar;
        Context context;
        DataBaseAdapter dba;
        public WorkAdapter(Context context, ArrayList<WorkoutDetails> results) {
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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.customer_row, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.textplan = (TextView) convertView.findViewById(R.id.textplan);
                holder.root_view = (LinearLayout) convertView.findViewById(R.id.root_view);

                convertView.setTag(holder);
            } else {

            }

            holder.text.setText(searchArrayList.get(position).getDate());
            holder.textplan.setText(searchArrayList.get(position).getType());

            final String FromDate = searchArrayList.get(position).getDate();
            final String ToDate = searchArrayList.get(position).getType();

            holder.root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,RemarkActivity.class);
                    i.putExtra("FromDate",FromDate);
                    i.putExtra("ToDate",ToDate);
                    context.startActivity(i);

                }
            });

            return convertView;
        }

    static class ViewHolder {

            TextView text,textplan;

            ImageView imgProduct;
            LinearLayout root_view;
        }
}
