package edu.gwinnetttech.gtcnsabuddy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.gwinnetttech.gtcnsabuddy.model.Job;
import edu.gwinnetttech.gtcnsabuddy.service.RemoteDataService;

public class ActivityJobDetails extends AppCompatActivity {

    // TODO: Tie this variable to the actual job status.
    private boolean isActiveJob = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get the job passed to this activity.
        Job selectedJob = (Job) getIntent().getParcelableExtra(ActivityMain.JOB_EXTRA);
        getSupportActionBar().setTitle("Job ID " + selectedJob.getJobID());

        // Get a reference to the RequestQueue and ImageLoader.
        RequestQueue requestQueue = RemoteDataService.getInstance(this).getRequestQueue();
        final ImageLoader imageLoader = RemoteDataService.getInstance(this).getImageLoader();

        // Make a request for the job details object.
        requestQueue.add(makeJobRequest("http://www.jumpcreek.com/nsabuddy/service1.svc/getjobdetails", selectedJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO: Parse the response into a JobDetails object and then populate the job_details_layout
                Log.i("ActivityJobDetails", response);
            }
        }));

        // Make a request to get any job images and then populate the NetworkImageView with the results.
        requestQueue.add(makeJobRequest("http://www.jumpcreek.com/nsabuddy/service1.svc/getjobimages", selectedJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    String imageUrl = new JSONObject(response).getJSONArray("Data").getJSONObject(0).getString("JobImageAddress");
                    NetworkImageView networkImageView = (NetworkImageView)findViewById(R.id.header_image);
                    networkImageView.setImageUrl(imageUrl, imageLoader);
                }
                catch ( JSONException je ) {
                    Log.e("ActivityJobDetails", je.getMessage());
                }

            }
        }));

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snackbarText;

                isActiveJob = !isActiveJob;

                if (isActiveJob) {
                    // TODO: Call the StartJob web method. Can use makeJobRequest.
                    fab.setImageDrawable(getDrawable(R.drawable.ic_job_done_w));
                    snackbarText = "Job marked as active.";
                } else {
                    // TODO: Call the CompleteJob web method. Can use makeJobRequest. May need to set the time.
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

    public StringRequest makeJobRequest(String url, final Job j, Response.Listener<String> onResponse) {
        return new StringRequest(
                Request.Method.POST,
                url,
                onResponse,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ActivityJobDetails", "An error occured: " + error.getMessage());
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                return gson.toJson(j).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
    }
}
