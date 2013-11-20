package com.quick.dictionary;

import com.quick.bean.DictWordDetailsBean;
import com.quick.global.GlobalConstants;
import com.quick.utilities.DateUtil;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;

public class DictWordDetailWraper extends VerticalLayout {

    private Label description;
    private DictWordDetailsBean wordDetails;

    public DictWordDetailWraper(final DictWordDetailsBean wordDetails,Object dictView) {

        setImmediate(true);
        setSpacing(false);
        setMargin(false);
        this.wordDetails=wordDetails;
        addStyleName("no-vertical-drag-hints");
        addStyleName("no-horizontal-drag-hints");
        //addStyleName("fourSideBorder");
        
        HorizontalLayout details = new HorizontalLayout();
        //details.setSpacing(true);
        //details.setMargin(true);
        //set size full - used to display data in one screen only without scroll
        details.setSizeFull();
        //details.addStyleName("fourSideBorder");
        details.addStyleName("topBorder");
        
        addComponent(details);
        setData(wordDetails);
        addLayoutClickListener((LayoutEvents.LayoutClickListener)dictView);

       
        FormLayout fields = new FormLayout();
        fields.setWidth("100%");
        //fields.addStyleName("rightBorder");
        details.addComponent(fields);
        details.setComponentAlignment(fields, Alignment.MIDDLE_LEFT);
        details.setExpandRatio(fields, 3);
        Label label;
        //String data="The activity of neurons in the brain and the code used by these neurons is described by mathematical neuron models at different levels of detail";
        
        String caption = "<table><tr><td bgcolor='deeppink' style='color: #fff;'>NEW</td><td><b>"+GlobalConstants.spaceString+wordDetails.getWord().toUpperCase()+"</b></td></tr></table>" +GlobalConstants.emptyString;
        
        label = new Label(caption, ContentMode.HTML);
        label.setSizeUndefined();
        fields.addComponent(label);

        description = new Label();
        description.setData(wordDetails.getMeaning());
        description.setCaption(GlobalConstants.emptyString);
        updateSynopsis(wordDetails, false);
        fields.addComponent(description);
        
//        if(wordDetails.getMeaning().length()>150)
//        {
//            final Button more = new Button("More…");
//            more.addStyleName("link");
//            fields.addComponent(more);
//            more.addClickListener(new ClickListener() {
//                @Override
//                public void buttonClick(ClickEvent event) {
//                    updateSynopsis(wordDetails, true);
//                    event.getButton().setVisible(false);
//                }
//            });
//        }
        
        
        
        String timeAndInstructor = "<b>&nbsp;&nbsp;&nbsp;FROM: </b>"+" " + DateUtil.formatDateInddMMyyyyFormat(wordDetails.getAddDate())
                +"" + " <b>. INSTRUCTORS: </b>"+" "+ wordDetails.getOwnerName()                
                + " <b>. LABELS: </b><div style='background-color:grey;color:white;white-space: nowrap;display:inline-block;'>"+wordDetails.getLabels()+"</div>&nbsp;&nbsp;&nbsp;";
        
        label = new Label(timeAndInstructor, ContentMode.HTML);
        //label.setSizeUndefined();
        label.setWidth("100%");        
        label.addStyleName("backgroundColor");
        fields.addComponent(label);
        fields.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    }

    private void updateSynopsis(DictWordDetailsBean wordDetails, boolean expand) {
        String synopsisText = description.getData().toString();
        if (wordDetails.getMeaning()!= null) {
            synopsisText = wordDetails.getMeaning();
            description.setData(wordDetails.getMeaning());
        }
        if (!expand) {
            //synopsisText = synopsisText.length() > 150 ? synopsisText.substring(0, 150) + "…" : synopsisText;

        }
        description.setValue(synopsisText);
    }

    /**
     * @return the topicDetails
     */
    public DictWordDetailsBean getWordDetails() {
        return wordDetails;
    }

    /**
     * @param wordDetails the topicDetails to set
     */
    public void setWordDetails(DictWordDetailsBean wordDetails) {
        this.wordDetails = wordDetails;
    }

    
}
