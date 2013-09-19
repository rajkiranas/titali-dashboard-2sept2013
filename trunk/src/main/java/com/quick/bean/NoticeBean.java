/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.bean;

import java.util.Date;

/**
 *
 * @author rajkirans
 */
public class NoticeBean {
    
     private int noticeid;
     private String std;
     private String sub;
     private Date noticedate;
     private String bywhom;
     private String fordiv;
     private String noticeline;
     private String noticebody;
     private String visibletorole;

    public int getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(int noticeid) {
        this.noticeid = noticeid;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Date getNoticedate() {
        return noticedate;
    }

    public void setNoticedate(Date noticedate) {
        this.noticedate = noticedate;
    }

    public String getBywhom() {
        return bywhom;
    }

    public void setBywhom(String bywhom) {
        this.bywhom = bywhom;
    }

    public String getFordiv() {
        return fordiv;
    }

    public void setFordiv(String fordiv) {
        this.fordiv = fordiv;
    }

    public String getNoticeline() {
        return noticeline;
    }

    public void setNoticeline(String noticeline) {
        this.noticeline = noticeline;
    }

    public String getNoticebody() {
        return noticebody;
    }

    public void setNoticebody(String noticebody) {
        this.noticebody = noticebody;
    }

    public String getVisibletorole() {
        return visibletorole;
    }

    public void setVisibletorole(String visibletorole) {
        this.visibletorole = visibletorole;
    }
    
}
