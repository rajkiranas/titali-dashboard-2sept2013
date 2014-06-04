/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.planner;


import com.quick.bean.AppointmentMstBean;
import com.quick.bean.QuickLearn;
import com.quick.bean.Userprofile;
import com.quick.data.MasterDataProvider;
import com.quick.entity.AppointmentMst;
import com.quick.entity.Std;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.addon.calendar.event.BasicEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.vaadin.dialogs.ConfirmDialog;



/**
 *
 * @author sonalis
 */
public class PlannerEventFilter extends Window implements Property.ValueChangeListener {

    private VerticalLayout baseLayout;
    private DateField startTime;
    private DateField endTime;
    private TextField eventCaption;
    private TextArea eventDesc;
    private ComboBox stdCombo;
    private ComboBox divCombo;
    private Button saveEvent;
    private Button deleteEvent;
    private Button cancelEvent;
    private Date startDate;
    private Date endDate;
    private GregorianCalendar calstartTime;
    private GregorianCalendar calendTime;
    private GregorianCalendar weekstartTime;
    private GregorianCalendar weekendTime;
//    private ScheduleAppointmentsOfEmp empAppoinments;
    private ComboBox eventColour;
    private static final List<String> colourList = Arrays.asList(new String[]{"Orange", "Blue"});
    private static final HashMap<String,String> colorToStyleMap=new HashMap<String,String>();
    private static final HashMap<String,String> styleToColorMap=new HashMap<String,String>();
    
    private static final List<Integer> hrList = Arrays.asList(new Integer[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});
    //private static final List<Integer> minList = Arrays.asList(new Integer[]{00, 10, 15, 30, 45});
    private static final List<Integer> minList = Arrays.asList(new Integer[]{00, 01, 02, 03, 04, 05, 10, 15, 30, 45});
    private boolean isAllDay = true;
    private int custId;
    private int empid;
    Calendar cal = Calendar.getInstance();
    private BasicEvent basicEvent;
    private AppointmentMst appointmentMst;
    private Label lblTimeSlot;
    private boolean isWeekEvent;
    private ComboBox cmbPaitent;
    private ComboBox cmbDoctor;
    String colourCode;
    private ComboBox endTimeHr;
    private ComboBox endTimeMin;
    private ComboBox startTimeHr;
    private ComboBox startTimeMin;
    private Button deleteEventBtn;
    private AbsoluteLayout timeLayout;
    private boolean isUpdate = false;
    private boolean isReschedule = false;
    private String username;
    private String msgToUserStr;
    byte[] encrptedByArray = null;
    byte[] original = null;
    public static String KEY = "sateri@gmail.com";
    public static String IV = "initialvector123";
    String emailId;
    private HashMap emailIdMap;
    private File appointmentiCalFile;
    private Userprofile loggedInUserProfile;
    private PlannerView view;
    private HashMap<String,AppointmentMstBean> plannerEventMap;
    private static final String All="All";
    private long eventIdForEditDeleteBean;
    private boolean isEditDeleteView=false;

    static
    {
        colorToStyleMap.put("Orange", "orangeBg");
        colorToStyleMap.put("Blue", "blueFbBg");
        
        styleToColorMap.put("orangeBg", "Orange");
        styleToColorMap.put("blueFbBg", "Blue");
    }

    public PlannerEventFilter() {
    }
    /* Added by suyog for c-cure healthcare changes*/

    //new event
    public PlannerEventFilter(Date date, Userprofile loggedInUserProfile, PlannerView view) {
        this.view=view;
        this.loggedInUserProfile=loggedInUserProfile;
        this.startDate = date;
        this.endDate = date;
        setCaption("New planner event");
        setModal(true);
        center();
        setWidth("35%");
        setHeight("80%");
        buildMainLayout();
    }
    
    //view popup for existing event
    public PlannerEventFilter(MUCEvent basicEvent, Userprofile loggedInUserProfile, PlannerView view, HashMap<String,AppointmentMstBean> plannerEventMap) {
        this.view=view;
        this.loggedInUserProfile=loggedInUserProfile;
        this.basicEvent=basicEvent;
        this.plannerEventMap=plannerEventMap;
        this.isEditDeleteView=true;
        setCaption("Planner event");
        setModal(true);
        center();
        setWidth("35%");
        setHeight("80%");        
        buildMainLayout();
        setFormData(basicEvent);
    }

//    public PlannerEventFilter(Date date, ScheduleAppoinments appoinments, int custId, int empid, boolean isWeekEvent, MyApplication app, HashMap emailIdMap) {
//        this.emailIdMap = emailIdMap;
//        this.app = app;
//        this.startDate = date;
//        this.endDate = date;
//        this.appoinments = appoinments;
//        this.custId = custId;
//        this.empid = empid;
//        this.isWeekEvent = isWeekEvent;
//        setCaption("Schedule Appointment");
//        setModal(true);
//        center();
//        setWidth("360px");
//        setHeight("400px");
//        buildMainLayout();
//    }
    // Added to show events on admin login

//    public PlannerEventFilter(Date date, ScheduleAppEmp empAppoinments, int empid, boolean isWeekEvent, List<CustMst> custList, List<Ofuser> userList, MyApplication app) {
//        this.app = app;
//        this.startDate = date;
//        this.endDate = date;
//        this.empAppoinments = empAppoinments;
//        this.custId = custId;
//        this.empid = empid;
//        this.isWeekEvent = isWeekEvent;
//        this.custList = custList;
//        this.doctorList = userList;
//        setCaption("Schedule Appointment");
//        setModal(true);
//        center();
//        setWidth("360px");
//        setHeight("400px");
//        buildEmpEventLayout();
//    }

//    public PlannerEventFilter(BasicEvent event, ScheduleAppoinments appoinments, int custId, String username, boolean isWeekEvent) {
//        this.basicEvent = event;
//        this.appoinments = appoinments;
//        this.custId = custId;
//        this.username = username;
//        this.isWeekEvent = isWeekEvent;
//        setCaption("Delete Appointment");
//        setModal(true);
//        center();
//        setWidth("360px");
//        setHeight("400px");
//        buildDeleteLayout();
//    }
    /* For Delete and Update */

    

