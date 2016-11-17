package edu.gwinnetttech.gtcnsabuddy;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gwinnetttech.gtcnsabuddy.model.JobDetails;

/**
 * Created by Albert on 10/30/2016.
 */

public class JobListAdapter extends ArrayAdapter<JobDetails> {
    private ArrayList<JobDetails> jobDetails;

    public JobListAdapter(Context context, ArrayList<JobDetails> jobDetails) {
        super(context, R.layout.job_list_item, jobDetails);
        this.jobDetails = jobDetails;

    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if ( view == null ) {
            view = inflater.inflate(R.layout.job_list_item, parent, false);
        }

        TextView txtJobID = (TextView) view.findViewById(R.id.job_ID);
        TextView txtJobDesc = (TextView) view.findViewById(R.id.job_desc);
        TextView txtJobAddress = (TextView) view.findViewById(R.id.job_address);

        JobDetails job = jobDetails.get(position);

        // Should have used Kotlin. Gotta love that ?
        if ( job.getAddress() != null ) {
            txtJobAddress.setText(job.getAddress());
        }
        if ( job.getService() != null ) {
            txtJobDesc.setText(job.getService());
        }

        txtJobID.setText(Integer.toString(job.getJobID()));

        return view;
    }



}
