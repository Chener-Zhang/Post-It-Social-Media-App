package edu.temple.project_post_it.ui.UserProfile;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import edu.temple.project_post_it.R;

public class UserProfileFragment extends Fragment {


    //Sign out
    public OnDataPass_UserProfileFragment main_activity;
    public Button sign_out_button;

    //Text view elements
    public TextView User_UID;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        main_activity = (OnDataPass_UserProfileFragment) context;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_userprofile, container, false);
        sign_out_button = root.findViewById(R.id.logout_button);
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sign out button", "click!");
                main_activity.sign_out();
            }
        });
        return root;
    }

    public interface OnDataPass_UserProfileFragment {
        public void sign_out();
    }
}