    private void buildMainLayout() {
        isAllDay = true;
        baseLayout = new VerticalLayout();
        baseLayout.setSpacing(true);
        baseLayout.setMargin(true);
        baseLayout.setSizeFull();

        FormLayout eventForm = new FormLayout();
        eventForm.setSizeFull();
        eventForm.setSpacing(true);
        
        startTime = new DateField("Start date");
        startTime.setRequired(true);
        startTime.setDateFormat(GlobalConstants.DATEFORMAT);
        startTime.setValue(startDate);
        startTime.setRequiredError("Please select start date");
        
        endTime = new DateField("End date");
        endTime.setRequired(true);
        endTime.setDateFormat(GlobalConstants.DATEFORMAT);
        endTime.setValue(startDate);
        endTime.setRequiredError("Please select end date");
        

        eventCaption = new TextField("Caption");
        eventCaption.setRequired(true);

        eventDesc=new TextArea("Description");
        eventDesc.setRequired(true);
        
        stdCombo = new ComboBox("Standard");
        stdCombo.setImmediate(true);
        stdCombo.setInputPrompt("Standard");
        stdCombo.addItem("Select");
        stdCombo.setValue("Select"); 
        stdCombo.setNullSelectionAllowed(false);
        stdCombo.setRequired(true);
        
        divCombo = new ComboBox("Division");
        divCombo.setImmediate(true);
        divCombo.setInputPrompt("Division");
        divCombo.addItem("Select");
        divCombo.setValue("Select");
        
        
        List<Std> stdList = MasterDataProvider.getStandardList();
        for(Std s: stdList)
        {
            stdCombo.addItem(s.getStd());
        }
        stdCombo.addItem(All);
        stdCombo.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(!stdCombo.getValue().equals("Select") && !stdCombo.getValue().equals(All)){
                    String std=String.valueOf(stdCombo.getValue());
                    List<QuickLearn> divBeanList  = MasterDataProvider.getDivisionBystd(std);
                    
                    if(!divBeanList.isEmpty())
                    {
                       for(QuickLearn bean : divBeanList)
                       {
                            divCombo.addItem(bean.getFordiv());
                       }
                    }     
               }
            }
        });

        
        eventColour = new ComboBox("Colour", colourList);
        eventColour.setNullSelectionAllowed(false);
        eventColour.setRequired(true);
        

        eventForm.addComponent(startTime);
        eventForm.addComponent(endTime);
        eventForm.addComponent(eventCaption);
        eventForm.addComponent(eventDesc);
        eventForm.addComponent(stdCombo);
        eventForm.addComponent(divCombo);
        eventForm.addComponent(eventColour);

        baseLayout.addComponent(eventForm);

        


        saveEvent = new Button("Save");
        saveEvent.setImmediate(true);
        saveEvent.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) 
            {
                if(validateEventForm())
                {
                    savePlannerEvent();
                    //MUCEvent e = view.constructEvent(startTime.getValue(), endTime.getValue(), eventDesc.getValue(), eventCaption.getValue(), true, colorMap.get(eventColour.getValue().toString()));
                    //view.addEventToCalendar(e);
                    getUI().getNavigator().navigateTo(GlobalConstants.ROUT_PLANNER);
                    closePopup();
                    
                }
            }
        });
        
        
        if(isEditDeleteView)
        {
            deleteEvent = new Button("Delete");
            deleteEvent.setImmediate(true);
            deleteEvent.addClickListener(new Button.ClickListener() {
                public void buttonClick(ClickEvent event) 
                {
                    if(validateEventForm())
                    {
                        ConfirmDialog dia =ConfirmDialog.show(getUI(), "Confirm delete", "Are you sure, you want to delete this event ?", "YES", "NO", new ConfirmDialog.Listener() {
            
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) 
                            {
                                deletePlannerEvent();
                                getUI().getNavigator().navigateTo(GlobalConstants.ROUT_PLANNER);
                                closePopup();
                            }
                            // else {
//                                //isBlankMsg = false;
//                            }
                        }
            });
                        dia.setWidth("33%");
                        dia.setHeight("15%");
                        Component c = dia.getParent();
                        c.setHeight("90%");
                    }
                }
            });
            
        }


        cancelEvent = new Button("Close");
        cancelEvent.setImmediate(true);
        cancelEvent.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                closePopup();
            }
        });
        

        
        
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        //buttons.setMargin(new MarginInfo(false, false, false, true));
        buttons.setMargin(false);
        
        if(!loggedInUserProfile.getRole().equals(GlobalConstants.student))
        {
            buttons.addComponent(saveEvent);
            if(deleteEvent!=null)
                buttons.addComponent(deleteEvent);            
        }
        
        buttons.addComponent(cancelEvent);        
        
        eventForm.addComponent(buttons);
        eventForm.setComponentAlignment(buttons, Alignment.MIDDLE_LEFT);
        
        
        setContent(baseLayout);
    }

    private void buildEmpEventLayout() {
        isAllDay = true;
        baseLayout = new VerticalLayout();
        baseLayout.setSpacing(true);
        baseLayout.setWidth("360px");
        baseLayout.setHeight("310px");

        Form eventForm = new Form();
        eventForm.setHeight("310px");
        startTime = new DateField("Date");
        startTime.setDateFormat(GlobalConstants.DATEFORMAT);
        startTime.setValue(startDate);
        startTime.setWidth("180px");
        startTime.setRequired(true);
        startTime.setImmediate(true);
        startTime.setRequiredError("Please select date");
        startTime.addListener(new DateField.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                //isValidStartDt();
                
            }
        });

        cmbPaitent.setImmediate(true);
        cmbPaitent.setNullSelectionAllowed(false);
        cmbPaitent.setRequired(true);
//        cmbCustomer.addListener((Property.ValueChangeListener)this);

        

        cmbDoctor.setImmediate(true);
        cmbDoctor.setRequired(true);
        cmbDoctor.setNullSelectionAllowed(false);

        



        eventCaption = new TextField("Caption");
        eventCaption.setWidth("180px");
        eventCaption.setRequired(true);

//        eventDesc=new TextArea("Description");
//        eventDesc.setWidth("180px");

        eventColour = new ComboBox("Colour", colourList);
        eventColour.setNullSelectionAllowed(false);
        eventColour.setWidth("180px");

        eventForm.getLayout().addComponent(startTime);

        isAllDay = false;
        lblTimeSlot = new Label();
        lblTimeSlot.setCaption("Time Slot");
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa");

        timeLayout = new AbsoluteLayout();
        timeLayout.setHeight("65px");
        timeLayout.setWidth("180px");
        timeLayout.setCaption("Time *");


        HorizontalLayout startTimeLayout = new HorizontalLayout();
        startTimeLayout.setCaption("Time");

