/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.QuickLearn;

import com.quick.bean.MasteParmBean;
import com.vaadin.demo.dashboard.*;
import com.quick.bean.QuickLearn;
import com.quick.bean.Userprofile;
import com.quick.entity.Std;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.quick.data.MasterDataProvider;
import com.quick.table.QuickUploadTable;
import com.quick.utilities.ConfirmationDialogueBox;
import com.quick.utilities.DateUtil;
import com.quick.utilities.UIUtils;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;
import java.io.File;
import java.util.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;




/**
 *
 * @author sonalis
 */
public class NewEditTopicDetailsAdmin extends Window implements Button.ClickListener{
    
    
    private VerticalLayout baseLayout;
    private TabSheet tabsheet;
    private MasteParmBean quickLearnPojo;
    private String strUserNotes;
    private TextArea userNotesTxtArea;
    private int selectedUploadId;
    private QuickUpload quickupload;
    
    public NewEditTopicDetailsAdmin()
    {
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("100%"); 
        setImmediate(true);
        
        setStandardList(MasterDataProvider.getStandardList());        
        setUploadedList(MasterDataProvider.getQuickLearnUploadList());
        
        buildBaseLayout();
        addTopicDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
        
        
        
    }
    
    
    public NewEditTopicDetailsAdmin(MasteParmBean learnRow, int selectedUploadId, QuickUpload quickupload){
        this.quickupload=quickupload;
        this.selectedUploadId=selectedUploadId;
        this.quickLearnPojo=learnRow;
        //upload id is set to master param - further used for deletion
        quickLearnPojo.setUploadId(selectedUploadId);
        this.strUserNotes=strUserNotes;
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("100%"); 
        setImmediate(true);
        
        
        buildBaseLayout();
        addTopicDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
    }
    
   /*  public ViewTopicDetailsWindow(StudentView studentView, List<Userprofile> studentList) {
            
        setModal(true);       
        setCaption("Welcome to Edit Student");
        setContent(baseLayout);
        center();        
        setClosable(false);
        setWidth("70%");
        setHeight("100%");
        buildBaseStudentLayout();
    } */
    
     private void buildBaseLayout(){
              
       baseLayout = new VerticalLayout();
       baseLayout.setImmediate(true);

       baseLayout.setSpacing(true);   
       baseLayout.setMargin(true);
//       baseLayout.setWidth("100%");
//       baseLayout.setHeight("100%");
    }

    @Override
    public void buttonClick(ClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addTopicDetails() 
    {
        /* tabsheet = new TabSheet();
        tabsheet.setSizeFull();
        
        tabsheet.addTab(getVideoPathLayout(),"Video");
        tabsheet.addTab(getNotesLayout(),"Notes");
        tabsheet.addTab(getOtherNotesLayout(),"Other references");
        tabsheet.addTab(getPreviousQuestionsLayout(),"Previous questions");
        tabsheet.addTab(getQuizLayout(),"Quiz"); */
        
        //CssLayout tabsheetLayout = UIUtils.createPanel(tabsheet);
        baseLayout.addComponent(getVideoPathLayout());
        baseLayout.addComponent(getNotesLayout());
        baseLayout.addComponent(getOtherNotesLayout());
        baseLayout.addComponent(getPreviousQuestionsLayout());
        baseLayout.addComponent(getQuizLayout());
        baseLayout.addComponent(getDeleteButtonLayout());
    }
    
    private List<Std> standardList;
    private List<MasteParmBean> uploadedList;
    private List<QuickLearn> subjectList;
    
    public List<Std> getStandardList() {
        return standardList;
    }

    public void setStandardList(List<Std> standardList) {
        this.standardList = standardList;
    }
    
    public List<QuickLearn> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<QuickLearn> subjectList) {
        this.subjectList = subjectList;
    }
    
    public List<MasteParmBean> getUploadedList() {
        return uploadedList;
    }

    public void setUploadedList(List<MasteParmBean> uploadedList) {
        this.uploadedList = uploadedList;
    }
    
