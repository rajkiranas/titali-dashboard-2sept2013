/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.table;

import com.quick.bean.MasteParmBean;
import com.quick.global.GlobalConstants;
import com.quick.ui.QuickLearn.QuickLearnDetailWraper;
import com.quick.utilities.ConfirmationDialogueBox;
import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.QuickUpload;
import com.vaadin.demo.dashboard.QuickUploadMasterContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import java.io.File;
import java.util.List;

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
        
        List<MasteParmBean> masterParamList = quickUpload.getUploadedList();
        
        for(MasteParmBean topicDetails:masterParamList)
        {
            addItem(new Object[]{new QuickLearnDetailWraper(topicDetails,quickUpload) },size()+1);
        }
    }
    public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{this,rowItemBean,""};
            btnRemove.setData(arr);
        }
    
     
}
