/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;

import com.quick.table.QuickUploadTable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author rajkiran
 */
public class UIUtils {
    public static CssLayout createPanel(Component content) {
        CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        //panel.setSizeFull();
        panel.setWidth("100%");
        panel.setHeight("97%");
        panel.addComponent(content);
        return panel;
    }
    
    
    public static HorizontalLayout getVerticalPaneView(Component c1 ,Component c2){
        HorizontalLayout horizontalLayout =new HorizontalLayout();
        horizontalLayout.addComponent(c1);
        horizontalLayout.addComponent(c2);
        return horizontalLayout;
    }
    
    public static VerticalLayout getHorizontalPaneView(Component c1 ,Component c2){
        
        VerticalLayout verticalLayout =new VerticalLayout();
        verticalLayout.addComponent(c1);
        verticalLayout.addComponent(c2);
        return verticalLayout;
        
    }
    
    
    //method requires descripttion of the passed comeponents
     public static TabSheet getTabSheetPaneView(Component... c1){
        TabSheet tabSheet =new TabSheet();
       tabSheet.setSizeFull();
       
        for(Component arg: c1) {
            tabSheet.addTab(arg,arg.getDescription());
        }
       
        return tabSheet;
        
    }
     
     /** Used to show the list of uploaded items
     * called when admin user goes to upload topics menu
     */
    public static VerticalLayout buildVerticalLayoutForComponent(Component c) {
        VerticalLayout mainVertical = new VerticalLayout();
        //HorizontalLayout tableView = new HorizontalLayout();
        mainVertical.setSpacing(true);
        mainVertical.setWidth("100%");
        mainVertical.setHeight("97%");
        mainVertical.addComponent(c);
        //mainVertical.addComponent(tableView);
        return mainVertical;
    }
    
}
