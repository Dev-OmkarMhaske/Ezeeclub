package com.tsysinfo.oneabove.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsysinfo.oneabove.R;
/**
 * Created by HP on 5/11/2016.
 */
public class CustomAndroidGridViewAdapter extends BaseAdapter {
    private final String[] string;
    private final int[] Imageid;
    private Context mContext;
    public CustomAndroidGridViewAdapter(Context c, String[] string, int[] Imageid) {
        mContext = c;
        this.Imageid = Imageid;
        this.string = string;
    }
 /*   @Override
    public int getCount() {
        return string.length;
    }
    @Override
    public Object getItem(int p) {
        return null;
    }
    @Override
    public long getItemId(int p) {
        return 0;
    }*/

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getCount() {
        return string.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int p, View convertView, ViewGroup parent) {
        CustomAndroidGridViewAdapter.ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_custom_layout, null);
            holder = new CustomAndroidGridViewAdapter.ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.gridview_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.gridview_image);

            convertView.setTag(holder);
        } else {
            holder = (CustomAndroidGridViewAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView.setText(string[p]);
        holder.imageView.setImageResource(Imageid[p]);
        return convertView;
    }
    static class ViewHolder {

        TextView textView;
        ImageView imageView;

    }
}