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
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 *
 * @author sonalis
 */
public class NewEventWindow extends Window implements Button.ClickListener{
    
    private VerticalLayout baseLayout;
    private String newlyCreatedEventId;
    
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
    private OptionGroup imageVideoOption;
    private TextField subject;
    private TextField txtVideoUrl;
    private TextArea desc;
    private File eventPicture;
    private Window w;
    private String imageFileName;
    private byte[] eventImageArray;

    private void getEventDetailsInputForm() 
    {
        initializeImageOrVideoOption();
        
        txtVideoUrl = new TextField();
        txtVideoUrl.setImmediate(true);
        txtVideoUrl.setInputPrompt("Please enter youtube URL");
        //txtVideoUrl.setWidth("50%");
        
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
        
        subject = new TextField("Subject");
        subject.setImmediate(true);
        subject.setInputPrompt("Event heading comes here");
        subject.setWidth("50%");
        
        
        
        desc = new TextArea("Description");
        desc.setImmediate(true);
        desc.setInputPrompt("Event description comes here");
        desc.setWidth("90%");
        desc.setRows(8);
        
        
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
        
        layout.addComponent(imageVideoOption);
        HorizontalLayout ivLayout = new HorizontalLayout();
//        ivLayout.setWidth("100%");
        ivLayout.setSpacing(true);
        ivLayout.addComponent(upload);
        ivLayout.addComponent(new Label("<b>OR</b>", ContentMode.HTML));
        ivLayout.addComponent(txtVideoUrl);
        
//        layout.addComponent(upload);
//        layout.addComponent(txtVideoUrl);
        layout.addComponent(ivLayout);
        layout.addComponent(subject);        
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
        else if(upload.isEnabled() && imageFileName == null)
        {
            Notification.show("Please upload image for the event.", Notification.Type.WARNING_MESSAGE);
        }
        else if(txtVideoUrl.isEnabled() && (txtVideoUrl.getValue()==null || txtVideoUrl.getValue().equals(GlobalConstants.emptyString)))
        {
            Notification.show("Please enter youtube url of video.", Notification.Type.WARNING_MESSAGE);
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
                    if(upload.isEnabled())
                    {
                        inputJson.put("image", new String(Base64.encode(eventImageArray)));
                        inputJson.put("image_filename", imageFileName);
                    }
                    else
                    {
                        inputJson.put("image", "null");
                    }
                    
                    if(txtVideoUrl.isEnabled())
                    {
                        inputJson.put("videoUrl", txtVideoUrl.getValue());
                    }
                    else
                    {
                        inputJson.put("videoUrl", "null");
                    }
                    
                    inputJson.put("owner", loggedinProfile.getName());
                    ViewEventDetailsWindow w = new ViewEventDetailsWindow();
                    inputJson.put("classToInvoke", w.getClass().getName());

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);


                JSONObject outNObject = null;
                String output = response.getEntity(String.class);
                outNObject = new JSONObject(output);
                newlyCreatedEventId = outNObject.getString("newlyCreatedEventId");
                if(upload.isEnabled())
                {
                    saveResizedTopicImageToFileSystem();
                }
                
                Notification.show(outNObject.getString(GlobalConstants.STATUS), Notification.Type.WARNING_MESSAGE);
                
                getUI().getNavigator().navigateTo(GlobalConstants.ROUT_FORUM);
                w.close();

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        }
    }
    
     private void saveResizedTopicImageToFileSystem() 
            {
                FileOutputStream fileOuputStream = null;
                try 
                {
                    String ext=imageFileName.substring(imageFileName.indexOf(GlobalConstants.FULL_STOP));
                    
//                     fileOuputStream = new FileOutputStream(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)
//                            +standardtxt.getValue()+GlobalConstants.HYPHEN
//                            +subjecttxt.getValue() +GlobalConstants.HYPHEN
//                            +topictxt.getValue()+GlobalConstants.HYPHEN
//                            +ext); 
                    fileOuputStream = new FileOutputStream(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)
                    +newlyCreatedEventId + ext);
                    
                    fileOuputStream.write(eventImageArray);
                    fileOuputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        fileOuputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

    private void initializeImageOrVideoOption() {
        imageVideoOption = new OptionGroup("Share Image/Video");
        imageVideoOption.setImmediate(true);
        imageVideoOption.addStyleName("horizontal");
        
        imageVideoOption.addItem("Image");
        imageVideoOption.setItemCaption("Image", "Image");
        
        imageVideoOption.addItem("Video");
        imageVideoOption.setItemCaption("Video", "Video");
        
        imageVideoOption.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
               String valueString = String.valueOf(event.getProperty().getValue());
//                System.out.println("***valueString="+valueString);
                if(valueString.equals("Image"))
                {
                    upload.setEnabled(true);
                    txtVideoUrl.setEnabled(false);
                }
                else
                {
                    txtVideoUrl.setEnabled(true);
                    upload.setEnabled(false);
                }
            }
        });
    }
}
