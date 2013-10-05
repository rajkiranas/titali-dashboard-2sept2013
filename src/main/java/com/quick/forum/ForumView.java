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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ForumView extends VerticalLayout implements View {

    Table forumTable;
    private List <ForumEventDetails> forumEventDetailsList;

    public List<ForumEventDetails> getForumEventDetailsList() {
        return forumEventDetailsList;
    }

    public void setForumEventDetailsList(List<ForumEventDetails> forumEventDetail) {
        this.forumEventDetailsList = forumEventDetail;
    }

    @Override
    public void enter(ViewChangeEvent event) {
//        setSizeFull();
        addStyleName("schedule");
        //addComponent(buildDraftsView());
        buildHeaderView();
        buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName("toolbar");
        addComponent(top);
        final Label title = new Label("Forum");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        addComponent(top);
    }
    
    
    
    private void buildBodyView() {
        
          forumTable  = new Table() {
              
             
            @Override
            protected String formatPropertyValue(Object rowId, Object colId,
                    Property<?> property) {
                if (colId.equals("Revenue")) {
                    if (property != null && property.getValue() != null) {
                        Double r = (Double) property.getValue();
                        String ret = new DecimalFormat("#.##").format(r);
                        return "$" + ret;
                    } else {
                        return "";
                    }
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
          
        forumTable.addContainerProperty("", VerticalLayout.class, null);
        forumTable.setCaption("Forum");

        forumTable.setWidth("100%");
        forumTable.setPageLength(2);
        forumTable.addStyleName("plain");
        forumTable.addStyleName("borderless");
        forumTable.setHeight("500px");
        forumTable.setSortEnabled(false);
        setForumEventDetailsList(getForumDetailList());
        
        for(ForumEventDetails eventDetails:getForumDetailList())
        {
            forumTable.addItem(new Object[]{new ForumDetailWraper(eventDetails) },forumTable.size()+1);
        }
        
        addComponent(forumTable);
        setExpandRatio(forumTable, 1.5f);
     
    }

    private List<ForumEventDetails> getForumDetailList() {
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

            java.lang.reflect.Type listType = new TypeToken<ArrayList<ForumEventDetails>>() {
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
