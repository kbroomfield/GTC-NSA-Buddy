package edu.gwinnetttech.gtcnsabuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

        if ( view == null ) {
            view = inflater.inflate(R.layout.job_list_item, parent, false);
        }

        TextView txtJobID = (TextView) view.findViewById(R.id.job_ID);
        TextView txtJobDesc = (TextView) view.findViewById(R.id.job_desc);
        TextView txtJobAddress = (TextView) view.findViewById(R.id.job_address);

        Job job = jobList.get(position);

        // Should have used Kotlin. Gotta love that ?
        if ( job.getLatitude() != null && job.getLongitude() != null) {
            txtJobAddress.setText("Lat: " + job.getLatitude().toString() + " Long: " + job.getLongitude().toString());
        }

        txtJobID.setText(Integer.toString(job.getJobID()));

        return view;
    }

    public ArrayList<Job> getJobList() {
        return this.jobList;
    }



}
