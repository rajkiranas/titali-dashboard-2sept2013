/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.notices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.NoticeBean;
import com.quick.bean.Userprofile;
import com.quick.container.NoticesContainer;
import com.quick.entity.Notices;
import com.quick.global.GlobalConstants;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rajkirans
 */
public class CreateNotices extends VerticalLayout implements View ,Button.ClickListener{
    private Table noticetbl;
    private List<NoticeBean> noticeList;
    private PopupDateField notecedate;
    private TextField noticeId;
    private TextField bywhom;
    private TextField noticeline;
    private TextArea noticebody;
    private Button createnotice;
    private Button clear;
    private Button delete;
    private int selectedNoticeId;
    private Userprofile profile;

    private  Property.ValueChangeListener noticetblValueChangeListener;

    public int getSelectedNoticeId() {
        return selectedNoticeId;
    }

    public void setSelectedNoticeId(int selectedNoticeId) {
        this.selectedNoticeId = selectedNoticeId;
    }
    
    
    public List<NoticeBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeBean> noticeList) {
        this.noticeList = noticeList;
    }
    
    
    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        addStyleName("schedule");
        profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        
    }
    
    public CreateNotices(){
        setNoticeList(fetchNotices());
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName("toolbar");
        addComponent(top);
        
        final Label title = new Label("Notices");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);

        
        HorizontalLayout row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 1.5f);
       // Component c=buildTabSheetLayout();
        
        //form created first because below method tries to set its values on table value change
        Component form = UIUtils.createPanel(getNoticeForm());
        
        row.addComponent(UIUtils.createPanel(getNoticeListView()));
        
        
        row.addComponent(form);
        row.setComponentAlignment(form, Alignment.MIDDLE_RIGHT);
    }

    private Component getNoticeListView() 
    {
        //noticetbl = new Table();
        
        noticetbl = new Table(){
            
            SimpleDateFormat df =
               new SimpleDateFormat(GlobalConstants.dateFormatMMddyyyy);
        
      @Override
       protected String formatPropertyValue(Object rowId,
           Object colId, Property property) {
       // Format by property type
       if (property.getType() == Date.class) {
           if(property.getValue()!=null)           
           return df.format((Date)property.getValue());
           else
             return GlobalConstants.emptyString;
       }
       return super.formatPropertyValue(rowId, colId, property);
   
}
  };
        
        
        
        noticetbl.addStyleName("borderless");
        ///noticetbl.setSortEnabled(false);
        noticetbl.setWidth("100%");
        noticetbl.setPageLength(15);
        noticetbl.setSelectable(true);
        noticetbl.setImmediate(true); // react at once when something is selected
        noticetbl.setContainerDataSource(NoticesContainer.getNoticesContainer(getNoticeList()));
        noticetbl.setVisibleColumns(NoticesContainer.NATURAL_COL_ORDER_NOTICES);
        noticetbl.setColumnHeaders(NoticesContainer.COL_HEADERS_ENGLISH_NOTICES);
        noticetblValueChangeListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
               updateNoticeFormField((NoticeBean)event.getProperty().getValue());
            }

        };
        
        noticetbl.addValueChangeListener(noticetblValueChangeListener);
        noticetbl.sort(new Object[]{"noticedate"}, new boolean[]{false});
        noticetbl.select(noticetbl.firstItemId());
        return noticetbl;
    }

    private static final String notice_details="Notice Details";
    
    private Component getNoticeForm() {
        FormLayout noticeForm = new FormLayout();
        noticeForm.setCaption(notice_details);
        noticeForm.setSizeFull();
        
        noticeId = new TextField("Notice Id");
        noticeId.setImmediate(true);
        noticeId.setWidth("15%");
        noticeId.setReadOnly(true);
        
        notecedate = new PopupDateField("Date");
        notecedate.setImmediate(true);
        notecedate.setWidth("25%");
        
        bywhom = new TextField("Created by");
        bywhom.setImmediate(true);
        bywhom.setWidth("30%");
        bywhom.setReadOnly(true);
        
        
        
        
        noticeline = new TextField("Subject");
        noticeline.setImmediate(true);
        noticeline.setWidth("70%");
        
        noticebody = new TextArea("Meassage");
        noticebody.setImmediate(true);
        noticebody.setWidth("70%");
        
        noticeForm.addComponent(noticeId);
        noticeForm.addComponent(notecedate);
        noticeForm.addComponent(bywhom);
        noticeForm.addComponent(noticeline);
        noticeForm.addComponent(noticebody);
        
        //profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        //if(profile.get)
        
        createnotice = new Button("Save notice");
        createnotice.setStyleName(GlobalConstants.default_style);
        createnotice.addClickListener((Button.ClickListener)this);
        createnotice.setImmediate(true);
        
        
        clear = new Button("New notice");
        clear.addClickListener((Button.ClickListener)this);
        clear.setImmediate(true);
        clear.setStyleName(GlobalConstants.default_style);
        
         
        delete = new Button("Delete Notice");
        delete.addClickListener((Button.ClickListener)this);
        delete.setImmediate(true);
        delete.setStyleName(GlobalConstants.default_style);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        
        buttonLayout.addComponent(clear);
        buttonLayout.addComponent(createnotice);        
        buttonLayout.addComponent(delete);
        
        noticeForm.addComponent(buttonLayout);
        return noticeForm;
    }

   
    private List<NoticeBean> fetchNotices() {
        List<NoticeBean> noticeList=null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_ALL_NOTICES));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
