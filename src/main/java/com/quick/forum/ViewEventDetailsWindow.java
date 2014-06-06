/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.forum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.EventCommentsBean;
import com.quick.bean.EventLikeBean;
import com.quick.bean.ForumEventDetailsBean;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.quick.utilities.DateUtil;
import com.quick.utilities.MyImageSource;
import com.quick.utilities.VideoUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author sonalis
 */
public class ViewEventDetailsWindow extends Window implements Button.ClickListener {
    
    
    private HorizontalLayout baseLayout;
    private TabSheet tabsheet;    
    private TextArea userNotesTxtArea;
    private int selectedUploadId;
    private ForumEventDetailsBean eventDtls;
    private List<EventLikeBean> eventLikesList;
    private List<EventCommentsBean> eventCommentsList;
    private TextField txtNewComment;
    private String People_who_like_this="People who like this";
    private VerticalLayout verticalForCommentStack;
    private HorizontalLayout likeCommentBtnLayout;
    
    
    
    public ViewEventDetailsWindow(ForumEventDetailsBean eventDtls) {
        this.eventDtls=eventDtls;
        this.selectedUploadId=selectedUploadId;        
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("90%"); 
        setImmediate(true);
        
        buildBaseStudentLayout();
        addEventDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
    }
    
    public ViewEventDetailsWindow(){
    }
    
    //called from forum wrapper click
    public Window doConstructorsWorKForReflection(ForumEventDetailsBean eventDtls,List<EventLikeBean> eventLikesList, List<EventCommentsBean> eventCommentsList)
    {
        this.eventDtls=eventDtls;
        //this.selectedUploadId=selectedUploadId;
        this.eventCommentsList=eventCommentsList;
        this.eventLikesList=eventLikesList;
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("90%"); 
        setImmediate(true);
        
        buildBaseStudentLayout();
        addEventDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
        //addStyleName("blackBg");
        return this;
    }
    
    
    //called from dashborad activity feeds
    public Window getEventDetailsWindow(String eventId)
    {
        getEventDetailsById(eventId);        
       
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("90%");
        setHeight("90%"); 
        setImmediate(true);
        
        
        buildBaseStudentLayout();
        addEventDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
        //addStyleName("blackBg");
        return this;
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
              
       baseLayout = new HorizontalLayout();
       baseLayout.setImmediate(true);
       baseLayout.setSpacing(false);   
       baseLayout.setMargin(false);
       baseLayout.setWidth("100%");
       baseLayout.setHeight("100%");
    }

