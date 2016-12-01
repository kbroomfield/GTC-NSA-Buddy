package edu.gwinnetttech.gtcnsabuddy.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Kyle on 11/23/2016.
 */

public class JobDetailsResponse {
    @SerializedName("Data")
    public ArrayList<JobDetails> jobDetailsList;
}
