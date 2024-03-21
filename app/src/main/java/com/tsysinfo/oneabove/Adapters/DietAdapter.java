package com.tsysinfo.oneabove.Adapters;
import android.content.Context;
import androidx.cardview.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.RemarkDetails;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.entityWorkout;

import java.util.ArrayList;
import java.util.Date;
/**
 * Created by tsysinfo on 6/22/2017.
 */
public class DietAdapter extends BaseAdapter implements View.OnCreateContextMenuListener{
    private static ArrayList<entityWorkout> searchArrayList;

    DataBaseAdapter dba;
    Models mod;
    Context context;
    private LayoutInflater mInflater;
    public DietAdapter(Context context, ArrayList<entityWorkout> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);

        this.context=context;
        dba = new DataBaseAdapter(context);
        mod = new Models();
    }
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        // empty implementation
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.workout_row, null);
            holder = new ViewHolder();

          /*  holder.cardView1=(CardView)convertView.findViewById(R.id.card1);
            holder.time1=(TextView)convertView.findViewById(R.id.time1);
            holder.warm=(TextView) convertView.findViewById(R.id.warmup1);*/
            holder.cardView=(CardView)convertView.findViewById(R.id.card);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.bodypart = (TextView) convertView.findViewById(R.id.bodpart);
            holder.set = (TextView) convertView.findViewById(R.id.set);
            holder.remark = (TextView) convertView.findViewById(R.id.Remark);
            holder.rep = (TextView) convertView.findViewById(R.id.reps);
            holder.exersixe1=(TextView)convertView.findViewById(R.id.Ex);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        Date dateObj;
        try {
          /*  holder.warm.setText(searchArrayList.get(position).getWarmup());
            holder.time1.setText(searchArrayList.get(position).getTime());*/
            holder.time.setText(searchArrayList.get(position).getTime());
            holder.bodypart.setText(searchArrayList.get(position).getBodypart());
            holder.rep.setText("Repetitions :"+searchArrayList.get(position).getReps());
            holder.set.setText("Sets: "+searchArrayList.get(position).getSets());
            holder.remark.setText(searchArrayList.get(position).getRemark());
            holder.exersixe1.setText(searchArrayList.get(position).getExercise());




            final String id=searchArrayList.get(position).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return convertView;
    }


    static class ViewHolder {

        TextView time;
        TextView bodypart;
        TextView exersixe1;
        TextView set;
        TextView rep;
        TextView remark;
        CardView cardView;
        CardView cardView1;
        TextView time1;
        TextView warm;

    }
}