    @Override
    public void buttonClick(ClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addEventDetails() 
    {
        /* tabsheet = new TabSheet();
        tabsheet.setSizeFull();
        
        tabsheet.addTab(getVideoPathLayout(),"Video");
        tabsheet.addTab(getNotesLayout(),"Notes");
        tabsheet.addTab(getOtherNotesLayout(),"Other references");
        tabsheet.addTab(getPreviousQuestionsLayout(),"Previous questions");
        tabsheet.addTab(getQuizLayout(),"Quiz"); */
        
        //CssLayout tabsheetLayout = UIUtils.createPanel(tabsheet);
        if(eventDtls.getStringImage()!=null)
        {
            getEventImage();
        }
        else
        {
            getYoutubeVideo();
        }
        getEventDetails();
        showLikeAndCommentsForm();
        showFullCommentsStack();
        
        //baseLayout.setExpandRatio(tabsheetLayout,2);
    }

    private Component getEventImage() 
    {
        byte[] by = eventDtls.getStringImage().getBytes();
        StreamResource.StreamSource imagesource = new MyImageSource(Base64.decode(by));
        StreamResource resource = new StreamResource(imagesource, "myimage.png");
        resource.setCacheTime(0);
        //Image coverImage = new Image(GlobalConstants.emptyString, resource);
//        coverImage.setHeight("425px");
//        coverImage.setWidth("475px");
        /* BufferedImage originalImage=null;
        try {
            originalImage = ImageIO.read(resource.getStream().getStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        } */
        
        Embedded coverImage = new Embedded(null,resource);
        coverImage.setType(Embedded.TYPE_IMAGE);
        //coverImage.setSizeFull();
//        coverImage.setHeight("90%");
//        coverImage.setWidth("90%");
/*        coverImage.setWidth(String.valueOf(originalImage.getWidth()));
        coverImage.setHeight(String.valueOf(originalImage.getHeight())); */
        
        //coverImage.setSizeFull();
        VerticalLayout imageLayout = new VerticalLayout();
        imageLayout.setSizeFull();
        imageLayout.addComponent(coverImage);
        imageLayout.setComponentAlignment(coverImage,Alignment.MIDDLE_CENTER);
        //imageLayout.addStyleName("blackBg");
        
        baseLayout.addComponent(imageLayout);
        baseLayout.setExpandRatio(imageLayout,2.75f);
        baseLayout.setComponentAlignment(imageLayout,Alignment.MIDDLE_CENTER);
        return coverImage;
    }

    VerticalLayout v;
    private Component getEventDetails() 
    {
        v = new VerticalLayout();
        v.setImmediate(true);
        v.setSizeFull();
        v.setSpacing(true);
        //v.setMargin(new MarginInfo(true, false, true, false));
        v.setMargin(false);
        v.setHeight("100%");
        v.addStyleName("whiteBg");
        
        HorizontalLayout photoAndEventDtls = new HorizontalLayout();

        photoAndEventDtls.addStyleName("whiteBottomBorder");
        
        photoAndEventDtls.setMargin(false);
        photoAndEventDtls.setSpacing(false);
        photoAndEventDtls.setWidth("100%");
        photoAndEventDtls.setHeight("100%");
        Image userImage = new Image(null, new ThemeResource("img/profile-pic.png"));

        userImage.setWidth("30px");
        userImage.setWidth("30px");
        photoAndEventDtls.addComponent(userImage);
        photoAndEventDtls.setExpandRatio(userImage, 0.5f);
        photoAndEventDtls.setComponentAlignment(userImage, Alignment.MIDDLE_LEFT);
                                    
        String cap = "<div style='color:#3b5998;display:inline-block;'> <b>" + eventDtls.getEventOwner() + "</b></div>" + "<div style='color:grey;font-size:13px;display:inline-block;'>&nbsp; shared </div> <div style='color:#3b5998;display:inline-block;'>"
                + eventDtls.getEventDesc()+"</div><br><div style='color:grey;font-size:11px;display:inline-block;'>" +DateUtil.getTimeIntervalOfTheActivity(eventDtls.getEventDate()) + "</div>";
        Label eventIntro = new Label(cap, ContentMode.HTML);
        eventIntro.setWidth("100%");
        photoAndEventDtls.addComponent(eventIntro);
        photoAndEventDtls.setExpandRatio(eventIntro, 3);
        photoAndEventDtls.setComponentAlignment(eventIntro, Alignment.MIDDLE_LEFT);
            
        Label eventDesc = new Label(eventDtls.getEventBody());
        eventDesc.setWidth("100%");
        eventDesc.setHeight("100%");
        
        v.addComponent(photoAndEventDtls);
        //v.setExpandRatio(photoAndEventDtls, 1);
        v.addComponent(eventDesc);
        //v.setExpandRatio(eventDesc, 3);
        
        Table forumTable=new Table();
        forumTable.addContainerProperty(GlobalConstants.emptyString, VerticalLayout.class, null);
        forumTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
        forumTable.addStyleName("plain");
        forumTable.addStyleName("borderless");
        forumTable.setHeight("99%");
        forumTable.setWidth("100%");
        forumTable.setSortEnabled(false);
        forumTable.addItem(new Object[]{v},forumTable.size()+1);
        
        VerticalLayout tableVertical = new VerticalLayout();
        tableVertical.setSizeFull();
        tableVertical.addStyleName("whiteBg");
        tableVertical.addComponent(forumTable);
        
        baseLayout.addComponent(tableVertical);
        baseLayout.setExpandRatio(tableVertical,2.25f);
        return v;
    }
    
    private void showLikeAndCommentsForm() {
        likeCommentBtnLayout = new HorizontalLayout();
        Embedded likeImage ;
        Embedded commentImage;
        
        likeCommentBtnLayout.setSpacing(false);
        likeCommentBtnLayout.setWidth("50%");
        //likeCommentLayout.setMargin(true);
        //likeCommentBtnLayout.addStyleName("backgroundColor");

        //likeCommentBtnLayout.setWidth("60%");
        Button likeBtn = new Button("Like");
        ///Button commentBtn = new Button("Comment");

        likeBtn.addStyleName(BaseTheme.BUTTON_LINK);
        likeBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                sendLike();
                fetchEventLikesAndComments(eventDtls);
//                fetchEventLikesAndComments();
//                               
//                if(verticalForCommentStack!=null)
                  v.removeComponent(verticalForCommentStack);
                  v.removeComponent(likeCommentBtnLayout);
                showLikeAndCommentsForm();
                showFullCommentsStack();
            }
        });
