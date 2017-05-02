package com.example.joachimvast.popular_movies_stage2.Settings;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.joachimvast.popular_movies_stage2.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view and store the actionbar as a variable
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        // If the action bar is not null, we display the back to home button (=> MainActivity)
        if(actionBar != null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Enable back button to return to overview of movies
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
