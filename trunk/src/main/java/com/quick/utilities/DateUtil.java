/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;

import com.quick.global.GlobalConstants;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

/**
 *
 * @author sonalis
 */
public class DateUtil {   
        
    private static final String format="EEE MMM dd";
    private static SimpleDateFormat sdf = new SimpleDateFormat(format);
    
    private static final String ddMMyyyy="dd/MM/yyyy";
    private static SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat(ddMMyyyy);
    
    public static String formatDateInddMMyyyyFormat(Date date)
    {
        return ddMMyyyyFormat.format(date);        
    }
    
  public static Calendar getCalenderInstance(){
         Calendar c= Calendar.getInstance();
         return c;
     }  
  
  public static int getYear(Calendar c){
        return c.get(Calendar.YEAR);
     }
     
  public static int getDateDifference(String value, Date date) {
        
         java.util.Calendar calendar = java.util.Calendar.getInstance();
         calendar.setTime(date);
         int dob=calendar.get(Calendar.YEAR);
         
         return dob-Integer.parseInt(value);
        
    }
  
    public static int getDateDifference(Date startDate, Date endDate ) {

        return (int) ((startDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    public static String getTimeIntervalOfTheActivity(Date releasedate) 
    {
        String returnTime=GlobalConstants.emptyString;
        Date now =new Date();
        int minutes = Minutes.minutesBetween(new DateTime(releasedate), new DateTime(now)).getMinutes();
        if(minutes<60)
        {
            returnTime=minutes+" minutes ago";
        }
        else
        {
            int hours=Hours.hoursBetween(new DateTime(releasedate), new DateTime(now)).getHours();
            if(hours<24)
            {
                returnTime=hours+" hours ago";
            }
            else
            {
//                int days=Days.daysBetween(new DateTime(releasedate), new DateTime(now)).getDays();
//                if(days==1)
//                    returnTime=days+" day ago";
//                else
//                    returnTime=days+" days ago on "+sdf.format(releasedate);
                returnTime="on "+sdf.format(releasedate);
            }
            
        }
         
        return returnTime;
    }
    
}