//        commentBtn.addStyleName(BaseTheme.BUTTON_LINK);
//        commentBtn.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(ClickEvent event) {  
//                //showFullCommentsStack();
//            }
//        });
//        likeBtn.addStyleName("link");
//        commentBtn.addStyleName("link");
        likeCommentBtnLayout.addComponent(likeBtn);
        //likeCommentBtnLayout.addComponent(commentBtn);
        likeImage =  new Embedded(null,new ThemeResource("./img/like-icon.jpg"));
        likeImage.setHeight("22px");
        likeImage.setWidth("22px");
        likeImage.addClickListener(new com.vaadin.event.MouseEvents.ClickListener() {

            @Override
            public void click(MouseEvents.ClickEvent event) {
                showLikesWindow();
            }
     });
        likeCommentBtnLayout.addComponent(likeImage);
        likeCommentBtnLayout.addComponent(new Label(GlobalConstants.emptyString + eventLikesList.size() ));
        commentImage =  new Embedded(null,new ThemeResource("./img/comments-icon.jpg"));
        commentImage.setHeight("22px");
        commentImage.setWidth("22px");
        likeCommentBtnLayout.addComponent(commentImage);
        likeCommentBtnLayout.addComponent(new Label(GlobalConstants.emptyString + eventCommentsList.size() ));