//            startTimeLayout.setSpacing(true);
        Label fromLbl = new Label("From:");
        Label toLbl = new Label("To:  ");
        Label startHrLbl = new Label("H");
        Label startMinLbl = new Label("M");
        Label endHrLbl = new Label("H");
        Label endMinLbl = new Label("M");
        calstartTime = new GregorianCalendar();
        calendTime = new GregorianCalendar();

        weekstartTime = new GregorianCalendar();
        weekendTime = new GregorianCalendar();

        calstartTime.setTime(startDate);
        weekstartTime.setTime(calstartTime.getTime());

        calendTime.setTime(startDate);
        calendTime.add(java.util.Calendar.HOUR, 1);
        weekendTime.setTime(calendTime.getTime());

        startTimeHr = new ComboBox("", hrList);
        startTimeHr.setWidth("50px");
        // startTimeHr.setRequired(true);
        startTimeMin = new ComboBox("", minList);
        startTimeMin.setWidth("50px");
        startTimeMin.setValue(00);

        endTimeHr = new ComboBox("", hrList);
        endTimeHr.setWidth("50px");
        // endTimeHr.setRequired(true);
        endTimeMin = new ComboBox("", minList);
        endTimeMin.setWidth("50px");
        endTimeMin.setValue(00);



        timeLayout.addComponent(fromLbl, "top:5px;left:0px");
        timeLayout.addComponent(startTimeHr, "top:5px;left:40px;");
        timeLayout.addComponent(startHrLbl, "top:5px;left:95px;");
        timeLayout.addComponent(startTimeMin, "top:5px;left:115px;");
        timeLayout.addComponent(startMinLbl, "top:5px;left:170px;");

        timeLayout.addComponent(toLbl, "top:40px;left:0px");
        timeLayout.addComponent(endTimeHr, "top:40px;left:40px;");
        timeLayout.addComponent(endHrLbl, "top:40px;left:95px;");
        timeLayout.addComponent(endTimeMin, "top:40px;left:115px;");
        timeLayout.addComponent(endMinLbl, "top:40px;left:170px;");




        eventForm.getLayout().addComponent(timeLayout);
        //eventForm.getLayout().addComponent(endDt);
        eventForm.getLayout().addComponent(cmbPaitent);
        eventForm.getLayout().addComponent(cmbDoctor);
        eventForm.getLayout().addComponent(eventCaption);
        // eventForm.getLayout().addComponent(eventDesc);
        eventForm.getLayout().addComponent(eventColour);

        baseLayout.addComponent(eventForm);

        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth("100%");
        footer.setMargin(new MarginInfo(false, false, false, true));

        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.setMargin(false);
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(false, false, false, true));
//        buttons.setSizeUndefined();


        saveEvent = new Button("Apply");
        saveEvent.setImmediate(true);
        saveEvent.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                try {
                    createEmpEvent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        buttons.addComponent(saveEvent);


        cancelEvent = new Button("Cancel");
        cancelEvent.setImmediate(true);
        cancelEvent.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                closePopup();
            }
        });
        buttons.addComponent(cancelEvent);


        footer.addComponent(buttons);
        footer.setComponentAlignment(buttons, Alignment.BOTTOM_LEFT);

        eventForm.getLayout().addComponent(footer);
//        baseLayout.addComponent(footer);
        //addComponent(baseLayout);

    }

    
    
    private void savePlannerEvent() 
    {
        if (validateEventForm()) 
        {
            try 
            {
                Client client = Client.create();
                WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_PLANNER_EVENT));
                //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
                JSONObject input = new JSONObject();
                try 
                {
                    
                    input.put("eventIdForEditDeleteBean", eventIdForEditDeleteBean);
                    input.put("startTime", startTime.getValue().getTime());
                    input.put("endTime", endTime.getValue().getTime());
                    String caption = String.valueOf(eventCaption.getValue());
                    input.put("caption", caption);
                    String desc = String.valueOf(eventDesc.getValue());
                    input.put("desc", desc);

                    String forWhom=stdCombo.getValue().toString();
                    if (!divCombo.getValue().toString().equals("Select")) {
                        forWhom=forWhom+GlobalConstants.HASH + divCombo.getValue().toString();
                    }
                    input.put("for_whom", forWhom);
                    
                    input.put("event_style", colorToStyleMap.get(eventColour.getValue().toString()));
                    input.put("isallday", true);
                    
                    input.put("owner_name", loggedInUserProfile.getName());
                    input.put("owner_username", loggedInUserProfile.getUsername());
                    

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, input);

                JSONObject outNObject = null;
                String output = response.getEntity(String.class);
                outNObject = new JSONObject(output);

                ShowNotification(outNObject.getString(GlobalConstants.STATUS));

            } catch (JSONException ex) {
                ex.printStackTrace();
                ShowNotification("Save planner event failed");
            }
        }
    }
    
    private void deletePlannerEvent() 
    {
        if (validateEventForm()) 
        {
            try 
            {
                Client client = Client.create();
                WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DELETE_PLANNER_EVENT));
                //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
                JSONObject input = new JSONObject();
                try 
                {
                    
                    input.put("eventIdForEditDeleteBean", eventIdForEditDeleteBean);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, input);

                JSONObject outNObject = null;
                String output = response.getEntity(String.class);
                outNObject = new JSONObject(output);

                ShowNotification(outNObject.getString(GlobalConstants.STATUS));

            } catch (JSONException ex) {
                ex.printStackTrace();
                ShowNotification("Save planner event failed");
            }
        }
    }

    public AppointmentMst setAppointmentDtls() {

        appointmentMst = new AppointmentMst();

        

        String caption = String.valueOf(eventCaption.getValue());
        appointmentMst.setEventCaption(caption);
        String desc = "Appointment-" + cmbDoctor.getValue() + " meets " + cmbPaitent.getItemCaption(cmbPaitent.getValue()) + "(" + cmbPaitent.getValue() + ")";
        appointmentMst.setEventDescription(desc);

        
        cal.setTime((Date) startTime.getValue());

        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTimeHr.getValue().toString()));
        cal.set(Calendar.MINUTE, Integer.valueOf(startTimeMin.getValue().toString()));
        cal.set(Calendar.SECOND, 0);
        appointmentMst.setStarttime(cal.getTime());

        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTimeHr.getValue().toString()));
        cal.set(Calendar.MINUTE, Integer.valueOf(endTimeMin.getValue().toString()));
        cal.set(Calendar.SECOND, 0);
        appointmentMst.setEndtime(cal.getTime());
//       }
        appointmentMst.setEventStyle(colourCode);
        appointmentMst.setIsallday(isAllDay);

        return appointmentMst;
    }

