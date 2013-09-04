/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

import com.quick.bean.ExamBean;
import com.quick.container.StudQuickLearnContainer;
import com.quick.container.StudentExamListContainer;
import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.TopGrossingMoviesChart;
import com.vaadin.demo.dashboard.TopSixTheatersChart;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import java.util.List;

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
    
    
    public Table getStudentExamList(List<ExamBean> list){
        t1 = new Table();
        t1.setCaption("Student ExamList");
       // t1.setSizeFull();
      //  addStyleName("plain");
        t1.addStyleName("borderless");
        t1.setSortEnabled(true);
        t1.setWidth("100%");
        t1.setPageLength(8);
        t1.setSelectable(true);
        //t1.setMultiSelect(true);
        t1.setImmediate(true); // react at once when something is selected
        t1.setContainerDataSource(StudentExamListContainer.getExamListContainer(list));
        t1.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_QUICKLEARN);
        t1.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_QUICKLEARN);
        
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

    public static Component getMyExamPieChart() {
        HorizontalLayout baseLayout = new HorizontalLayout();
        baseLayout.setSizeFull();
        Component c1 = createPanel(new TopSixTheatersChart());
        baseLayout.addComponent(c1);
        baseLayout.setExpandRatio(c1, 1.5f);
         Component c2 = createPanel(new TopGrossingMoviesChart());
        baseLayout.addComponent(c2);
        baseLayout.setExpandRatio(c2,2);
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

    public static Component getExamResult() {
        HorizontalLayout baseLayout = new HorizontalLayout();
         baseLayout.setSizeFull();
        baseLayout.addComponent(createPanel(new TopSixTheatersChart()));
        baseLayout.addComponent(createPanel(new TopGrossingMoviesChart()));
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
