package edu.gwinnetttech.gtcnsabuddy.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Kyle on 11/17/2016.
 */

public class EmployeeResponse {
    @SerializedName("Data")
    public ArrayList<Employee> EmployeeResponse;
}
