package com.example.raja.manageaccounts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private static ProgressBar spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //spinner=(ProgressBar)findViewById(R.id.progressBar2);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference update = findPreference("update");

            update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    String url = "https://github.com/ilayaraja97/ManageAccounts";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return true;
                }
            });
            Preference backup = findPreference("backup_now");
            backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    ExportImport.export(getActivity());
                    Toast.makeText(getActivity(), "Backup created in Downloads folder", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            Preference restore = findPreference("restore_now");
            restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    ExportImport.importDb(getActivity());
                    Toast.makeText(getActivity(), "Restored from Downloads folder", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

    }

}