package com.tsysinfo.oneabove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsysinfo.oneabove.database.DataBaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class DietRemarkAdapter   extends BaseAdapter {
    private static ArrayList<DietRemarkDetails> searchArrayList;
    private LayoutInflater mInflater;
    Calendar calendar;
    Context context;
    DataBaseAdapter dba;

    public DietRemarkAdapter(Context context, ArrayList<DietRemarkDetails> results) {
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
        final DietRemarkAdapter.ViewHolder holder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.diet_remark_row, null);
            holder = new DietRemarkAdapter.ViewHolder();
//            holder.cardView=(CardView)convertView.findViewById(R.id.card);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.diet = (TextView) convertView.findViewById(R.id.diet);
            holder.copy=(Button)convertView.findViewById(R.id.copy);
            convertView.setTag(holder);
        }
        else {
            holder = (DietRemarkAdapter.ViewHolder) convertView.getTag();
        }
        holder.time.setText(searchArrayList.get(position).getTime());
        holder.diet.setText(searchArrayList.get(position).getDiet());
       // holder.diet.setText(searchArrayList.get(position).getSets());


//        holder.textDay.setText("Day: "+searchArrayList.get(position).getDay());
//        holder.textbody.setText("Bodypart: "+searchArrayList.get(position).getBodyPart());
////        holder.textsets.setText("set: "+searchArrayList.get(position).getSets());
////        holder.textrepitations.setText("set: "+searchArrayList.get(position).getRepetations());

//
//        final String Remark = searchArrayList.get(position).getRemark();
//        final   String FromDate = searchArrayList.get(position).getFromDate();
//        final String ToDate = searchArrayList.get(position).getToDate();
//        final String Day = searchArrayList.get(position).getDay();



//        holder.root_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(context,WorkoutPlan.class);
//
//                i.putExtra("Remark" , Remark  );
//                i.putExtra("FromDate" , FromDate );
//                i.putExtra("ToDate" , ToDate  );
//                i.putExtra("Day" , Day  );
//                context.startActivity(i);
//
//            }
//        });



        return convertView;
    }

    static class ViewHolder {

        TextView time;
        TextView diet;
        TextView PlaceOfEating;
        Button copy;
        Button buttonDelete;
        CardView cardView;
        LinearLayout root_view;
        ImageView imgProduct;
    }
}
