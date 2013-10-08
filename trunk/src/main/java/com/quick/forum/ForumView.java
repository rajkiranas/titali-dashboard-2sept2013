/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.quick.forum;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.ForumEventDetailsBean;
import com.quick.bean.Userprofile;
import java.text.DecimalFormat;
import com.vaadin.data.Property;
import com.quick.entity.ForumEventDetails;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ForumView extends VerticalLayout implements View {

    private Table forumTable;
    private String New="New";
    private List <ForumEventDetailsBean> forumEventDetailsList;

    public List<ForumEventDetailsBean> getForumEventDetailsList() {
        return forumEventDetailsList;
    }

    public void setForumEventDetailsList(List<ForumEventDetailsBean> forumEventDetail) {
        this.forumEventDetailsList = forumEventDetail;
    }

    @Override
    public void enter(ViewChangeEvent event) {
//        setSizeFull();
        addStyleName("schedule");
        //addStyleName("dashboard");
        //addComponent(buildDraftsView());
        buildHeaderView();
        buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        //top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName("toolbar");
        addComponent(top);
        final Label title = new Label("Forum");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        Button newEventBtn =  new Button(New);
        newEventBtn.setImmediate(true);
        newEventBtn.addStyleName("default");
        newEventBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(new NewEventWindow(event));
            }
        });
        
        top.addComponent(newEventBtn);
        top.setComponentAlignment(newEventBtn, Alignment.MIDDLE_RIGHT);
        top.setExpandRatio(newEventBtn, 1);
        
        addComponent(top);
    }
    
    
    
    private void buildBodyView() {
        
          forumTable  = new Table();
          //{
//              
//             
//            @Override
//            protected String formatPropertyValue(Object rowId, Object colId,
//                    Property<?> property) {
//                if (colId.equals("Revenue")) {
//                    if (property != null && property.getValue() != null) {
//                        Double r = (Double) property.getValue();
//                        String ret = new DecimalFormat("#.##").format(r);
//                        return "$" + ret;
//                    } else {
//                        return "";
//                    }
//                }
//                return super.formatPropertyValue(rowId, colId, property);
//            }
//        };
          
        forumTable.addContainerProperty(GlobalConstants.emptyString, VerticalLayout.class, null);
        //forumTable.setCaption("Forum");

        forumTable.setWidth("100%");
        forumTable.setPageLength(3);
        forumTable.addStyleName("plain");
        forumTable.addStyleName("borderless");
        forumTable.setHeight("500px");
        forumTable.setSortEnabled(false);
        setForumEventDetailsList(getForumDetailList());
        
        for(ForumEventDetailsBean eventDetails:getForumDetailList())
        {
            forumTable.addItem(new Object[]{new ForumDetailWraper(eventDetails) },forumTable.size()+1);
        }
        
        //forumTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        addComponent(forumTable);
        setExpandRatio(forumTable, 1.5f);
     
    }

    private List<ForumEventDetailsBean> getForumDetailList() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_All_FORUM_EVENTS));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
//                inputJson.put("username",userprofile.getUsername());
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            java.lang.reflect.Type listType = new TypeToken<ArrayList<ForumEventDetailsBean>>() {
            }.getType();
            
            forumEventDetailsList=new Gson().fromJson(outNObject.getString(GlobalConstants.eventDetailsList), listType);
            
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
          //  L.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return forumEventDetailsList;
    }
    
    
}