//    private void buildDeleteLayout() {
//
//        baseLayout = new VerticalLayout();
//        baseLayout.setSpacing(true);
//        baseLayout.setWidth("410px");
//        baseLayout.setHeight("310px");
//
//        Form eventForm = new Form();
//        startDt = new DateField("Start Date");
//        //startDt.setDateFormat(GlobalConstants.DATEFORMAT);
//        startDt.setValue(basicEvent.getStart());
//        startDt.setWidth("180px");
//        startDt.setEnabled(false);
//
//
//        eventCaption = new TextField("Caption");
//        eventCaption.setWidth("180px");
//        eventCaption.setEnabled(false);
//        eventCaption.setValue(basicEvent.getCaption());
//
////        eventDesc=new TextArea("Description");
////        eventDesc.setWidth("180px");
////        eventDesc.setEnabled(false);
////        eventDesc.setValue(basicEvent.getDescription());
//
//
//        eventForm.getLayout().addComponent(startDt);
//
////        if(isWeekEvent)
////        {
//        isAllDay = false;
//        lblTimeSlot = new Label();
//        lblTimeSlot.setCaption("Time Slot");
//        SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa");
//
//        calstartTime = new GregorianCalendar();
//        calendTime = new GregorianCalendar();
//
//        calstartTime.setTime(basicEvent.getStart());
//
//        calendTime.setTime(basicEvent.getEnd());
//
//        lblTimeSlot.setValue(df.format(calstartTime.getTime()) + " To " + df.format(calendTime.getTime()));
//        eventForm.getLayout().addComponent(lblTimeSlot);
////        }
//        eventForm.getLayout().addComponent(eventCaption);
//        // eventForm.getLayout().addComponent(eventDesc);
//
//        baseLayout.addComponent(eventForm);
//
//        HorizontalLayout footer = new HorizontalLayout();
//        footer.setWidth("80%");
//        footer.setMargin(true);
//
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.setMargin(false);
//        buttons.setSpacing(true);
//        buttons.setSizeUndefined();
//
//
//        applyEvent = new Button("Delete");
//        applyEvent.setImmediate(true);
//        applyEvent.addListener(new Button.ClickListener() {
//            public void buttonClick(ClickEvent event) {
//                deleteEvent();
//                appoinments.deleteEvent();
//                closePopup();
//            }
//        });
//        buttons.addComponent(applyEvent);
//
//
//        cancelEvent = new Button("Cancel");
//        cancelEvent.setImmediate(true);
//        cancelEvent.addListener(new Button.ClickListener() {
//            public void buttonClick(ClickEvent event) {
//                closePopup();
//            }
//        });
//        buttons.addComponent(cancelEvent);
//
//
//        footer.addComponent(buttons);
//        footer.setComponentAlignment(buttons, Alignment.TOP_RIGHT);
//
//
//        baseLayout.addComponent(footer);
//        addComponent(baseLayout);
//
//    }
//
//    public void deleteEvent() {
//        List<AppointmentMst> appointmentMsts = scheduleAppointmentService.getAptmntsAsPerTime(username, basicEvent.getStart(), basicEvent.getEnd());
//
//        for (AppointmentMst list : appointmentMsts) {
//            scheduleAppointmentService.deleteAppointment(list);
//        }
//
//        //appoinments.setEventData();
//        getWindow().showNotification("Event deleted successfully");
//    }
//
//    public AppointmentMst getAptmntsAsPerTime(int empId, Date startDt, Date endDt) {
//        List<AppointmentMst> appointmentMsts = scheduleAppointmentService.getAptmntsAsPerTime(empId, startDt, endDt);
//        AppointmentMst appointment = new AppointmentMst();
//////        appointmentMstset.add(appointmentMst);
////        scheduleAppointmentService.deleteAppointment(appointmentMst);
//        for (AppointmentMst list : appointmentMsts) {
//            appointment = list;
//
//        }
//
//        return appointment;
//    }
//
//    public AppointmentMst getAptmntsAsPerDate(int empId, Date startDt, Date endDt) {
//        List<AppointmentMst> appointmentMsts = scheduleAppointmentService.getAptmntsAsPerDate(empId, startDt, endDt);
//        AppointmentMst appointment = new AppointmentMst();
//////        appointmentMstset.add(appointmentMst);
////        scheduleAppointmentService.deleteAppointment(appointmentMst);
//        for (AppointmentMst list : appointmentMsts) {
//            appointment = list;
//            //System.out.println("list"+list.getAppointmentId());
////            scheduleAppointmentService.deleteAppointment(list);
//        }
////
////        //appoinments.setEventData();
////        getWindow().showNotification("Event deleted successfully");
//        return appointment;
//    }

    private void closePopup() {
        this.close();
    }

    private void createEmpEvent() throws Exception {
        Map map = new HashMap();
        Calendar calanderStartDate = Calendar.getInstance();
        Calendar calanderEndDate = Calendar.getInstance();
        if (validateForm()) {

//        if(isWeekEvent)
//        {
//            calanderStartDate=weekstartTime.getTime();
//            calanderEndDate=weekendTime.getTime();
//        }
//        else
//        {

            username = GlobalConstants.emptyString + cmbDoctor.getValue();
//            System.out.println("In CreateEmpEvent Method");

            calanderStartDate.setTime((Date) startTime.getValue());
            calanderEndDate.setTime((Date) startTime.getValue());

            calanderStartDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTimeHr.getValue().toString()));
            calanderStartDate.set(Calendar.MINUTE, Integer.valueOf(startTimeMin.getValue().toString()));
            calanderStartDate.set(Calendar.SECOND, 0);
            calanderEndDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTimeHr.getValue().toString()));
            calanderEndDate.set(Calendar.MINUTE, Integer.valueOf(endTimeMin.getValue().toString()));
            calanderEndDate.set(Calendar.SECOND, 0);

