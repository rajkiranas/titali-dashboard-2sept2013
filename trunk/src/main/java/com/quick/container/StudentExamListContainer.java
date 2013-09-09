/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.container;

import com.quick.bean.ExamBean;
import com.quick.global.GlobalConstants;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rajkiran
 */
public class StudentExamListContainer extends BeanItemContainer<ExamBean> {
    public static Object[] NATURAL_COL_ORDER_EXAM_LIST={"examId","exName","examTypeName","startDt","endDt"};
    public static String[] COL_HEADERS_ENGLISH_EXAM_LIST={"Exam Id","Exam name","Exam type","Start date","End date"};
    
    
   public static Object[] NATURAL_COL_ORDER_EXAM_PRESENT={"username","responseDt","totalMarks","result"};
    public static String[] COL_HEADERS_ENGLISH_EXAM_PRESENT={"Name","Date","Marks Obtained","Result"};
    
    
    public static Object[] NATURAL_COL_ORDER_EXAM_ABSENT={"username","totalMarks","result"};
    public static String[] COL_HEADERS_ENGLISH_EXAM_ABSENT={"Name","Marks Obtained","Result"};

   
    
    
    public StudentExamListContainer(){
        super(ExamBean.class);
    }
    
    
    private static final String Objective="Objective";
    private static final String Descriptive="Descriptive";
    
    public static StudentExamListContainer getExamListContainer(List<ExamBean> list){
        StudentExamListContainer container = new StudentExamListContainer();
       ExamBean bean =null;
        for(ExamBean eb:list){
           bean = new ExamBean();
           bean.setExName(eb.getExName());
           bean.setStartDt(eb.getStartDt()); 
           bean.setEndDt(eb.getEndDt()); 
           bean.setExamId(eb.getExamId());
           //1=objective
           //2=descriptive
           bean.setExType(eb.getExType());
           if(bean.getExType()==1)
           {
               bean.setExamTypeName((Objective));
           }
           else if(bean.getExType()==2)
           {
               bean.setExamTypeName((Descriptive));
           }
           container.addItem(bean);
        }
        
        return container;
    }
    
     public static StudentExamListContainer getPresentStudentListContainer(List<ExamBean> list){
        StudentExamListContainer container = new StudentExamListContainer();
       ExamBean bean =null;
        for(ExamBean eb:list){
           bean = new ExamBean();
           bean.setUsername(eb.getUsername());
           bean.setResponseDt(eb.getResponseDt()); 
           bean.setTotalMarks(eb.getTotalMarks()); 
           bean.setResult(eb.getResult()); 
           container.addItem(bean);
        }
        
        return container;
    }
     
     
    public static Container getAbsentStudentListContainer(List<ExamBean> list) {
         StudentExamListContainer container = new StudentExamListContainer();
       ExamBean bean =null;
        for(ExamBean eb:list){
           bean = new ExamBean();
           bean.setUsername(eb.getUsername());
           bean.setResponseDt(null); 
           bean.setTotalMarks(0); 
           bean.setResult(GlobalConstants.DASH); 
           container.addItem(bean);
        }
        
        return container;
    }  
     
}
