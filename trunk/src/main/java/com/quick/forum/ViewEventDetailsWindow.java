/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.forum;

import com.quick.bean.EventCommentsBean;
import com.quick.bean.EventLikeBean;
import com.quick.bean.ForumEventDetailsBean;
import com.quick.global.GlobalConstants;
import com.quick.utilities.DateUtil;
import com.quick.utilities.MyImageSource;
import com.sun.jersey.core.util.Base64;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;
import java.util.List;

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
    
    
    
    public ViewEventDetailsWindow(ForumEventDetailsBean eventDtls) {
        this.eventDtls=eventDtls;
        this.selectedUploadId=selectedUploadId;        
        setModal(true);
        setCaption("View topic details");
        center();        
        setClosable(true);
        setWidth("80%");
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
        setWidth("80%");
        setHeight("90%"); 
        setImmediate(true);
        
        buildBaseStudentLayout();
        addEventDetails();
        //addUserNotes();
        setContent(baseLayout);
        addStyleName("schedule");
        
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

       baseLayout.setSpacing(true);   
       baseLayout.setMargin(true);
//       baseLayout.setWidth("100%");
//       baseLayout.setHeight("100%");
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
        getEventImage();
        getEventDetails();
        
        //baseLayout.setExpandRatio(tabsheetLayout,2);
    }

    private Image getEventImage() 
    {
        byte[] by = eventDtls.getStringImage().getBytes();
        StreamResource.StreamSource imagesource = new MyImageSource(Base64.decode(by));
        StreamResource resource = new StreamResource(imagesource, "myimage.png");
        Image coverImage = new Image("Image", resource);
        coverImage.setHeight("425px");
        coverImage.setWidth("475px");
        //coverImage.setSizeFull();
        baseLayout.addComponent(coverImage);
        baseLayout.setExpandRatio(coverImage,3);
        return coverImage;
    }

    VerticalLayout v;
    private Component getEventDetails() 
    {
        v = new VerticalLayout();
        v.setSizeFull();
        
        HorizontalLayout photoAndEventDtls = new HorizontalLayout();

        photoAndEventDtls.addStyleName("whiteBottomBorder");
        photoAndEventDtls.setMargin(false);
        photoAndEventDtls.setSpacing(true);
        photoAndEventDtls.setWidth("100%");
        photoAndEventDtls.setHeight("100%");
        Image userImage = new Image(null, new ThemeResource("img/profile-pic.png"));

        userImage.setWidth("30px");
        userImage.setWidth("30px");
        photoAndEventDtls.addComponent(userImage);
        photoAndEventDtls.setExpandRatio(userImage, 1);
        photoAndEventDtls.setComponentAlignment(userImage, Alignment.MIDDLE_LEFT);
        
        String cap = "<div style='color:deeppink;display:inline-block;'> <b>" + eventDtls.getEventOwner() + "</b></div>" + "<div style='color:grey;font-size:13px;display:inline-block;'>&nbsp; shared </div> <div style='color:deeppink;display:inline-block;'>"
                + eventDtls.getEventDesc() + "</div>";
        Label eventIntro = new Label(cap, ContentMode.HTML);
        eventIntro.setWidth("100%");
        photoAndEventDtls.addComponent(eventIntro);
        photoAndEventDtls.setExpandRatio(eventIntro, 3);
        photoAndEventDtls.setComponentAlignment(eventIntro, Alignment.MIDDLE_LEFT);
            
        Label eventDesc = new Label(eventDtls.getEventBody());
        eventDesc.setWidth("100%");
        eventDesc.setHeight("100%");
        
        v.addComponent(photoAndEventDtls);
        v.addComponent(eventDesc);
        showLikeAndCommentsForm();
        
        baseLayout.addComponent(v);
        baseLayout.setExpandRatio(v,2);
        return v;
    }
    
    private void showLikeAndCommentsForm() {
        HorizontalLayout likeCommentBtnLayout = new HorizontalLayout();
        Embedded likeImage ;
        Embedded commentImage;
        
        likeCommentBtnLayout.setSpacing(true);
        //likeCommentLayout.setMargin(true);
        //likeCommentBtnLayout.addStyleName("backgroundColor");

        //likeCommentBtnLayout.setWidth("60%");
        Button likeBtn = new Button("Like");
        Button commentBtn = new Button("Comment");

        likeBtn.addStyleName(BaseTheme.BUTTON_LINK);
        likeBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
//                sendLike();
//                fetchEventLikesAndComments();
//                               
//                if(verticalForCommentStack!=null)
//                    fields.removeComponent(verticalForCommentStack);
//                fields.removeComponent(likeCommentBtnLayout);
//                showLikeAndCommentsForm();
//                showFullCommentsStack();
            }
        });
        commentBtn.addStyleName(BaseTheme.BUTTON_LINK);
        commentBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {  
                //showFullCommentsStack();
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
                //showLikesWindow();
            }
     });
        likeCommentBtnLayout.addComponent(likeImage);
        likeCommentBtnLayout.addComponent(new Label(GlobalConstants.emptyString + eventLikesList.size() ));
        commentImage =  new Embedded(null,new ThemeResource("./img/comments-icon.jpg"));
        commentImage.setHeight("22px");
        commentImage.setWidth("22px");
        likeCommentBtnLayout.addComponent(commentImage);
        likeCommentBtnLayout.addComponent(new Label(GlobalConstants.emptyString + eventCommentsList.size() ));
        Label eventTime=new Label( DateUtil.getTimeIntervalOfTheActivity(eventDtls.getEventDate()));
        eventTime.addStyleName("lightGrayColorAndSmallFont");
        likeCommentBtnLayout.addComponent(eventTime);
        v.addComponent(likeCommentBtnLayout);
    }
    
      
    
}
