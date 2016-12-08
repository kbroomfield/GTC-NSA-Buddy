package edu.gwinnetttech.gtcnsabuddy;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.gwinnetttech.gtcnsabuddy.model.Job;

/**
 * Created by Albert on 10/30/2016.
 */

public class JobListAdapter extends ArrayAdapter<Job> {
    private ArrayList<Job> jobList;

    public JobListAdapter(Context context, ArrayList<Job> jobList) {
        super(context, R.layout.job_list_item, jobList);
        this.jobList = jobList;

    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());


        if (view == null) {
            view = inflater.inflate(R.layout.job_list_item, parent, false);
        }

        TextView txtJobID = (TextView) view.findViewById(R.id.job_ID);
        TextView txtJobDesc = (TextView) view.findViewById(R.id.job_desc);
        TextView txtJobAddress = (TextView) view.findViewById(R.id.job_address);
        ImageView imgJobImage = (ImageView) view.findViewById(R.id.customer_image);


        Job job = jobList.get(position);

        GeocoderTask addressTask = new GeocoderTask(txtJobAddress);
        addressTask.execute(job);

        txtJobID.setText(Integer.toString(job.getJobID()));

        txtJobDesc.setText(job.getJobNotes());

        // They are changing the service to add the pictures to JobDetails object, so it will already be available
        // here without another call. But you will want to go ahead anc change the imgJobImage type to a NetworkImageView.
        // Then we will use that to download the picture from the URL in the Job Object
//        imgJobImage.setImageURI(Uri.EMPTY); //TODO: get images for the jobList

        return view;
    }

    public ArrayList<Job> getJobList() {
        return this.jobList;
    }

    private class GeocoderTask extends AsyncTask<Job, Void, List<Address>> {

        private TextView txtJobAddress;

        public GeocoderTask(TextView txtJobAddress) {
            this.txtJobAddress = txtJobAddress;
        }

        @Override
        protected List<Address> doInBackground(Job... params) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> translatedAddress = null;

            Job j = params[0];

            if (j.getLongitude() != null && j.getLatitude() != null) {
                try {
                    translatedAddress = geocoder.getFromLocation(j.getLatitude(), j.getLongitude(), 1);
                } catch (IOException e) {
                    Log.e("GeocoderTask", e.getMessage());
                }
            }

            return translatedAddress;
        }

        @Override
        protected void onPostExecute(List<Address> addressList) {
            if (addressList != null && addressList.size() > 0) {
                String addressLine = addressList.get(0).getAddressLine(0);
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String zipcode = addressList.get(0).getPostalCode();
                txtJobAddress.setText(addressLine + "\n" + city + ", " + state + " " + zipcode);

            } else {
                txtJobAddress.setText("Address not available. Please contact Supervisor");
            }
        }
    }

}