    private Component getVideoPathLayout() 
    {
        final ComboBox subjecttxt = new ComboBox();
        subjecttxt.setImmediate(true);
        subjecttxt.setInputPrompt("Subject");
        subjecttxt.setNullSelectionAllowed(false);
        subjecttxt.setWidth("185px");
        
        
        final ComboBox standardtxt = new ComboBox("Standard");
        standardtxt.setImmediate(true);
        standardtxt.setInputPrompt("Standard");
        standardtxt.addItem("Select");
        standardtxt.setValue("Select"); 
        standardtxt.setNullSelectionAllowed(false);
        standardtxt.setWidth("185px");
        
        
        Iterator it=getStandardList().iterator();
        while(it.hasNext()){
            Std s=(Std) it.next();
            standardtxt.addItem(s.getStd());            
        }
        standardtxt.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(!standardtxt.getValue().equals("Select")){
                    String std=String.valueOf(standardtxt.getValue());
                    setSubjectList(MasterDataProvider.getSubjectBystd(std));
                    if(!getSubjectList().isEmpty()){
                         Iterator subItr=getSubjectList().iterator();
                         while(subItr.hasNext()){
                         QuickLearn s=(QuickLearn) subItr.next();
                         subjecttxt.addItem(s.getSub());            
                       }  
                    }     
               }
            }
        });
        
        TextField topictxt=new TextField();
        topictxt.setInputPrompt("Topic name");   
        
        TextField topicTagstxt=new TextField("TAGS");
        topicTagstxt.setInputPrompt("TOPIC TAGS");
        topicTagstxt.setWidth("90%");
        
        TextArea txtTopicIntro=new TextArea();
        txtTopicIntro.setInputPrompt("About topic");
        txtTopicIntro.setRows(4);
        txtTopicIntro.setWidth("90%");
