/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.forum;

import com.google.gdata.util.common.util.Base64;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.quick.utilities.ImageResizer;
import com.quick.utilities.UploadReceiver;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.io.File;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 *
 * @author sonalis
 */
public class NewEventWindow extends Window implements Button.ClickListener{
    
    private VerticalLayout baseLayout;
    
    public NewEventWindow(ClickEvent event){
        setModal(true);
        setCaption("New event");
        center();
        setClosable(true);
        setWidth("50%");
        setHeight("75%"); 
        buildBaseStudentLayout();
        getEventDetailsInputForm();
        setContent(baseLayout);
        addStyleName("schedule");
        
        setClosable(false);
        setResizable(false);
        //setDraggable(false);
//        setPositionX(event.getClientX() - (event.getRelativeX()+300));
//        setPositionY(event.getClientY() - event.getRelativeY());
        //setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        w=this;
    }
    
     private void buildBaseStudentLayout(){
              
       baseLayout = new VerticalLayout();
       baseLayout.setSpacing(true);   
       baseLayout.setImmediate(true);
       baseLayout.setMargin(true);
       baseLayout.setWidth("100%");
       baseLayout.setHeight("100%");
    }

    @Override
    public void buttonClick(ClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private UploadReceiver uploadReceiver = new UploadReceiver();
    private Upload upload;
    private TextField subject;
    private TextArea desc;
    private File eventPicture;
    private Window w;
    private String imageFileName;
    private byte[] eventImageArray;

    private void getEventDetailsInputForm() 
    {
        subject = new TextField("Subject");
        subject.setImmediate(true);
        subject.setInputPrompt("Event heading comes here");
        subject.setWidth("50%");
        
        desc = new TextArea("Description");
        desc.setImmediate(true);
        desc.setInputPrompt("Event description comes here");
        desc.setWidth("90%");
        desc.setRows(8);
        
        upload = new Upload(null, uploadReceiver);
        upload.setImmediate(true);
        upload.setButtonCaption("Image");
        upload.addStyleName("notifications");


        final Button cancelProcessing = new Button("Cancel");
        cancelProcessing.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setStyleName("small");

        upload.addListener(new Upload.SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                
                
                imageFileName=uploadReceiver.getFileName();
                eventPicture=uploadReceiver.getFile();
                eventImageArray= ImageResizer.resize(eventPicture,imageFileName);
            }
        });

        upload.addListener(new Upload.ProgressListener() {
            
            @Override
            public void updateProgress(long readBytes, long contentLength) {
            }
        });
        
        
           HorizontalLayout h = new HorizontalLayout();
           h.setWidth("100%");
           h.setSpacing(true);
           Button save = new Button("Save");
           save.setImmediate(true);
           save.addStyleName("default");
           save.setWidth("100%");
           save.addListener(new Button.ClickListener() {

               @Override
               public void buttonClick(ClickEvent event) {
                   saveEventDetails();
                   
               }
           });
           
           Button cancel = new Button("Close");
           cancel.setImmediate(true);
           cancel.addStyleName("default");
           cancel.setWidth("100%");
           cancel.addListener(new Button.ClickListener() {

               @Override
               public void buttonClick(ClickEvent event) {
                   w.close();
               }
           });
           
           h.addComponent(save);
           h.addComponent(cancel);
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.addComponent(subject);
        layout.addComponent(upload);
        layout.addComponent(desc);
        
        baseLayout.addComponent(layout);
        baseLayout.addComponent(h);
        
        baseLayout.setExpandRatio(layout,3f);        
        baseLayout.setExpandRatio(h,0.5f);
    }
    
    
    private void saveEventDetails() 
    {
        if (subject.getValue() == null || ((String) subject.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter event subject.", Notification.Type.WARNING_MESSAGE);
        }
        else if (((String) subject.getValue()).trim().length() > 100) 
        {
            Notification.show("Event subject cannot be more than 100 characters.", Notification.Type.WARNING_MESSAGE);
        }
        else if(imageFileName == null)
        {
            Notification.show("Please upload image for the event.", Notification.Type.WARNING_MESSAGE);
        }
        else if (desc.getValue() == null || ((String) desc.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter event description.", Notification.Type.WARNING_MESSAGE);
        }
        else if (((String) desc.getValue()).trim().length() > 1000) 
        {
            Notification.show("Event description cannot be more than 1000 characters.", Notification.Type.WARNING_MESSAGE);
        }
         else {
            try {
                Client client = Client.create();
                WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_EVENT_DETAILS));
                //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
                JSONObject inputJson = new JSONObject();
                try {

                    Userprofile loggedinProfile = (Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile);
                    inputJson.put("event_desc", subject.getValue());
                    inputJson.put("event_body", desc.getValue());
                    inputJson.put("image", new String(Base64.encode(eventImageArray)));
                    inputJson.put("owner", loggedinProfile.getUsername());
                    inputJson.put("image_filename", imageFileName);
                    ViewEventDetailsWindow w = new ViewEventDetailsWindow();
                    inputJson.put("classToInvoke", w.getClass().getName());

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);


                JSONObject outNObject = null;
                String output = response.getEntity(String.class);
                outNObject = new JSONObject(output);
                Notification.show(outNObject.getString(GlobalConstants.STATUS), Notification.Type.WARNING_MESSAGE);
                
                getUI().getNavigator().navigateTo(GlobalConstants.ROUT_FORUM);
                w.close();

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        }
    }
}
