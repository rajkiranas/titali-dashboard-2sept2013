/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard;

import com.quick.bean.QuickLearn;
import com.quick.bean.Userprofile;
import com.quick.entity.Std;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.quick.data.MasterDataProvider;
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
public class ViewTopicDetailsWindow extends Window implements Button.ClickListener{
    
    
    private VerticalLayout baseLayout;
    private TabSheet tabsheet;
    private QuickLearn quickLearnPojo;
    private String strUserNotes;
    private TextArea userNotesTxtArea;
    private int selectedUploadId;
    
    
    
    public ViewTopicDetailsWindow(QuickLearn learnRow, String strUserNotes, int selectedUploadId){
        this.selectedUploadId=selectedUploadId;
        this.quickLearnPojo=learnRow;
        this.strUserNotes=strUserNotes;
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("100%"); 
        setImmediate(true);
        
        
        buildBaseStudentLayout();
        addTopicDetails();
        addUserNotes();
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
    
     private void buildBaseStudentLayout(){
              
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
        baseLayout.addComponent(addUserNotes());
        //baseLayout.setExpandRatio(tabsheetLayout,2);
    }
    
    private Component getVideoPathLayout() 
    {
        Label topicName=new Label("<b><h1>"+quickLearnPojo.getTopic()+"</h1></b>", ContentMode.HTML);
        topicName.setImmediate(true);
        
        Label instructorName=new Label("<b><h4><b>INSTRUCTORS: </b>"+quickLearnPojo.getOtherNotesInformation()+"</h4></b>", ContentMode.HTML);
        instructorName.setImmediate(true);
        
        Label recommendedFor=new Label("<b><h4><b>RECOMMENDED FOR: </b>"+quickLearnPojo.getStd()+"</h4></b>", ContentMode.HTML);
        recommendedFor.setImmediate(true);
        
        Label topicIntro = new Label();
        topicIntro.setImmediate(true);
        topicIntro.setValue(quickLearnPojo.getLectureNotesInformation());
        
        VerticalLayout topicInfoLayout = new VerticalLayout();
        topicInfoLayout.setSpacing(true);
        topicInfoLayout.setMargin(true);
        
        topicInfoLayout.addComponent(topicName);
        topicInfoLayout.addComponent(instructorName);
        topicInfoLayout.addComponent(recommendedFor);
        topicInfoLayout.addComponent(topicIntro);
        
        
           VerticalLayout videoInfoLayout = new VerticalLayout();
           //layout.setSpacing(true);
           videoInfoLayout.setWidth("100%");
           videoInfoLayout.setHeight("100%");
           videoInfoLayout.setMargin(new MarginInfo(true, true, false, true));
           
           

       
       if(this.quickLearnPojo.getVideoPath()!=null)
       {
           
           Video vPlayer;
           // video is available, show it on video player
           vPlayer = new Video();
           vPlayer.setImmediate(true);
           vPlayer.setWidth("100%");
           vPlayer.setHeight("100%");
           vPlayer.setPoster(new FileResource(new File("/home/rajkirans/NetBeansProjects/projectFromSept29/dash/trunk/src/main/webapp/VAADIN/themes/dashboard/img/learnMore.png")));
           
           vPlayer.addSource(new FileResource(new File(this.quickLearnPojo.getVideoPath())));
           vPlayer.setShowControls(true);
           vPlayer.setMuted(false);
           
           //return vPlayer;
           videoInfoLayout.addComponent(vPlayer);
           videoInfoLayout.setComponentAlignment(vPlayer, Alignment.MIDDLE_CENTER);
         
           
//           HorizontalLayout h = new HorizontalLayout();
//           h.setWidth("100%");
//           Button play = new Button("Play");
//           play.setImmediate(true);
//           play.setWidth("100%");
//           play.addListener(new Button.ClickListener() {
//
//               @Override
//               public void buttonClick(ClickEvent event) {
//                   vPlayer.play();
//               }
//           });
//           
//           Button stop = new Button("Stop");
//           stop.setImmediate(true);
//           stop.setWidth("100%");
//           stop.addListener(new Button.ClickListener() {
//
//               @Override
//               public void buttonClick(ClickEvent event) {
//                   vPlayer.pause();
//               }
//           });
//           
//           h.addComponent(play);
//           h.addComponent(stop);
           
           //layout.addComponent(vPlayer);
           //layout.setExpandRatio(vPlayer, 2.5f);
//           layout.addComponent(h);
//           layout.setExpandRatio(h, 0.5f);
           
       }
       else
       {
           Label lableVideo;
           // not video available, accept path from user
           lableVideo = new Label("<b><h3>No video available for this topic.</h3></b>", ContentMode.HTML);
           lableVideo.setImmediate(true);
           //txtVideoPath.setInputPrompt("Enter server video path");
           lableVideo.setCaption("About Video");
           lableVideo.setWidth("90%");
           //txtVideoPath.addValueChangeListener(this);
           videoInfoLayout.addComponent(lableVideo);
           videoInfoLayout.setComponentAlignment(lableVideo, Alignment.MIDDLE_CENTER);
         
         
       }
       
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
        
        Label strNotes = new Label();
        strNotes.setImmediate(true);
        strNotes.setValue(quickLearnPojo.getLectureNotes());
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(topicNotes);
        layout.addComponent(strNotes);
        
        /* TextArea notesTextArea;
        notesTextArea = new TextArea();
        
        notesTextArea.setSizeFull();
        if(this.quickLearnPojo.getLectureNotes()!=null)
        {
            notesTextArea.setValue(this.quickLearnPojo.getLectureNotes());
        }
        
        notesTextArea.setImmediate(true);
        
        notesTextArea.setReadOnly(true);
        
        layout.addComponent(notesTextArea); */
        
        //notesTextArea.addValueChangeListener(this);
        //layout.setExpandRatio(notesTextArea, 2);
        
        return layout;
        
    }
    
    private VerticalLayout getOtherNotesLayout() 
    {
        
        Label otherRef=new Label("<b><h4>"+"OTHER REFERENCES"+"</h4></b>", ContentMode.HTML);
        otherRef.setImmediate(true);
        
        Label strOtherNotes = new Label();
        strOtherNotes.setImmediate(true);
        strOtherNotes.setValue(quickLearnPojo.getOtherNotes());
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(otherRef);
        layout.addComponent(strOtherNotes);
        
        /* TextArea otherNotesTextArea = new TextArea();
        
        otherNotesTextArea.setSizeFull();
        if(this.quickLearnPojo.getOtherNotes()!=null)
        {
            otherNotesTextArea.setValue(this.quickLearnPojo.getOtherNotes());
        }
        
        otherNotesTextArea.setImmediate(true);
        otherNotesTextArea.setReadOnly(true);
        
        layout.addComponent(otherNotesTextArea);
        layout.setExpandRatio(otherNotesTextArea, 2); */
        
        return layout;
        
    }
    
    
    private VerticalLayout getPreviousQuestionsLayout() 
    {
        
        Label previousQuestions=new Label("<b><h4>"+"PREVIOUS QUESTIONS"+"</h4></b>", ContentMode.HTML);
        previousQuestions.setImmediate(true);
        
        Label strQuestions = new Label();
        strQuestions.setImmediate(true);
        strQuestions.setValue(quickLearnPojo.getPreviousQuestion());
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(previousQuestions);
        layout.addComponent(strQuestions);
        
        /* TextArea previousQuestionsTextArea;
        previousQuestionsTextArea = new TextArea();
        
        previousQuestionsTextArea.setSizeFull();
        if(this.quickLearnPojo.getPreviousQuestion()!=null)
        {
            previousQuestionsTextArea.setValue(this.quickLearnPojo.getPreviousQuestion());
        }
        
        previousQuestionsTextArea.setImmediate(true);
        previousQuestionsTextArea.setReadOnly(true);
        
        layout.addComponent(previousQuestionsTextArea);
        layout.setExpandRatio(previousQuestionsTextArea, 2); */
        
        return layout;
        
    }
    
    private VerticalLayout getQuizLayout() 
    {
        Label topicQuiz=new Label("<b><h4>"+"TOPIC QUIZ"+"</h4></b>", ContentMode.HTML);
        topicQuiz.setImmediate(true);
        
        Label strQuiz = new Label();
        strQuiz.setImmediate(true);
        strQuiz.setValue(quickLearnPojo.getQuiz());
        
        
        VerticalLayout layout= new VerticalLayout();
        //layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.addStyleName("bottomBorder");
        
        layout.addComponent(topicQuiz);
        layout.addComponent(strQuiz);
        
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

    private CssLayout addUserNotes() 
    {
        userNotesTxtArea = new TextArea("My short notes for the topic");
        userNotesTxtArea.setSizeFull();
        userNotesTxtArea.setInputPrompt("My short notes");
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
    }

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
    
}
