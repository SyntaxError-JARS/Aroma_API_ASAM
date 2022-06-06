package com.revature.aroma.customer;

//Todo: important

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

// this annotation is related to HibernateUtil
@Entity
@Table(name = "customer")

public class Customer {
    @Id
    @Column( name = "username", length =25)
    private String username;
    @Column(name = "fname", length = 20, nullable = false)
    private String fname;
    @Column(name = "lname", length = 20, nullable = false)
    private String lname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private double balance;

    //@Type(type= "true_false")
    @Column(name = "is_admin")
    private boolean isAdmin;

    public Customer () {};

    public Customer (String username, String fname, String lname, String password, double balance, boolean isAdmin){
        super();
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public Customer(String password){
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Customer{" +
                " username='" + username + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", balance='" + balance + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                "}";
    }
}
