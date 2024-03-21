package com.tsysinfo.oneabove.Adapters;
import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tsysinfo.oneabove.NotificationActivity;
import com.tsysinfo.oneabove.NotoEntity;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;
import com.tsysinfo.oneabove.database.NotificationTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
/**
 * Created by tsysinfo on 6/22/2017.
 */
public class NotificationAdapter extends BaseAdapter implements View.OnCreateContextMenuListener{
    private static ArrayList<NotoEntity> searchArrayList;

    DataBaseAdapter dba;
    Models mod;
    Context context;
    private LayoutInflater mInflater;
    public NotificationAdapter(Context context, ArrayList<NotoEntity> results) {
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
            convertView = mInflater.inflate(R.layout.notification_row, null);
            holder = new ViewHolder();
            holder.cardView=(CardView)convertView.findViewById(R.id.card);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.notification = (TextView) convertView.findViewById(R.id.noti);
            holder.mark = (Button) convertView.findViewById(R.id.mark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(searchArrayList.get(position).getTime());
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if(seconds<60)
            {
                holder.time.setText(seconds+" seconds ago");
                System.out.println(seconds+" seconds ago");
            }
            else if(minutes<60)
            {
                holder.time.setText(minutes+" minutes ago");
                System.out.println(minutes+" minutes ago");
            }
            else if(hours<24)
            {
                holder.time.setText(hours+" hours ago");
                System.out.println(hours+" hours ago");
            }
            else
            {
                holder.time.setText(days+" days ago");
                System.out.println(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        if (!searchArrayList.get(position).getStatus().equalsIgnoreCase("Read"))
        {

         holder.cardView.setBackgroundColor(Color.parseColor("#FFCFAFF4"));
        }else {
            holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.mark.setVisibility(View.GONE);
        }
        Date dateObj;
        try {
            holder.notification.setText(searchArrayList.get(position).getNoti());
            holder.mark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dba.open();
                    String sql="Update "+ NotificationTable.DATABASE_TABLE+" set status='Read' where id="+searchArrayList.get(position).getId();
                    DataBaseAdapter.ourDatabase.execSQL(sql);
                    dba.close();

                    if (context instanceof NotificationActivity)
                    {
                        ((NotificationActivity)context).setList();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
    static class ViewHolder {
        TextView time;
        TextView notification;
        Button mark;
        CardView cardView;

    }
}
