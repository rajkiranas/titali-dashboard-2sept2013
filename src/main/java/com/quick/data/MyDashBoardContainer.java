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
import com.vaadin.data.util.BeanItemContainer;
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
        "Notification's",""};
    //"Notification's"};
    
    
    
    
     public static final Object[] NATURAL_COL_ORDER_NOtice = new Object[]{
       "notification"};
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH__NOtice = new String[]{
        "Notice"};
    
    
    
     public static final Object[] NATURAL_COL_ORDER_Activity = new Object[]{
       "notification"};
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH_Activity = new String[]{
        "Notification's"};

    

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
                 //int minutesAgo=Minutes.minutesBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getMinutes();
                 
                 String timeInterval = getTimeIntervalOfTheActivity(w.getReleasedate());
                 
                 
                 
                /* System.out.print("****** "+Days.daysBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getDays() + " days, ");
		System.out.print("****** "+Hours.hoursBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getHours() % 24 + " hours, ");
		System.out.print("****** "+Minutes.minutesBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getMinutes() % 60 + " minutes, ");
		System.out.print("****** "+Seconds.secondsBetween(new DateTime(w.getReleasedate()), new DateTime(new Date())).getSeconds() % 60 + " seconds."); */
                
                bean.setDateTime(timeInterval);
                
                 boardContainer.addItem(bean);
           }
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return boardContainer;
        
    }
    
    private static String getTimeIntervalOfTheActivity(Date releasedate) 
    {
        String returnTime=GlobalConstants.emptyString;
        Date now =new Date();
        int minutes = Minutes.minutesBetween(new DateTime(releasedate), new DateTime(now)).getMinutes()%60;
        if(minutes<60)
        {
            returnTime=minutes+" minutes ago";
        }
        else
        {
            int hours=Hours.hoursBetween(new DateTime(releasedate), new DateTime(now)).getHours()%60;
            if(hours<24)
            {
                returnTime=hours+" hours ago";
            }
            else
            {
                int days=Days.daysBetween(new DateTime(releasedate), new DateTime(now)).getDays()%24;
                if(days==1)
                    returnTime=days+" day ago";
                else
                    returnTime=days+" days ago";
            }
            
        }
         
        return returnTime;
    }
    
    
      public static MyDashBoardContainer getNoticesForMeContainer(List<Notices>noticeses) {
       MyDashBoardContainer boardContainer=null;
       MyDashBoardBean bean;
       boardContainer=new MyDashBoardContainer();
       try{
        
             
           for(Notices notices:noticeses){
                 bean=new MyDashBoardBean();
                 bean.setNotification(notices.getNoticeline());
                 boardContainer.addItem(bean);
           }
           
           
       }catch(Exception ex){
           ex.printStackTrace();
       }
       
       
       
       return boardContainer;
        
    }

      
       public static MyDashBoardContainer getWhoIsDoingWhatContainer(List<MasteParmBean>whoisdoingwhats) {
       MyDashBoardContainer boardContainer=null;
       MyDashBoardBean bean;
       boardContainer=new MyDashBoardContainer();
       try{
       
            for(MasteParmBean whoisdoingwhat:whoisdoingwhats){
                bean = new MyDashBoardBean();
                /* bean.setItemid(""+whoisdoingwhat.getActivityId());
                bean.setBywhome(whoisdoingwhat.getByWhom());
                bean.setTopic(whoisdoingwhat.getTopic()); */
                bean.setNotification(whoisdoingwhat.getDisplaynotification());
                bean.setUploadId(String.valueOf(whoisdoingwhat.getUploadId()));                
                boardContainer.addItem(bean);
            }
           
           
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return boardContainer;
        
    }
        
}
