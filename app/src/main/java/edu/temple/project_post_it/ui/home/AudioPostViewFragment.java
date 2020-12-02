package edu.temple.project_post_it.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.temple.project_post_it.R;

public class AudioPostViewFragment extends Fragment {

    private static final String POST_ID = "Post_ID";
    private String post_ID;

    public AudioPostViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post_ID = getArguments().getString(POST_ID);
        }
        Log.v("Post ID Equals:", post_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_post_view, container, false);
        return view;
    }
}