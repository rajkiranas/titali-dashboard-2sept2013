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
public class DictWordDetailsBean 
{
    private String word;
     private String meaning;
     private String labels;
     private Date addDate;
     private String ownerUsername;
     private String ownerName;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
     
     
    
}
