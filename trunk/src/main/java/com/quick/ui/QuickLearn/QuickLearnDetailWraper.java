package com.quick.ui.QuickLearn;

import com.quick.bean.MasteParmBean;
import com.quick.bean.QuickLearn;
import com.quick.global.GlobalConstants;
import com.vaadin.demo.dashboard.StudQuickLearn;
import com.vaadin.demo.dashboard.ViewTopicDetailsWindow;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;


import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class QuickLearnDetailWraper extends VerticalLayout {

    private Label description = new Label();
    private MasteParmBean topicDetails;

    public QuickLearnDetailWraper(final MasteParmBean topicDetails, StudQuickLearn quickLearn) {

        setImmediate(true);
        this.topicDetails=topicDetails;
        //setCaption(topicDetails.getEventDesc());
        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");
        
        HorizontalLayout details = new HorizontalLayout();
        //details.setSpacing(true);
        //details.setMargin(true);
        details.setSizeFull();
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
        //fields.setSpacing(true);
        //fields.setMargin(true);
        details.addComponent(fields);
        details.setComponentAlignment(fields, Alignment.MIDDLE_LEFT);
        details.setExpandRatio(fields, 3);
        Label label;
        //String data="The activity of neurons in the brain and the code used by these neurons is described by mathematical neuron models at different levels of detail";
        
        String caption = "<h3><b>" + topicDetails.getUploadId()+ ":"+topicDetails.getTopic() + "</b></h3>";
//                + "<h5> " + topicDetails.getLectureNotesInformation() + "</h5>" 
//                + "<h4><b>STARTS: </b>"+" " + topicDetails.getUploadDate()+"" + " <b>. INSTRUCTORS: </b>"+" "+ topicDetails.getOtherNotesInformation() +" </h4>";
                //+ "<h4><tr><b>STARTS: </b></h4>"+" <h5>" + topicDetails.getUploadDate()+"</h5>" + " <h4><b>. INSTRUCTORS: </b></h4>"+" <h5>"+ topicDetails.getOtherNotesInformation() +"</h5></tr>";
        
        label = new Label(caption, ContentMode.HTML);
        label.setSizeUndefined();
        fields.addComponent(label);

        
        description.setData(topicDetails.getLectureNotesInformation());
        description.setCaption("");
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
        
        
        String timeAndInstructor = "<h4><b>STARTS: </b>"+" " + topicDetails.getUploadDate()+"" + " <b>. INSTRUCTORS: </b>"+" "+ topicDetails.getOtherNotesInformation() +" </h4>";
        label = new Label(timeAndInstructor, ContentMode.HTML);
        label.setSizeUndefined();
        fields.addComponent(label);
        
        
         
        
        Embedded coverImage =  new Embedded(GlobalConstants.emptyString,
                new ThemeResource("./img/learnMore.png"));
        coverImage.setHeight("100px");
        coverImage.setWidth("100px");
        details.addComponent(coverImage);
        details.setComponentAlignment(coverImage, Alignment.MIDDLE_RIGHT);
        details.setExpandRatio(coverImage, 1);

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
