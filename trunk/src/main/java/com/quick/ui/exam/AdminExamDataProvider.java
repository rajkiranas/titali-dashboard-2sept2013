/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.Userprofile;
import com.quick.container.StudQuickLearnContainer;
import com.quick.container.StudentExamListContainer;
import com.quick.global.GlobalConstants;
import com.quick.global.GlobalNotifications;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.TopGrossingMoviesChart;
import com.vaadin.demo.dashboard.TopSixTheatersChart;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rajkiran
 */
public class AdminExamDataProvider {

    
    private AdminExam adminExam;
    private Table t1;

    public AdminExamDataProvider()
    {
        
    }
    
    public AdminExamDataProvider(AdminExam exam)
    {
        this.adminExam=exam;
    }
    
    public  Table getStudentExamList(List<ExamBean> list){
        //Table t1 = new Table();
//        t1.setCaption("Exam List");
//        //setSizeFull();
//      //  addStyleName("plain");
//        t1.addStyleName("borderless");
//        t1.setSortEnabled(false);
//        t1.setWidth("100%");
//        t1.setPageLength(10);
//        t1.setSelectable(true);
//        //t1.setMultiSelect(true);
//        t1.setImmediate(true); // react at once when something is selected
//        t1.setContainerDataSource(StudentExamListContainer.getExamListContainer(list));
//        t1.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_QUICKLEARN);
//        t1.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_QUICKLEARN);
//        t1.sort(new Object[]{"examId"}, new boolean[]{true});
//        
//        t1.addGeneratedColumn("Remove", new Table.ColumnGenerator() 
//            {
//            @Override
//                public Object generateCell(Table source, Object rowItemBean, Object columnId) 
//                {
//                    Button btnRemove=new Button("Remove");
//                    btnRemove.setImmediate(true);
//                    btnRemove.setStyleName(BaseTheme.BUTTON_LINK);
//                    setItemData(btnRemove,rowItemBean);
//                    adminExam.addListenertoBtn(btnRemove);               
//                    return btnRemove;
//                }
//            });
        
        Table t1 = new Table(){
        
      @Override
       protected String formatPropertyValue(Object rowId,
           Object colId, Property property) {
       // Format by property type
       if (property.getType() == Date.class) {
           SimpleDateFormat df =
               new SimpleDateFormat("MM/dd/yyyy");
           return df.format((Date)property.getValue());
       }
       return super.formatPropertyValue(rowId, colId, property);
   
}
  };
        t1.setCaption("Exam List");
        //setSizeFull();
      //  addStyleName("plain");
        t1.addStyleName("borderless");
        t1.setSortEnabled(false);
        t1.setWidth("100%");
        t1.setPageLength(10);
        t1.setSelectable(true);
        //t1.setMultiSelect(true);
        t1.setImmediate(true); // react at once when something is selected
        t1.setContainerDataSource(StudentExamListContainer.getExamListContainer(list));
        t1.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_QUICKLEARN);
        t1.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_QUICKLEARN);
        t1.sort(new Object[]{"examId"}, new boolean[]{true});
        
        t1.addGeneratedColumn("Remove", new Table.ColumnGenerator() 
            {
            @Override
                public Object generateCell(Table source, Object rowItemBean, Object columnId) 
                {
                    Button btnRemove=new Button("Remove");
                    btnRemove.setImmediate(true);
                    btnRemove.setStyleName(BaseTheme.BUTTON_LINK);
                    setItemData(btnRemove,rowItemBean);
                    adminExam.addListenertoBtn(btnRemove);               
                    return btnRemove;
                }
            });    
        
       
        return t1;
    }
    
       

public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{t1,rowItemBean,""};
            btnRemove.setData(arr);
        }
    
    
    
    public static Table getPresentStudentsForExam(List<ExamBean> list){
       
       ExamBean examBean = list.get(0);
       Table t1 = new Table();
        t1.setDescription("Present Students");
       // t1.setSizeFull();
      //  addStyleName("plain");
        t1.addStyleName("borderless");
        t1.setSortEnabled(true);
        t1.setWidth("100%");
        t1.setPageLength(5);
        //t1.setSelectable(true);
        t1.setImmediate(true); // react at once when something is selected
        t1.setContainerDataSource(StudentExamListContainer.getPresentStudentListContainer(getPresentStudentsForExamById(examBean.getExamId())));
        t1.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_EXAM_PRESENT);
        t1.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_EXAM_PRESENT);
        t1.sort(new Object[]{"username"}, new boolean[]{true});
        return t1;
    }
    
    
    
     public static Table getAbsentStudentsForExam(List<ExamBean> list){
       
       ExamBean examBean = list.get(0);
       Table t1 = new Table();
        t1.setDescription("Absent Students");
       // t1.setSizeFull();
      //  addStyleName("plain");
        t1.addStyleName("borderless");
        t1.setSortEnabled(true);
        t1.setWidth("100%");
        t1.setPageLength(5);
        //t1.setSelectable(true);
        t1.setImmediate(true); // react at once when something is selected
        t1.setContainerDataSource(StudentExamListContainer.getAbsentStudentListContainer(getAbsentStudentsForExamById(examBean.getExamId(),examBean.getStd(),examBean.getFordiv())));
        t1.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_EXAM_ABSENT);
        t1.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_EXAM_ABSENT);
        t1.sort(new Object[]{"username"}, new boolean[]{true});
        return t1;
    }
   
   
     
     
    
    
   private static List<ExamBean> getPresentStudentsForExamById(int examId) 
   {
       List<ExamBean> list=null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_PRESENT_STUD_FOR_EXAM));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("examId", examId);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }


            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);

            /*
             * if (response.getStatus() != 201) { throw new
             * RuntimeException("Failed : HTTP error code : " +
             * response.getStatus()); }
             */

            String output = response.getEntity(String.class);


            JSONObject response1 = null;

            response1 = new JSONObject(output);

            if (response1.get(GlobalConstants.STATUS).equals(GlobalConstants.YES)) 
            {
                Type listType = new TypeToken<ArrayList<ExamBean>>() {}.getType();
                list =new Gson().fromJson(response1.getString(GlobalConstants.EXAMLIST), listType);
                
                
            } else {
                 Notification.show("Fetching exam details failed");
            }
        } catch (JSONException ex) {
            Notification.show("Fetching exam details failed");
            ex.printStackTrace();
          
        }
        return list;
   }
   
   
   
   
    
   private static List<ExamBean> getAbsentStudentsForExamById(int examId,String forstd,String fordiv ) 
   {
       
       //inputRequest.getString("forstd"),inputRequest.getString("fordiv")
       List<ExamBean> list=null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_ABSENT_STUD_FOR_EXAM));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("examId", examId);
                inputJson.put("forstd", forstd);
                inputJson.put("fordiv", fordiv);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }


            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);

            /*
             * if (response.getStatus() != 201) { throw new
             * RuntimeException("Failed : HTTP error code : " +
             * response.getStatus()); }
             */

            String output = response.getEntity(String.class);


            JSONObject response1 = null;

            response1 = new JSONObject(output);

            if (response1.get(GlobalConstants.STATUS).equals(GlobalConstants.YES)) 
            {
                Type listType = new TypeToken<ArrayList<ExamBean>>() {}.getType();
                list =new Gson().fromJson(response1.getString(GlobalConstants.EXAMLIST), listType);
                
                
            } else {
                 Notification.show("Fetching exam details failed");
            }
        } catch (JSONException ex) {
            Notification.show("Fetching exam details failed");
            ex.printStackTrace();
          
        }
        return list;
   }
    
}
