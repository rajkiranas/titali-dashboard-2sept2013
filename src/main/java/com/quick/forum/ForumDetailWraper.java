package com.quick.forum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.*;
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
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.themes.BaseTheme;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ForumDetailWraper extends VerticalLayout {

    private Label synopsis;
    private ForumEventDetailsBean eventDetails;
    private List<EventLikeBean> eventLikesList;
    private List<EventCommentsBean> eventCommentsList;
    private VerticalLayout fields;
    private  Embedded likeImage ;
    private  Embedded commentImage;
    private TextField txtNewComment;
    private VerticalLayout verticalForCommentStack;
    private HorizontalLayout likeCommentBtnLayout;
    private String People_who_like_this="People who like this";

    public ForumDetailWraper(final ForumEventDetailsBean eventDetails,ForumView view) {

        this.eventDetails = eventDetails;
        setCaption(eventDetails.getEventDesc());
        setHeight("100%");
        setWidth("99%");
        setMargin(false);
        addStyleName("blackBg");
//        addStyleName("no-vertical-drag-hints");
//        addStyleName("no-horizontal-drag-hints");
        
        setData(eventDetails);
        HorizontalLayout details = new HorizontalLayout();
        details.setSpacing(true);
        details.setMargin(false);
        details.setSizeFull();
        addComponent(details);
        setComponentAlignment(details,Alignment.MIDDLE_CENTER);
        addLayoutClickListener(view);

//        final Image coverImage = new Image("", new ExternalResource(
//                ""));
//        
//        Embedded coverImage =  new Embedded("Image from a theme resource",
//                new ThemeResource("./img/dashboard-pie.png"));

//        StreamResource imageResource = null;
//        StreamResource.StreamSource source = new StreamResource.StreamSource() {
//
//            @Override
//            public InputStream getStream() {
//                return new ByteArrayInputStream(eventDetails.getEventImage());
//            }
//        };
//        
//     
//         imageResource = new StreamResource(source, eventDetails.getImageFileName());
//      
//          Embedded coverImage =  new Embedded("Image from a theme resource",
//                 (Resource)imageResource);

        // Create an instance of our stream source.
        final Label more = new Label("<div style='color:#3b5998;display:inline-block;'> <b>" +"Read More..."+ "</b></div>",ContentMode.HTML);
        fields = new VerticalLayout();
        fields.setWidth("60%");
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("rightAndLeftBorder");
        fields.addStyleName("whiteBg");
        
        Label label;

        String cap = "<div style='color:#3b5998;display:inline-block;'> <b>" + eventDetails.getEventOwner() + "</b></div>" + "<div style='color:grey;font-size:13px;display:inline-block;'>&nbsp; shared </div> <div style='color:#3b5998;display:inline-block;'>"
                + eventDetails.getEventDesc() + "</div>";
        label = new Label(cap, ContentMode.HTML);
        label.setWidth("100%");
        fields.addComponent(label);
        //label.setStyleName("deepPinkColor");
        //label.setCaption("");
        
        synopsis = new Label();
        synopsis.setWidth("100%");
        synopsis.setData(eventDetails.getEventOwner());
        updateSynopsis(eventDetails, false);
        fields.addComponent(synopsis);
        
        
        
        
        if(eventDetails.getStringImage()!=null)
        {
            byte[] by = eventDetails.getStringImage().getBytes();
            StreamResource.StreamSource imagesource = new MyImageSource(Base64.decode(by));
            StreamResource resource = new StreamResource(imagesource, "myimage.png");

            Image coverImage = new Image("Image", resource);

            coverImage.setHeight("70%");
            coverImage.setWidth("75%");

            DragAndDropWrapper cover = new DragAndDropWrapper(coverImage);
            cover.setDragStartMode(DragStartMode.NONE);

            cover.setHeight("70%");
            cover.setWidth("75%");
            cover.addStyleName("cover");
            cover.setDropHandler(new DropHandler() {

                @Override
                public void drop(DragAndDropEvent event) {
                    DragAndDropWrapper d = (DragAndDropWrapper) event.getTransferable().getSourceComponent();
                    if (d == event.getTargetDetails().getTarget()) {
                        return;
                    }
                    updateSynopsis(eventDetails, false);
                    more.setVisible(true);
                }

                @Override
                public AcceptCriterion getAcceptCriterion() {
                    return AcceptAll.get();
                }
            });
//            details.addComponent(cover);
//            details.setExpandRatio(cover, 0.5f);
            fields.addComponent(cover);
        }
        else
        {
            String str = VideoUtil.getYoutubeEmbedingString(eventDetails.getVideoUrl());
            Label video = new Label(str, ContentMode.HTML);
            fields.addComponent(video);
        }

        

        fields.addComponent(more);
        /* more.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                updateSynopsis(eventDetails, true);
                event.getButton().setVisible(false);
                
                fetchEventLikesAndComments();
                showLikeAndCommentsForm();
            }
        }); */
        
        details.addComponent(fields);
        details.setExpandRatio(fields, 2);
        details.setComponentAlignment(fields,Alignment.MIDDLE_CENTER);

    }
    
    private void showLikeAndCommentsForm() {
        likeCommentBtnLayout = new HorizontalLayout();
        likeCommentBtnLayout.setSpacing(true);
        //likeCommentLayout.setMargin(true);
        //likeCommentBtnLayout.addStyleName("backgroundColor");

        //likeCommentBtnLayout.setWidth("60%");
        Button likeBtn = new Button("Like");
        Button commentBtn = new Button("Comment");

        likeBtn.addStyleName(BaseTheme.BUTTON_LINK);
        likeBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                sendLike();
                fetchEventLikesAndComments();
                               
                if(verticalForCommentStack!=null)
                    fields.removeComponent(verticalForCommentStack);
                fields.removeComponent(likeCommentBtnLayout);
                showLikeAndCommentsForm();
                showFullCommentsStack();
            }
        });
        commentBtn.addStyleName(BaseTheme.BUTTON_LINK);
        commentBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {  
                showFullCommentsStack();
            }
        });
