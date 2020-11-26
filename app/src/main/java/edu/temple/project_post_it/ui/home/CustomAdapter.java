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

    public ArrayList<String> post_title;
    public ArrayList<String> post_text;

    //CustomAdapter Constructor
    public CustomAdapter(ArrayList<String> post_title, ArrayList<String> post_text) {
        this.post_title = post_title;
        this.post_text = post_text;
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
        holder.getTitle_textView().setText(post_title.get(position));
        holder.getText_textView().setText(post_text.get(position));

    }

    @Override
    public int getItemCount() {
        return post_title.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_textView;
        TextView text_textView;

        //ViewHolder Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_textView = itemView.findViewById(R.id.post_title);
            text_textView = itemView.findViewById(R.id.post_text);
        }

        public TextView getTitle_textView() {
            return title_textView;
        }

        public TextView getText_textView() {
            return text_textView;
        }

    }


}
