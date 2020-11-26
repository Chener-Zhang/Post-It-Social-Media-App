package edu.temple.project_post_it.ui.UserProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import edu.temple.project_post_it.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preferences;
    Context context;


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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
        if (key.equals("ANON KEY")) {
            SwitchPreference anon = findPreference(context.getResources().getString(R.string.anon_key));
            if(anon.isChecked())
                Toast.makeText(getActivity(), "Showing Anonymous Posts", Toast.LENGTH_SHORT).show();
        } else if (key.equals("GROUP KEY")) {
            SwitchPreference group = findPreference(context.getResources().getString(R.string.group_key));
            if(group.isChecked())
                Toast.makeText(getActivity(), "Showing Group Posts", Toast.LENGTH_SHORT).show();
        } else if (key.equals("ALL KEY")) {
            SwitchPreference all = findPreference(context.getResources().getString(R.string.all_key));
            if(all.isChecked())
                Toast.makeText(getActivity(), "Showing All Posts", Toast.LENGTH_SHORT).show();
        }
    }
}