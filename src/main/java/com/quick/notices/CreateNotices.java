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
import com.quick.global.GlobalConstants;
import com.quick.utilities.ConfirmationDialogueBox;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
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
    private Button editSaveNoticeBtn;
    private Button newNoticeBtn;
    private Button delete;
    private int selectedNoticeId;
    private Userprofile profile;
    private HorizontalLayout row;
    private VerticalLayout blankVerticalLayout;

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
        setVisibilityOfAddDeleteButtonsByRole();
    }
    
    public CreateNotices(){
        setNoticeList(fetchNotices());
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(true);        
        top.addStyleName(GlobalConstants.toolbar_style);
        top.addStyleName("lightBackgroundForDashboardActivity");
        top.addStyleName("lightGrayFourSideBorder");
        addComponent(top);
        
        final Label title = new Label("Notices");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        HorizontalLayout buttonLayout=getNewSaveButtonLayout();
        top.addComponent(buttonLayout);
        top.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);

        
        row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 1.5f);
       // Component c=buildTabSheetLayout();
        
        //form created first because below method tries to set its values on table value change
        getNoticeForm();
        
        
        blankVerticalLayout = new VerticalLayout();
        blankVerticalLayout.setSizeFull();
        row.addComponent(blankVerticalLayout);
        
        Component l = UIUtils.createPanel(getNoticeListView());
        blankVerticalLayout.addComponent(l);
        
        
