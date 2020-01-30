package com.jean_philippe.projetwear;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

public class Adapteur extends RecyclerView.Adapter<Adapteur.MyViewHolder>
        implements Filterable {

    List<ModeleDonnees> dataModelArrayList;
    private List<ModeleDonnees> dataModelAll;
    private Filter filterResult = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModeleDonnees> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataModelAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModeleDonnees model : dataModelAll) {
                    if (model.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            dataModelArrayList.clear();
            dataModelArrayList.addAll((List<? extends ModeleDonnees>) filterResults.values);
            notifyDataSetChanged();
        }

    };

    public Adapteur(List<ModeleDonnees> datamodel) {
        this.dataModelArrayList = datamodel;
        this.dataModelAll = new ArrayList<>(dataModelArrayList);

    }

    public Integer getItem(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return filterResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set item layout using layout inflater
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ModeleDonnees dataModel = dataModelArrayList.get(position);
        holder.task_category.setText(dataModel.getCategory());
        holder.task_studrnt_id.setText(dataModel.getStudent_id());
        holder.gps_lat.setText(dataModel.getGps_lat());
        holder.gps_long.setText(dataModel.getGps_long());
        holder.message.setText(dataModel.getMessage());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(getItem(position));
        holder.task_image.setTextColor(color);
        holder.task_image.setText(HtmlCompat.fromHtml("&#11044;", HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre;
        TextView task_studrnt_id, gps_lat, gps_long, message;
        TextView task_category, task_image;

        public MyViewHolder(View view) {
            super(view);
            //initialize textViews and buttons
            task_image = view.findViewById(R.id.task_image);
            task_category = view.findViewById(R.id.task_category);
            task_studrnt_id = view.findViewById(R.id.student_id);
            message = view.findViewById(R.id.task_list_message);
        }
    }
}