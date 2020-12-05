package edu.temple.project_post_it.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.temple.project_post_it.R;

public class PostDetailRecycleViewAdaptor extends RecyclerView.Adapter<PostDetailRecycleViewAdaptor.ViewHolder> {
    public List<String> groupList;


    public PostDetailRecycleViewAdaptor(ArrayList<String> data) {
        groupList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postdetailrecycleview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.getUserPost().setText(groupList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userPost;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userPost = itemView.findViewById(R.id.userDetailReply);
        }

        public TextView getUserPost() {
            return userPost;
        }


    }
}