//        row.addComponent(form);
//        row.setComponentAlignment(form, Alignment.MIDDLE_RIGHT);
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
        noticetbl.setHeight("100%");
        noticetbl.setPageLength(20);
        noticetbl.setSelectable(true);
        noticetbl.setImmediate(true); // react at once when something is selected
        noticetbl.setContainerDataSource(NoticesContainer.getNoticesContainer(getNoticeList()));
        noticetbl.setVisibleColumns(NoticesContainer.NATURAL_COL_ORDER_NOTICES);
        noticetbl.setColumnHeaders(NoticesContainer.COL_HEADERS_ENGLISH_NOTICES);
        noticetblValueChangeListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                NoticeBean bean = (NoticeBean)event.getProperty().getValue();
                if(bean!=null)
                {
                    showSelectedTechnologyDetailsOnHtmlFrom(bean);
                    updateNoticeFormField(bean);
                    lastSelectedNoticeBean=bean;
                }
            }

        };
        
        noticetbl.addValueChangeListener(noticetblValueChangeListener);
        noticetbl.sort(new Object[]{"noticedate"}, new boolean[]{false});
        noticetbl.select(noticetbl.firstItemId());
        
        noticetbl.addGeneratedColumn("Remove", new Table.ColumnGenerator() 
            {
            @Override
                public Object generateCell(Table source, Object rowItemBean, Object columnId) 
                {
                    Button btnRemove=new Button("Remove");
                    btnRemove.setImmediate(true);
                    btnRemove.setStyleName(BaseTheme.BUTTON_LINK);
                    setItemData(btnRemove,rowItemBean);
                    addListenertoBtn(btnRemove);               
                    return btnRemove;
                }
            });
        return noticetbl;
    }
    
    public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{noticetbl,rowItemBean,GlobalConstants.emptyString};
            btnRemove.setData(arr);
        }
    
    /**
      * adding listener to the remove button for delete topic from upload screen
      * */
     public void addListenertoBtn(Button btnRemove) 
    {
        
        btnRemove.addListener(new Button.ClickListener() 
        {
            public void buttonClick(final Button.ClickEvent event) 
            {
                //UI.getCurrent().addWindow(new ConfirmationDialogueBox());
                
                UI.getCurrent().addWindow(new ConfirmationDialogueBox("Confirmation", "Are you sure you want to remove this notice ?", new ConfirmationDialogueBox.Callback() {

                    @Override
                    public void onDialogResult(boolean flag) 
                    {
                        if(flag)
                        {
                            Object data[] = (Object[]) event.getButton().getData();
                            
                            Table t = (Table) data[0];
                            
                            //System.out.println("****"+((MasteParmBean)data[1]));
                            
                            deleteNotice();
                            
                            // temporary removing value change listener of the quick upload table so that after removing item it will not attempt to display that particular item
                            t.removeValueChangeListener(noticetblValueChangeListener);
        
                            t.removeItem(data[1]);
                            
                            //restoring the value change listener so that selected uploaded item will be displayed in the right panel
                            t.addValueChangeListener(noticetblValueChangeListener);
                            //updateUtList();   

                            t.select(t.firstItemId());
                        }
                    }
                }));
            }
        });
        
        
    }
    
    private void showSelectedTechnologyDetailsOnHtmlFrom(NoticeBean bean) {
                
                Label html;
                if(bean!=null)
                {
                    html = new Label("<table height='100%'>" //+" <tr><td align='right'><b>Create by :</b></td><td>"+bean.getBywhom()+"</td></tr>"
                        +"<tr><td align='right' width='15%'><b>Date:</b></td><td width='85%'>"+bean.getNoticedate()+"</td></tr>"
                            +"<tr><td align='right' width='15%'></td><td width='85%'>"+GlobalConstants.emptyString+"</td></tr>"
                        +"<tr width='100%'><td align='right' width='15%'><b>Subject:</b></td><td width='85%'>"+bean.getNoticeline()+"</td></tr>"
                            +"<tr><td align='right' width='15%'></td><td width='85%'>"+GlobalConstants.emptyString+"</td></tr>"
                        +"<tr><td align='right' width='15%'><b>Details:</b></td><td width='85%'>"+bean.getNoticebody()+"</td></tr>"
                        + "</table>", ContentMode.HTML);
                    //setSelectedUTId(bean.getTechnologyid());
                    updateNoticeFormField(bean);
                    
                    
                }
                else
                {
                    html = new Label("<table>"// + "<tr><td align='right'><b>Create by :</b></td><td>"+"</td></tr>"
                        +"<tr><td align='right'><b>Date :</b></td><td>"+"</td></tr>"
                        +"<tr><td align='right'><b>Subject :</b></td><td>"+"</td></tr>"
                        +"<tr><td align='right'><b>Details :</b></td><td>"+"</td></tr>"
                        + "</table>", ContentMode.HTML);
                    
                }
                
                if(topicDetailsHtmlCssLayout!=null)
                {
                    row.removeComponent(topicDetailsHtmlCssLayout);                    
                }
                VerticalLayout htmlLayoutForDetails;
                htmlLayoutForDetails= new VerticalLayout();
                htmlLayoutForDetails.setCaption(notice_details);
                htmlLayoutForDetails.setSizeFull();
                htmlLayoutForDetails.addComponent(html);
                topicDetailsHtmlCssLayout = UIUtils.createPanel(htmlLayoutForDetails);
                //topicDetailsHtmlCssLayout.setHeight("100%");
                row.addComponent(topicDetailsHtmlCssLayout);
            }
    
    private CssLayout topicDetailsHtmlCssLayout;
    private NoticeBean lastSelectedNoticeBean;
    private static final String notice_details="Notice Details";
    private VerticalLayout noticeFormLayout;
    private VerticalLayout getNoticeForm() {
        noticeFormLayout = new VerticalLayout();
        noticeFormLayout.setCaption(notice_details);
        //noticeFormLayout.setSizeFull();
        noticeFormLayout.setSpacing(true);
        
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
        
        noticeFormLayout.addComponent(noticeId);
        noticeFormLayout.addComponent(notecedate);
        noticeFormLayout.addComponent(bywhom);
        noticeFormLayout.addComponent(noticeline);
        noticeFormLayout.addComponent(noticebody);
        
        //profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        //if(profile.get)
        
        
        return noticeFormLayout;
    }
    private HorizontalLayout getNewSaveButtonLayout() 
    {
        
        editSaveNoticeBtn = new Button("Edit notice");
        editSaveNoticeBtn.setStyleName(GlobalConstants.default_style);
        editSaveNoticeBtn.addClickListener((Button.ClickListener)this);
        editSaveNoticeBtn.setImmediate(true);
        
        newNoticeBtn = new Button("New notice");
        newNoticeBtn.addClickListener((Button.ClickListener)this);
        newNoticeBtn.setImmediate(true);
        newNoticeBtn.setStyleName(GlobalConstants.default_style);
        
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        
        buttonLayout.addComponent(newNoticeBtn);
        buttonLayout.addComponent(editSaveNoticeBtn);        
        
        return buttonLayout;
        
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

    /* @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        if(source==editSaveNoticeBtn){
            createNewNotice();
            updateNoticeList();
        }else if(source==newNoticeBtn){
            clearForm();
        }else if(source==delete){
            deleteNotice();
            updateNoticeList();            
        }
    } */
    
    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        if(source==editSaveNoticeBtn)
        {
            if(editSaveNoticeBtn.getCaption().equals("Edit notice"))
            {
                editSaveNoticeBtn.setCaption("Save notice");
                newNoticeBtn.setCaption("Cancel");
                
                row.removeComponent(topicDetailsHtmlCssLayout);
                row.addComponent(noticeFormLayout);
            }
            else if(editSaveNoticeBtn.getCaption().equals("Save notice"))
            {
                createNewNotice();

            }
            
        }else if(source==newNoticeBtn){
            
            clearForm();
            if(newNoticeBtn.getCaption().equals("New notice"))
            {
                editSaveNoticeBtn.setCaption("Save notice");
                newNoticeBtn.setCaption("Cancel");
                
                row.removeComponent(topicDetailsHtmlCssLayout);
                row.addComponent(noticeFormLayout);
                
            }
            else if(newNoticeBtn.getCaption().equals("Cancel"))
            {
                updateNoticeFormField(null);
                editSaveNoticeBtn.setCaption("Edit notice");
                newNoticeBtn.setCaption("New notice");
                
                row.removeComponent(noticeFormLayout);
                row.addComponent(topicDetailsHtmlCssLayout);
            }
        }
        //else if(source==delete){
//            deleteTechnology();
//            updateUtList();            
//        }
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

    private void createNewNotice() 
    {
        if(notecedate.getValue()==null)
        {
            Notification.show("Please enter notice date.", Notification.Type.WARNING_MESSAGE);
        }
        else if(noticeline.getValue()==null || ((String)noticeline.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter notice subject.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)noticeline.getValue()).trim().length()>150)
        {
            Notification.show("Notice subject cannot be more than 150 characters.", Notification.Type.WARNING_MESSAGE);
        }
        else if(noticebody.getValue()==null || ((String)noticebody.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter notice message.", Notification.Type.WARNING_MESSAGE);
        }
        else if (((String) noticebody.getValue()).trim().length() > 300) {
            Notification.show("Notice message cannot be more than 300 characters.", Notification.Type.WARNING_MESSAGE);
        } 
        else {
            try {
                Client client = Client.create();
                WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_NOTICE));
                JSONObject inputJson = new JSONObject();
                try {
                    if (noticeId.getValue() != null && !noticeId.getValue().trim().equals(GlobalConstants.emptyString)) {
                        inputJson.put("noticeId", noticeId.getValue());
                    } else {
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

                if (status == GlobalConstants.YES) 
                {
                    Notification.show("Successfully saved notice", Notification.Type.WARNING_MESSAGE);
                    updateNoticeList();
                    row.removeComponent(noticeFormLayout);
                    editSaveNoticeBtn.setCaption("Edit notice");
                    newNoticeBtn.setCaption("New notice");
                } else {
                    Notification.show("Notice saving failed", Notification.Type.WARNING_MESSAGE);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
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
        
        if(bean==null)
        {
            bean=lastSelectedNoticeBean;
        }
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

private void setVisibilityOfAddDeleteButtonsByRole() 
    {
        if(profile.getRole().equals(GlobalConstants.student))
        {
            editSaveNoticeBtn.setVisible(false);
            newNoticeBtn.setVisible(false);
            noticetbl.setVisibleColumns(NoticesContainer.NATURAL_COL_ORDER_NOTICES);
        }
        
        
    }   
    
}