//        }

            //Date calanderEndDate=(Date) endDt.getValue();
            //Date calanderEndDate=(Date) endDt.getValue();
            String caption = String.valueOf(eventCaption.getValue());
            //   String desc=String.valueOf(eventDesc.getValue());
            String desc = "Appointment-" + cmbDoctor.getValue() + " meets " + cmbPaitent.getItemCaption(cmbPaitent.getValue()) + "(" + cmbPaitent.getValue() + ")";
            colourCode = String.valueOf(eventColour.getValue());
            username = "" + cmbDoctor.getValue();
            custId = Integer.parseInt(cmbPaitent.getValue().toString());
            ////if ((isEventBooked(calanderStartDate.getTime(), calanderEndDate.getTime()))) {
                String loggedInUser = null;
                loggedInUser = loggedInUser.substring(0, loggedInUser.indexOf(GlobalConstants.AT_THE_RATE));
                //  System.out.println("loggedInUser=" + loggedInUser);
                if (loggedInUser.equalsIgnoreCase(cmbDoctor.getValue().toString())) {
                    //empAppoinments.createEvent(calanderStartDate.getTime(), calanderEndDate.getTime(), caption, desc, colourCode, isAllDay);
                }

                //scheduleAppointmentService.saveAppointment(setAppointmentDtls());
                map.put("caption", caption);
                map.put("desc", desc);
                map.put("calanderStartDate", calanderStartDate);
                map.put("calanderEndDate", calanderEndDate);

                //appointmentiCalFile = ICalendarPrintICS.createICSFile(map);

                //  empAppoinments.getWindow().showNotification("Appointment has been scheduled successfully",Notification.TYPE_WARNING_MESSAGE); 
                closePopup();


                int id = Integer.parseInt(cmbPaitent.getValue().toString());
                String custEmailId = null;
                



                //custEmailId += GlobalConstants.AT_THE_RATE + GlobalConstants.SERVER;
                String userEmaiId = cmbDoctor.getValue().toString() + GlobalConstants.AT_THE_RATE ;
//                messageBodyFOrmat(custEmailId, userEmaiId, true);
//                messageBodyFOrmat(userEmaiId, custEmailId, false);
                appointmentiCalFile.delete();
           //// }
        }




    }

//    private boolean isEventBooked(Date calanderStartDate, Date calanderEndDate) {
//
//        //SimpleDateFormat df = new SimpleDateFormat(GlobalConstants.DATEFORMAT);
////        Date StartDate = new Date(df.format(calanderStartDate));
////        Date EndDate = new Date(df.format(calanderEndDate));
//
//        //   List<AppointmentMst> appointmentMsts=scheduleAppointmentService.getAptmntsAsPerDate(empid, StartDate, EndDate);
//        // List<AppointmentMst> appointmentMsts=scheduleAppointmentService.getAptmntsAsPerTime(username, StartDate, EndDate);
//        //   username = ""+cmbDoctor.getValue();
//        // username = ""+cmbDoctor.getValue();
//        // List<AppointmentMst> appointmentMsts=scheduleAppointmentService.getAppointmentsOfEmp(username);
//        List<AppointmentMst> appointmentMsts = scheduleAppointmentService.getAptmntsAsPerTime(username, calanderStartDate, calanderEndDate);
//        // List<AppointmentMst> appointmentMsts=scheduleAppointmentService.getAptmntsAsPerTime(empid,calanderStartDate,calanderEndDate);
//
//        //  List<AppointmentMst> appointmentMsts=scheduleAppointmentService.getAppointmentsAsPerEmp(empid, custId);
//        /*  for (AppointmentMst list : appointmentMsts) {
//         // if hours of start time and end time matches
//            
//         if ((list.getStarttime().getHours() == calanderStartDate.getHours()) || (list.getEndtime().getHours() == calanderEndDate.getHours())
//         || (list.getEndtime().getHours() == calanderStartDate.getHours()) || (list.getStarttime().getHours() == calanderEndDate.getHours())) {
//         ShowNotification("Appointment already booked");
//         return false;
//         } // if start time falling between previous appointments start and end time.
//         else if ((list.getStartdate().after(calanderStartDate)) && (list.getStartdate().before(calanderStartDate)) && (list.getStartdate().before(calanderEndDate)) && (list.getStartdate().after(calanderEndDate))) {
//         return false;
//         } else if ((calanderEndDate.getHours() > list.getStarttime().getHours()) && (calanderEndDate.getHours() < list.getEndtime().getHours())) {
//         return false;
//         }
//         }  */
//
//        if (appointmentMsts.size() > 0) {
//            ShowNotification("Appointment already booked");
//            return false;
//        }
//
//        return true;
//
//    }

