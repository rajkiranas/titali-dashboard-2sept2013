/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.entity;

import java.util.Date;

/**
 *
 * @author rajkirans
 */
public class AppointmentMst {
    private long appointmentId;     
     private String ownerName;
     private String ownerUsername;
     
     private Date starttime;
     private Date endtime;
     private String eventDescription;
     private String eventCaption;
     private String eventStyle;
     private Boolean isallday;
     private String forWhom;

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }


    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventCaption() {
        return eventCaption;
    }

    public void setEventCaption(String eventCaption) {
        this.eventCaption = eventCaption;
    }

    public String getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(String eventStyle) {
        this.eventStyle = eventStyle;
    }

    public Boolean getIsallday() {
        return isallday;
    }

    public void setIsallday(Boolean isallday) {
        this.isallday = isallday;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }
     
}
