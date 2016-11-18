package edu.gwinnetttech.gtcnsabuddy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

public class ActivityJobDetails extends AppCompatActivity {

    private boolean isActiveJob = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Job ID 12345");

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snackbarText;

                isActiveJob = !isActiveJob;

                if (isActiveJob){
                    fab.setImageDrawable(getDrawable(R.drawable.ic_job_done_w));
                    snackbarText = "Job marked as active.";
                }
                else {
                    fab.setImageDrawable(getDrawable(R.drawable.ic_active_job_w));
                    snackbarText = "Job marked done.";
                }

                Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_job_details, menu);

        return true;
    }
}
