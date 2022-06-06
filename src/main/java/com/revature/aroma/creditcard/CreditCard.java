package com.revature.aroma.creditcard;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "credit_card")

public class CreditCard {
    @Id
    @Column(name = "cc_number")
    private String ccNumber;

    @Column(name = "cc_name", length = 20, nullable = false)
    private String ccName;

    private int ccv;
    @Column(name = "exp_date", length = 15, nullable = false)
    private String expDate;

    private int zip;
    @Column(name = "limit_l")
    private double creditLimit;
    @Column(name = "customer_username", length = 25, nullable = false)
    private String customerUsername;

 /*   @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer", referencedColumnName = "usernameUsername")
    private Customer customer;*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return ccv == that.ccv && zip == that.zip && Double.compare(that.creditLimit, creditLimit) == 0 && Objects.equals(ccNumber, that.ccNumber) && Objects.equals(ccName, that.ccName) && Objects.equals(expDate, that.expDate) && Objects.equals(customerUsername, that.customerUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ccNumber, ccName, ccv, expDate, zip, creditLimit, customerUsername);
    }

    public CreditCard() {};

    public CreditCard(String ccNumber, String ccName, int ccv, String expDate, int zip, double creditLimit, String customerUsername) {
        super();
        this.ccNumber = ccNumber;
        this.ccName = ccName;
        this.ccv = ccv;
        this.expDate = expDate;
        this.zip = zip;
        this.creditLimit = creditLimit;
        this.customerUsername = customerUsername;
    }


    public CreditCard(String ccNumber, double creditLimit) {
        this.ccNumber = ccNumber;
        this.creditLimit = creditLimit;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public int getCcv() {
        return ccv;
    }

    public void setCcv(int ccv) {
        this.ccv = ccv;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

/*    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }*/

    @Override
    public String toString() {
        return "CreditCard{" +
                "ccNumber='" + ccNumber + '\'' +
                ", ccName='" + ccName + '\'' +
                ", ccv=" + ccv +
                ", expDate='" + expDate + '\'' +
                ", zip=" + zip +
                ", limit=" + creditLimit +
                ", customerUsername='" + customerUsername + '\'' +

                '}';
    }
}
