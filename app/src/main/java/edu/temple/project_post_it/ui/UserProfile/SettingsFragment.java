package edu.temple.project_post_it.ui.UserProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import edu.temple.project_post_it.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preferences;
    Context context;
    boolean anon, group, all;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //restore settings on application coming into foreground
    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(context)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    //save settings when activity paused
    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(context)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //clean up with some test code but preferences saved, need to implement flags in database
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "ANON KEY":
                anon = !anon;
                Toast.makeText(getActivity(), "Show Anon is " + String.valueOf(anon), Toast.LENGTH_SHORT).show();
            case "GROUP KEY":
                group = !group;
                Toast.makeText(getActivity(), "Show Group is " + String.valueOf(group), Toast.LENGTH_SHORT).show();
            case "ALL KEY":
                all = !all;
                Toast.makeText(getActivity(), "Show All is " + String.valueOf(all), Toast.LENGTH_SHORT).show();

        }
    }
}