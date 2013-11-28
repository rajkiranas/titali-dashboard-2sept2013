/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

import com.quick.bean.ExamBean;
import com.quick.container.StudQuickLearnContainer;
import com.quick.container.StudentExamListContainer;
import com.quick.global.GlobalConstants;
import com.quick.utilities.UIUtils;
import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.TopGrossingMoviesChart;
import com.vaadin.demo.dashboard.TopSixTheatersChart;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElementDecl;

/**
 *
 * @author rajkiran
 */
public class StudentExamDataProvider {
    
    
    /* public static Table getStudentExamList(List<ExamBean> list){
        Table t1 = new Table();
        t1.setCaption("Student ExamList");
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
       
        return t1;
    } */
    
    private Table t1;
    public  Table getStudentExamList(List<ExamBean> list){
        
        t1 = new Table(){
        
      @Override
       protected String formatPropertyValue(Object rowId,
           Object colId, Property property) {
       // Format by property type
       if (property.getType() == Date.class) {
           SimpleDateFormat df =
               new SimpleDateFormat(GlobalConstants.dateFormatMMddyyyy);
           return df.format((Date)property.getValue());
       }
       return super.formatPropertyValue(rowId, colId, property);
}
  };
        t1.setCaption("ExamList");
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
        t1.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_EXAM_LIST);
        t1.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_EXAM_LIST);
        t1.sort(new Object[]{"examId"}, new boolean[]{true});        
        
       
        return t1;
    }

    public static Component getMyExamPieChart(List<ExamBean> subjectWiseAvgPerformanceList, List<ExamBean> subwiseAvgScoreForStud) {
        HorizontalLayout baseLayout = new HorizontalLayout();
        baseLayout.setCaption("Performance");
        baseLayout.setSizeFull();
        
        String[] xAxisCategories =  new String[subjectWiseAvgPerformanceList.size()];
        Number[] classAvgScore = new Number[subjectWiseAvgPerformanceList.size()];
        int i=0;
        for(ExamBean bean:subjectWiseAvgPerformanceList)
        {
            xAxisCategories[i]=bean.getSub();
            classAvgScore[i]=bean.getSubjectWiseAvgPerformance();
            i++;
        }
        
        Number[] studAvgScore = new Number[subwiseAvgScoreForStud.size()];
        
        int j=0;
         for(ExamBean bean:subwiseAvgScoreForStud)
        {
            //xAxisCategories[i]=bean.getSub();
            studAvgScore[j]=bean.getSubjectWiseAvgPerformance();
            j++;
        }

          /*  Label l1= new Label("exam pie chart comes here");
          baseLayout.addComponent(l1);
          baseLayout.setExpandRatio(l1, 1.5f);
          
          
          
          Label l2= new Label("2  chart comes here");
          baseLayout.addComponent(l2);
          baseLayout.setExpandRatio(l2, 2); */
        //UIUtils.getBarChart(xAxisCategories, scores, "Subjectwise comparison", "Score", "Marks", "300px", "300px");
        
        baseLayout.addComponent(UIUtils.getColumnChart(xAxisCategories, classAvgScore,studAvgScore, "Subjectwise comparison", "Score", "Marks", "90%", "100%"));
        return baseLayout;
    }

    public static Component getSelectedExamDetails(Object object) {
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        TextField subtxt =new TextField();
        subtxt.setCaption("subject");
        TextField markstxt =new TextField();
        markstxt.setCaption("Marks");
        TextField scoretxt =new TextField();
        scoretxt.setCaption("Score");
        TextField questionstxt =new TextField();
        questionstxt.setCaption("Questions");
       formLayout.addComponent(subtxt);
        formLayout.addComponent(markstxt);
        formLayout.addComponent(scoretxt);
        formLayout.addComponent(questionstxt);
        //throw new UnsupportedOperationException("Not yet implemented");
        return formLayout;
    }

    public static Component getExamResult(Object object) {
        HorizontalLayout baseLayout = new HorizontalLayout();
        baseLayout.setSizeFull();
//        baseLayout.addComponent(createPanel(new TopSixTheatersChart()));
//        baseLayout.addComponent(createPanel(new TopGrossingMoviesChart()));
//        return baseLayout;



        Label l1 = new Label("exam pie chart comes here");
        Label l2 = new Label("2  chart comes here");
        baseLayout.addComponent(createPanel(l1));
        baseLayout.addComponent(createPanel(l2));
        return baseLayout;

    }
    
    
    
     private static CssLayout createPanel(Component content) {
        CssLayout panel = new CssLayout();
       // panel.addStyleName("layout-panel");
        panel.setSizeFull();
        panel.addComponent(content);
        return panel;
    }

    
}
