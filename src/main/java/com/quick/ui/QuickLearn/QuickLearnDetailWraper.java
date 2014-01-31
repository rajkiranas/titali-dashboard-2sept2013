package com.quick.ui.QuickLearn;

import com.quick.bean.MasteParmBean;
import com.quick.global.GlobalConstants;
import com.quick.utilities.DateUtil;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;


import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.File;

public class QuickLearnDetailWraper extends VerticalLayout {

    private Label description;
    private MasteParmBean topicDetails;

    public QuickLearnDetailWraper(final MasteParmBean topicDetails, Object quickLearn) {

        setImmediate(true);
        setSpacing(false);
        setMargin(false);
        this.topicDetails=topicDetails;
        //setCaption(topicDetails.getEventDesc());
        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");
        //addStyleName("fourSideBorder");
        
        HorizontalLayout details = new HorizontalLayout();
        //details.setSpacing(true);
        //details.setMargin(true);
        //set size full - used to display data in one screen only without scroll
        details.setSizeFull();
        details.addStyleName("fourSideBorder");
        addComponent(details);
        setData(topicDetails);
        addLayoutClickListener((LayoutEvents.LayoutClickListener)quickLearn);

        // Create an instance of our stream source.
//        byte[] by = topicDetails.getStringImage().getBytes();
//        StreamResource.StreamSource imagesource = new MyImageSource(Base64.decode(by));
//
//// Create a resource that uses the stream source and give it a name.
//// The constructor will automatically register the resource in
//// the application.
//        StreamResource resource = new StreamResource(imagesource, "myimage.png");
//
//// Create an image component that gets its contents
//// from the resource.
////layout.addComponent(new Image("Image title", resource));
//        Image coverImage = new Image("Image", resource);
//
//
//        coverImage.setHeight("150px");
//        coverImage.setWidth("150px");
//
//
//
        //final Button more = new Button("More…");
//
//        DragAndDropWrapper cover = new DragAndDropWrapper(coverImage);
//        cover.setDragStartMode(DragStartMode.NONE);
//        cover.setWidth("200px");
//        cover.setHeight("270px");
//        cover.addStyleName("cover");
//        cover.setDropHandler(new DropHandler() {
//            @Override
//            public void drop(DragAndDropEvent event) {
//                DragAndDropWrapper d = (DragAndDropWrapper) event
//                        .getTransferable().getSourceComponent();
//                if (d == event.getTargetDetails().getTarget()) {
//                    return;
//                }
////                Movie m = (Movie) d.getData();
////                setCaption(m.title);
//                updateSynopsis(topicDetails, false);
//                more.setVisible(true);
//            }
//
//            @Override
//            public AcceptCriterion getAcceptCriterion() {
//                return AcceptAll.get();
//            }
//        });
//        details.addComponent(cover);

        //final Button more = new Button("More…");
        FormLayout fields = new FormLayout();
        fields.setWidth("100%");
        //fields.addStyleName("rightBorder");
        //fields.setSpacing(true);
        //fields.setMargin(true);
        details.addComponent(fields);
        details.setComponentAlignment(fields, Alignment.MIDDLE_LEFT);
        details.setExpandRatio(fields, 3);
        Label label;
        //String data="The activity of neurons in the brain and the code used by these neurons is described by mathematical neuron models at different levels of detail";
        
        String caption = "<table><tr><td bgcolor='#3b5998' style='color: #fff;'>NEW</td><td><div style='color:#3b5998;display:inline-block;'><b>"+topicDetails.getUploadId()+ ": "+topicDetails.getTopic() +"</b></div></td></tr></table>" +"";
//                + "<h5> " + topicDetails.getLectureNotesInformation() + "</h5>" 
//                + "<h4><b>STARTS: </b>"+" " + topicDetails.getUploadDate()+"" + " <b>. INSTRUCTORS: </b>"+" "+ topicDetails.getOtherNotesInformation() +" </h4>";
                //+ "<h4><tr><b>STARTS: </b></h4>"+" <h5>" + topicDetails.getUploadDate()+"</h5>" + " <h4><b>. INSTRUCTORS: </b></h4>"+" <h5>"+ topicDetails.getOtherNotesInformation() +"</h5></tr>";
        
        label = new Label(caption, ContentMode.HTML);
        label.setSizeUndefined();
        fields.addComponent(label);

        description = new Label();
        description.setData(topicDetails.getLectureNotesInformation());
        description.setCaption(GlobalConstants.emptyString);
        updateSynopsis(topicDetails, false);
        fields.addComponent(description);
        
//        more.addStyleName("link");
//        fields.addComponent(more);
//        more.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                updateSynopsis(topicDetails, true);
//                event.getButton().setVisible(false);
//            }
//        });
        
        
        String timeAndInstructor = "<b>&nbsp;&nbsp;&nbsp;STARTS: </b>"+" " + DateUtil.formatDateInddMMyyyyFormat(topicDetails.getUploadDate())
                +"" + " <b>. INSTRUCTORS: </b>"+" "+ topicDetails.getOtherNotesInformation() 
                +"" + " <b>. SUB: </b>"+" "+ topicDetails.getSub() 
                + " <b>. RECOMMENDED: </b>"+topicDetails.getStd()+"&nbsp;&nbsp;&nbsp;";
        
        label = new Label(timeAndInstructor, ContentMode.HTML);
        //label.setSizeUndefined();
        label.setWidth("98%");        
        label.addStyleName("backgroundColor");
        fields.addComponent(label);
        fields.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        
        
        Embedded coverImage ;
//                =  new Embedded(GlobalConstants.emptyString,
//                new ThemeResource("./img/learnMore.jpg"));
        
        if(new File(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)+topicDetails.getUploadId()+".jpg").exists())
        {
            coverImage =  new Embedded(null,
                new ThemeResource("./img/topic_images/"+topicDetails.getUploadId()+".jpg"));
        }
        else if(new File(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)+topicDetails.getUploadId()+".jpeg").exists())
        {
            coverImage =  new Embedded(null,
                new ThemeResource("./img/topic_images/"+topicDetails.getUploadId()+".jpeg"));
        }
        else if(new File(GlobalConstants.getProperty(GlobalConstants.UPLOAD_TOPIC_IMAGES_PATH)+topicDetails.getUploadId()+".png").exists())
        {
            coverImage =  new Embedded(null,
                new ThemeResource("./img/topic_images/"+topicDetails.getUploadId()+".png"));            
        }
        else
        {
            coverImage =  new Embedded(null,
                new ThemeResource("./img/learnMore-Topics.jpg"));
        }
         
        
        
//        coverImage.setHeight("92%");
//        coverImage.setWidth("92%");
        coverImage.setSizeFull();
        
        VerticalLayout imageLayout = new VerticalLayout();
        imageLayout.setSizeFull();
        imageLayout.addComponent(coverImage);
        //imageLayout.addStyleName("threeSideBorder");
        imageLayout.setComponentAlignment(coverImage, Alignment.MIDDLE_CENTER);

        
        details.addComponent(imageLayout);
        details.setComponentAlignment(imageLayout, Alignment.MIDDLE_RIGHT);
        details.setExpandRatio(imageLayout, 0.5f);

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
    public MasteParmBean getTopicDetails() {
        return topicDetails;
    }

    /**
     * @param topicDetails the topicDetails to set
     */
    public void setTopicDetails(MasteParmBean topicDetails) {
        this.topicDetails = topicDetails;
    }

    
}
