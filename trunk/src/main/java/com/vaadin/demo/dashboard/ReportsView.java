/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.vaadin.demo.dashboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.quick.ui.exam.StudentExamDataProvider;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ReportsView extends VerticalLayout implements View {

    private Userprofile profile;

    @Override
    public void enter(ViewChangeEvent event) {
        profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        setSizeFull();
        addStyleName("reports");
        //addComponent(buildDraftsView());
        buildHeaderView();
        buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(true);
        top.addStyleName("toolbar");
        top.addStyleName("lightBackgroundForDashboardActivity");
        top.addStyleName("lightGrayFourSideBorder");
        addComponent(top);
        final Label title = new Label("Reports");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        addComponent(top);
    }
    
    
    
private void buildBodyView() {
    //first row    
    HorizontalLayout row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        
//        getSubWiseComparisonList();
//        row.addComponent(UIUtils.createPanel(StudentExamDataProvider.getMyExamPieChart(getSubjectWiseAvgPerformanceList(),getSubwiseAvgScoreForStud())));
        if(profile.getRole().equals(GlobalConstants.student))
        {
            //getSubWiseComparisonList();
            //noticeAndGraphLayout.addComponent(UIUtils.createPanel(StudentExamDataProvider.getMyExamPieChart(getSubjectWiseAvgPerformanceList(),getSubwiseAvgScoreForStud())));
            
            getSubWiseComparisonList();
            row.addComponent(UIUtils.createPanel(StudentExamDataProvider.getMyExamPieChart(getSubjectWiseAvgPerformanceList(),getSubwiseAvgScoreForStud())));

        }
        else
        {
            row.addComponent(UIUtils.createPanel(UIUtils.getSubwiseProgressChart()));
//            Flash f = new Flash(strViewMore,new FileResource(new File("/home/rajkirans/Desktop/bikestorm/bikestorm.swf")));
//            f.setImmediate(true);
//            f.setSizeFull();
//            
//            row.addComponent(UIUtils.createPanel(f));
        }
        
            
        
        addComponent(row);
        setExpandRatio(row, 1.5f);
        setComponentAlignment(row, Alignment.MIDDLE_CENTER);
        
        
        row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        
        CssLayout activityChart=UIUtils.createPanel(UIUtils.getMyActivityLogsPeiChart());
        activityChart.setSizeFull();
        row.addComponent(activityChart);
        
        CssLayout attendanceChart=UIUtils.createPanel(UIUtils.getMyAttendancePeiChart());
        attendanceChart.setSizeFull();
        row.addComponent(attendanceChart);
        
        addComponent(row);
        setExpandRatio(row, 1.5f);
        setComponentAlignment(row, Alignment.MIDDLE_CENTER);
        
    }

public  List<ExamBean> getSubWiseComparisonList() {
       
         List<ExamBean> subWiseComparisonList = null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_SUB_WISE_COMPARISON));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
                Userprofile userprofile = (Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile);
                inputJson.put("std", userprofile.getStd());
                inputJson.put("div", userprofile.getDiv());
                inputJson.put("username",userprofile.getUsername());
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            java.lang.reflect.Type listType = new TypeToken<ArrayList<ExamBean>>() {
            }.getType();
            
            subjectWiseAvgPerformanceList = new Gson().fromJson(outNObject.getString(GlobalConstants.subjectWiseAvgPerformance), listType);
            subwiseAvgScoreForStud = new Gson().fromJson(outNObject.getString(GlobalConstants.subwiseAvgScoreForStud), listType);
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
          //  L.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subWiseComparisonList;            
    }

private List<ExamBean> subjectWiseAvgPerformanceList;
    
    private List<ExamBean> subwiseAvgScoreForStud;
    
    /**
     * @return the subjectWiseAvgPerformanceList
     */
    public List<ExamBean> getSubjectWiseAvgPerformanceList() {
        return subjectWiseAvgPerformanceList;
    }

    /**
     * @param subjectWiseAvgPerformanceList the subjectWiseAvgPerformanceList to set
     */
    public void setSubjectWiseAvgPerformanceList(List<ExamBean> subjectWiseAvgPerformanceList) {
        this.subjectWiseAvgPerformanceList = subjectWiseAvgPerformanceList;
    }

    /**
     * @return the subwiseAvgScoreForStud
     */
    public List<ExamBean> getSubwiseAvgScoreForStud() {
        return subwiseAvgScoreForStud;
    }

    /**
     * @param subwiseAvgScoreForStud the subwiseAvgScoreForStud to set
     */
    public void setSubwiseAvgScoreForStud(List<ExamBean> subwiseAvgScoreForStud) {
        this.subwiseAvgScoreForStud = subwiseAvgScoreForStud;
    }
}
