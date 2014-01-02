package com.quick.planner;

import com.vaadin.addon.calendar.event.BasicEvent;
import java.text.DateFormat;

public class MUCEvent extends BasicEvent implements Cloneable {
    private static final long serialVersionUID = 1L;

    private String privateEventOwner;

    /**
     * 
     * @return 
     */
    public String getPrivateEventOwner() {
        return privateEventOwner;
    }

    /**
     * 
     * @param privateEvent 
     */
    public void setPrivateEventOwner(String privateEvent) {
        this.privateEventOwner = privateEvent;
    }

    /**
     * 
     * @return
     * @throws CloneNotSupportedException 
     */
    @Override
    public MUCEvent clone() throws CloneNotSupportedException {
        return (MUCEvent) super.clone();
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return getTime() + ": " + getCaption();
    }

    /**
     * 
     * @return 
     */
    public String getTime() {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);
        String time = formatter.format(getStart()) + "-"
                + formatter.format(getEnd());

        return time;

    }

}