//    private void buildUpdateLayout() {
//
//        baseLayout = new VerticalLayout();
//        baseLayout.setSpacing(true);
//        baseLayout.setWidth("360px");
//        baseLayout.setHeight("310px");
//
//        Form eventForm = new Form();
//        eventForm.setHeight("310px");
//        startDt = new DateField("Start Date");
//        startDt.setDateFormat(GlobalConstants.DATEFORMAT);
//        startDt.setValue(basicEvent.getStart());
//        startDt.setWidth("180px");
//        startDt.addListener((Property.ValueChangeListener) this);
//
//        eventCaption = new TextField("Caption");
//        eventCaption.setWidth("180px");
////        eventCaption.setEnabled(false);
//        eventCaption.setValue(basicEvent.getCaption());
//
////        eventDesc=new TextArea("Description");
////        eventDesc.setWidth("180px");
//////        eventDesc.setEnabled(false);
////        eventDesc.setValue(basicEvent.getDescription());
//
//
//        eventColour = new ComboBox("Colour", colourList);
//        eventColour.setNullSelectionAllowed(false);
//        eventColour.setWidth("180px");
////        eventColour.setEnabled(false);
//        if (basicEvent.getStyleName() != null) {
//            if (basicEvent.getStyleName().equalsIgnoreCase("redbg")) {
//                eventColour.setValue("Red");
//            } else if (basicEvent.getStyleName().equalsIgnoreCase("greenbg")) {
//                eventColour.setValue("Green");
//            } else if (basicEvent.getStyleName().equalsIgnoreCase("purpleBg")) {
//                eventColour.setValue("Purple");
//            } else if (basicEvent.getStyleName().equalsIgnoreCase("grayBg")) {
//                eventColour.setValue("Gray");
//            }
//            /* else if (basicEvent.getStyleName().equalsIgnoreCase("grayBg")) {
//             eventColour.setValue("Gray");
//             } */
//        }
//        eventForm.getLayout().addComponent(startDt);
//
////        if(isWeekEvent)
////        {
//        isAllDay = false;
//        lblTimeSlot = new Label();
//        lblTimeSlot.setCaption("Time Slot");
//        SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa");
//
//        calstartTime = new GregorianCalendar();
//        calendTime = new GregorianCalendar();
//
//        calstartTime.setTime(basicEvent.getStart());
//
//        calendTime.setTime(basicEvent.getEnd());
//
//        lblTimeSlot.setValue(df.format(calstartTime.getTime()) + " To " + df.format(calendTime.getTime()));
//
//        AppointmentMst appointmentMst = getAptmntsAsPerTime(empid, basicEvent.getStart(), basicEvent.getEnd());
//        if (appointmentMst.getCustMst() != null) {
//            custId = appointmentMst.getCustMst().getCustId();
//        }
//
//
//        cmbPaitent = new ComboBox(GlobalConstants.getProperty(GlobalConstants.CUSTOMER));
//        cmbPaitent.setImmediate(true);
//        cmbPaitent.setNullSelectionAllowed(false);
//
//        for (CustMst custNameList : custList) {
//
//            cmbPaitent.addItem(custNameList.getCustId());
//            cmbPaitent.setItemCaption(custNameList.getCustId(), custNameList.getCustName());
//            if (custNameList.getCustId() == custId) {
//                cmbPaitent.setValue(custNameList.getCustId());
//            }
//        }
//
//        cmbDoctor = new ComboBox(GlobalConstants.getProperty(GlobalConstants.USER));
//        cmbDoctor.setImmediate(true);
//        cmbDoctor.setNullSelectionAllowed(false);
//
//        for (Ofuser doctors : doctorList) {
//
//            cmbDoctor.addItem(doctors.getUsername());
//            cmbDoctor.setItemCaption(doctors.getUsername(), doctors.getUsername());
//
//        }
//
//
//
//        timeLayout = new AbsoluteLayout();
//        timeLayout.setHeight("65px");
//        timeLayout.setWidth("180px");
//        timeLayout.setCaption("Time");
//
//
//        Label fromLbl = new Label("From:");
//        Label toLbl = new Label("To:  ");
//        Label startHrLbl = new Label("H");
//        Label startMinLbl = new Label("M");
//        Label endHrLbl = new Label("H");
//        Label endMinLbl = new Label("M");
//
//
//        startTimeHr = new ComboBox("", hrList);
//        startTimeHr.setWidth("50px");
//        startTimeHr.setValue(basicEvent.getStart().getHours());
////            startTimeHr.setRequired(true);
//        startTimeMin = new ComboBox("", minList);
//        startTimeMin.setWidth("50px");
//        startTimeMin.setValue(basicEvent.getStart().getMinutes());
////            startTimeMin.setValue(00);
//
//        endTimeHr = new ComboBox("", hrList);
//        endTimeHr.setWidth("50px");
//        endTimeHr.setValue(basicEvent.getEnd().getHours());
////            endTimeHr.setRequired(true);
//        endTimeMin = new ComboBox("", minList);
//        endTimeMin.setWidth("50px");
//        endTimeMin.setValue(basicEvent.getEnd().getMinutes());
////            endTimeMin.setValue(00);
//
//
//
//        timeLayout.addComponent(fromLbl, "top:5px;left:0px");
//        timeLayout.addComponent(startTimeHr, "top:5px;left:40px;");
//        timeLayout.addComponent(startHrLbl, "top:5px;left:95px;");
//        timeLayout.addComponent(startTimeMin, "top:5px;left:115px;");
//        timeLayout.addComponent(startMinLbl, "top:5px;left:170px;");
//
//        timeLayout.addComponent(toLbl, "top:40px;left:0px");
//        timeLayout.addComponent(endTimeHr, "top:40px;left:40px;");
//        timeLayout.addComponent(endHrLbl, "top:40px;left:95px;");
//        timeLayout.addComponent(endTimeMin, "top:40px;left:115px;");
//        timeLayout.addComponent(endMinLbl, "top:40px;left:170px;");
//
//
//        eventForm.getLayout().addComponent(timeLayout);
//
//
//        //eventForm.getLayout().addComponent(endDt);
//        eventForm.getLayout().addComponent(cmbPaitent);
//        eventForm.getLayout().addComponent(cmbDoctor);
//        eventForm.getLayout().addComponent(eventCaption);
//        // eventForm.getLayout().addComponent(eventDesc);
//        if (basicEvent.getStyleName() != null) {
//            eventForm.getLayout().addComponent(eventColour);
//        }
//
//        baseLayout.addComponent(eventForm);
//
//        HorizontalLayout footer = new HorizontalLayout();
//        footer.setWidth("100%");
////        footer.setMargin(true);
//
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.setMargin(false);
//        buttons.setSpacing(true);
////        buttons.setSizeUndefined();
//
//
//        applyEvent = new Button("Update");
//        applyEvent.setImmediate(true);
//        applyEvent.addListener(new Button.ClickListener() {
//            public void buttonClick(ClickEvent event) {
//                updateEmpEvent();
//
//            }
//        });
//        buttons.addComponent(applyEvent);
//
//        deleteEventBtn = new Button("Delete");
//        deleteEventBtn.setImmediate(true);
//        deleteEventBtn.addListener(new Button.ClickListener() {
//            public void buttonClick(ClickEvent event) {
//                deleteEvent();
//                empAppoinments.deleteEvent(basicEvent);
//                closePopup();
//            }
//        });
//        buttons.addComponent(deleteEventBtn);
//
//        cancelEvent = new Button("Cancel");
//        cancelEvent.setImmediate(true);
//        cancelEvent.addListener(new Button.ClickListener() {
//            public void buttonClick(ClickEvent event) {
//                closePopup();
//            }
//        });
//        buttons.addComponent(cancelEvent);
//
//
//        footer.addComponent(buttons);
//        footer.setComponentAlignment(buttons, Alignment.BOTTOM_LEFT);
//
//        eventForm.getLayout().addComponent(footer);
////        baseLayout.addComponent(footer);
//        addComponent(baseLayout);
//
//    }

