package edu.gwinnetttech.gtcnsabuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import edu.gwinnetttech.gtcnsabuddy.model.Job;
import edu.gwinnetttech.gtcnsabuddy.model.JobDetails;
import edu.gwinnetttech.gtcnsabuddy.model.JobDetailsResponse;
import edu.gwinnetttech.gtcnsabuddy.service.RemoteDataService;

public class ActivityJobDetails extends AppCompatActivity {

    // TODO: Tie this variable to the actual job status.
    private boolean isActiveJob = false;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get the job passed to this activity.
        Job selectedJob = getIntent().getParcelableExtra(ActivityMain.JOB_EXTRA);
        getSupportActionBar().setTitle("Job ID " + selectedJob.getJobID());

        // Get a reference to the RequestQueue and ImageLoader.
        RequestQueue requestQueue = RemoteDataService.getInstance(this).getRequestQueue();
        final ImageLoader imageLoader = RemoteDataService.getInstance(this).getImageLoader();

        // Make a request for the job details object.
        requestQueue.add(makeJobRequest("http://www.jumpcreek.com/nsabuddy/service1.svc/getjobdetails", selectedJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                ArrayList<JobDetails> jobDetailsResponse = gson.fromJson(response, JobDetailsResponse.class).jobDetailsList;

                if ( jobDetailsResponse.size() > 0 ) {
                    JobDetails jobDetails = jobDetailsResponse.get(0);

                    populateJobDetailsLayout(jobDetails);
                }
            }
        }));

        // Make a request to get any job images and then populate the NetworkImageView with the results.
        requestQueue.add(makeJobRequest("http://www.jumpcreek.com/nsabuddy/service1.svc/getjobimages", selectedJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String imageUrl = new JSONObject(response).getJSONArray("Data").getJSONObject(0).getString("JobImageAddress");

                    imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            ImageView headerImageView = (ImageView) findViewById(R.id.header_image);
                            Bitmap image = response.getBitmap();
                            headerImageView.setImageBitmap(image);
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ActivityJobDetails", "An error occurred retrieving job picture: " + error.getMessage());
                        }
                    });

                } catch (JSONException je) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
//                Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show();
                takePicture();
                break;
            default:
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadImage(imageBitmap);

            ImageView headerImageView = (ImageView)findViewById(R.id.header_image);

            if ( headerImageView != null ) {
                headerImageView.setImageBitmap(imageBitmap);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ( requestCode == 6 ){
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private void takePicture() {
        // Get the location permissions that we need if we don't have them already.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 6);
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void uploadImage(Bitmap image) {
        JSONObject jsonObject = new JSONObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

    }

    private StringRequest makeJobRequest(String url, final Job j, Response.Listener<String> onResponse) {
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

    /**
     * Use the job details object to set the text in the job details layout.
     * @param jobDetails
     */
    private void populateJobDetailsLayout(JobDetails jobDetails) {
        TextView streetAddress = (TextView) findViewById(R.id.job_details_street_addr);
        TextView cityStateZip = (TextView) findViewById(R.id.job_details_city_st_zip);
        TextView email = (TextView) findViewById(R.id.job_details_email);
        TextView phone = (TextView) findViewById(R.id.job_details_phone);

        setTextNotNull(streetAddress, jobDetails.getAddress());
        setTextNotNull(cityStateZip, jobDetails.getCity());
        setTextNotNull(email, jobDetails.getEmail());
        setTextNotNull(phone, jobDetails.getPhone());
    }

    /**
     * If both the textView and the contents are not null, sets the text.
     * @param textView
     * @param contents
     */
    private void setTextNotNull(@Nullable TextView textView, @Nullable String contents) {
        if ( textView != null && contents != null ) {
            textView.setText(contents);
        }
    }
}
