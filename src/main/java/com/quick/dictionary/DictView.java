/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.quick.dictionary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.DictWordDetailsBean;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.quick.utilities.LoadEarlierBtnWraper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DictView extends VerticalLayout implements View,LayoutEvents.LayoutClickListener {

    private Table dictTable;
    private static final String New="New";
    private List <DictWordDetailsBean> dictWordDetailsList;
    private Userprofile loggedInUserProfile;
    private List<List> wrapperList = new ArrayList<List>();
    private LoadEarlierBtnWraper loadMoreWraper = new LoadEarlierBtnWraper(this);
    private static final String dict="Dictionary";
    private static final List<String> searchOptions = Arrays.asList(new String[] {"Word", "Labels"});

    public List<DictWordDetailsBean> getDictWordDetailsList() {
        return dictWordDetailsList;
    }

    public void setDictWordDetailsList(List<DictWordDetailsBean> dictWordDetailList) {
        this.dictWordDetailsList = dictWordDetailList;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        addStyleName("schedule");
        loggedInUserProfile =((Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile));

        buildHeaderView();
        buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        // add page title
        top.addStyleName(GlobalConstants.toolbar_style);
        addComponent(top);
        final Label title = new Label(dict);
        title.setSizeUndefined();
        title.addStyleName(GlobalConstants.h1_style);
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        top.addStyleName("lightBackgroundForDashboardActivity");
        top.addStyleName("lightGrayFourSideBorder");
        // add serach layout
        
        

        
        final OptionGroup select = new OptionGroup(null, searchOptions);
        select.setSizeUndefined();
        select.addStyleName("horizontal");
        select.setNullSelectionAllowed(false); // user can not 'unselect'
        select.select("Word"); // select this by default
        select.setImmediate(true); // send the change to the server at once
        
        
        final TextField searchBox = new TextField();
        searchBox.setImmediate(true);
        searchBox.setInputPrompt("Enter here");
        searchBox.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {

            @Override
            public void handleAction(Object sender, Object target) {
                if (searchBox.getValue() != null && !searchBox.getValue().equals(GlobalConstants.emptyString)) {
                    if(searchBox.getValue().length()>300)
                    {
                        Notification.show("Comment cannot be more than 300 characters.", Notification.Type.WARNING_MESSAGE);
                    }
                    else
                    {
                        searchWordsList(searchBox.getValue(),(String)select.getValue());
                        
                    }
                }
            }
        });
        
         searchBox.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.BACKSPACE, null) {

            @Override
            public void handleAction(Object sender, Object target) {
                if (searchBox.getValue() == null || searchBox.getValue().equals(GlobalConstants.emptyString)) {
                    dictTable.removeAllItems();
                    wrapperList=new ArrayList<List>();
                    setDictWordDetailsList(getDictWordsList());
                    addDictWordsIntoTable();
                }
                else
                {
                    searchBox.setValue(searchBox.getValue().substring(0,searchBox.getValue().length()-1));
                }
            }
        });
        
        
        
        
        HorizontalLayout serachLayout = new HorizontalLayout();
        serachLayout.setMargin(true);
        serachLayout.setSpacing(true);
        
        
        serachLayout.addComponent(new Label("<b>Search</b>",ContentMode.HTML));
        serachLayout.addComponent(select);
        serachLayout.addComponent(searchBox);
        //serachLayout.addStyleName("fourSideBorder");
        
        top.addComponent(serachLayout);        
        top.setComponentAlignment(serachLayout, Alignment.MIDDLE_CENTER);
        top.setExpandRatio(serachLayout, 1);
        
        
        
        if(!loggedInUserProfile.getRole().equals(GlobalConstants.student))
        {
            // add new button
            Button newEventBtn = new Button(New);
            newEventBtn.setImmediate(true);
            newEventBtn.addStyleName(GlobalConstants.default_style);
            newEventBtn.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    UI.getCurrent().addWindow(new NewWordWindow(event));
                }
            });

            top.addComponent(newEventBtn);
            top.setComponentAlignment(newEventBtn, Alignment.MIDDLE_RIGHT);
            top.setExpandRatio(newEventBtn, 1);
        }
        
        addComponent(top);
    }
    
    
    
    private void buildBodyView() {
        
        dictTable  = new Table();          
          
        dictTable.addContainerProperty(GlobalConstants.emptyString, VerticalLayout.class, null);
        
        dictTable.addStyleName("plain");
        dictTable.addStyleName("borderless");
        dictTable.setHeight("100%");
        dictTable.setWidth("100%");
        dictTable.setSortEnabled(false);
        setDictWordDetailsList(getDictWordsList());
        
        addDictWordsIntoTable();
        
        //forumTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        addComponent(dictTable);
        setExpandRatio(dictTable, 1.5f);
        setHeight("100%");
        setWidth("100%");
    }

    private List<DictWordDetailsBean> getDictWordsList() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_WORD_LIST));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
                 if (dictTable == null) {
                     inputJson.put("fetchResultsFrom", 0);
                 } else {
                     inputJson.put("fetchResultsFrom", (dictTable.size() - 1));
                 }
//                inputJson.put("username",userprofile.getUsername());
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            java.lang.reflect.Type listType = new TypeToken<ArrayList<DictWordDetailsBean>>() {
            }.getType();
    
            Gson gson=  new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();
            
            dictWordDetailsList=gson.fromJson(outNObject.getString(GlobalConstants.dictWordList), listType);
            if(dictWordDetailsList.size()>0)
            {
                wrapperList.add(dictWordDetailsList);
            }
            
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        return dictWordDetailsList;
    }
    
    private List<DictWordDetailsBean> searchWordsList(String wordOrLabelToBeSearched, String select) {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SEARCH_WORD_LIST));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
                 if(select.equals("Word"))
                 {
                     inputJson.put("searchFor", "Word");
                     inputJson.put("searchWord", wordOrLabelToBeSearched);
                 }
                 else
                 {
                     inputJson.put("searchFor", "Label");
                     inputJson.put("searchWord", wordOrLabelToBeSearched);
                 }
                 
                 if (dictTable == null) 
                 {
                     inputJson.put("fetchResultsFrom", 0);
                 } else 
                 {
                     inputJson.put("fetchResultsFrom", (dictTable.size() - 1));
                 }
//                inputJson.put("username",userprofile.getUsername());
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            java.lang.reflect.Type listType = new TypeToken<ArrayList<DictWordDetailsBean>>() {
            }.getType();
    
            Gson gson=  new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();
            
            dictWordDetailsList=gson.fromJson(outNObject.getString(GlobalConstants.dictWordList), listType);
            if(dictWordDetailsList.size()>0)
            {
                wrapperList = new ArrayList<List>();
                wrapperList.add(dictWordDetailsList);
                
                if (dictTable != null) 
                 {
                     dictTable.removeAllItems();
                     addDictWordsIntoTable();
                 }
                
            }
            
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        return dictWordDetailsList;
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
            getDictWordsList();

            dictTable.removeAllItems();
            addDictWordsIntoTable();
            
        }
        
    }

    private void addDictWordsIntoTable() 
    {
        for(List<DictWordDetailsBean> eventDetailsList:wrapperList)
        {
            for(DictWordDetailsBean wordDetails:eventDetailsList)
            {
                dictTable.addItem(new Object[]{new DictWordDetailWraper(wordDetails,this) },dictTable.size()+1);
            }            
        }
        dictTable.addItem(new Object[]{loadMoreWraper},dictTable.size()+1);
    }
}
