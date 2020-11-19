package edu.temple.project_post_it.ui.UserProfile;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import edu.temple.project_post_it.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}