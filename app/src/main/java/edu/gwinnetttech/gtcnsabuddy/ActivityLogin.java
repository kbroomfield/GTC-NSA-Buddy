package edu.gwinnetttech.gtcnsabuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import edu.gwinnetttech.gtcnsabuddy.service.RemoteDataService;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private EditText employeeIdInput;
    private EditText passwordInput;
    private TextInputLayout idInputLayout;
    private TextInputLayout passwordInputLayout;

    private Location currentLocation;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Check SharedPreferences to see if the user is already logged in. If so, move to the main activity.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the location permissions that we need if we don't have them already.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 6);
        }

        // Set up the GoogleApiClient
        setupGoogleApiClient();

        idInputLayout = (TextInputLayout)findViewById(R.id.login_id_input_layout);
        passwordInputLayout = (TextInputLayout)findViewById(R.id.login_password_input_layout);
        passwordInput = (EditText)findViewById(R.id.login_input_password);
        employeeIdInput = (EditText)findViewById(R.id.login_input_id);
        currentLocation = null;

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if ( googleApiClient != null ) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if ( googleApiClient != null ) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Button click listener to log the user in.
     * @param v
     */
    @Override
    public void onClick(View v) {
        double latitude = 0, longitude = 0;

        if ( employeeIdInput.getText().toString().equals("") ) {
            idInputLayout.setError("Must enter Employee ID to login.");
            return;
        }

        int employeeId = Integer.parseInt(employeeIdInput.getText().toString());
        long timeIn = System.currentTimeMillis();
        String password = passwordInput.getText().toString();

        if ( currentLocation != null ) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
        }

        // Prepare a JSONObject to send in the request.
        try{
            JSONObject requestObject = new JSONObject();
            requestObject.put("EmployeeID", employeeId);
            requestObject.put("Latitude", latitude);
            requestObject.put("Longitude", longitude);
            requestObject.put("Password", password);
            requestObject.put("TimeIn", "/Date(" + timeIn + ")/");

            // Prepare the request.
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://www.jumpcreek.com/NSABuddy/Service1.svc/EmployeeLogIn", requestObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // TODO: Move to next activity, store the EmployeeID and LoginID.
                            Log.i("ActivityLogin", "Login Success!");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: More detailed error handling. Notify if password was bad or employee ID was bad.
                    Log.e("ActivityLogin", "Login failure.");
                    idInputLayout.setError("Login failure. Check Employee ID and try again.");
                }
            });

            Log.i("ActivityLogin", "Request body is: " + requestObject.toString());

            // Make the request.
//            RemoteDataService.getInstance(this).getRequestQueue().add(request);
        }
        catch ( JSONException ex ) {
            Log.e("ActivityLogin", ex.getMessage());
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("ActivityLogin", "Connected to Google API.");

        // Check that we have the correct permission so that the compiler doesn't complain.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("ActivityLogin", "Attempting to get location.");
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("ActivityLogin", "Location services connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ActivityLogin", "Location services connection failed.");
    }

    private void nextActivity() {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

    private void setupGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    private boolean handleSuccessfulLogin(JSONObject returnObject) {
        return (googleApiClient != null);
    }
}