//    private void updateEmpEvent() {
//        Calendar calanderStartDate = Calendar.getInstance();
//        Calendar calanderEndDate = Calendar.getInstance();
//        if (validateForm()) {
//
//
//            calanderStartDate.setTime((Date) startDt.getValue());
//            calanderEndDate.setTime((Date) startDt.getValue());
//
//            calanderStartDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTimeHr.getValue().toString()));
//            calanderStartDate.set(Calendar.MINUTE, Integer.valueOf(startTimeMin.getValue().toString()));
//            calanderStartDate.set(Calendar.SECOND, 0);
//            calanderEndDate.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTimeHr.getValue().toString()));
//            calanderEndDate.set(Calendar.MINUTE, Integer.valueOf(endTimeMin.getValue().toString()));
//            calanderEndDate.set(Calendar.SECOND, 0);
//
////        }
//
//            //Date calanderEndDate=(Date) endDt.getValue();
//            //Date calanderEndDate=(Date) endDt.getValue();
//            String caption = String.valueOf(eventCaption.getValue());
//            // String desc=String.valueOf(eventDesc.getValue());
//            String desc = "Appointment-" + cmbDoctor.getValue() + " meets " + cmbPaitent.getItemCaption(cmbPaitent.getValue()) + "(" + cmbPaitent.getValue() + ")";
//            colourCode = String.valueOf(eventColour.getValue());
//
//            if ((isReschedule)) {
//                if ((isEventBooked(calanderStartDate.getTime(), calanderEndDate.getTime()))) {
//                    empAppoinments.deleteEvent(basicEvent);
//                    deleteEvent();
//                    empAppoinments.createEvent(calanderStartDate.getTime(), calanderEndDate.getTime(), caption, desc, colourCode, isAllDay);
//
//                    custId = Integer.parseInt(cmbPaitent.getValue().toString());
//
//                    scheduleAppointmentService.saveAppointment(setAppointmentDtls());
//
//                    empAppoinments.ShowNotification("Appointment updated successfully");
//                    closePopup();
//                }
//            }
//            if (!(isReschedule)) {
//                empAppoinments.deleteEvent(basicEvent);
//                deleteEvent();
//                empAppoinments.createEvent(calanderStartDate.getTime(), calanderEndDate.getTime(), caption, desc, colourCode, isAllDay);
//
//                custId = Integer.parseInt(cmbPaitent.getValue().toString());
//
//                scheduleAppointmentService.saveAppointment(setAppointmentDtls());
//
//                empAppoinments.ShowNotification("Event updated successfully");
//                closePopup();
//            }
//
//        }
////        else
////        {
////            getWindow().showNotification("Event creation failed",2);
////            return;
////        }
//
//
//
//
//    }

    private boolean validateForm() {
        if (startTime.getValue() == null) {
            ShowNotification("Please select event date");
            return false;
        } /* else if (!DateUtil.ComapreDate((Date) startDt.getValue())) {
            ShowNotification("Start date should be greater than or equal to current date");
            return false;
        } */ else if (startTimeHr.getValue() == null) {
            ShowNotification("Please select from time");
            return false;
        } else if (endTimeHr.getValue() == null) {
            ShowNotification("Please select to time");
            return false;
        } else if (!validateEventTime()) {
            ShowNotification("<h3>Start time should not be greater than or equal to end time</h3>");
            return false;
        }  else if ((eventCaption.getValue().toString() == "")) {
            ShowNotification("Please provide event caption");
            return false;
        }
//        else if((eventDesc.getValue().toString()==""))
//        {
//            ShowNotification("Please provide event description");
//            return false;
//        }
//        else if(eventColour.getValue()==null)
//        {
//            ShowNotification("Please select event color");
//            return false;
//        }


        return true;
    }

    public boolean validateEventTime() {
        boolean isvalidate = true;
        if ((!(startTimeHr.getValue().equals(GlobalConstants.SELECT)))
                && (!(startTimeMin.getValue().equals(GlobalConstants.SELECT)))
                && (!(endTimeHr.getValue().equals(GlobalConstants.SELECT)))
                && (!(endTimeMin.getValue().equals(GlobalConstants.SELECT)))) {

            Time startTime=null;
            Time endTime=null;
            int startHrs = Integer.parseInt((String) startTimeHr.getValue().toString());
            int endHrs = Integer.parseInt((String) endTimeHr.getValue().toString());
            int startMin = Integer.parseInt((String) startTimeMin.getValue().toString());
            int endMin = Integer.parseInt((String) endTimeMin.getValue().toString());
//            startTime = DateUtil.convertTime(startHrs, startMin);
//            endTime = DateUtil.convertTime(endHrs, endMin);
            if (startTime.compareTo(endTime) >= 0) {
                isvalidate = false;
            }
        }
        return isvalidate;

    }

    private boolean validateEventForm() {
        if (startTime.getValue() == null) 
        {
            ShowNotification("Please select event start date");
            return false;
        } else if (endTime.getValue() == null) {
            ShowNotification("Please select event end date");
            return false;
        } 
        else if ((eventCaption.getValue().toString().trim().equals(GlobalConstants.emptyString))) {
            ShowNotification("Please provide event caption");
            return false;
        }
        else if ((eventDesc.getValue().toString().trim().equals(GlobalConstants.emptyString))) {
            ShowNotification("Please provide event description");
            return false;
        }
        else if(stdCombo.getValue()==null || stdCombo.getValue().toString().equals("Select"))
        {
            ShowNotification("Please select standard");
            return false;
        }
        else if(eventColour.getValue()==null)
        {
            ShowNotification("Please select event color");
            return false;
        }

        return true;
    }

    public void ShowNotification(String Message) {
//        Window.Notification notification = new Window.Notification(Message, Window.Notification.TYPE_WARNING_MESSAGE);
//        notification.setDelayMsec(2000);
        getUI().showNotification(Message);
    }

    public void valueChange(ValueChangeEvent event) {
        Property property = event.getProperty();
        if (property == startTime) {
            isReschedule = true;
        }
//        throw new UnsupportedOperationException("Not supported yet.");
    }

