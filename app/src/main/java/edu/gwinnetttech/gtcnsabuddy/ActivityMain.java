package edu.gwinnetttech.gtcnsabuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.gwinnetttech.gtcnsabuddy.model.Employee;
import edu.gwinnetttech.gtcnsabuddy.model.JobDetails;
import edu.gwinnetttech.gtcnsabuddy.model.EmployeeResponse;
import edu.gwinnetttech.gtcnsabuddy.service.RemoteDataService;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int employeeId;
    private int loginId;

    private RemoteDataService remoteDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        remoteDataService = RemoteDataService.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupListView();
        setupNavigationHeader();

        employeeId = getSharedPreferences(ActivityLogin.NSA_PREFERENCES, Context.MODE_PRIVATE).getInt(ActivityLogin.EMPLOYEE_ID, -1);
        loginId = getSharedPreferences(ActivityLogin.NSA_PREFERENCES, Context.MODE_PRIVATE).getInt(ActivityLogin.LOGIN_ID, -1);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch ( id ) {
            case R.id.menu_job_list:
                break;
            case R.id.menu_active_job:
                break;
            case R.id.menu_settings:
                break;
            case R.id.menu_logout:
                employeeLogout();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Builds an request that needs an Employee object.
     * @param url
     * @param responseHandler
     * @return
     */
    private StringRequest buildEmployeeRequest(String url, Response.Listener<String> responseHandler){
        // Using a StringRequest to allow Gson usage. In retrospect this turned out to be harder than just doing a JSONObject request and calling toString() on it..
        return new StringRequest(
                Request.Method.POST,
                url,
                responseHandler,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ActivityMain", error.getMessage());
                    }
                }
        ){
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new GsonBuilder().create();
                String requestObject = gson.toJson(new Employee(employeeId, "", "", "", "", ""));
                Log.i("ActivityLogin", requestObject);
                return requestObject.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
    }

    private void setupListView(){
        // Setup the listview. Using mocked JobDetails for testing.
        ArrayList<JobDetails> jobDetails = new ArrayList<>();

        for ( int i = 1; i < 20; i++ ){
            JobDetails job = new JobDetails();
            job.setJobID(i);
            job.setService("Painting");
            job.setAddress("75 Maddox Road, Buford, GA 30518");
            jobDetails.add(job);
        }

        ListView listView = (ListView)findViewById(R.id.job_list_view);
        JobListAdapter jobListAdapter = new JobListAdapter(this, jobDetails);

        listView.setAdapter(jobListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startJobDetailsIntent = new Intent(view.getContext(), ActivityJobDetails.class);
                startActivity(startJobDetailsIntent);
            }
        });
    }

    private void employeeLogout() {
        try{
            JSONObject requestObject = new JSONObject();
            requestObject.put("EmployeeID", employeeId);
            requestObject.put("Latitude", 0);
            requestObject.put("Longitude", 0);
            requestObject.put("TimeIn", "/Date(12345)/");
            requestObject.put("TimeOut", "/Date(" + System.currentTimeMillis() + ")/");
            requestObject.put("LoginID", loginId);

            Log.i("ActivityMain", requestObject.toString());

            // Prepare the request.
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://www.jumpcreek.com/NSABuddy/Service1.svc/EmployeeLogOut", requestObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("ActivityMain", "Successfully logged out.");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: More detailed error handling.
                    Log.e("ActivityMain", "HTTP logout failure: " + error.getMessage());
                }
            });

            remoteDataService.getRequestQueue().add(request);
        }
        catch (JSONException je) {
            Log.e("ActivityMain", "An error occured while logging out: " + je.getMessage());
        }

        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(ActivityLogin.NSA_PREFERENCES ,MODE_PRIVATE).edit();
        sharedPreferencesEditor.remove(ActivityLogin.LOGIN_ID);
        sharedPreferencesEditor.remove(ActivityLogin.EMPLOYEE_ID);
        sharedPreferencesEditor.apply();

        // TODO: Prevent back navigation to this activity.
        Intent startLoginActivity = new Intent(this, ActivityLogin.class);
        startActivity(startLoginActivity);

    }

    private void setupNavigationHeader() {
        StringRequest employeeRequest = buildEmployeeRequest("http://www.jumpcreek.com/NSABuddy/Service1.svc/GetEmployeeByID", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                EmployeeResponse employeeResponse = gson.fromJson(response, EmployeeResponse.class);
                Employee employee = employeeResponse.EmployeeResponse.get(0);

                TextView navEmail = (TextView)findViewById(R.id.nav_employee_email);
                TextView navName = (TextView)findViewById(R.id.nav_employee_name);

                if ( employee.getFirstName() != null && employee.getLastName() != null ){
                    navName.setText(employee.getFirstName() + " " + employee.getLastName());
                }

                if ( employee.getEmail() != null ) {
                    navEmail.setText(employee.getEmail());
                }
            }
        });

        // TODO: Fix employee image request. NetworkImageView is a null ref.
//        final NetworkImageView networkImageView = (NetworkImageView)findViewById(R.id.nav_image_view);
//        final RemoteDataService remoteDataServiceInstance = RemoteDataService.getInstance(this);

        StringRequest employeeImageRequest = buildEmployeeRequest("http://www.jumpcreek.com/nsabuddy/Service1.svc/GetEmployeeImage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonResponse = new JSONObject(response).getJSONArray("Data");
                    String imageUrl = jsonResponse.getJSONObject(0).getString("ImageAddress");

                    NetworkImageView networkImageView = (NetworkImageView)findViewById(R.id.nav_image_view);
                    networkImageView.setImageUrl(imageUrl, remoteDataService.getImageLoader());

                }
                catch ( JSONException je ) {
                    Log.e("ActivityMain", "Error parsing JSON for employee image request: " + je.getMessage());
                }

            }
        });

        remoteDataService.getRequestQueue().add(employeeRequest);
        remoteDataService.getRequestQueue().add(employeeImageRequest);
    }
}
