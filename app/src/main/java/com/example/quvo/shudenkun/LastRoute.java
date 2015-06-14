package com.example.quvo.shudenkun;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by shikichi_takuya on 15/06/13.
 */
public class LastRoute {
    private String depature;
    private String destination;
    private Date depatureAt;

    public LastRoute(String depature, String destination, Date depatureAt) {
        this.depature = depature;
        this.destination = destination;
        this.depatureAt = depatureAt;
    }

    public String getDepature() {
        return depature;
    }

    public void setDepature(String depature) {
        this.depature = depature;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDepatureAt() {
        return depatureAt;
    }

}
