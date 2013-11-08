package com.vaadin.demo.dashboard;

import com.quick.bean.MasteParmBean;
import com.quick.bean.MyDashBoardBean;
import com.quick.global.GlobalConstants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.File;

public class DashboardActivityWraper extends VerticalLayout {

    private Label description;
    private MyDashBoardBean activityDetails;
    private static final String IN="In";

    public DashboardActivityWraper(MyDashBoardBean activityDetails, DashboardView dash) {

        setImmediate(true);
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        this.activityDetails=activityDetails;
        //setCaption(topicDetails.getEventDesc());
        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");
        addStyleName("lightGrayFourSideBorder");
        //addStyleName("bottomBorder");
        
        HorizontalLayout activityDetailsLayout = new HorizontalLayout();
        //activityDetailsLayout.addStyleName("lightBackgroundForDashboardActivity");
        //details.setSpacing(true);
        activityDetailsLayout.setImmediate(true);
        activityDetailsLayout.setMargin(false);
        activityDetailsLayout.setSpacing(true);
        //set size full - used to display data in one screen only without scroll
        activityDetailsLayout.setWidth("100%");
        activityDetailsLayout.setHeight("70%");
        //activityDetailsLayout.addStyleName("fourSideBorder");
        addComponent(activityDetailsLayout);
        setExpandRatio(activityDetailsLayout,0.5f);
        setData(activityDetails);
        addLayoutClickListener((LayoutEvents.LayoutClickListener)dash);

      
        
        Embedded userImage =  new Embedded(GlobalConstants.emptyString,
                new ThemeResource("./img/profile-pic.png"));
        //userImage.setHeight("100px");
        userImage.setWidth("40px");
        
        VerticalLayout imageLayout = new VerticalLayout();
        imageLayout.setSizeFull();
        imageLayout.addComponent(userImage);
        //imageLayout.addStyleName("threeSideBorder");
        imageLayout.setComponentAlignment(userImage, Alignment.MIDDLE_CENTER);

        
        activityDetailsLayout.addComponent(imageLayout);
        activityDetailsLayout.setComponentAlignment(imageLayout, Alignment.MIDDLE_CENTER);
        activityDetailsLayout.setExpandRatio(imageLayout, 0.5f);
        
        
        VerticalLayout fields = new VerticalLayout();
        fields.setWidth("100%");
        //fields.addStyleName("rightBorder");
        //fields.setSpacing(true);
        //fields.setMargin(true);
        activityDetailsLayout.addComponent(fields);
        activityDetailsLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        activityDetailsLayout.setExpandRatio(fields, 3);
        Label label;
        //String data="The activity of neurons in the brain and the code used by these neurons is described by mathematical neuron models at different levels of detail";
        
//        label = new Label(GlobalConstants.emptyString);
//        label.setWidth("100%");
//        label.addStyleName("deepPinkColor");
//        fields.addComponent(label);
        
        String activityCaption = activityDetails.getNotification();
        label = new Label(activityCaption);
        label.setWidth("100%");
        label.addStyleName("deepPinkColor");
        fields.addComponent(label);

        
        String time = activityDetails.getDateTime();
        label = new Label( IN +GlobalConstants.spaceString+ activityDetails.getStandard() +GlobalConstants.spaceString +GlobalConstants.HYPHEN +GlobalConstants.spaceString + time);
        label.setWidth("100%");
        label.addStyleName("lightGrayColorAndSmallFont");
        
        fields.addComponent(label);
        
        HorizontalLayout activityImageLayout = new HorizontalLayout();
        activityImageLayout.setSizeFull();
        activityImageLayout.setMargin(false);
        activityImageLayout.setSpacing(false);
        //activityImageLayout.addStyleName("fourSideBorder");
        addComponent(activityImageLayout);
        setExpandRatio(activityImageLayout,3);
        Embedded activityImage;
        
        if(new File(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)+activityDetails.getUploadId()+".jpg").exists())
        {
            activityImage =  new Embedded(null,
                new ThemeResource("./img/topic_images/"+activityDetails.getUploadId()+".jpg"));
        }
        else if(new File(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)+activityDetails.getUploadId()+".jpeg").exists())
        {
            activityImage =  new Embedded(null,
                new ThemeResource("./img/topic_images/"+activityDetails.getUploadId()+".jpeg"));
        }
        else if(new File(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)+activityDetails.getUploadId()+".png").exists())
        {
            activityImage =  new Embedded(null,
                new ThemeResource("./img/topic_images/"+activityDetails.getUploadId()+".png"));            
            
        }
        else
        {
            activityImage =  new Embedded(null,
                new ThemeResource("./img/learnMore.jpg"));
            
        }
        
        
        activityImage.setHeight("90px");
        activityImage.setWidth("90px");
        
//        VerticalLayout il = new VerticalLayout();
//        il.setSizeFull();
        activityImageLayout.addComponent(activityImage);
        activityImageLayout.setComponentAlignment(activityImage, Alignment.MIDDLE_CENTER);
        activityImageLayout.setExpandRatio(activityImage, 1);
        
         VerticalLayout il = new VerticalLayout();
         il.setSizeFull();
         
        label = new Label(activityDetails.getTopicintro());
        label.setWidth("100%");
        //label.addStyleName("deepPinkColor");
        il.addComponent(label);
        label.addStyleName("lightBackgroundForDashboardActivity");

        
        activityImageLayout.addComponent(il);
        activityImageLayout.setComponentAlignment(il, Alignment.MIDDLE_RIGHT);
        activityImageLayout.setExpandRatio(il, 2);
        
        
        
        
//        description = new Label();
//        description.setData(activityDetails.getDateTime());
//        description.setCaption(GlobalConstants.emptyString);
//        updateSynopsis(activityDetails, false);
//        fields.addComponent(description);
        
//        more.addStyleName("link");
//        fields.addComponent(more);
//        more.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                updateSynopsis(topicDetails, true);
//                event.getButton().setVisible(false);
//            }
//        });
        
        
//        String timeAndInstructor = "<b>&nbsp;&nbsp;&nbsp;STARTS: </b>"+" " + DateUtil.formatDateInddMMyyyyFormat(activityDetails.getUploadDate())
//                +"" + " <b>. INSTRUCTORS: </b>"+" "+ activityDetails.getOtherNotesInformation() 
//                +"" + " <b>. SUB: </b>"+" "+ activityDetails.getSub() 
//                + " <b>. RECOMMENDED FOR: </b>"+activityDetails.getStd()+"&nbsp;&nbsp;&nbsp;";
//        
//        label = new Label(timeAndInstructor, ContentMode.HTML);
//        //label.setSizeUndefined();
//        label.setWidth("98%");        
//        label.addStyleName("backgroundColor");
//        fields.addComponent(label);
//        fields.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

    }

    private void updateSynopsis(MasteParmBean topicDetails, boolean expand) {
        String synopsisText = description.getData().toString();
        if (topicDetails.getLectureNotesInformation()!= null) {
            synopsisText = topicDetails.getLectureNotesInformation();
            description.setData(topicDetails.getLectureNotesInformation());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 150 ? synopsisText
                    .substring(0, 150) + "…" : synopsisText;

        }
        description.setValue(synopsisText);
    }

    /**
     * @return the topicDetails
     */
    public MyDashBoardBean getTopicDetails() {
        return activityDetails;
    }

    /**
     * @param topicDetails the topicDetails to set
     */
    public void setTopicDetails(MyDashBoardBean topicDetails) {
        this.activityDetails = topicDetails;
    }

    
}
