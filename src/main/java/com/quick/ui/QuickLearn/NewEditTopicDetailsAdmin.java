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
import com.quick.utilities.ConfirmationDialogueBox;
import com.quick.utilities.ImageResizer;
import com.quick.utilities.UIUtils;
import com.quick.utilities.UploadReceiver;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Userprofile loggedInProfile;
    private static final String Select = "Select";
    private int newlyCreatedUploadId;
    private static final String saveTopic="Save topic";
    
    public NewEditTopicDetailsAdmin(Userprofile loggedInProfile)
    {
        this.loggedInProfile=loggedInProfile;
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("100%"); 
        setImmediate(true);
        
        setStandardList(MasterDataProvider.getStandardList());        
        setUploadedList(MasterDataProvider.getQuickLearnUploadList(null,0));
        
        buildBaseLayout();
        addTopicDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
    }
    
    
    public NewEditTopicDetailsAdmin(MasteParmBean learnRow, int selectedUploadId, QuickUpload quickupload, Userprofile loggedInProfile){
        this.loggedInProfile=loggedInProfile;
        this.quickupload=quickupload;
        this.selectedUploadId=selectedUploadId;
        this.quickLearnPojo=learnRow;
        //upload id is set to master param - further used for deletion
        quickLearnPojo.setUploadId(selectedUploadId);
        //this.strUserNotes=strUserNotes;
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("100%"); 
        setImmediate(true);
        
        setStandardList(MasterDataProvider.getStandardList());        
        setUploadedList(MasterDataProvider.getQuickLearnUploadList(null,0));
        
        buildBaseLayout();
        addTopicDetails();
        //addUserNotes();
        setFormData();
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
        baseLayout.addComponent(UIUtils.getSchoolBannerLayout());
        baseLayout.addComponent(getVideoPathLayout());
        baseLayout.addComponent(getNotesLayout());
        baseLayout.addComponent(getOtherNotesLayout());
        baseLayout.addComponent(getPreviousQuestionsLayout());
        baseLayout.addComponent(getQuizLayout());
        baseLayout.addComponent(getAddButtonLayout());
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
    
    
    private ComboBox subjecttxt;
    private ComboBox standardtxt;
    private TextField topictxt;
    private TextField topicTagstxt;
    private TextArea txtTopicIntro;
    private TextField videoInputPath;
    private byte[] topicImageArray;
    private String topicFileName;
    
    private Component getVideoPathLayout() 
    {
        subjecttxt = new ComboBox("Subject");
        subjecttxt.setImmediate(true);
        subjecttxt.setInputPrompt("Subject");
        subjecttxt.setNullSelectionAllowed(false);
        subjecttxt.setWidth("185px");
        
        
        standardtxt = new ComboBox("Standard");
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
        
        topictxt=new TextField("Topic name");
        topictxt.setInputPrompt("Name");   
        
        topicTagstxt=new TextField("TAGS");
        topicTagstxt.setInputPrompt("TOPIC TAGS");
        topicTagstxt.setWidth("90%");
        
        txtTopicIntro=new TextArea("Topic intro");
        txtTopicIntro.setInputPrompt("About topic");
        txtTopicIntro.setRows(4);
        txtTopicIntro.setWidth("90%");
//        topicTagstxt.setHeight("50px");      
        
        
        VerticalLayout topicInfoLayout = new VerticalLayout();
        topicInfoLayout.setSpacing(true);
        topicInfoLayout.setMargin(true);
        topicInfoLayout.addStyleName("fourSideBorder");
        //topicInfoLayout.addStyleName("bottomBorder");
        
        Label lableVideo;
        lableVideo = new Label("<b>Enter video path</b>", ContentMode.HTML);
        lableVideo.setImmediate(true);
        //lableVideo.setCaption("About Video");
        lableVideo.setWidth("90%");
        
        videoInputPath = new TextField();
        videoInputPath.setInputPrompt("Please enter youtube url of the video");
        videoInputPath.setWidth("90%");
        
        final UploadReceiver uploadReceiver = new UploadReceiver();
        final Upload upload;
        
        
        upload = new Upload(null, uploadReceiver);
        upload.setImmediate(true);
        upload.setButtonCaption("Image");
        upload.addStyleName("notifications");
        upload.addSucceededListener(new Upload.SucceededListener() {

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                topicFileName=uploadReceiver.getFileName();
                File topicPicture=uploadReceiver.getFile();
                topicImageArray= ImageResizer.resize(topicPicture,topicFileName);
            }
        });
        final Button cancelProcessing = new Button("Cancel");
        cancelProcessing.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setStyleName("small");
        
        topicInfoLayout.addComponent(topictxt);
        topicInfoLayout.addComponent(standardtxt);
        topicInfoLayout.addComponent(subjecttxt);
        topicInfoLayout.addComponent(lableVideo);
        topicInfoLayout.addComponent(videoInputPath);
        topicInfoLayout.addComponent(upload);
        topicInfoLayout.addComponent(txtTopicIntro);
        topicInfoLayout.addComponent(topicTagstxt);
        
        
//           VerticalLayout videoInfoLayout = new VerticalLayout();
//           videoInfoLayout.setImmediate(true);
//           videoInfoLayout.setSizeFull();
//           videoInfoLayout.setMargin(new MarginInfo(true, true, false, true));
//           videoInfoLayout.addStyleName("fourSideBorder");
//           //videoInfoLayout.addStyleName("heightFix");
//           videoInfoLayout.addComponent(lableVideo);
//           
//           videoInfoLayout.addComponent(videoInputPath);
//         
//       HorizontalLayout h = new HorizontalLayout();
//       h.setSizeFull();
//       //h.addStyleName("bottomBorder");
//       h.addComponent(topicInfoLayout);
//       h.setExpandRatio(topicInfoLayout, 1);
//       
//       h.addComponent(videoInfoLayout);       
//       h.setExpandRatio(videoInfoLayout, 1);
       
       return topicInfoLayout;       
    }
    
    private TextArea notesTextArea;
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
        
        
        notesTextArea = new TextArea();
        notesTextArea.setImmediate(true);
        notesTextArea.setSizeFull();
        notesTextArea.setRows(15);