//        Label eventTime=new Label( DateUtil.getTimeIntervalOfTheActivity(eventDtls.getEventDate()));
//        eventTime.addStyleName("lightGrayColorAndSmallFont");
//        likeCommentBtnLayout.addComponent(eventTime);
        v.addComponent(likeCommentBtnLayout);
        //v.setExpandRatio(likeCommentBtnLayout, 1);
    }
    
    private void showFullCommentsStack() 
    {
        verticalForCommentStack = new VerticalLayout();
        verticalForCommentStack.setMargin(false);
        verticalForCommentStack.setSpacing(true);
        verticalForCommentStack.setWidth("100%");
        verticalForCommentStack.setHeight("100%");
        verticalForCommentStack.addStyleName("backgroundColor");
        HorizontalLayout fullCommentsLayout;
        for (EventCommentsBean comment : eventCommentsList) {
            fullCommentsLayout = new HorizontalLayout();
            fullCommentsLayout.addStyleName("whiteBottomBorder");
            fullCommentsLayout.setMargin(false);
            fullCommentsLayout.setSpacing(true);
            fullCommentsLayout.setWidth("100%");
            fullCommentsLayout.setHeight("100%");
            Image userImage = new Image(null, new ThemeResource("img/profile-pic.png"));

            userImage.setWidth("30px");
            userImage.setWidth("30px");
            fullCommentsLayout.addComponent(userImage);
            fullCommentsLayout.setExpandRatio(userImage, 0.5f);
            fullCommentsLayout.setComponentAlignment(userImage, Alignment.TOP_LEFT);
            Label lblComment = new Label("<b>" + comment.getName() + ": </b>" + comment.getCommentBody(), ContentMode.HTML);
            fullCommentsLayout.addComponent(lblComment);
            fullCommentsLayout.setExpandRatio(lblComment, 4);
            fullCommentsLayout.setComponentAlignment(lblComment, Alignment.MIDDLE_LEFT);
            
            Label lblCommentTime=new Label(DateUtil.getTimeIntervalOfTheActivity(comment.getCommentTime()));
            lblCommentTime.addStyleName("lightGrayColorAndSmallFont");
            fullCommentsLayout.addComponent(lblCommentTime);
            fullCommentsLayout.setExpandRatio(lblCommentTime, 1.25f);
            fullCommentsLayout.setComponentAlignment(lblCommentTime, Alignment.MIDDLE_RIGHT);

            verticalForCommentStack.addComponent(fullCommentsLayout);

        }
        fullCommentsLayout = new HorizontalLayout();
        fullCommentsLayout.addStyleName("whiteBottomBorder");
        fullCommentsLayout.setMargin(false);
        fullCommentsLayout.setSpacing(false);
        fullCommentsLayout.setWidth("100%");
        fullCommentsLayout.setHeight("100%");

        Image userImage = new Image(null, new ThemeResource("img/profile-pic.png"));
        userImage.setWidth("30px");
        userImage.setWidth("30px");
        txtNewComment = new TextField();
        txtNewComment.setWidth("100%");
        txtNewComment.setImmediate(true);
        txtNewComment.setInputPrompt("Write a comment...");
        txtNewComment.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {

            @Override
            public void handleAction(Object sender, Object target) {
                if (txtNewComment.getValue() != null && !txtNewComment.getValue().equals(GlobalConstants.emptyString)) {
                    if(txtNewComment.getValue().length()>1000)
                    {
                        Notification.show("Comment cannot be more than 1000 characters.", Notification.Type.WARNING_MESSAGE);
                    }
                    else
                    {
                        saveComment(txtNewComment.getValue());
                        v.removeComponent(verticalForCommentStack);
                        v.removeComponent(likeCommentBtnLayout);
                        fetchEventLikesAndComments(eventDtls);
                        showLikeAndCommentsForm();
                        showFullCommentsStack();
                    }
                }
            }
        });

        fullCommentsLayout.addComponent(userImage);
        fullCommentsLayout.setExpandRatio(userImage, 0.5f);
        fullCommentsLayout.setComponentAlignment(userImage, Alignment.TOP_LEFT);
        fullCommentsLayout.addComponent(txtNewComment);
        fullCommentsLayout.setExpandRatio(txtNewComment, 4);
        fullCommentsLayout.setComponentAlignment(txtNewComment, Alignment.MIDDLE_LEFT);
        verticalForCommentStack.addComponent(fullCommentsLayout);
        
        v.addComponent(verticalForCommentStack);
        //v.setExpandRatio(verticalForCommentStack, 5);
    }
    
    private void showLikesWindow() {

        VerticalLayout verticalForLikes = new VerticalLayout();
        verticalForLikes.setMargin(false);
        verticalForLikes.setSpacing(true);
        verticalForLikes.setWidth("100%");
        verticalForLikes.addStyleName("backgroundColor");
        HorizontalLayout likeNamesLayout;
        for (EventLikeBean like : eventLikesList) {
            likeNamesLayout = new HorizontalLayout();
            likeNamesLayout.addStyleName("whiteBottomBorder");
            likeNamesLayout.setMargin(false);
            likeNamesLayout.setSpacing(false);
            likeNamesLayout.setWidth("100%");
            likeNamesLayout.setHeight("100%");
            Image userImage = new Image(null, new ThemeResource("img/profile-pic.png"));

            userImage.setWidth("30px");
            userImage.setWidth("30px");
            likeNamesLayout.addComponent(userImage);
            likeNamesLayout.setExpandRatio(userImage, 0.5f);
            likeNamesLayout.setComponentAlignment(userImage, Alignment.TOP_LEFT);
            Label lblComment = new Label("<b>" + like.getName() + "</b>", ContentMode.HTML);
            likeNamesLayout.addComponent(lblComment);
            likeNamesLayout.setExpandRatio(lblComment, 1);
            likeNamesLayout.setComponentAlignment(lblComment, Alignment.MIDDLE_LEFT);

            Label lblCommentTime = new Label(DateUtil.getTimeIntervalOfTheActivity(like.getLikeTime()));
            lblCommentTime.addStyleName("lightGrayColorAndSmallFont");
            likeNamesLayout.addComponent(lblCommentTime);
            likeNamesLayout.setExpandRatio(lblCommentTime, 1);
            likeNamesLayout.setComponentAlignment(lblCommentTime, Alignment.MIDDLE_RIGHT);

            verticalForLikes.addComponent(likeNamesLayout);

        }

        Window w = new Window(People_who_like_this);
        w.center();
        w.setWidth("35%");
        w.setHeight("40%");
        w.setContent(verticalForLikes);
        getUI().getCurrent().addWindow(w);
    }
    
    private void sendLike() {
        try {
            Userprofile profile = ((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile));
            JSONObject input = new JSONObject();
            input.put("event_id", eventDtls.getEventDetailId());
            input.put("username", profile.getUsername());
            input.put("name", profile.getName());

            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_EVENT_LIKE));
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, input);

            /*
             * if (response.getStatus() != 201) { throw new
             * RuntimeException("Failed : HTTP error code : " +
             * response.getStatus()); }
             */

            String output = response.getEntity(String.class);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }
    
    private void getEventDetailsById(String eventId) {
        try 
        {
            Client client = Client.create();
            
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_FORUM_EVENT_BY_ID));
            
            JSONObject inputJson = new JSONObject();
            
            inputJson.put("eventId",eventId);
             
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            java.lang.reflect.Type listType = new TypeToken<ArrayList<ForumEventDetailsBean>>() {
            }.getType();
    
            Gson gson=  new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();
            
            List <ForumEventDetailsBean> forumEventDetailsList=gson.fromJson(outNObject.getString(GlobalConstants.eventDetailsList), listType);
            this.eventDtls=forumEventDetailsList.get(0);
            
           Type listType1 = new TypeToken<ArrayList<EventLikeBean>>() {
            }.getType();
            
             Gson eventLikesGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            eventLikesList = eventLikesGson.fromJson(outNObject.getString(GlobalConstants.eventLikes), listType1);
            
            Type listType2 = new TypeToken<ArrayList<EventCommentsBean>>() {
            }.getType();
            
             Gson eventCommentsGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            eventCommentsList = eventCommentsGson.fromJson(outNObject.getString(GlobalConstants.eventComments), listType2);
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    private void fetchEventLikesAndComments(ForumEventDetailsBean eventDtls) {
        try {
            JSONObject input = new JSONObject();
            input.put("event_id", eventDtls.getEventDetailId());

            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.FETCH_EVENT_LIKES_BY_ID));
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, input);

            /*
             * if (response.getStatus() != 201) { throw new
             * RuntimeException("Failed : HTTP error code : " +
             * response.getStatus()); }
             */

            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

             Type listType1 = new TypeToken<ArrayList<EventLikeBean>>() {
            }.getType();
            
             Gson eventLikesGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            eventLikesList = eventLikesGson.fromJson(outNObject.getString(GlobalConstants.eventLikes), listType1);
            
            Type listType2 = new TypeToken<ArrayList<EventCommentsBean>>() {
            }.getType();
            
             Gson eventCommentsGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            eventCommentsList = eventCommentsGson.fromJson(outNObject.getString(GlobalConstants.eventComments), listType2);
            
            //System.out.println("eve"+eventLikesList);
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }
    
    private void saveComment(String value) 
    {
          try {
            Userprofile profile = ((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile));
            JSONObject input = new JSONObject();
            input.put("event_id", eventDtls.getEventDetailId());
            input.put("event_desc", eventDtls.getEventDesc());
            input.put("username", profile.getUsername());
            input.put("name", profile.getName());
            input.put("comment", value);

            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_EVENT_COMMENT));
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, input);

            /*
             * if (response.getStatus() != 201) { throw new
             * RuntimeException("Failed : HTTP error code : " +
             * response.getStatus()); }
             */

            String output = response.getEntity(String.class);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
          
   }

    private void getYoutubeVideo() 
    {
        String str=VideoUtil.getYoutubeEmbedingString(eventDtls.getVideoUrl());
        Label video = new Label(str, ContentMode.HTML);
        video.setWidth("50%");
        baseLayout.addComponent(video);
        baseLayout.setExpandRatio(video,2.75f);
        baseLayout.setComponentAlignment(video,Alignment.MIDDLE_CENTER);
    }
    
}
