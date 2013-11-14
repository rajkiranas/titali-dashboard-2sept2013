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

import com.quick.forum.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.DictWordDetailsBean;
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

public class DictView extends VerticalLayout implements View,LayoutEvents.LayoutClickListener {

    private Table dictTable;
    private static final String New="New";
    private List <DictWordDetailsBean> dictWordDetailsList;
    private Userprofile loggedInUserProfile;
    private List<List> wrapperList = new ArrayList<List>();
    private LoadEarlierBtnWraper loadMoreWraper = new LoadEarlierBtnWraper(this);
    private static final String dict="Dictionary";

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
        //top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName(GlobalConstants.toolbar_style);
        addComponent(top);
        final Label title = new Label(dict);
        title.setSizeUndefined();
        title.addStyleName(GlobalConstants.h1_style);
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        Button newEventBtn =  new Button(New);
        newEventBtn.setImmediate(true);
        newEventBtn.addStyleName(GlobalConstants.default_style);
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
        setWidth("99%");
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
