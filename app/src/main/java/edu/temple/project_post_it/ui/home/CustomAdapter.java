package edu.temple.project_post_it.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.temple.project_post_it.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    public ArrayList<String> stringArrays;

    //CustomAdapter Constructor
    public CustomAdapter(ArrayList<String> test_data) {
        this.stringArrays = test_data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(stringArrays.get(position));
    }

    @Override
    public int getItemCount() {
        return stringArrays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        //ViewHolder Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rv_post_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }


}
