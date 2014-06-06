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
public class ForumEventDetailsBean 
{
    private int eventDetailId;
     private Date eventDate;
     private String eventDesc;
     private String eventBody;
     private String eventOwner;
     private byte[] eventImage;
     private String stringImage;
     private Integer parentForumId;
     private String imageFileName;
     private List<EventLikeBean> eventLikesList;
     private String videoUrl;

    @Override
    public String toString() {
        return "ForumEventDetailsBean{" + "eventDetailId=" + eventDetailId + ", eventDate=" + eventDate + ", eventDesc=" + eventDesc + ", eventBody=" + eventBody + ", eventOwner=" + eventOwner + ", eventImage=" + eventImage + ", stringImage=" + stringImage + ", parentForumId=" + parentForumId + ", imageFileName=" + imageFileName + ", eventLikesList=" + eventLikesList + '}';
    }
     
     

    public int getEventDetailId() {
        return eventDetailId;
    }

    public void setEventDetailId(int eventDetailId) {
        this.eventDetailId = eventDetailId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventBody() {
        return eventBody;
    }

    public void setEventBody(String eventBody) {
        this.eventBody = eventBody;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }

    public Integer getParentForumId() {
        return parentForumId;
    }

    public void setParentForumId(Integer parentForumId) {
        this.parentForumId = parentForumId;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * @return the stringImage
     */
    public String getStringImage() {
        return stringImage;
    }

    /**
     * @param stringImage the stringImage to set
     */
    public void setStringImage(String stringImage) {
        this.stringImage = stringImage;
    }
     
    /**
     * @return the eventLikesList
     */
    public List<EventLikeBean> getEventLikesList() {
        return eventLikesList;
    }

    /**
     * @param eventLikesList the eventLikesList to set
     */
    public void setEventLikesList(List<EventLikeBean> eventLikesList) {
        this.eventLikesList = eventLikesList;
    }

    /**
     * @return the videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * @param videoUrl the videoUrl to set
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
