package com.tsysinfo.oneabove;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;

import java.util.ArrayList;
import java.util.Date;

public class RewardAdapter extends BaseAdapter implements View.OnCreateContextMenuListener{
    private static ArrayList<Rewards> searchArrayList;

    DataBaseAdapter dba;
    Models mod;
    Context context;

    int alphaAmount = 25; // Some value 0-255 where 0 is fully transparent and 255 is fully opaque
    int alphaAmount1 = 0; // Some value 0-255 where 0 is fully transparent and 255 is fully opaque
    // Some value 0-255 where 0 is fully transparent and 255 is fully opaque
    // Some value 0-255 where 0 is fully transparent and 255 is fully opaque

    private LayoutInflater mInflater;
    public RewardAdapter(Context context, ArrayList<Rewards> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);

        this.context=context;
        dba = new DataBaseAdapter(context);
        mod = new Models();
    }
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

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
        Rewards rewards=searchArrayList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.neww, null);
            holder = new RewardAdapter.ViewHolder();
            holder.cardView=(CardView)convertView.findViewById(R.id.card);
            holder.msg=(TextView)convertView.findViewById(R.id.msg);
            holder.messageno=(TextView)convertView.findViewById(R.id.messageno);
            holder.status=(TextView)convertView.findViewById(R.id.status);

            holder.rewardmessage=(TextView)convertView.findViewById(R.id.rewardmessage);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.llPan);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(holder);

        } else {
            holder = (RewardAdapter.ViewHolder) convertView.getTag();
        }

            holder.msg.setText(rewards.getMessage());
            holder.messageno.setText( rewards.getMessageNo());
            holder.status.setText("Status :  "+rewards.getStatus());

        String facility = searchArrayList.get(position).getMessageNo();
        String Status = searchArrayList.get(position).getStatus();

        if(facility.equals( rewards.getMessageNo())){

            holder.rewardmessage.setText("Rewards for " +rewards.getMessageNo() + " days");

        }

        if(Status.equals("Expired" )){

            holder.imageView.setImageResource(R.drawable.expired);

        }

        if(Status.equals("Active" )){

            holder.msg.setText(rewards.getMessage());

        }
        else{


        }
        if(Status.equals("Inactive" )){

            holder.imageView.setImageResource(R.drawable.logo23);

        }
        else{


        }


        return convertView;
    }


    static class ViewHolder {



        TextView Message;
        TextView branchno;
        TextView EmpName;
        TextView msg;
        TextView messageno;
        TextView reply;
        TextView senddt;
        TextView sendto;
        TextView type;
        TextView status,rewardmessage;
        TextView username;
        TextView sendby;
        ImageView imageView;
        LinearLayout linearLayout;





        CardView cardView;

    }
}