//        topicTagstxt.setHeight("50px");      
        
        
        VerticalLayout topicInfoLayout = new VerticalLayout();
        topicInfoLayout.setSpacing(true);
        topicInfoLayout.setMargin(true);
        topicInfoLayout.addStyleName("fourSideBorder");
        
        topicInfoLayout.addComponent(topictxt);
        topicInfoLayout.addComponent(subjecttxt);
        topicInfoLayout.addComponent(standardtxt);
        topicInfoLayout.addComponent(txtTopicIntro);
        topicInfoLayout.addComponent(topicTagstxt);
        
        
           VerticalLayout videoInfoLayout = new VerticalLayout();
           //layout.setSpacing(true);
           videoInfoLayout.setImmediate(true);
          // videoInfoLayout.setSizeFull();
           videoInfoLayout.setHeight("100%");
           videoInfoLayout.setMargin(new MarginInfo(true, true, false, true));
           videoInfoLayout.addStyleName("fourSideBorder");
           
           TextField videoInputPath=new TextField();
            videoInputPath.setInputPrompt("Please enter video path on the SERVER");
            videoInputPath.setWidth("90%");
            
           Label lableVideo;
           // not video available, accept path from user
           lableVideo = new Label("<b><h3>Enter video path</h3></b>", ContentMode.HTML);
           lableVideo.setImmediate(true);
           //txtVideoPath.setInputPrompt("Enter server video path");
           lableVideo.setCaption("About Video");
           lableVideo.setWidth("90%");
           //txtVideoPath.addValueChangeListener(this);
           videoInfoLayout.addComponent(lableVideo);
           //videoInfoLayout.setComponentAlignment(lableVideo, Alignment.MIDDLE_CENTER);
           
           videoInfoLayout.addComponent(videoInputPath);
           //videoInfoLayout.setComponentAlignment(videoInputPath, Alignment.MIDDLE_CENTER);
         
         
      
       
       HorizontalLayout h = new HorizontalLayout();
       h.setSizeFull();
       h.addStyleName("bottomBorder");
       h.addComponent(topicInfoLayout);
       h.addComponent(videoInfoLayout);       
       
       return h;       
    }
    
    private VerticalLayout getNotesLayout() 
    {
        
        Label topicNotes=new Label("<b><h4>"+"TOPIC NOTES"+"</h4></b>", ContentMode.HTML);
        topicNotes.setImmediate(true);
        
        
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(topicNotes);
        
        TextArea notesTextArea;
        notesTextArea = new TextArea();
        
        notesTextArea.setSizeFull();
//        if(this.quickLearnPojo.getLectureNotes()!=null)
//        {
//            notesTextArea.setValue(this.quickLearnPojo.getLectureNotes());
//        }
        
        notesTextArea.setImmediate(true);
        
        //notesTextArea.setReadOnly(true);
        
        layout.addComponent(notesTextArea);
        
        //notesTextArea.addValueChangeListener(this);
        //layout.setExpandRatio(notesTextArea, 2);
        
        return layout;
        
    }
    
    private VerticalLayout getOtherNotesLayout() 
    {
        
        Label otherRef=new Label("<b><h4>"+"OTHER REFERENCES"+"</h4></b>", ContentMode.HTML);
        otherRef.setImmediate(true);
        
        
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(otherRef);
        
        TextArea otherNotesTextArea = new TextArea();
        
        otherNotesTextArea.setSizeFull();
//        if(this.quickLearnPojo.getOtherNotes()!=null)
//        {
//            otherNotesTextArea.setValue(this.quickLearnPojo.getOtherNotes());
//        }
        
        otherNotesTextArea.setImmediate(true);
        
        layout.addComponent(otherNotesTextArea);
        layout.setExpandRatio(otherNotesTextArea, 2);
        
        return layout;
        
    }
    
    
    private VerticalLayout getPreviousQuestionsLayout() 
    {
        
        Label previousQuestions=new Label("<b><h4>"+"PREVIOUS QUESTIONS"+"</h4></b>", ContentMode.HTML);
        previousQuestions.setImmediate(true);
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(previousQuestions);
        
        TextArea previousQuestionsTextArea;
        previousQuestionsTextArea = new TextArea();
        
        previousQuestionsTextArea.setSizeFull();
//        if(this.quickLearnPojo.getPreviousQuestion()!=null)
//        {
//            previousQuestionsTextArea.setValue(this.quickLearnPojo.getPreviousQuestion());
//        }
        
        previousQuestionsTextArea.setImmediate(true);
        
        
        layout.addComponent(previousQuestionsTextArea);
        layout.setExpandRatio(previousQuestionsTextArea, 2); 
        
        return layout;
        
    }
    
    private VerticalLayout getQuizLayout() 
    {
        Label topicQuiz=new Label("<b><h4>"+"TOPIC QUIZ"+"</h4></b>", ContentMode.HTML);
        topicQuiz.setImmediate(true);
        
        
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(topicQuiz);
        
        TextArea quizTextArea;
        quizTextArea = new TextArea();
        
        quizTextArea.setSizeFull();
//        if(this.quickLearnPojo.getQuiz()!=null)
//        {
//            quizTextArea.setValue(this.quickLearnPojo.getQuiz());
//        }
        
        quizTextArea.setImmediate(true);
        
        
        layout.addComponent(quizTextArea);
        layout.setExpandRatio(quizTextArea, 2); 
        
        return layout;
        
    }
    
    private HorizontalLayout getDeleteButtonLayout() 
    {
               
        Button deleteTopic = new Button();
        deleteTopic.setImmediate(true);
        deleteTopic.setCaption("Delete topic");
        deleteTopic.addStyleName("default");
        deleteTopic.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                deleteThisTopic();
                
            }
        });

        
        HorizontalLayout layout= new HorizontalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        
        layout.addComponent(deleteTopic);
        
        /* TextArea quizTextArea;
        quizTextArea = new TextArea();
        
        quizTextArea.setSizeFull();
        if(this.quickLearnPojo.getQuiz()!=null)
        {
            quizTextArea.setValue(this.quickLearnPojo.getQuiz());
        }
        
        quizTextArea.setImmediate(true);
        quizTextArea.setReadOnly(true);
        
        layout.addComponent(quizTextArea);
        layout.setExpandRatio(quizTextArea, 2); */
        
        return layout;
        
    }

   /*  private CssLayout addUserNotes() 
    {
        userNotesTxtArea = new TextArea("My short notes for the topic");
        userNotesTxtArea.setSizeFull();
        userNotesTxtArea.setInputPrompt("My short notes");
        userNotesTxtArea.setImmediate(true);
        if(this.strUserNotes!=null)
        {
            userNotesTxtArea.setValue(strUserNotes);            
        }
        
        userNotesTxtArea.addBlurListener(new FieldEvents.BlurListener() {

            @Override
            public void blur(BlurEvent event) {
                setUserNotes(userNotesTxtArea.getValue());
                if(selectedUploadId!=0)
                {
                    updateUserShortNotes();
                }
            }
        });
        
        
        CssLayout panel = UIUtils.createPanel(userNotesTxtArea);
        panel.addStyleName("notes");
//        baseLayout.addComponent(panel);
//        baseLayout.setExpandRatio(panel,1);
        
        return panel;
    } */

    /**
     * @return the userNotes
     */
    public String getUserNotes() {
        return strUserNotes;
    }

    /**
     * @param userNotes the userNotes to set
     */
    public void setUserNotes(String userNotes) {
        this.strUserNotes = userNotes;
    }
    
    private void updateUserShortNotes(){
       try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.UPDATE_USER_NOTES_FOR_TOPIC_URL));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("uploadId", selectedUploadId);
                inputJson.put("userNotes", getUserNotes());
                Userprofile loggedinProfile= (Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
                inputJson.put("userName", loggedinProfile.getUsername());
                
              
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);

          
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
            //Notification.show(outNObject.getString(GlobalConstants.STATUS), Notification.Type.WARNING_MESSAGE);
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        
    }
    
    private void deleteThisTopic()
    {
        UI.getCurrent().addWindow(new ConfirmationDialogueBox("Confirmation", "Are you sure you want to remove this topic ?", new ConfirmationDialogueBox.Callback() {

            @Override
            public void onDialogResult(boolean flag) {
                if (flag) {
                    quickupload.deleteTopicInformationFromDB(quickLearnPojo);
                    getUI().getCurrent().getNavigator().navigateTo("/topics");
                    NewEditTopicDetailsAdmin.this.close();
                }
            }
        }));
    }
}
