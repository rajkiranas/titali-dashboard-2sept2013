package com.quick.forum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.*;
import com.vaadin.demo.dashboard.*;
import java.text.SimpleDateFormat;

import com.quick.data.DataProvider.Movie;
import com.quick.entity.ForumEventDetails;
import com.quick.global.GlobalConstants;
import com.quick.utilities.DateUtil;
import com.quick.utilities.MyImageSource;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.themes.BaseTheme;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public ForumDetailWraper(final ForumEventDetailsBean eventDetails) {

        this.eventDetails = eventDetails;
        setCaption(eventDetails.getEventDesc());
        setHeight("99%");
        setWidth("99%");
//        addStyleName("no-vertical-drag-hints");
//        addStyleName("no-horizontal-drag-hints");
        addStyleName("fourSideBorder");

        HorizontalLayout details = new HorizontalLayout();
        details.setSpacing(true);
        details.setMargin(true);
        details.setSizeFull();
        addComponent(details);

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
        byte[] by = eventDetails.getStringImage().getBytes();
        StreamResource.StreamSource imagesource = new MyImageSource(Base64.decode(by));

// Create a resource that uses the stream source and give it a name.
// The constructor will automatically register the resource in
// the application.
        StreamResource resource = new StreamResource(imagesource, "myimage.png");

// Create an image component that gets its contents
// from the resource.
//layout.addComponent(new Image("Image title", resource));
        Image coverImage = new Image("Image", resource);


        coverImage.setHeight("150px");
        coverImage.setWidth("150px");



        final Button more = new Button("More…");

        DragAndDropWrapper cover = new DragAndDropWrapper(coverImage);
        cover.setDragStartMode(DragStartMode.NONE);
        cover.setWidth("150px");
        cover.setHeight("150px");
        cover.addStyleName("cover");
        cover.setDropHandler(new DropHandler() {

            @Override
            public void drop(DragAndDropEvent event) {
                DragAndDropWrapper d = (DragAndDropWrapper) event.getTransferable().getSourceComponent();
                if (d == event.getTargetDetails().getTarget()) {
                    return;
                }
                //Movie m = (Movie) d.getData();
//                coverImage.setSource(new ExternalResource(m.posterUrl));
//                coverImage.setAlternateText(m.title);
                //setCaption(m.title);
                updateSynopsis(eventDetails, false);
                more.setVisible(true);
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return AcceptAll.get();
            }
        });
        details.addComponent(cover);
        details.setExpandRatio(cover, 0.5f);

        fields = new VerticalLayout();
        fields.setWidth("100%");
        fields.setSpacing(true);
        fields.setMargin(true);
        details.addComponent(fields);
        details.setExpandRatio(fields, 2);
        Label label;
//        if (event != null) {
//            SimpleDateFormat df = new SimpleDateFormat();
//
//            df.applyPattern("dd-mm-yyyy");
//            label = new Label(df.format(event.start));
//            label.setSizeUndefined();
//            label.setCaption("Date");
//            fields.addComponent(label);
//
//            df.applyPattern("hh:mm a");
//            label = new Label(df.format(event.start));
//            label.setSizeUndefined();
//            label.setCaption("Starts");
//            fields.addComponent(label);
//
//            label = new Label(df.format(event.end));
//            label.setSizeUndefined();
//            label.setCaption("Ends");
//            fields.addComponent(label);
//        }


        String cap = "<b>" + eventDetails.getEventDesc() + "</b>" + " by <b>"
                + eventDetails.getEventOwner() + "</b>";
        label = new Label(cap, ContentMode.HTML);
        label.setWidth("100%");
        label.setStyleName("deepPinkColor");
        //label.setCaption("");
        fields.addComponent(label);

//        label = new Label("<h4><b>"+eventDetails.getEventOwner()+"</b></h4>",ContentMode.HTML);
//        label.setSizeUndefined();
//        label.setCaption("By");
//        fields.addComponent(label);
//        
//        label = new Label("<h4><b>"+eventDetails.getEventDate()+"</b></h4>",ContentMode.HTML);
//        label.setSizeUndefined();
//        label.setCaption("on");
//        fields.addComponent(label);

        synopsis = new Label();
        synopsis.setWidth("100%");
        synopsis.setData(eventDetails.getEventOwner());
        //synopsis.setCaption(GlobalConstants.emptyString);
        updateSynopsis(eventDetails, false);
        fields.addComponent(synopsis);

        more.addStyleName("link");
        fields.addComponent(more);
        more.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                updateSynopsis(eventDetails, true);
                event.getButton().setVisible(false);
                
                fetchEventLikesAndComments();
                showLikeAndCommentsForm();
            }
        });

        

//        Button ok = new Button("Close");
//        ok.addStyleName("wide");
//        ok.addStyleName("default");
//        ok.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                close();
//            }
//        });
//        footer.addComponent(ok);
//        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
//        l.addComponent(footer);
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