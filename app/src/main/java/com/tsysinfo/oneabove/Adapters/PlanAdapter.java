package com.tsysinfo.oneabove.Adapters;
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

import com.tsysinfo.oneabove.PlanEntity;
import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;

import java.util.ArrayList;
import java.util.Date;
/**
 * Created by tsysinfo on 6/22/2017.
 */
public class PlanAdapter extends BaseAdapter implements View.OnCreateContextMenuListener{
    private static ArrayList<PlanEntity> searchArrayList;

    DataBaseAdapter dba;
    Models mod;
    Context context;
    private LayoutInflater mInflater;
    public PlanAdapter(Context context, ArrayList<PlanEntity> results) {
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
        PlanEntity planEntity=searchArrayList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.plan_row, null);
            holder = new ViewHolder();


            holder.active = (TextView) convertView.findViewById(R.id.Status);
            holder.saleamt = (TextView) convertView.findViewById(R.id.sellAmt);
            holder.startdate = (TextView) convertView.findViewById(R.id.startDate);
            holder.program = (TextView) convertView.findViewById(R.id.Program);
            holder.enddate = (TextView) convertView.findViewById(R.id.enddate);
            holder.paidamt = (TextView) convertView.findViewById(R.id.paidAmt);
            holder.balance = (TextView) convertView.findViewById(R.id.balAmt);
            holder.planname = (TextView) convertView.findViewById(R.id.PlanName);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.llPan);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        Date dateObj;
        try {



            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.linearLayout.getVisibility()==View.GONE)
                    {
                        holder.linearLayout.setVisibility(View.VISIBLE);
                        holder.imageView.setImageResource(R.drawable.expand);
                    }else {
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.imageView.setImageResource(R.drawable.down);
                    }

                }
            });
            holder.active.setText(planEntity.getActive());
            holder.saleamt.setText("Amount: "+planEntity.getSaleAmount());
            holder.startdate.setText(planEntity.getStartDt());
            holder.enddate.setText(planEntity.getEndDt());
            holder.planname.setText("Plan Name: "+planEntity.getPlanName());
            holder.balance.setText("Balance: "+planEntity.getBalanceAmount());
            holder.paidamt.setText("Paid: "+planEntity.getPaidAmount());
            holder.program.setText(planEntity.getProgramName());




           /* holder.time.setText(searchArrayList.get(position).getTime());
            holder.bodypart.setText(searchArrayList.get(position).getBodypart());
            holder.excersixe.setText(searchArrayList.get(position).getWorkout());
            holder.rep.setText(searchArrayList.get(position).getReps());
            holder.set.setText(searchArrayList.get(position).getSets());
            holder.remark.setText(searchArrayList.get(position).getRemark());
*/


        } catch (Exception e) {
            e.printStackTrace();
        }



        return convertView;
    }


    static class ViewHolder {



        TextView active;
        TextView balance;
        TextView enddate;
        TextView startdate;
        TextView paidamt;
        TextView planname;
        TextView program;
        TextView saleamt;
        ImageView imageView;
        LinearLayout linearLayout;





        CardView cardView;

    }
}
