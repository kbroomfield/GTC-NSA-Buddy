package edu.gwinnetttech.gtcnsabuddy.model;

import java.io.Serializable;

/**
 * Created by Kyle on 11/17/2016.
 *
 * This class does not follow Java naming conventions. It is written to conform to the Employee JSON
 * object returned by the NSA Buddy web service and is meant to be used with GSON.
 *
 * "Email": "LawDawg@hotmail.com",
 "EmployeeID": 1,
 "FirstName": "Lawrence ",
 "LastName": "D'Addio",
 "Password": "happyDance99",
 "Phone": "770-899-9632"
 */

public class Employee implements Serializable {
    private int EmployeeID;
    private String Email;
    private String FirstName;
    private String LastName;
    private String Password;
    private String Phone;

    public Employee() {
    }

    public Employee(int employeeID, String email, String firstName, String lastName, String password, String phone) {
        EmployeeID = employeeID;
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
        Phone = phone;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
