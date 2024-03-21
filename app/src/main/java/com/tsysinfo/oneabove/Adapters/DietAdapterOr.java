package com.tsysinfo.oneabove.Adapters;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import androidx.cardview.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tsysinfo.oneabove.R;
import com.tsysinfo.oneabove.Utils.DietEntity;
import com.tsysinfo.oneabove.database.DataBaseAdapter;
import com.tsysinfo.oneabove.database.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by tsysinfo on 6/22/2017.
 */
public class DietAdapterOr extends BaseAdapter implements View.OnCreateContextMenuListener{
    private static ArrayList<DietEntity> searchArrayList;

    DataBaseAdapter dba;
    Models mod;
    Context context;
    private LayoutInflater mInflater;
    public DietAdapterOr(Context context, ArrayList<DietEntity> results) {
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
            convertView = mInflater.inflate(R.layout.row_diet_hist_list, null);
            holder = new ViewHolder();

            holder.cardView=(CardView)convertView.findViewById(R.id.card);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.diet = (TextView) convertView.findViewById(R.id.diet);
            holder.copy=(Button)convertView.findViewById(R.id.copy);
            holder.buttonDelete=(Button)convertView.findViewById(R.id.buttonDelete);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Date dateObj;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("K:mm");
           dateObj = sdf.parse(searchArrayList.get(position).getTime());
            System.out.println(dateObj);


                    holder.copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", searchArrayList.get(position).getDiet());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "text copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        //holder.time.setText(new SimpleDateFormat("HH:mm aa").format(dateObj));
            holder.time.setText(searchArrayList.get(position).getTime());
        holder.diet.setText(searchArrayList.get(position).getDiet());
        final String id=searchArrayList.get(position).getId();
        } catch (final ParseException e) {
            e.printStackTrace();
        }



        return convertView;
    }


    static class ViewHolder {

        TextView time;
        TextView diet;
        TextView PlaceOfEating;
        Button copy;
        Button buttonDelete;
        CardView cardView;

    }
}
