package com.example.raja.manageaccounts;

/**
 * Created by raja on 09/06/17.
 */
public class Person {
    private int pid;
    private String name;
    private float cumulative_value;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCumulative_value() {
        return cumulative_value;
    }

    public void setCumulative_value(float cumulative_value) {
        this.cumulative_value = cumulative_value;
    }
}
