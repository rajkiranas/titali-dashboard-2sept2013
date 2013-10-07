package com.quick.forum;

import com.quick.bean.ForumEventDetailsBean;
import com.vaadin.demo.dashboard.*;
import java.text.SimpleDateFormat;

import com.quick.data.DataProvider.Movie;
import com.quick.entity.ForumEventDetails;
import com.quick.utilities.MyImageSource;
import com.sun.jersey.core.util.Base64;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ForumDetailWraper extends VerticalLayout {

    Label synopsis = new Label();

    public ForumDetailWraper(final ForumEventDetailsBean eventDetails) {
       
        setCaption(eventDetails.getEventDesc());
        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");

        HorizontalLayout details = new HorizontalLayout();
        details.setSpacing(true);
        details.setMargin(true);
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
        byte[] by=eventDetails.getStringImage().getBytes();
StreamResource.StreamSource imagesource = new MyImageSource (Base64.decode(by));
	
// Create a resource that uses the stream source and give it a name.
// The constructor will automatically register the resource in
// the application.
StreamResource resource = new StreamResource(imagesource, "myimage.png");
 
// Create an image component that gets its contents
// from the resource.
//layout.addComponent(new Image("Image title", resource));
Image coverImage =new Image("Image", resource);


        coverImage.setHeight("150px");
        coverImage.setWidth("150px");
        
        

        final Button more = new Button("More…");

        DragAndDropWrapper cover = new DragAndDropWrapper(coverImage);
        cover.setDragStartMode(DragStartMode.NONE);
        cover.setWidth("200px");
        cover.setHeight("270px");
        cover.addStyleName("cover");
        cover.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                DragAndDropWrapper d = (DragAndDropWrapper) event
                        .getTransferable().getSourceComponent();
                if (d == event.getTargetDetails().getTarget())
                    return;
                Movie m = (Movie) d.getData();
//                coverImage.setSource(new ExternalResource(m.posterUrl));
//                coverImage.setAlternateText(m.title);
                setCaption(m.title);
                updateSynopsis(eventDetails, false);
                more.setVisible(true);
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return AcceptAll.get();
            }
        });
        details.addComponent(cover);

        FormLayout fields = new FormLayout();
        fields.setWidth("35em");
        fields.setSpacing(true);
        fields.setMargin(true);
        details.addComponent(fields);

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

        
        String cap = "<h3><b>"+eventDetails.getEventDesc()+"</b></h3>"+"<h4> by <b>"
                +eventDetails.getEventOwner()+"</b></h4>"+"<h4> on <b>"+eventDetails.getEventDate()+"</b></h4>";
        label = new Label(cap,ContentMode.HTML);
        label.setSizeUndefined();
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

        synopsis.setData(eventDetails.getEventOwner());
        synopsis.setCaption("");
        updateSynopsis(eventDetails, false);
        fields.addComponent(synopsis);

        more.addStyleName("link");
        fields.addComponent(more);
        more.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                updateSynopsis(eventDetails, true);
                event.getButton().setVisible(false);
            }
        });
        
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName("footer");
        footer.setWidth("100%");
        footer.setMargin(true);

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

    public void updateSynopsis(ForumEventDetailsBean eventDetails, boolean expand) {
        String synopsisText = synopsis.getData().toString();
        if (eventDetails.getEventBody() != null) {
            synopsisText = eventDetails.getEventBody();
            synopsis.setData(eventDetails.getEventBody());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 300 ? synopsisText
                    .substring(0, 300) + "…" : synopsisText;

        }
        synopsis.setValue(synopsisText);
    }
}