//        likeBtn.addStyleName("link");
//        commentBtn.addStyleName("link");
        likeCommentBtnLayout.addComponent(likeBtn);
        likeCommentBtnLayout.addComponent(commentBtn);
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
        Label eventTime=new Label( DateUtil.getTimeIntervalOfTheActivity(eventDetails.getEventDate()));
        eventTime.addStyleName("lightGrayColorAndSmallFont");
        likeCommentBtnLayout.addComponent(eventTime);
        fields.addComponent(likeCommentBtnLayout);
    }
    private void showLikesWindow() {

        VerticalLayout verticalForCommentStack = new VerticalLayout();
        verticalForCommentStack.setMargin(false);
        verticalForCommentStack.setSpacing(true);
        verticalForCommentStack.setWidth("100%");
        verticalForCommentStack.addStyleName("backgroundColor");
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

            verticalForCommentStack.addComponent(likeNamesLayout);

        }

        Window w = new Window(People_who_like_this);
        w.center();
        w.setWidth("35%");
        w.setHeight("40%");
        w.setContent(verticalForCommentStack);
        getUI().getCurrent().addWindow(w);
    }
    private void showFullCommentsStack() 
    {
        if(verticalForCommentStack!=null)
                    fields.removeComponent(verticalForCommentStack);
        
        verticalForCommentStack = new VerticalLayout();
        verticalForCommentStack.setMargin(false);
        verticalForCommentStack.setSpacing(true);
        verticalForCommentStack.setWidth("65%");
        //verticalForCommentStack.setHeight("200px");
        verticalForCommentStack.addStyleName("backgroundColor");
        HorizontalLayout fullCommentsLayout;
        for (EventCommentsBean comment : eventCommentsList) {
            fullCommentsLayout = new HorizontalLayout();
            fullCommentsLayout.addStyleName("whiteBottomBorder");
            fullCommentsLayout.setMargin(false);
            fullCommentsLayout.setSpacing(false);
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
            fullCommentsLayout.setExpandRatio(lblCommentTime, 1);
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
                        fetchEventLikesAndComments();
                        fields.removeComponent(verticalForCommentStack);
                        fields.removeComponent(likeCommentBtnLayout);
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
        fields.addComponent(verticalForCommentStack);
    }
    private void saveComment(String value) 
    {
          try {
            Userprofile profile = ((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile));
            JSONObject input = new JSONObject();
            input.put("event_id", eventDetails.getEventDetailId());
            input.put("event_desc", eventDetails.getEventDesc());
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

    public void updateSynopsis(ForumEventDetailsBean eventDetails, boolean expand) {
        String synopsisText = synopsis.getData().toString();
        if (eventDetails.getEventBody() != null) {
            synopsisText = eventDetails.getEventBody();
            synopsis.setData(eventDetails.getEventBody());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 300 ? synopsisText.substring(0, 300) + "…" : synopsisText;

        }
        synopsis.setValue(synopsisText);
    }

    private void sendLike() {
        try {
            Userprofile profile = ((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile));
            JSONObject input = new JSONObject();
            input.put("event_id", eventDetails.getEventDetailId());
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
    
        private void fetchEventLikesAndComments() {
        try {
            JSONObject input = new JSONObject();
            input.put("event_id", eventDetails.getEventDetailId());

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
            
            System.out.println("eve"+eventLikesList);
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }
}