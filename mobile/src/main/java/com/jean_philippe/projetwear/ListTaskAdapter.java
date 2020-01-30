package com.jean_philippe.projetwear;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ferdousur Rahman Sarker on 10/23/2017.
 */

public class ListTaskAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public ListTaskAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListTaskViewHolder holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.task_list_row, parent, false);
            holder.task_image = convertView.findViewById(R.id.task_image);
            holder.task_category = convertView.findViewById(R.id.task_category);
            holder.student_id = convertView.findViewById(R.id.student_id);
            holder.task_gps_lat = convertView.findViewById(R.id.task_gps_lat);
            holder.task_gps_long = convertView.findViewById(R.id.task_gps_long);
            holder.task_message = convertView.findViewById(R.id.task_list_message);
            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder) convertView.getTag();
        }
        holder.task_image.setId(position);
        holder.task_category.setId(position);
        holder.student_id.setId(position);
        holder.task_gps_lat.setId(position);
        holder.task_message.setId(position);
        holder.task_gps_long.setId(position);

        HashMap<String, String> song;
        song = data.get(position);

        try {
            holder.task_category.setText(song.get(TaskHome.KEY_CATEGORY));
            holder.student_id.setText(song.get(TaskHome.KEY_TASK));
            holder.task_gps_lat.setText(song.get(TaskHome.KEY_DATE));
            holder.task_message.setText(song.get(TaskHome.KEY_DESCRIPTION));
            holder.task_gps_long.setText(song.get(TaskHome.KEY_HOUR));
            /* Image */
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.task_image.setTextColor(color);
            holder.task_image.setText(HtmlCompat.fromHtml("&#11044;", HtmlCompat.FROM_HTML_MODE_LEGACY));
            /* Image */

        } catch (Exception ignored) {
        }
        return convertView;
    }
}

class ListTaskViewHolder {
    TextView task_image;
    TextView student_id, task_gps_lat, task_gps_long;
    TextView task_category, task_message;
}