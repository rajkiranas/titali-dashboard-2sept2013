/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.table;

import com.quick.bean.MasteParmBean;
import com.quick.container.StudQuickLearnContainer;
import com.quick.data.MyDashBoardContainer;
import com.quick.global.GlobalConstants;
import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.StudQuickLearn;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.BaseTheme;

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
        setSortEnabled(false);
        setWidth("100%");
        setPageLength(10);
        setSelectable(true);
        setImmediate(true); // react at once when something is selected
        setContainerDataSource(StudQuickLearnContainer.getStudQuickLearnContainer(quickLearn.getTopicList()));
        setVisibleColumns(StudQuickLearnContainer.NATURAL_COL_ORDER_QUICKLEARN);
        setColumnHeaders(StudQuickLearnContainer.COL_HEADERS_ENGLISH_QUICKLEARN);
        addValueChangeListener((Property.ValueChangeListener)quickLearn);
        
        addGeneratedColumn("View details", new Table.ColumnGenerator() 
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
            });
       
        }
    
    public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{this,rowItemBean,GlobalConstants.emptyString};
            btnRemove.setData(arr);
        }
    
    
    
    
    
    
}
