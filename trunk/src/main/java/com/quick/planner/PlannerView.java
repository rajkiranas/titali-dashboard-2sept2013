/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.quick.planner;

import com.quick.forum.*;
import com.quick.bean.ForumEventDetailsBean;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.quick.utilities.LoadEarlierBtnWraper;
import com.vaadin.addon.calendar.event.BasicEventProvider;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.DateClickEvent;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClick;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClickHandler;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.RangeSelectHandler;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.WeekClick;
import com.vaadin.addon.calendar.ui.handler.BasicDateClickHandler;
import com.vaadin.addon.calendar.ui.handler.BasicWeekClickHandler;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlannerView extends VerticalLayout implements View,LayoutEvents.LayoutClickListener {

    //private Table forumTable;
    private String New="New";
    private List <ForumEventDetailsBean> forumEventDetailsList;
    private Userprofile loggedInUserProfile;
    private List<List> wrapperList = new ArrayList<List>();
    private LoadEarlierBtnWraper loadMoreWraper = new LoadEarlierBtnWraper(this);

    public List<ForumEventDetailsBean> getForumEventDetailsList() {
        return forumEventDetailsList;
    }

    public void setForumEventDetailsList(List<ForumEventDetailsBean> forumEventDetail) {
        this.forumEventDetailsList = forumEventDetail;
    }

    @Override
    public void enter(ViewChangeEvent event) {
//        setSizeFull();
        addStyleName("schedule");
        //addStyleName("blackBg");
        //addStyleName("dashboard");
        //addComponent(buildDraftsView());
        loggedInUserProfile =((Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile));

        buildHeaderView();
        buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(true);
        top.addStyleName("toolbar");
        addComponent(top);
        final Label title = new Label("Planner");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        top.addStyleName("lightBackgroundForDashboardActivity");
        top.addStyleName("lightGrayFourSideBorder");
        
        Button addEventBtn = new Button("Schedule Appointment");
        addEventBtn.setImmediate(true);
        addEventBtn.addClickListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                //String dt = format.format(new Date());
                //Date date;
                try 
                {
                    //date = (Date) format.parse(dt);
                    getUI().addWindow((new PlannerEventFilter(new Date())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        
         top.addComponent(addEventBtn);
        top.setComponentAlignment(addEventBtn, Alignment.MIDDLE_RIGHT);
        top.setExpandRatio(addEventBtn, 1);
        
        /* Button newEventBtn =  new Button(New);
        newEventBtn.setImmediate(true);
        newEventBtn.addStyleName("default");
        newEventBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(new NewEventWindow(event));
            }
        });
        
        top.addComponent(newEventBtn);
        top.setComponentAlignment(newEventBtn, Alignment.MIDDLE_RIGHT);
        top.setExpandRatio(newEventBtn, 1); */
        
        addComponent(top);
    }
    
    
    private VerticalLayout v;
    
    private void buildBodyView() {
        
        v = new VerticalLayout();
        v.setSizeFull();

        initCalendar();
        
        addComponent(v);
        setExpandRatio(v, 1.5f);
        setHeight("100%");
        setWidth("100%");
    }
    
    BasicEventProvider dataSource;
    Calendar calendar;
    
    private void initCalendar() {
        
        dataSource = new BasicEventProvider();
        calendar = new Calendar(dataSource);
        
        calendar.setLocale(Locale.getDefault());
        calendar.setImmediate(true);
        calendar.setSizeFull();

        GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.getLocale());

        boolean showWeeklyView =false;
        if (!showWeeklyView) {
            final int rollAmount = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
            gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
            //resetTime(false);
            Date currentMonthsFirstDate = gregorianCalendar.getTime();
            calendar.setStartDate(currentMonthsFirstDate);
            gregorianCalendar.add(GregorianCalendar.MONTH, 1);
            gregorianCalendar.add(GregorianCalendar.DATE, -1);
            calendar.setEndDate(gregorianCalendar.getTime());
        }

        Date now = new Date();
        calendar.addEvent(constructEvent(now, now, "Today", "Today",true));
        
        java.util.Calendar javaCal = java.util.Calendar.getInstance();
        
        javaCal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        javaCal.set(java.util.Calendar.MONTH, 0);
        javaCal.set(java.util.Calendar.YEAR, 2014);
        Date newYear = javaCal.getTime();
        calendar.addEvent(constructEvent(newYear, newYear, "Happy New Year!", "Happy New Year!",true));
        
        javaCal.set(java.util.Calendar.DAY_OF_MONTH, 10);
        javaCal.set(java.util.Calendar.MONTH, 0);
        javaCal.set(java.util.Calendar.YEAR, 2014);
        
        Date startBCS = javaCal.getTime();
        
        javaCal.set(java.util.Calendar.DAY_OF_MONTH, 12);
        Date endBCS = javaCal.getTime();
        calendar.addEvent(constructEvent(startBCS, endBCS, "4th Bhartiya Chhatra Sansad", "4th Bhartiya Chhatra Sansad",true));
        
        javaCal.set(java.util.Calendar.DAY_OF_MONTH, 26);
        Date indDay = javaCal.getTime();
        calendar.addEvent(constructEvent(indDay, indDay, "Independence Day!", "Independence Day!",true));
        
        //addCalendarEventListeners();
        
        v.addComponent(calendar);
    }
    
     private MUCEvent constructEvent(Date fromDate,Date toDate, String eventDescription, String eventCaption,boolean isAllDay)
    {
        MUCEvent event = new MUCEvent();
        event.setDescription(eventDescription);
        event.setAllDay(isAllDay);
        event.setCaption(eventCaption);        
        event.setStart(fromDate);        
        event.setEnd(toDate);
        event.setStyleName("orangeBg");
        return event;
        
    }

    private void addCalendarEventListeners() {
        // Register week clicks by changing the schedules start and end dates.
        calendar.setHandler(new BasicWeekClickHandler() {

            @Override
            public void weekClick(final WeekClick event) {
                // let BasicWeekClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.weekClick(event);
                //switchToWeekView();
            }
        });

        calendar.setHandler(new EventClickHandler() {
            @Override
            public void eventClick(final EventClick event) {
                //showEventPopup(event.getCalendarEvent(), false);
            }
        });

        calendar.setHandler(new BasicDateClickHandler() {
            @Override
            public void dateClick(final DateClickEvent event) {
                // let BasicDateClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.dateClick(event);
                //switchToDayView();
            }
        });

        calendar.setHandler(new RangeSelectHandler() {

            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                //handleRangeSelect(event);
            }
        });
    }
    
    

//    private List<ForumEventDetailsBean> getForumDetailList() {
//        try {
//            Client client = Client.create();
//            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_All_FORUM_EVENTS));
//            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
//            JSONObject inputJson = new JSONObject();
//             try
//             {           
//                 if (forumTable == null) {
//                     inputJson.put("fetchResultsFrom", 0);
//                 } else {
//                     inputJson.put("fetchResultsFrom", (forumTable.size() - 1));
//                 }
////                inputJson.put("username",userprofile.getUsername());
//             }catch(Exception ex){
//                 ex.printStackTrace();
//             }
//            
//            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
//            
//            JSONObject outNObject = null;
//            String output = response.getEntity(String.class);
//            outNObject = new JSONObject(output);
//
//            java.lang.reflect.Type listType = new TypeToken<ArrayList<ForumEventDetailsBean>>() {
//            }.getType();
//    
//            Gson gson=  new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();
//            
//            forumEventDetailsList=gson.fromJson(outNObject.getString(GlobalConstants.eventDetailsList), listType);
//            if(forumEventDetailsList.size()>0)
//            {
//                wrapperList.add(forumEventDetailsList);
//            }
//            
//            
//        } catch (JSONException ex) 
//        {
//            ex.printStackTrace();
//          //  L.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return forumEventDetailsList;
//    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) 
    {
        Component c = event.getComponent();
        
//        if(c instanceof ForumDetailWraper)
//        {
//            ForumDetailWraper activityWraper =(ForumDetailWraper) event.getComponent();
//            ForumEventDetailsBean eventDtls = (ForumEventDetailsBean)activityWraper.getData();
//            fetchEventLikesAndComments(eventDtls);
//            ViewEventDetailsWindow w = new ViewEventDetailsWindow();
//            UI.getCurrent().addWindow(w.doConstructorsWorKForReflection(eventDtls, eventLikesList, eventCommentsList));
//        }
//         else if (c instanceof LoadEarlierBtnWraper) {
//            getForumDetailList();
//
//            System.out.println("&^^^^^^^^^^^&^&^&^&^ Button Clicked&^&^&^&^&^&^&^^&");
//            forumTable.removeAllItems();
//            addForumEventsInToTable();
//
//        }
        
    } 

  /*   private void addForumEventsInToTable() 
    {
        for(List<ForumEventDetailsBean> eventDetailsList:wrapperList)
        {
            for(ForumEventDetailsBean eventDetails:eventDetailsList)
            {
                forumTable.addItem(new Object[]{new ForumDetailWraper(eventDetails,this) },forumTable.size()+1);
            }            
        }
        forumTable.addItem(new Object[]{loadMoreWraper},forumTable.size()+1);
    } */
    
//    private List<EventLikeBean> eventLikesList;
//    private List<EventCommentsBean> eventCommentsList;
//    
//    private void fetchEventLikesAndComments(ForumEventDetailsBean eventDtls) {
//        try {
//            JSONObject input = new JSONObject();
//            input.put("event_id", eventDtls.getEventDetailId());
//
//            Client client = Client.create();
//            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.FETCH_EVENT_LIKES_BY_ID));
//            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, input);
//
//            /*
//             * if (response.getStatus() != 201) { throw new
//             * RuntimeException("Failed : HTTP error code : " +
//             * response.getStatus()); }
//             */
//
//            JSONObject outNObject = null;
//            String output = response.getEntity(String.class);
//            outNObject = new JSONObject(output);
//
//             Type listType1 = new TypeToken<ArrayList<EventLikeBean>>() {
//            }.getType();
//            
//             Gson eventLikesGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
//            eventLikesList = eventLikesGson.fromJson(outNObject.getString(GlobalConstants.eventLikes), listType1);
//            
//            Type listType2 = new TypeToken<ArrayList<EventCommentsBean>>() {
//            }.getType();
//            
//             Gson eventCommentsGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
//            eventCommentsList = eventCommentsGson.fromJson(outNObject.getString(GlobalConstants.eventComments), listType2);
//            
//            //System.out.println("eve"+eventLikesList);
//            
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//
//    }
}
