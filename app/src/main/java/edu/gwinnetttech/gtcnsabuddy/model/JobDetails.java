package edu.gwinnetttech.gtcnsabuddy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Kyle on 11/17/2016.
 *
 * This class goes against Java conventions but I didn't feel like annotating every propery with
 * SerializedName(""). This class corresponds directly with the JSON object returned by the GetJobDetails
 * web method and is meant to be used with GSON.
 *
 * Note: Some properties use different names for clarity.
 *
 */

public class JobDetails implements Serializable {
    private String Address;
    private String AppointmentDate;
    private String City;
    private String CompletionDate;
    private int CustomerID;
    private String CustomerType;
    private String Email;
    private Employee Employee;
    @SerializedName("FirstName") private String CustomerFirstName;
    private int JobID;
    @SerializedName("JobImage") private String JobImageUrl;
    private String JobNotes;
    private String JobPriority;
    private String JobStatus;
    @SerializedName("LastName") private String CustomerLastName;
    private double Latitude;
    private double Longitude;
    private String Phone;
    private String Service;
    private String State;
    private String Zip;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(long appointmentDateTimeMilliseconds) {
        AppointmentDate = JobDetails.formatDateTime(appointmentDateTimeMilliseconds);
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCompletionDate() {
        return CompletionDate;
    }

    public void setCompletionDate(long completionDate) {
        CompletionDate = JobDetails.formatDateTime(completionDate);
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getCustomerType() {
        return CustomerType;
    }

    public void setCustomerType(String customerType) {
        CustomerType = customerType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public edu.gwinnetttech.gtcnsabuddy.model.Employee getEmployee() {
        return Employee;
    }

    public void setEmployee(edu.gwinnetttech.gtcnsabuddy.model.Employee employee) {
        Employee = employee;
    }

    public String getCustomerFirstName() {
        return CustomerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        CustomerFirstName = customerFirstName;
    }

    public void setJobID(int jobID) {
        JobID = jobID;
    }

    public int getJobID() {
        return JobID;
    }

    public String getJobImageUrl() {
        return JobImageUrl;
    }

    public void setJobImageUrl(String jobImageUrl) {
        JobImageUrl = jobImageUrl;
    }

    public String getJobNotes() {
        return JobNotes;
    }

    public void setJobNotes(String jobNotes) {
        JobNotes = jobNotes;
    }

    public String getJobPriority() {
        return JobPriority;
    }

    public void setJobPriority(String jobPriority) {
        JobPriority = jobPriority;
    }

    public String getJobStatus() {
        return JobStatus;
    }

    public void setJobStatus(String jobStatus) {
        JobStatus = jobStatus;
    }

    public String getCustomerLastName() {
        return CustomerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        CustomerLastName = customerLastName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    /**
     * Static method to format the date/time in the format that the web service expects. This is static
     * because we will likely want to use it elsewhere and it didn't fit anywhere else.
     * @param timeInMilliseconds
     * @return
     */
    public static String formatDateTime(long timeInMilliseconds) {
        return "/Date(" + timeInMilliseconds + ")/";
    }
}
