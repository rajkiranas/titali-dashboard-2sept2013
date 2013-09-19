/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.upcomingtechnology;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.UpcomingTechnologyBean;
import com.quick.bean.Userprofile;
import com.quick.container.UpcomingTechnologyContainer;
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
public class CreateUpcomingTechnology extends VerticalLayout implements View ,Button.ClickListener{
    private Table upcomingTechnologyTbl;
    private List<UpcomingTechnologyBean> utBeanList;
    private PopupDateField utDate;
    private TextField utId;
    private TextField bywhom;
    private TextField utLine;
    private TextArea utBody;
    private Button createnUT;
    private Button clear;
    private Button delete;
    private int selectedUTId;
    private Userprofile profile;

    private  Property.ValueChangeListener utTblValueChangeListener;

    public int getSelectedUTId() {
        return selectedUTId;
    }

    public void setSelectedUTId(int selectedUTId) {
        this.selectedUTId = selectedUTId;
    }
    
    
    public List<UpcomingTechnologyBean> getUTList() {
        return utBeanList;
    }

    public void setTechnologiesList(List<UpcomingTechnologyBean> utBeanList) {
        this.utBeanList = utBeanList;
    }
    
    
    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        addStyleName("schedule");
        profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        
    }
    
    public CreateUpcomingTechnology(){
        setTechnologiesList(fetchTechnologies());
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName("toolbar");
        addComponent(top);
        
        final Label title = new Label("Technologies");
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
        Component form = UIUtils.createPanel(getUTForm());
        
        row.addComponent(UIUtils.createPanel(getUTListView()));
        
        
        row.addComponent(form);
        row.setComponentAlignment(form, Alignment.MIDDLE_RIGHT);
    }

    private Component getUTListView() 
    {
        //noticetbl = new Table();
        
        upcomingTechnologyTbl = new Table(){
            
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
        
        
        
        upcomingTechnologyTbl.addStyleName("borderless");
        ///noticetbl.setSortEnabled(false);
        upcomingTechnologyTbl.setWidth("100%");
        upcomingTechnologyTbl.setPageLength(15);
        upcomingTechnologyTbl.setSelectable(true);
        upcomingTechnologyTbl.setImmediate(true); // react at once when something is selected
        upcomingTechnologyTbl.setContainerDataSource(UpcomingTechnologyContainer.getUTContainer(getUTList()));
        upcomingTechnologyTbl.setVisibleColumns(UpcomingTechnologyContainer.NATURAL_COL_ORDER_TECHNOLOGIES);
        upcomingTechnologyTbl.setColumnHeaders(UpcomingTechnologyContainer.COL_HEADERS_ENGLISH_TECHNOLOGIES);
        utTblValueChangeListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
               updateUtFormField((UpcomingTechnologyBean)event.getProperty().getValue());
            }

        };
        
        upcomingTechnologyTbl.addValueChangeListener(utTblValueChangeListener);
        upcomingTechnologyTbl.sort(new Object[]{"technologydate"}, new boolean[]{false});
        upcomingTechnologyTbl.select(upcomingTechnologyTbl.firstItemId());
        return upcomingTechnologyTbl;
    }

    private static final String technology_details="Technology Details";
    
    private Component getUTForm() {
        FormLayout utForm = new FormLayout();
        utForm.setCaption(technology_details);
        utForm.setSizeFull();
        
        utId = new TextField("Technology Id");
        utId.setImmediate(true);
        utId.setWidth("15%");
        utId.setReadOnly(true);
        
        utDate = new PopupDateField("Date");
        utDate.setImmediate(true);
        utDate.setWidth("25%");
        
        bywhom = new TextField("Created by");
        bywhom.setImmediate(true);
        bywhom.setWidth("30%");
        bywhom.setReadOnly(true);
        
        
        
        
        utLine = new TextField("Subject");
        utLine.setImmediate(true);
        utLine.setWidth("70%");
        
        utBody = new TextArea("Meassage");
        utBody.setImmediate(true);
        utBody.setWidth("70%");
        
        utForm.addComponent(utId);
        utForm.addComponent(utDate);
        utForm.addComponent(bywhom);
        utForm.addComponent(utLine);
        utForm.addComponent(utBody);
        
        //profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        //if(profile.get)
        
        createnUT = new Button("Save technology");
        createnUT.setStyleName(GlobalConstants.default_style);
        createnUT.addClickListener((Button.ClickListener)this);
        createnUT.setImmediate(true);
        
        
        clear = new Button("New technology");
        clear.addClickListener((Button.ClickListener)this);
        clear.setImmediate(true);
        clear.setStyleName(GlobalConstants.default_style);
        
         
        delete = new Button("Delete technology");
        delete.addClickListener((Button.ClickListener)this);
        delete.setImmediate(true);
        delete.setStyleName(GlobalConstants.default_style);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        
        buttonLayout.addComponent(clear);
        buttonLayout.addComponent(createnUT);        
        buttonLayout.addComponent(delete);
        
        utForm.addComponent(buttonLayout);
        return utForm;
    }

   
    private List<UpcomingTechnologyBean> fetchTechnologies() {
        List<UpcomingTechnologyBean> technologyList=null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_ALL_TECHNOLOGY));
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

            Type listType = new TypeToken<ArrayList<UpcomingTechnologyBean>>() {
            }.getType();
            
             technologyList=new Gson().fromJson(outNObject.getString(GlobalConstants.Technologies), listType);
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        return technologyList;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        if(source==createnUT){
            createNewTechnology();
            updateUtList();
        }else if(source==clear){
            clearForm();
        }else if(source==delete){
            deleteTechnology();
            updateUtList();            
        }
    }
    
    
    private void clearForm() {
        utId.setReadOnly(false);
        utId.setValue(GlobalConstants.emptyString);
        utId.setReadOnly(true);
        utDate.setValue(null);
        
        bywhom.setReadOnly(false);
        bywhom.setValue(profile.getName());
        bywhom.setReadOnly(true);
        
        utLine.setValue(GlobalConstants.emptyString);
        utBody.setValue(GlobalConstants.emptyString);
         
    }

    private void createNewTechnology() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_TECHNOLOGY));
            JSONObject inputJson = new JSONObject();
            try 
            {
                if(utId.getValue()!=null && !utId.getValue().trim().equals(GlobalConstants.emptyString))
                {
                    inputJson.put("technologyId", utId.getValue());
                }
                else
                {
                    inputJson.put("technologyId", 0);
                }
                inputJson.put("technologydate", utDate.getValue().getTime());
                inputJson.put("bywhom", bywhom.getValue());
                inputJson.put("technologyline", utLine.getValue());
                inputJson.put("technologybody", utBody.getValue());
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
                Notification.show("Successfully created technology", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Technology creation failed", Notification.Type.WARNING_MESSAGE);
            }

          
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }

    
     private void deleteTechnology() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DELETE_TECHNOLOGY));
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("technologyId",getSelectedUTId());
              
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
                Notification.show("Successfully deleted technology", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Technology deletion failed", Notification.Type.WARNING_MESSAGE);
            }

          
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }

    
    private void updateUtFormField(UpcomingTechnologyBean bean) {
        
        utId.setReadOnly(false);
        utId.setValue(String.valueOf(bean.getTechnologyid()));
        utId.setReadOnly(true);
        
        utDate.setValue(bean.getTechnologydate());
        
        bywhom.setReadOnly(false);
        bywhom.setValue(bean.getBywhom());        
        bywhom.setReadOnly(true);
        
        utLine.setValue(bean.getTechnologyline());
        utBody.setValue(bean.getTechnologybody()); 
        setSelectedUTId(bean.getTechnologyid());
    }

    private void updateUtList() 
    {
        upcomingTechnologyTbl.removeValueChangeListener(utTblValueChangeListener);
        upcomingTechnologyTbl.getContainerDataSource().removeAllItems();
        setTechnologiesList(fetchTechnologies());
        upcomingTechnologyTbl.setContainerDataSource(UpcomingTechnologyContainer.getUTContainer(getUTList()));
        upcomingTechnologyTbl.setVisibleColumns(UpcomingTechnologyContainer.NATURAL_COL_ORDER_TECHNOLOGIES);
        upcomingTechnologyTbl.setColumnHeaders(UpcomingTechnologyContainer.COL_HEADERS_ENGLISH_TECHNOLOGIES);
        upcomingTechnologyTbl.addValueChangeListener(utTblValueChangeListener);
        upcomingTechnologyTbl.sort(new Object[]{"technologydate"}, new boolean[]{false});
        upcomingTechnologyTbl.select(upcomingTechnologyTbl.firstItemId());
    }

   
    
}
