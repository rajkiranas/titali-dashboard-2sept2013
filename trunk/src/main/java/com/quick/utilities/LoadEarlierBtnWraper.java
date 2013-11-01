package com.quick.utilities;

import com.quick.global.GlobalConstants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

public class LoadEarlierBtnWraper extends VerticalLayout {

    private static final String htmlLabel="<table><tr><td align='center'><b>Load earlier</b></td></tr></table>";
public LoadEarlierBtnWraper(Object layoutClickImplementor)
{
    Label loadEarlier = new Label(htmlLabel,ContentMode.HTML);
    //loadEarlier.addStyleName("brownBackgroundColor");
    loadEarlier.setImmediate(true);
    
    
    //Label earlier = new Label(" earlier");
    //loadEarlier.addStyleName("brownBackgroundColor");
    //earlier.setImmediate(true);
//    loadEarlier.setWidth("100%");
//    loadEarlier.setHeight("100%");

    HorizontalLayout h = new HorizontalLayout();
    h.setImmediate(true);
    //h.addStyleName("blueBackgroundWhiteColor");
    
    Label empty=new Label(GlobalConstants.emptyString);
    h.addComponent(empty);
    h.setComponentAlignment(empty, Alignment.MIDDLE_RIGHT);
    h.setExpandRatio(empty, 2);
    
    h.addComponent(loadEarlier);
    h.setComponentAlignment(loadEarlier, Alignment.MIDDLE_LEFT);
    h.setExpandRatio(loadEarlier, 3);
    
//    h.addComponent(earlier);
//    h.setComponentAlignment(earlier, Alignment.MIDDLE_LEFT);
    
    
    h.setWidth("100%");
    h.setHeight("100%");
    h.setImmediate(true);
    
    setImmediate(true);
    addStyleName("blueBackgroundWhiteColor");
    addComponent(h);
    setComponentAlignment(h, Alignment.MIDDLE_CENTER);
    setWidth("99%");
    addLayoutClickListener((LayoutEvents.LayoutClickListener)layoutClickImplementor);
}
}
