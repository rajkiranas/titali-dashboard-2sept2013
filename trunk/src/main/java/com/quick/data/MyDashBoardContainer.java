/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.data;

import com.quick.bean.MasteParmBean;
import com.quick.bean.MyDashBoardBean;
import com.quick.entity.Notices;
import com.quick.entity.Whatsnew;
import com.quick.entity.Whoisdoingwhat;
import com.quick.global.GlobalConstants;
import com.quick.utilities.DateUtil;
import com.vaadin.data.util.BeanItemContainer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

/**
 *
 * @author suyogn
 */
public class MyDashBoardContainer extends BeanItemContainer<MyDashBoardBean> {
     public static final Object[] NATURAL_COL_ORDER_WHATS_NEW = new Object[]{
         "notification","dateTime"};
       
     //"notification"};
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH_WHATS_NEW = new String[]{
        "Whats New",GlobalConstants.emptyString};
    //"Notification's"};
    
    
    
    
     public static final Object[] NATURAL_COL_ORDER_NOtice = new Object[]{
       "notification"};
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH__NOtice = new String[]{
        "Notification's"};
    
    
    
     public static final Object[] NATURAL_COL_ORDER_Activity = new Object[]{
       "notification","dateTime"};
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH_Activity = new String[]{
        "Who's doing what",GlobalConstants.emptyString};
    
    
    

    public MyDashBoardContainer(){
        super(MyDashBoardBean.class);
    }
    
    public static MyDashBoardContainer getWhatsNewForMeContainer(List<Whatsnew>whatsnews) {
       MyDashBoardContainer boardContainer=null;
       MyDashBoardBean bean;
       boardContainer=new MyDashBoardContainer();
       try{
           
           for(Whatsnew w:whatsnews){
                 bean=new MyDashBoardBean();
                 bean.setNotification(w.getDisplaynotification());
                 bean.setItemid(String.valueOf(w.getItemid()));
                 bean.setTopicintro(w.getTopicintro());
                 //int minutesAgo=Minutes.minutesBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getMinutes();
                 
                 
                 
                 
                 
              /*  System.out.print("****** "+Days.daysBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getDays() + " days, ");
		System.out.print("****** "+Hours.hoursBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getHours() % 24 + " hours, ");
		System.out.print("****** "+Minutes.minutesBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getMinutes() % 60 + " minutes, ");
		System.out.print("****** "+Seconds.secondsBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getSeconds() % 60 + " seconds.");
                
                
                System.out.print("****** "+Days.daysBetween(new DateTime(new Date()), new DateTime(w.getReleasedate())).getDays() + " days, ");
		System.out.print("****** "+Hours.hoursBetween(new DateTime(new Date()), new DateTime(w.getReleasedate())).getHours() % 24 + " hours, ");
		System.out.print("****** "+Minutes.minutesBetween(new DateTime(new Date()), new DateTime(w.getReleasedate())).getMinutes() % 60 + " minutes, ");
		System.out.print("****** "+Seconds.secondsBetween(new DateTime(new Date()), new DateTime(w.getReleasedate())).getSeconds() % 60 + " seconds."); */
                String timeInterval = DateUtil.getTimeIntervalOfTheActivity(w.getReleasedate());
                bean.setDateTime(timeInterval);
                
                 boardContainer.addItem(bean);
           }
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return boardContainer;
        
    }
    
    
    
    
      public static MyDashBoardContainer getNoticesForMeContainer(List<Notices>noticeses) {
       MyDashBoardContainer boardContainer=null;
       MyDashBoardBean bean;
       boardContainer=new MyDashBoardContainer();
       try{
           for(Notices notices:noticeses){
                 bean=new MyDashBoardBean();
                 bean.setNotification(notices.getNoticeline());
                 
                 String timeInterval = DateUtil.getTimeIntervalOfTheActivity(notices.getNoticedate());
                 bean.setDateTime(timeInterval);
                 boardContainer.addItem(bean);
           }
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return boardContainer;
    }

      
       public static MyDashBoardContainer getWhoIsDoingWhatContainer(List<List> wraperList) {
       MyDashBoardContainer boardContainer=null;
       MyDashBoardBean bean;
       boardContainer=new MyDashBoardContainer();
       try{
       
           for(List<MasteParmBean> whoisdoingwhatsInnerList:wraperList)
           {
            for(MasteParmBean whoisdoingwhat:whoisdoingwhatsInnerList){
                bean = new MyDashBoardBean();
                /* bean.setItemid(""+whoisdoingwhat.getActivityId());
                bean.setBywhome(whoisdoingwhat.getByWhom());
                bean.setTopic(whoisdoingwhat.getTopic()); */
                bean.setNotification(whoisdoingwhat.getDisplaynotification());
                bean.setUploadId(String.valueOf(whoisdoingwhat.getUploadId()));   
                bean.setTopicintro(whoisdoingwhat.getTopicintro());                
                String timeInterval = DateUtil.getTimeIntervalOfTheActivity(whoisdoingwhat.getUploadDate());
                bean.setDateTime(timeInterval);
                bean.setStandard(whoisdoingwhat.getStd());
                bean.setClassToInvoke(whoisdoingwhat.getClassToInvoke());
                bean.setTopic(whoisdoingwhat.getTopic());
                bean.setVideoUrl(whoisdoingwhat.getVideoUrl());
                boardContainer.addItem(bean);
            }
       }
           
       }catch(Exception ex){
           ex.printStackTrace();
       }
           System.out.println("CCCCCCCCCCC boardContainer="+boardContainer.size());
       return boardContainer;
    }
        
}
