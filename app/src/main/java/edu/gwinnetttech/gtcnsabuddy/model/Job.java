package edu.gwinnetttech.gtcnsabuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Kyle on 11/18/2016.
 *
 * A class to wrap the simple job details returned from the API.
 *
 * {"ApptDateTime":"\/Date(1478557200000-0800)\/","CompDateTime":null,"CustomerID":119,"EmployeeID":1,"JobID":8873,"JobNotes":"Test Notes",
 * "JobPriorityID":null,"JobStatusID":3,"Latitude":34.005734,"Longitude":-83.966391,"ServiceID":10,"StartDateTime":"\/Date(1478558400000-0800)\/"}
 */

public class Job implements Parcelable {
    private String ApptDateTime;
    private String CompDateTime;
    private int CustomerID;
    private int EmployeeID;
    private int JobID;
    private String JobNotes;
    private Integer JobPriorityID;
    private int JobStatusID;
    private Double Latitude;
    private Double Longitude;
    private int ServiceID;
    private String StartDateTime;

    public Job() {

    }

    private Job(Parcel input) {
        ApptDateTime = input.readString();
        CompDateTime = input.readString();
        CustomerID = input.readInt();
        EmployeeID = input.readInt();
        JobNotes = input.readString();
        JobPriorityID = input.readInt();
        JobStatusID = input.readInt();
        Latitude = input.readDouble();
        Longitude = input.readDouble();
        ServiceID = input.readInt();
        StartDateTime = input.readString();
        JobID = input.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ApptDateTime);
        dest.writeString(CompDateTime);
        dest.writeInt(CustomerID);
        dest.writeInt(EmployeeID);
        dest.writeString(JobNotes);

        if ( JobPriorityID != null ) {
            dest.writeInt(JobPriorityID);
        }
        else {
            dest.writeInt(-1);
        }

        dest.writeInt(JobStatusID);

        if ( Latitude != null ) {
            dest.writeDouble(Latitude);
        }
        else {
            dest.writeDouble(-1);
        }

        if ( Longitude != null ) {
            dest.writeDouble(Longitude);
        }
        else {
            dest.writeDouble(-1);
        }

        dest.writeInt(ServiceID);
        dest.writeString(StartDateTime);
        dest.writeInt(JobID);
    }

    public String getApptDateTime() {
        return ApptDateTime;
    }

    public void setApptDateTime(String apptDateTime) {
        ApptDateTime = apptDateTime;
    }

    public String getCompDateTime() {
        return CompDateTime;
    }

    public void setCompDateTime(String compDateTime) {
        CompDateTime = compDateTime;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public int getJobID() {
        return JobID;
    }

    public void setJobID(int jobID) {
        JobID = jobID;
    }

    public String getJobNotes() {
        return JobNotes;
    }

    public void setJobNotes(String jobNotes) {
        JobNotes = jobNotes;
    }

    public int getJobStatusID() {
        return JobStatusID;
    }

    public void setJobStatusID(int jobStatusID) {
        JobStatusID = jobStatusID;
    }

    public Integer getJobPriorityID() {
        return JobPriorityID;
    }

    public void setJobPriorityID(Integer jobPriorityID) {
        JobPriorityID = jobPriorityID;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public static final Parcelable.Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel source) {
            return new Job(source);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };


}
