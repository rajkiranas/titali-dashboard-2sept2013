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

import com.quick.utilities.UIUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ReportsView extends VerticalLayout implements View {

    

    @Override
    public void enter(ViewChangeEvent event) {
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
        
        row.addComponent(UIUtils.createPanel(UIUtils.getSubwiseProgressChart()));
        
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
}
