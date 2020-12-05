package edu.temple.project_post_it.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.temple.project_post_it.R;
import edu.temple.project_post_it.dataBaseManagement;
import edu.temple.project_post_it.post.Post;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    public ArrayList<Post> post_list;
    private final Activity activity;
    public dataBaseManagement dataBaseManagement;

    //CustomAdapter Constructor
    public CustomAdapter(Activity activity, ArrayList<Post> post_list) {
        //Pass the Array list to the local adapter
        this.post_list = post_list;
        this.activity = activity;
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
        holder.getDelete_button().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write the delete code here
                String current_post = post_list.get(position).getPost_ID();
                dataBaseManagement.databaseRemovePostInMembers(current_post, post_list.get(position).getGroupID());
            }
        });
        holder.getReplyButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.80);
                int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.50);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.replydialog);
                dialog.getWindow().setLayout(width, height);
                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return post_list.size();
    }

    //ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Declase the item in the ViewHolder
        ImageButton delete_button;
        Button replyButton;

        TextView title_textView;
        TextView text_textView;


        //ViewHolder Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Init the variable in the post_row_item.xml -> findViewById
            title_textView = itemView.findViewById(R.id.post_title);
            text_textView = itemView.findViewById(R.id.group_name);
            delete_button = itemView.findViewById(R.id.delete_button);
            replyButton = itemView.findViewById(R.id.reply_button);
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

        public Button getReplyButton() {
            return replyButton;
        }
    }


}
