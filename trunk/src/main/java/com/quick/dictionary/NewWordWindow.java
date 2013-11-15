/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.dictionary;

import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 *
 * @author sonalis
 */
public class NewWordWindow extends Window implements Button.ClickListener{
    
    private VerticalLayout baseLayout;
    
    public NewWordWindow(ClickEvent event){
        setModal(true);
        setCaption("New word");
        center();
        setClosable(true);
        setWidth("50%");
        setHeight("80%"); 
        buildBaseLayout();
        getWordDetailsInputForm();
        setContent(baseLayout);
        addStyleName("schedule");
        
        setClosable(false);
        setResizable(false);

        w=this;
    }
    
     private void buildBaseLayout(){
              
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

    
    private TextField word;
    private TextArea meaning;
    private TextField wordLabel;
    private Window w;

    private void getWordDetailsInputForm() 
    {
        word = new TextField("Word");
        word.setImmediate(true);
        word.setWidth("60%");
        word.setInputPrompt("Enter word here");
        
        meaning = new TextArea("Meaning");
        meaning.setImmediate(true);
        meaning.setWidth("90%");
        meaning.setRows(8);
        meaning.setInputPrompt("Meaning, usage and examples of the word comes here");
        
        wordLabel = new TextField("Labels");
        wordLabel.setImmediate(true);
        wordLabel.setWidth("90%");
        wordLabel.setInputPrompt("Enter comma separated Subjects, Topics, Categories here");
        
        
        
        
        
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
                   saveWordDetails();
                   
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
        
        layout.addComponent(word);
        layout.addComponent(meaning);
        layout.addComponent(wordLabel);
        
        baseLayout.addComponent(layout);
        baseLayout.addComponent(h);
        
        baseLayout.setExpandRatio(layout,3f);        
        baseLayout.setExpandRatio(h,0.5f);
    }
    
    
    private void saveWordDetails() 
    {
        if (word.getValue() == null || ((String) word.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter word.", Notification.Type.WARNING_MESSAGE);
        }
        else if (((String) word.getValue()).trim().length() > 50) 
        {
            Notification.show("Word length cannot be more than 50 characters.", Notification.Type.WARNING_MESSAGE);
        }
        
        else if (meaning.getValue() == null || ((String) meaning.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter meaning of the word.", Notification.Type.WARNING_MESSAGE);
        }
        else if (((String) meaning.getValue()).trim().length() > 1500) 
        {
            Notification.show("Word meaning cannot be more than 1500 characters.", Notification.Type.WARNING_MESSAGE);
        }
        else if (wordLabel.getValue() == null || ((String) wordLabel.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter labels for the word.", Notification.Type.WARNING_MESSAGE);
        }
        else if (((String) wordLabel.getValue()).trim().length() > 300) 
        {
            Notification.show("Word meaning cannot be more than 300 characters.", Notification.Type.WARNING_MESSAGE);
        }
         else {
            try {
                Client client = Client.create();
                WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_NEW_WORD_DETAILS));
                //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
                JSONObject inputJson = new JSONObject();
                try {

                    Userprofile loggedinProfile = (Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile);
                    inputJson.put("word", word.getValue());
                    inputJson.put("meaning", meaning.getValue());
                    inputJson.put("labels", wordLabel.getValue());
                    inputJson.put("owner_username", loggedinProfile.getUsername());
                    inputJson.put("owner_name", loggedinProfile.getName());
                    //inputJson.put("image_filename", imageFileName);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);


                JSONObject outNObject = null;
                String output = response.getEntity(String.class);
                outNObject = new JSONObject(output);
                Notification.show(outNObject.getString(GlobalConstants.STATUS), Notification.Type.WARNING_MESSAGE);
                
                getUI().getNavigator().navigateTo(GlobalConstants.ROUT_DICT);
                w.close();

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        }
    }
}
