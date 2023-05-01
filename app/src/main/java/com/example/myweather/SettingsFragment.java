package com.example.myweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    ListPreference listPreference_temp, listPreference_speed;
    SwitchPreferenceCompat switchNotify;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    String u_speed, u_temp;
    Boolean notify;
    MainActivity mainActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                listPreference_speed = findPreference("unit_speed");
                listPreference_temp = findPreference("unit_temp");
                switchNotify = findPreference("notfications");
                notify = switchNotify.isChecked();
                u_speed = listPreference_speed.getValue();
                u_temp = listPreference_temp.getValue();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("unity_speed_pref", u_speed);
                editor.putString("unity_temp_pref", u_temp);
                editor.putBoolean("notify_check",notify);
                editor.apply();
            }
        };
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        mainActivity = (MainActivity) getActivity();
        mainActivity.binding.fab.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}