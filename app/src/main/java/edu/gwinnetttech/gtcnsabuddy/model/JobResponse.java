package edu.gwinnetttech.gtcnsabuddy.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Kyle on 11/18/2016.
 *
 * There has got to be a better way to do this.
 * 
 */

public class JobResponse {
    @SerializedName("Data")
    public ArrayList<Job> jobList;
}
