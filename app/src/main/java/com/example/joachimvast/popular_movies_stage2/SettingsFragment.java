package com.example.joachimvast.popular_movies_stage2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import java.util.prefs.Preferences;

/**
 * Created by JoachimVAST on 05/03/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_movies);

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        for(int i= 0; i < count; i ++){
            Preference p = prefScreen.getPreference(i);
            String value = preferences.getString(p.getKey(), "");
            setPreferenceSummary(p, value);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    private void setPreferenceSummary(Preference pref, String value) {
        if(pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;

            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex >= 0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if(p != null){
            String value = sharedPreferences.getString(p.getKey(),"");
            setPreferenceSummary(p, value);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
