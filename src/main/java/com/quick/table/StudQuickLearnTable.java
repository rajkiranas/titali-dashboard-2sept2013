/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.table;

import com.quick.global.GlobalConstants;
import com.vaadin.demo.dashboard.StudQuickLearn;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author rajkiran
 */
public class StudQuickLearnTable extends Table {
    
    
    public StudQuickLearnTable(final StudQuickLearn quickLearn){
        
        ///setCaption("Whats New");
        //setSizeFull();
      //  addStyleName("plain");
        addStyleName("borderless");
        addStyleName("plain");
        setSortEnabled(false);
        setWidth("100%");
        setHeight("500px");
        setPageLength(10);
        setSelectable(true);
        setImmediate(true);// react at once when something is selected
        setSortEnabled(false);
        addContainerProperty(GlobalConstants.emptyString, VerticalLayout.class, null);
        
        
        //List<MasteParmBean> masterParamList = quickLearn.getTopicList();
        
        
        
//        setContainerDataSource(StudQuickLearnContainer.getStudQuickLearnContainer(quickLearn.getTopicList()));
//        setVisibleColumns(StudQuickLearnContainer.NATURAL_COL_ORDER_QUICKLEARN);
//        setColumnHeaders(StudQuickLearnContainer.COL_HEADERS_ENGLISH_QUICKLEARN);
        
        //addValueChangeListener((Property.ValueChangeListener)quickLearn);
        
        
        /* addGeneratedColumn("View details", new Table.ColumnGenerator() 
            {
            @Override
                public Object generateCell(Table source, Object rowItemBean, Object columnId) 
                {
                    Button btnRemove=new Button("View details");
                    btnRemove.setImmediate(true);
                    btnRemove.setStyleName(BaseTheme.BUTTON_LINK);
                    setItemData(btnRemove,rowItemBean);
                    quickLearn.addListenertoBtn(btnRemove);               
                    return btnRemove;
                }
            }); */
       
        }
    
    public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{this,rowItemBean,GlobalConstants.emptyString};
            btnRemove.setData(arr);
        }
    
}
