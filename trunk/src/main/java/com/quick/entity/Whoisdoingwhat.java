package com.quick.entity;
// Generated 27 Apr, 2013 12:52:34 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Whoisdoingwhat generated by hbm2java
 */

public class Whoisdoingwhat  implements java.io.Serializable {


     private int activityid;
     private Std std;
     private Sub sub;
     private Date activitydate;
     private String bywhom;
     private String doingwhat;
     private String fordiv;
     private String topic;
     private String topicintro;

    public Date getActivitydate() {
        return activitydate;
    }

    public void setActivitydate(Date activitydate) {
        this.activitydate = activitydate;
    }

    public int getActivityid() {
        return activityid;
    }

    public void setActivityid(int activityid) {
        this.activityid = activityid;
    }

    public String getBywhom() {
        return bywhom;
    }

    public void setBywhom(String bywhom) {
        this.bywhom = bywhom;
    }

    public String getDoingwhat() {
        return doingwhat;
    }

    public void setDoingwhat(String doingwhat) {
        this.doingwhat = doingwhat;
    }

    public String getFordiv() {
        return fordiv;
    }

    public void setFordiv(String fordiv) {
        this.fordiv = fordiv;
    }

    public Std getStd() {
        return std;
    }

    public void setStd(Std std) {
        this.std = std;
    }

    public Sub getSub() {
        return sub;
    }

    public void setSub(Sub sub) {
        this.sub = sub;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the topicintro
     */
    public String getTopicintro() {
        return topicintro;
    }

    /**
     * @param topicintro the topicintro to set
     */
    public void setTopicintro(String topicintro) {
        this.topicintro = topicintro;
    }

   


}