//        if(this.quickLearnPojo.getLectureNotes()!=null)
//        {
//            notesTextArea.setValue(this.quickLearnPojo.getLectureNotes());
//        }
        
        
        
        //notesTextArea.setReadOnly(true);
        
        layout.addComponent(notesTextArea);
        
        //notesTextArea.addValueChangeListener(this);
        //layout.setExpandRatio(notesTextArea, 2);
        
        return layout;
        
    }
    
    private TextArea otherNotesTextArea;
    
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
        
        otherNotesTextArea = new TextArea();
        otherNotesTextArea.setImmediate(true);
        otherNotesTextArea.setSizeFull();
        //otherNotesTextArea.setRows(15);
//        if(this.quickLearnPojo.getOtherNotes()!=null)
//        {
//            otherNotesTextArea.setValue(this.quickLearnPojo.getOtherNotes());
//        }
        
        
        
        layout.addComponent(otherNotesTextArea);
        layout.setExpandRatio(otherNotesTextArea, 2);
        
        return layout;
        
    }
    
    private TextArea previousQuestionsTextArea;
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
        
        
        previousQuestionsTextArea = new TextArea();
        previousQuestionsTextArea.setImmediate(true);
        previousQuestionsTextArea.setSizeFull();
        previousQuestionsTextArea.setRows(10);
