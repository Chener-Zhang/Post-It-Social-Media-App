package edu.temple.project_post_it.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.Post;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    public ArrayList<Post> postList;

    public dataBaseManagement dataBaseManagement;
    String current_post;
    //CustomAdapter Constructor
    public CustomAdapter(ArrayList<Post> postList) {
        //Pass the Array list to the local adapter
        this.postList = postList;
        dataBaseManagement = new dataBaseManagement();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //set the post_row_item.xml which can be customize
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.getTitle_textView().setText(post_list.get(position).getTitle());
        holder.getText_textView().setText(post_list.get(position).getText());
        Post post = post_list.get(position);
        current_post = post_list.get(position).getPost_ID();
        Bundle args = new Bundle();
        args.putString("Post_ID", current_post);
        int type = post.getType();
        if (type == 1){
            holder.getView_Button().setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_imagePostViewFragment, args));
        } else if (type == 2){
            holder.getView_Button().setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_audioPostViewFragment, args));
        } else {
            holder.getView_Button().setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_textPostViewFragment, args));
        }
        holder.getDelete_button().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseManagement.databaseRemoveData(current_post);
            }
        });


    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    //ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Declase the item in the ViewHolder
        ImageButton delete_button;
        Button viewButton;
        Button reply_Button;
        Spinner viewReplySpinner;
        TextView title_textView;
        TextView text_textView;


        //ViewHolder Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Init the variable in the post_row_item.xml -> findViewById
            title_textView = itemView.findViewById(R.id.post_title);
            text_textView = itemView.findViewById(R.id.group_name);
            delete_button = itemView.findViewById(R.id.delete_button);
            viewButton = itemView.findViewById(R.id.viewEditButton);
        }


        //Get title
        public TextView getTitle_textView() {
            return title_textView;
        }

        //Get text
        public TextView getText_textView() {
            return text_textView;
        }

        //Set delete button
        public ImageButton getDelete_button() {
            return delete_button;
        }

        public Button getView_Button(){
            return viewButton;
        }
    }


}
