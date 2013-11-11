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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ForumEventDetailsBean;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.quick.utilities.LoadEarlierBtnWraper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ForumView extends VerticalLayout implements View,LayoutEvents.LayoutClickListener {

    private Table forumTable;
    private String New="New";
    private List <ForumEventDetailsBean> forumEventDetailsList;
    private Userprofile loggedInUserProfile;
    private List<List> wrapperList = new ArrayList<List>();
    private LoadEarlierBtnWraper loadMoreWraper = new LoadEarlierBtnWraper(this);

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
        loggedInUserProfile =((Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile));

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

        
        //forumTable.setPageLength(3);
        forumTable.addStyleName("plain");
        forumTable.addStyleName("borderless");
        forumTable.setHeight("100%");
        forumTable.setWidth("100%");
        forumTable.setSortEnabled(false);
        setForumEventDetailsList(getForumDetailList());
        
        addForumEventsInToTable();
        
        //forumTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        addComponent(forumTable);
        setExpandRatio(forumTable, 1.5f);
        setHeight("100%");
        setWidth("99%");
    }

    private List<ForumEventDetailsBean> getForumDetailList() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_All_FORUM_EVENTS));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
                 if (forumTable == null) {
                     inputJson.put("fetchResultsFrom", 0);
                 } else {
                     inputJson.put("fetchResultsFrom", (forumTable.size() - 1));
                 }
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
    
            Gson gson=  new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();
            
            forumEventDetailsList=gson.fromJson(outNObject.getString(GlobalConstants.eventDetailsList), listType);
            if(forumEventDetailsList.size()>0)
            {
                wrapperList.add(forumEventDetailsList);
            }
            
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
          //  L.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return forumEventDetailsList;
    }
    
     @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) 
    {
        /*if(c instanceof DashboardActivityWraper)
        {
            DashboardActivityWraper activityWraper =(DashboardActivityWraper) event.getComponent();
                MyDashBoardBean activityDetails = (MyDashBoardBean)activityWraper.getData();
                QuickLearn learn =getStudentQuickLearnDetails(activityDetails.getUploadId());
                UI.getCurrent().addWindow(new ViewTopicDetailsWindow(learn,getUserNotes(),Integer.parseInt(activityDetails.getUploadId())));
        }
        else*/
        Component c = event.getComponent();
         if(c instanceof LoadEarlierBtnWraper)
        {
            getForumDetailList();

            System.out.println("&^^^^^^^^^^^&^&^&^&^ Button Clicked&^&^&^&^&^&^&^^&");
            forumTable.removeAllItems();
            addForumEventsInToTable();
            
        }
        
    }

    private void addForumEventsInToTable() 
    {
        for(List<ForumEventDetailsBean> eventDetailsList:wrapperList)
        {
            for(ForumEventDetailsBean eventDetails:eventDetailsList)
            {
                forumTable.addItem(new Object[]{new ForumDetailWraper(eventDetails) },forumTable.size()+1);
            }            
        }
        forumTable.addItem(new Object[]{loadMoreWraper},forumTable.size()+1);
    }
}