//    public void sendMail(String emailId) {
//        Ssmailbox ssmailbox = getSsmailbox(emailId);
//        ssconversationService.sendMessage(ssmailbox);
//    }
//
//    private Ssmailbox getSsmailbox(String emailId) {
//        OfUserService ofUserService = new OfUserService();
//        Ssattachement ssattachement = null;
//        int counter = 0;
//        String messatgeTo = emailId;
//        String[] message = messatgeTo.split(GlobalConstants.COMMA);
//        String messageToList = null;
//        String groupUser = null;
//        messageToList = messatgeTo;
//        messageToList = ServicesUtils.removeDuplicateUserId(messageToList);
//        msgToUserStr = messageToList;
//        Ssmailbox ssmailbox = new Ssmailbox();
//        ssmailbox.setMessagebody(Encryption.byteArrayToHexString(encrptedByArray));
//        ssmailbox.setMsgfrom(app.getLoginUserProfile().getUsername());
//        ssmailbox.setMsgto(messageToList);
//        ssmailbox.setSubject("Appointment Schedule");
//        ssmailbox.setSsconversationses(getSsconversations(ssmailbox, messageToList));
//
//        List<File> files = new ArrayList<File>();
//        files.add(appointmentiCalFile);
//        ssmailbox.setSsattachements(ofUserService.setAttachments(files, ssmailbox));
//        ssmailbox.setIsAttachment(true);
//        return ssmailbox;
//    }
//
//    private Set<Ssconversations> getSsconversations(Ssmailbox ssmailbox, String messatgeTo) {
//        Set<Ssconversations> s = new HashSet<Ssconversations>();
//        s.add(getSsconversationsPojo(ssmailbox, ssmailbox.getMsgfrom(), GlobalConstants.SENT));
//        String msgto[] = messatgeTo.split(",");
//        // for inbox
//        for (int i = 0; i < msgto.length; i++) {
//            s.add(getSsconversationsPojo(ssmailbox, msgto[i], GlobalConstants.INBOX));
//        }
//
//
//        return s;
//    }
//
//    private Ssconversations getSsconversationsPojo(Ssmailbox ssmailbox, String userId, String folder) {
//
//        //  Ssmailbox ssmail = new Ssmailbox();
//        Ssmailbox ssmail = null;
//        ssmail = ssmailbox;
//        Ssconversations ssconversations = new Ssconversations();
//        ssconversations.setFolder(folder);
//        ssconversations.setImpflag(false);
//        //  ssconversations.setMessagestatus(GlobalConstants.UNREAD);
//        ssconversations.setParentmessageid(0);
//        ssconversations.setMessagestatus(GlobalConstants.UNREADSTATUS);//0
//        ssconversations.setReadtimemobile(null);
//        ssconversations.setReadtimeweb(null);
//        ssconversations.setReceivetimemobile(null);
//        ssconversations.setReceivetimeweb(null);
//        ssconversations.setSenttimemobile(null);
//        ssconversations.setSenttimeweb(new Date());
//        ssconversations.setSsmailbox(ssmail);
//        ssconversations.setStoreflag(false);
//        ssconversations.setUserid(userId.trim());
//        return ssconversations;
//    }
//
//    public void messageBodyFOrmat(String email1, String email2, boolean toFlag) throws Exception {
//        String message = GlobalConstants.getProperty(GlobalConstants.MESSAGE_BODY_FOR_MEETING);
//        String strHtmlMessage = IOUtils.toString(new FileInputStream(new File(GlobalConstants.getProperty(GlobalConstants.MESSAGE_BODY_FOR_MEETING_HTML_NOTIFICATION))));
////        message = message.replace("<user1>", email1.split(GlobalConstants.AT_THE_RATE)[0]);
////        message = message.replace("<user2>", email2.split(GlobalConstants.AT_THE_RATE)[0]);
//
//        if (toFlag) {
//            message = message.replace("<user1>", custContact.getContactName());
//            message = message.replace("<user2>", ofUser.getName());
//
//            strHtmlMessage = strHtmlMessage.replace("<user1>", custContact.getContactName());
//            strHtmlMessage = strHtmlMessage.replace("<user2>", ofUser.getName());
//
//        } else {
//            message = message.replace("<user1>", ofUser.getName());
//            message = message.replace("<user2>", custContact.getContactName());
//
//            strHtmlMessage = strHtmlMessage.replace("<user1>", ofUser.getName());
//            strHtmlMessage = strHtmlMessage.replace("<user2>", custContact.getContactName());
//
//        }
//
//        message = message.replace("<time>", startTimeHr.getValue().toString() + ":" + (startTimeMin.getValue().toString().equals("0") ? "00" : startTimeMin.getValue().toString()) + " to " + endTimeHr.getValue().toString() + ":" + (endTimeMin.getValue().toString().equals("0") ? "00" : endTimeMin.getValue().toString()));
//        message = message.replace("<date>", startDt.toString());
//        message = message.replace("<meeting_purpose>", eventCaption.getValue().toString());
//
//        strHtmlMessage = strHtmlMessage.replace("<time>", startTimeHr.getValue().toString() + ":" + (startTimeMin.getValue().toString().equals("0") ? "00" : startTimeMin.getValue().toString()) + " to " + endTimeHr.getValue().toString() + ":" + (endTimeMin.getValue().toString().equals("0") ? "00" : endTimeMin.getValue().toString()));
//        strHtmlMessage = strHtmlMessage.replace("<date>", startDt.toString());
//        strHtmlMessage = strHtmlMessage.replace("<meeting_purpose>", eventCaption.getValue().toString());
//
////        message = message.replace("<emailFrom>", app.getLoginUserProfile().getUsername().split(GlobalConstants.AT_THE_RATE)[0]);
////        if (toFlag) {
////            message = message.replace("<emailFrom>", custContact != null ? custContact.getContactName() : "");
////            strHtmlMessage = strHtmlMessage.replace("<emailFrom>", custContact != null ? custContact.getContactName() : "");
////        } else {
////            message = message.replace("<emailFrom>", ofUser != null ? ofUser.getName() : "");
////            strHtmlMessage = strHtmlMessage.replace("<emailFrom>", ofUser != null ? ofUser.getName() : "");
////        }
//
//        try {
//            original = message.getBytes(GlobalConstants.UTF8);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(WorkSpace.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        byte[] key = KEY.getBytes();
//        byte[] iv = IV.getBytes();
//        encrptedByArray = Encryption.aesEncrypt(original, key, iv);
//        sendMail(email1);
//
//        SS_EmailMode l_objWebMail = new SS_EmailMode();
//
//        if (toFlag) {
//            l_objWebMail.main_proc_att(custContact.getEmail1(), "Appoinment", message, strHtmlMessage, appointmentiCalFile.getAbsolutePath(), appointmentiCalFile.getName());
//        } else {
//            l_objWebMail.main_proc_att(ofUser.getEmail(), "Appoinment", message, strHtmlMessage, appointmentiCalFile.getAbsolutePath(), appointmentiCalFile.getName()); //this is for consultant
//        }
//    }
//    
//    
//    private boolean isValidStartDt() {
//        boolean isValidDt = true;
//        if (startDt.getValue() == null) {
//            ShowNotification("Please select event date");
//            isValidDt = false;
//        } else if (!DateUtil.ComapreDate((Date) startDt.getValue())) {
//            ShowNotification("Start date should be greater than or equal to current date");
//            isValidDt = false;
//        }
//
//        return isValidDt;
//    }

    private void setFormData(MUCEvent basicEvent) {
        this.startTime.setValue(basicEvent.getStart());
        this.endTime.setValue(basicEvent.getEnd());
        this.eventCaption.setValue(basicEvent.getCaption());
        String desc=basicEvent.getDescription();
        
        AppointmentMstBean bean = plannerEventMap.get(desc);
        
        eventIdForEditDeleteBean = bean.getAppointmentId();
        
        desc=desc.substring(desc.indexOf(GlobalConstants.HASH)+1);
        this.eventDesc.setValue(desc);
        

        String std = bean.getForWhom();
        String div=GlobalConstants.emptyString;
        if(std.contains(GlobalConstants.HASH))
        {
            div=std.substring(std.indexOf(GlobalConstants.HASH)+1);
            std=std.substring(0,std.indexOf(GlobalConstants.HASH));            
        }
            
        this.stdCombo.setValue(std);
        
        if(!div.equals(GlobalConstants.emptyString))
            this.divCombo.setValue(div);
        
        this.eventColour.setValue(styleToColorMap.get(bean.getEventStyle()));
        
    }
}
