package edu.temple.project_post_it.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;


public class MyGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyGroupRecyclerViewAdapter.ViewHolder> {

    public List<String> groupList = null;
    public dataBaseManagement dataBaseManagement;

    public MyGroupRecyclerViewAdapter(List<String> groupList) {
        this.groupList = groupList;
        dataBaseManagement = new dataBaseManagement();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.get_group_name().setText(groupList.get(position));
        holder.get_delete_button().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write the delete code here
                String selectedGroup = groupList.get(position);
                dataBaseManagement.databaseRemoveGroup(selectedGroup);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (!(groupList == null)) {
            return groupList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            groupName = view.findViewById(R.id.groupName);
            deleteButton = view.findViewById(R.id.deleteButton);
        }

        public TextView get_group_name() {
            return groupName;
        }

        public ImageButton get_delete_button() {
            return deleteButton;
        }
    }


}