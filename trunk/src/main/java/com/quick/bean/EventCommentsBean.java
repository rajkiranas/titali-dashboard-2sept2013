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
public class EventCommentsBean 
{
     private String name;
     private String commentBody;
     private int eventDetailId;
     private String username;
     private Date commentTime;

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public int getEventDetailId() {
        return eventDetailId;
    }

    public void setEventDetailId(int eventDetailId) {
        this.eventDetailId = eventDetailId;
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
