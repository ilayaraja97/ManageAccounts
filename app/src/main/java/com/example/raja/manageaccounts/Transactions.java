package com.example.raja.manageaccounts;

import android.util.Log;

import java.util.Date;

/**
 * Created by raja on 09/06/17.
 */
public class Transactions {
    private int pid;
    private int tid;
    private float amount;
    private String description;
    private Date time_of_transaction;

    public Date getTime_of_transaction() {
        return time_of_transaction;
    }

    public void setTime_of_transaction(Date time_of_transaction) {
        Log.d("ilaya","hi from transactions1");
        this.time_of_transaction = time_of_transaction;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
