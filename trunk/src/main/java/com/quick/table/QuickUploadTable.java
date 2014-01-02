/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.table;

import com.quick.global.GlobalConstants;
import com.vaadin.demo.dashboard.QuickUpload;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Sonali Sangle
 */
public class QuickUploadTable extends Table {


    public QuickUploadTable(final QuickUpload quickUpload) {

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
        setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
        //List<MasteParmBean> masterParamList = quickUpload.getUploadedList();
        
        
    }
    public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{this,rowItemBean,""};
            btnRemove.setData(arr);
        }
    
     
}
