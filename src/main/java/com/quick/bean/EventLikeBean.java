/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.bean;


import java.util.Date;
import java.util.List;

/**
 *
 * @author rajkirans
 */
public class EventLikeBean 
{
   private String name;
   private int eventDetailId;
   private String username;
   private Date likeTime;

    @Override
    public String toString() {
        return "EventLikeBean{" + "name=" + name + ", eventDetailId=" + eventDetailId + ", username=" + username + ", likeTime=" + likeTime + '}';
    }
   
   

    public int getEventDetailId() {
        return eventDetailId;
    }

    public void setEventDetailId(int eventDetailId) {
        this.eventDetailId = eventDetailId;
    }

    public Date getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(Date likeTime) {
        this.likeTime = likeTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
   
   
}