//        if(this.quickLearnPojo.getPreviousQuestion()!=null)
//        {
//            previousQuestionsTextArea.setValue(this.quickLearnPojo.getPreviousQuestion());
//        }
        
        
        
        
        layout.addComponent(previousQuestionsTextArea);
        layout.setExpandRatio(previousQuestionsTextArea, 2); 
        
        return layout;
        
    }
    
    private TextArea quizTextArea;
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
    
    private Button addOrEditTopic;
    private HorizontalLayout getAddButtonLayout() 
    {
               
        addOrEditTopic = new Button();        
        addOrEditTopic.setImmediate(true);
        addOrEditTopic.setCaption("Add topic");
        addOrEditTopic.addStyleName("default");
        addOrEditTopic.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    validateAndSaveQuickUploadDetails();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                
            }

            
        });

        
        HorizontalLayout layout= new HorizontalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        
        layout.addComponent(addOrEditTopic);
        
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
                    getUI().getCurrent().getNavigator().navigateTo(GlobalConstants.ROUT_TOPICS);
                    NewEditTopicDetailsAdmin.this.close();
                }
            }
        }));
    }
    
    private void validateAndSaveQuickUploadDetails() throws JSONException 
    {
        //to bypass validations on edit
        if (addOrEditTopic.getCaption().equals(saveTopic)) 
        {
            topicFileName=GlobalConstants.emptyString;            
        }
        
        //validations
        if(loggedInProfile.getName()==null || loggedInProfile.getName().trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("User not properly logged in.", Notification.Type.WARNING_MESSAGE);
        }
        else if(standardtxt.getValue()==null || ((String)standardtxt.getValue()).trim().equals(GlobalConstants.emptyString) || ((String)standardtxt.getValue()).trim().equalsIgnoreCase(Select))
        {
            Notification.show("Please enter standard.", Notification.Type.WARNING_MESSAGE);
        }
        else if(subjecttxt.getValue()==null || ((String)subjecttxt.getValue()).trim().equals(GlobalConstants.emptyString)|| ((String)subjecttxt.getValue()).trim().equalsIgnoreCase(Select))
        {
            Notification.show("Please enter subject.", Notification.Type.WARNING_MESSAGE);
        }
        else if(topictxt.getValue()==null || ((String)topictxt.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter topic.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)topictxt.getValue()).trim().length()>30)
        {
            Notification.show("Topic name cannot be more than 30 characters.", Notification.Type.WARNING_MESSAGE);
        }
        else if(videoInputPath.getValue()==null || ((String)videoInputPath.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter video path.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)videoInputPath.getValue()).trim().length()>200)
        {
            Notification.show("Video path cannot be more than 200 characters.", Notification.Type.WARNING_MESSAGE);
        }
        else if(topicFileName == null)
        {
            Notification.show("Please upload image for the topic.", Notification.Type.WARNING_MESSAGE);
        }
        else if(txtTopicIntro.getValue()==null || ((String)txtTopicIntro.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter topic introduction.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)txtTopicIntro.getValue()).trim().length()>300)
        {
            Notification.show("Topic information cannot be more than 300 characters.", Notification.Type.WARNING_MESSAGE);
        }
        else if(topicTagstxt.getValue()==null || ((String)topicTagstxt.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter tags for topic.", Notification.Type.WARNING_MESSAGE);
        }
        
        else if(notesTextArea.getValue()==null || ((String)notesTextArea.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter topic notes.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)notesTextArea.getValue()).trim().length()>10000)
        {
            Notification.show("Topic notes cannot be more than 10000 characters.", Notification.Type.WARNING_MESSAGE);
        }
        
        
        
        else if(otherNotesTextArea.getValue()==null || ((String)otherNotesTextArea.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter other references.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)otherNotesTextArea.getValue()).trim().length()>5000)
        {
            Notification.show("Other references cannot be more than 5000 characters.", Notification.Type.WARNING_MESSAGE);
        }
        
         else if(previousQuestionsTextArea.getValue()==null || ((String)previousQuestionsTextArea.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter previous questions.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)previousQuestionsTextArea.getValue()).trim().length()>5000)
        {
            Notification.show("Previous questions cannot be more than 5000 characters.", Notification.Type.WARNING_MESSAGE);
        }
        
        else if(quizTextArea.getValue()==null || ((String)quizTextArea.getValue()).trim().equals(GlobalConstants.emptyString))
        {
            Notification.show("Please enter quiz.", Notification.Type.WARNING_MESSAGE);
        }
        else if(((String)quizTextArea.getValue()).trim().length()>1000)
        {
            Notification.show("Quiz cannot be more than 1000 characters.", Notification.Type.WARNING_MESSAGE);
        }
        
        
        else
        {
            
            
            //executions - inserting in db
            JSONObject inputJson = new JSONObject();
            try 
            {
                String uploadedBy = loggedInProfile.getName();
                inputJson.put("uploadedBy", uploadedBy);
                inputJson.put("std", standardtxt.getValue());
                inputJson.put("sub", subjecttxt.getValue());
                inputJson.put("topic", topictxt.getValue());
                inputJson.put("tags", topicTagstxt.getValue());
                
                if (videoInputPath != null)
                {
                    //video player doesnt exists, need to update video path
                    inputJson.put("video_path", videoInputPath.getValue());
                }
                else
                {
                    //video player exists, no need to update video path
                    //String dummyEmptyString=GlobalConstants.emptyString;
                    //inputJson.put("video_path", getQuikLearnMasterParamDetails().getVideoPath());
                }
                

                if (!notesTextArea.getValue().equals(GlobalConstants.emptyString))
                {
                    inputJson.put("notes", notesTextArea.getValue());
                } else {
                    inputJson.put("notes", "no data");
                }

                if (!otherNotesTextArea.getValue().equals(GlobalConstants.emptyString)) {
                    inputJson.put("othernotes", otherNotesTextArea.getValue());
                } else {
                    inputJson.put("othernotes", "no data");
                }

                if (!previousQuestionsTextArea.getValue().equals(GlobalConstants.emptyString)) {
                    inputJson.put("pq", previousQuestionsTextArea.getValue());
                } else {
                    inputJson.put("pq", "no data");
                }
                
                if (!quizTextArea.getValue().equals(GlobalConstants.emptyString)) {
                    inputJson.put("quiz", quizTextArea.getValue());
                } else {
                    inputJson.put("quiz", "no data");
                }
                
                if (!txtTopicIntro.getValue().equals(GlobalConstants.emptyString)) {
                    inputJson.put("topicIntro", txtTopicIntro.getValue());
                } else {
                    inputJson.put("topicIntro", "no data");
                }
                
                if(addOrEditTopic.getCaption().equals("Add topic"))
                {
                    inputJson.put("isNewQuickUpload", true);
                    inputJson.put("uploadId", "null");
                }
                else if(addOrEditTopic.getCaption().equals(saveTopic))
                {
                    inputJson.put("isNewQuickUpload", false);
                    inputJson.put("uploadId", quickLearnPojo.getUploadId());
                }
                
                ViewTopicDetailsWindow w = new ViewTopicDetailsWindow();
                inputJson.put("classToInvoke",w.getClass().getName());
                
//                if (isNewQuickUpload) {
//                    inputJson.put("uploadId", "null");
//                } else {
//                    inputJson.put("uploadId", uploadId);
//                }

            } catch (JSONException ex) 
            {
                ex.printStackTrace();
                throw ex;
            }


            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_UPLOAD_DETAILS_URL));
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);

            /*
             * if (response.getStatus() != 201) { throw new RuntimeException("Failed
             * : HTTP error code : " + response.getStatus()); }
             */

            String output = response.getEntity(String.class);
            System.out.println("output=" + output);
            
            JSONObject outputJson = new JSONObject(output);
            if(Integer.parseInt(outputJson.getString(GlobalConstants.STATUS)) == GlobalConstants.YES)
            {
                Notification.show("Saved successfully", Notification.Type.WARNING_MESSAGE);
                //save resized image only in case of new upload
                if(!topicFileName.equals(GlobalConstants.emptyString))
                {
                    saveResizedTopicImageToFileSystem();
                }
                if(outputJson.has("newlyCreatedUploadId"))
                {
                    newlyCreatedUploadId=outputJson.getInt("newlyCreatedUploadId");
                }
                getUI().getCurrent().getNavigator().navigateTo(GlobalConstants.ROUT_TOPICS);
                this.close();
            }
            else
            {
                Notification.show("Saving failed", Notification.Type.WARNING_MESSAGE);
            }
    }
}
    
    private void saveResizedTopicImageToFileSystem() 
            {
                FileOutputStream fileOuputStream = null;
                try 
                {
                    String ext=topicFileName.substring(topicFileName.indexOf(GlobalConstants.FULL_STOP));
                    
                    /* fileOuputStream = new FileOutputStream(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)
                            +standardtxt.getValue()+GlobalConstants.HYPHEN
                            +subjecttxt.getValue() +GlobalConstants.HYPHEN
                            +topictxt.getValue()+GlobalConstants.HYPHEN
                            +ext); */
                    fileOuputStream = new FileOutputStream(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)
                    +newlyCreatedUploadId + ext);
                    
                    fileOuputStream.write(topicImageArray);
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

    
    private void setFormData() {
        standardtxt.setValue(quickLearnPojo.getStd());
        subjecttxt.setValue(quickLearnPojo.getSub());
        
        topictxt.setValue(quickLearnPojo.getTopic());
        txtTopicIntro.setValue(quickLearnPojo.getLectureNotesInformation());
        topicTagstxt.setValue(quickLearnPojo.getTopicTags());
        videoInputPath.setValue(quickLearnPojo.getVideoPath());
        
//        private byte[] topicImageArray;
//        private String topicFileName;
    
        notesTextArea.setValue(quickLearnPojo.getLectureNotes());
        otherNotesTextArea.setValue(quickLearnPojo.getOtherNotes());
        previousQuestionsTextArea.setValue(quickLearnPojo.getPreviousQuestion());
        quizTextArea.setValue(quickLearnPojo.getQuiz());
        addOrEditTopic.setCaption(saveTopic);
        
    }
    
        private void editThisTopic()
    {
        UI.getCurrent().addWindow(new ConfirmationDialogueBox("Confirmation", "Are you sure you want to edit this topic ?", new ConfirmationDialogueBox.Callback() {

            @Override
            public void onDialogResult(boolean flag) {
                if (flag) {
                    quickupload.deleteTopicInformationFromDB(quickLearnPojo);
                    getUI().getCurrent().getNavigator().navigateTo(GlobalConstants.ROUT_TOPICS);
                    //ViewTopicDetailsForAdmin.this.close();
                }
            }
        }));
    }
}