//            try {
//                inputJson.put("standard", "MCA-I");
//                inputJson.put("division", "A");
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);

          
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<NoticeBean>>() {
            }.getType();
            
             noticeList=new Gson().fromJson(outNObject.getString(GlobalConstants.NOTICES), listType);
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        return noticeList;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        if(source==createnotice){
            createNewNotice();
            updateNoticeList();
        }else if(source==clear){
            clearForm();
        }else if(source==delete){
            deleteNotice();
            updateNoticeList();            
        }
    }
    
    
    private void clearForm() {
        noticeId.setReadOnly(false);
        noticeId.setValue(GlobalConstants.emptyString);
        noticeId.setReadOnly(true);
        notecedate.setValue(null);
        
        bywhom.setReadOnly(false);
        bywhom.setValue(profile.getName());
        bywhom.setReadOnly(true);
        
        noticeline.setValue(GlobalConstants.emptyString);
        noticebody.setValue(GlobalConstants.emptyString);
         
    }

    private void createNewNotice() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_NOTICE));
            JSONObject inputJson = new JSONObject();
            try 
            {
                if(noticeId.getValue()!=null && !noticeId.getValue().trim().equals(GlobalConstants.emptyString))
                {
                    inputJson.put("noticeId", noticeId.getValue());
                }
                else
                {
                    inputJson.put("noticeId", 0);
                }
                inputJson.put("noticedate", notecedate.getValue().getTime());
                inputJson.put("bywhom", bywhom.getValue());
                inputJson.put("noticeline", noticeline.getValue());
                inputJson.put("noticebody", noticebody.getValue());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
            int status = Integer.parseInt(outNObject.getString(GlobalConstants.STATUS));
            
            if(status == GlobalConstants.YES)
            {
                Notification.show("Successfully created notice", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Notice creation failed", Notification.Type.WARNING_MESSAGE);
            }

          
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }

    
     private void deleteNotice() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DELETE_NOTICE));
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("noticeId",getSelectedNoticeId());
              
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
            int status = Integer.parseInt(outNObject.getString(GlobalConstants.STATUS));
            
            if(status == GlobalConstants.YES)
            {
                Notification.show("Successfully deleted notice", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Notice deletion failed", Notification.Type.WARNING_MESSAGE);
            }

          
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }

    
    private void updateNoticeFormField(NoticeBean bean) {
        
        noticeId.setReadOnly(false);
        noticeId.setValue(String.valueOf(bean.getNoticeid()));
        noticeId.setReadOnly(true);
        
        notecedate.setValue(bean.getNoticedate());
        
        bywhom.setReadOnly(false);
        bywhom.setValue(bean.getBywhom());        
        bywhom.setReadOnly(true);
        
        noticeline.setValue(bean.getNoticeline());
        noticebody.setValue(bean.getNoticebody()); 
        setSelectedNoticeId(bean.getNoticeid());
    }

    private void updateNoticeList() 
    {
        noticetbl.removeValueChangeListener(noticetblValueChangeListener);
        noticetbl.getContainerDataSource().removeAllItems();
        setNoticeList(fetchNotices());
        noticetbl.setContainerDataSource(NoticesContainer.getNoticesContainer(getNoticeList()));
        noticetbl.setVisibleColumns(NoticesContainer.NATURAL_COL_ORDER_NOTICES);
        noticetbl.setColumnHeaders(NoticesContainer.COL_HEADERS_ENGLISH_NOTICES);
        noticetbl.addValueChangeListener(noticetblValueChangeListener);
        noticetbl.sort(new Object[]{"noticedate"}, new boolean[]{false});
        noticetbl.select(noticetbl.firstItemId());
    }

   
    
}
