/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.MasteParmBean;
import com.quick.bean.Userprofile;
import com.quick.container.StudentExamListContainer;
import com.vaadin.demo.dashboard.TopSixTheatersChart;


import com.quick.entity.Notices;
import com.quick.entity.Whatsnew;
import com.quick.entity.Whoisdoingwhat;
import com.quick.table.MyDashBoardDataProvider;
import java.text.DecimalFormat;

import com.vaadin.data.Property;
import com.quick.data.Generator;
import com.quick.global.GlobalConstants;
import com.quick.table.QuickUploadTable;
import com.quick.utilities.ConfirmationDialogueBox;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.addon.charts.Chart;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.demo.dashboard.QuickUpload;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.RowHeaderMode;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AdminExam extends VerticalLayout implements View  {

         TextField subtxt ;
         TextField markstxt;
         TextField scoretxt;
         TextField questionstxt;
         Button newExamBtn;
         Button viewExam;
         Table examlistTbl;
         ValueChangeListener exmlistListner;
        HorizontalLayout row1;
        HorizontalLayout row2;
        private CssLayout examsummaryPannel;
        
    Table t;
    MyDashBoardDataProvider boardDataProvider = new MyDashBoardDataProvider();
    private List<ExamBean> selectedExam;

   
    /**
     * @return the selectedExam
     */
    public List<ExamBean> getSelectedExam() {
        return selectedExam;
    }

    /**
     * @param selectedExam the selectedExam to set
     */
    public void setSelectedExam(List<ExamBean> selectedExam) {
        this.selectedExam = selectedExam;
    }

    
    
    
    
    public AdminExam() {
       // addStyleName("dashboard-view");
    }
    
    public void buildUi(){
        
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        final Label title = new Label("Exam management");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);

        Button notify = new Button("2");
        notify.setDescription("Notifications (2 unread)");
        // notify.addStyleName("borderless");
        notify.addStyleName("notifications");
        notify.addStyleName("unread");
        notify.addStyleName("icon-only");
        notify.addStyleName("icon-bell");
        notify.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                //((DashboardUI) getUI()).clearDashboardButtonBadge();
                event.getButton().removeStyleName("unread");
                event.getButton().setDescription("Notifications");

                if (notifications != null && notifications.getUI() != null)
                    notifications.close();
                else {
                    buildNotifications(event);
                    getUI().addWindow(notifications);
                    notifications.focus();
                    ((CssLayout) getUI().getContent())
                            .addLayoutClickListener(new LayoutClickListener() {
                                @Override
                                public void layoutClick(LayoutClickEvent event) {
                                    notifications.close();
                                    ((CssLayout) getUI().getContent())
                                            .removeLayoutClickListener(this);
                                }
                            });
                }

            }
        });
        top.addComponent(notify);
        top.setComponentAlignment(notify, Alignment.MIDDLE_LEFT);

        Button edit = new Button();
        edit.addStyleName("icon-edit");
        edit.addStyleName("icon-only");
        top.addComponent(edit);
        edit.setDescription("Edit Dashboard");
        edit.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                final Window w = new Window("Edit Dashboard");

                w.setModal(true);
                w.setClosable(false);
                w.setResizable(false);
                w.addStyleName("edit-dashboard");

                getUI().addWindow(w);

                w.setContent(new VerticalLayout() {
                    TextField name = new TextField("Dashboard Name");
                    {
                        addComponent(new FormLayout() {
                            {
                                setSizeUndefined();
                                setMargin(true);
                                name.setValue(title.getValue());
                                addComponent(name);
                                name.focus();
                                name.selectAll();
                            }
                        });

                        addComponent(new HorizontalLayout() {
                            {
                                setMargin(true);
                                setSpacing(true);
                                addStyleName("footer");
                                setWidth("100%");

                                Button cancel = new Button("Cancel");
                                cancel.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        w.close();
                                    }
                                });
                                cancel.setClickShortcut(KeyCode.ESCAPE, null);
                                addComponent(cancel);
                                setExpandRatio(cancel, 1);
                                setComponentAlignment(cancel,
                                        Alignment.TOP_RIGHT);

                                Button ok = new Button("Save");
                                ok.addStyleName("wide");
                                ok.addStyleName("default");
                                ok.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        title.setValue(name.getValue());
                                        w.close();
                                    }
                                });
                                ok.setClickShortcut(KeyCode.ENTER, null);
                                addComponent(ok);
                            }
                        });

                    }
                });

            }
        });
        top.setComponentAlignment(edit, Alignment.MIDDLE_LEFT);
        newExamBtn = new Button("New Exam");
        newExamBtn.addStyleName("default");
        newExamBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                startExam();
            }
        });       
        
        top.addComponent(newExamBtn);
        top.setComponentAlignment(newExamBtn, Alignment.MIDDLE_LEFT);        
        
        viewExam = new Button("View Exam");
        viewExam.addStyleName("default");
        viewExam.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
               viewExam();
            }            
        });        
        
        top.addComponent(viewExam);
        top.setComponentAlignment(viewExam, Alignment.MIDDLE_LEFT);
        
         row1 = new HorizontalLayout();
        row1.setSizeFull();
        row1.setMargin(new MarginInfo(true, true, false, true));
        row1.setSpacing(true);
        addComponent(row1);
        setExpandRatio(row1, 2); 

        Userprofile userprofile = (Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile);
          Component examdtls = buildSelectedExamDetails();
         
        
        AdminExamDataProvider provider = new AdminExamDataProvider(this);
         examlistTbl = provider.getExamListForTeacher(getExamList(userprofile.getStd(),userprofile.getDiv()));
         exmlistListner = new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
               ExamBean eb = (ExamBean) event.getProperty().getValue(); 
               setSelectedExam(getSelectedExamDetailsById(eb.getExamId()));
               updateExamDetails();
               updateExamSummary();
            }
        };
         
        examlistTbl.addValueChangeListener(exmlistListner);
        examlistTbl.select(examlistTbl.firstItemId());

        row1.addComponent(UIUtils.createPanel(UIUtils.buildVerticalLayoutForComponent(examlistTbl)));
       // row.addComponent(UIUtils.createPanel(AdminExamDataProvider.getMyExamPieChart()));
        row1.addComponent(UIUtils.createPanel(new Label("My Exam Pie chart")));

        row2 = new HorizontalLayout();
        row2.setMargin(true);
        row2.setSizeFull();
        row2.setSpacing(true);
        addComponent(row2);
        setExpandRatio(row2, 2);

        
       
        //UIUtils.getVerticalPaneView(examdtls, examdetailspieChart))
        row2.addComponent(UIUtils.createPanel(examdtls));

       // row.addComponent(UIUtils.createPanel(AdminExamDataProvider.getExamResult()));
        Component examChart = getExamDetailsPieChart();
        examsummaryPannel = UIUtils.createPanel(UIUtils.getTabSheetPaneView(examChart,
                AdminExamDataProvider.getPresentStudentsForExam(getSelectedExam())  
                , AdminExamDataProvider.getAbsentStudentsForExam(getSelectedExam())));
        row2.addComponent(examsummaryPannel);

    }
    
     
   
    public  Component buildSelectedExamDetails() {
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        subtxt =new TextField();
        subtxt.setImmediate(true);
        subtxt.setCaption("subject");
        
        markstxt =new TextField();
        markstxt.setCaption("Marks");
        markstxt.setImmediate(true);
        
        scoretxt =new TextField();
        scoretxt.setCaption("Score");
        scoretxt.setImmediate(true);
        
        questionstxt =new TextField();
        questionstxt.setCaption("Questions");
        questionstxt.setImmediate(true);
        formLayout.addComponent(subtxt);
        formLayout.addComponent(markstxt);
        formLayout.addComponent(scoretxt);
        formLayout.addComponent(questionstxt);
        //throw new UnsupportedOperationException("Not yet implemented");
        return formLayout;
    }


//    private CssLayout createPanel(Component content) {
//        CssLayout panel = new CssLayout();
//        panel.addStyleName("layout-panel");
//        panel.setSizeFull();
//
//        Button configure = new Button();
//        configure.addStyleName("configure");
//        configure.addStyleName("icon-cog");
//        configure.addStyleName("icon-only");
//        configure.addStyleName("borderless");
//        configure.setDescription("Configure");
//        configure.addStyleName("small");
//        configure.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                Notification.show("Not implemented in this demo");
//            }
//        });
//        panel.addComponent(configure);
//        
//
//        panel.addComponent(content);
//        return panel;
//    }

    @Override
    public void enter(ViewChangeEvent event) {
         setSizeFull();
         buildUi();
//        DataProvider dataProvider = ((DashboardUI) getUI()).dataProvider;
//        t.setContainerDataSource(dataProvider.getRevenueByTitle());
    }

    Window notifications;

    private void buildNotifications(ClickEvent event) {
        notifications = new Window("Notifications");
        VerticalLayout l = new VerticalLayout();
        l.setMargin(true);
        l.setSpacing(true);
        notifications.setContent(l);
        notifications.setWidth("300px");
        notifications.addStyleName("notifications");
        notifications.setClosable(false);
        notifications.setResizable(false);
        notifications.setDraggable(false);
        notifications.setPositionX(event.getClientX() - event.getRelativeX());
        notifications.setPositionY(event.getClientY() - event.getRelativeY());
        notifications.setCloseShortcut(KeyCode.ESCAPE, null);

        Label label = new Label(
                "<hr><b>"
                        + Generator.randomFirstName()
                        + " "
                        + Generator.randomLastName()
                        + " created a new report</b><br><span>25 minutes ago</span><br>"
                        + Generator.randomText(18), ContentMode.HTML);
        l.addComponent(label);

        label = new Label("<hr><b>" + Generator.randomFirstName() + " "
                + Generator.randomLastName()
                + " changed the schedule</b><br><span>2 days ago</span><br>"
                + Generator.randomText(10), ContentMode.HTML);
        l.addComponent(label);
    }
    
    
    
     private void updateExamDetails() {
       updateSelectedExamDetailsPanel();         
     }

     private void updateSelectedExamDetailsPanel() {
        ExamBean eb = getSelectedExam().get(0);
      
        subtxt.setValue(""+eb.getSub());
       
        markstxt.setValue(""+eb.getTotalMarks());
        
        scoretxt.setValue(""+eb.getPassingMarks());
        
        questionstxt.setValue(""+eb.getNoOfQuestions());
    }
    
  
      private void startExam() {
          CreateAdminExamPopup examPopup = new CreateAdminExamPopup(this);
          UI.getCurrent().addWindow(examPopup);
          examPopup.focus();
      }
    
    
    
    public  List<ExamBean> getExamList(String std,String div) {
       
         List<ExamBean> examList = null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8084/titali/rest/examResource/getExamList");
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try{           
                inputJson.put("std", std);
                inputJson.put("div", div);
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<ExamBean>>() {
            }.getType();
            
            examList= new Gson().fromJson(outNObject.getString(GlobalConstants.EXAMLIST), listType);
        } catch (JSONException ex) {
          ex.printStackTrace();
        }
        return examList;
            
    }
    
    
    public List<ExamBean> getSelectedExamDetailsById(int examId){
         List<ExamBean> selectedExamDetails = null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8084/titali/rest/examResource/getExamDetailsById");
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try{           
                inputJson.put("exmId", examId);
//                inputJson.put("div", "A-1");
             }catch(Exception ex){
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
                      
            Type listType = new TypeToken<ArrayList<ExamBean>>() {
            }.getType();
            Gson gson=  new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();
            selectedExamDetails=gson.fromJson(outNObject.getString(GlobalConstants.EXAMLIST), listType);
        } catch (JSONException ex) {
          ex.printStackTrace();
        }
        return selectedExamDetails;
    }

    private void viewExam() {
       ExamBean eb = getSelectedExam().get(0);
       CreateAdminExamPopup examPopup = new CreateAdminExamPopup(this,eb);
       UI.getCurrent().addWindow(examPopup);
       examPopup.focus();
    }

    public void updateExamList(){
        examlistTbl.removeValueChangeListener(exmlistListner);
        examlistTbl.getContainerDataSource().removeAllItems();
        Userprofile userprofile = (Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        examlistTbl.setContainerDataSource(StudentExamListContainer.getExamListContainer(getExamList(userprofile.getStd(),userprofile.getDiv())));
        examlistTbl.setVisibleColumns(StudentExamListContainer.NATURAL_COL_ORDER_EXAM_LIST);
        examlistTbl.setColumnHeaders(StudentExamListContainer.COL_HEADERS_ENGLISH_EXAM_LIST);
        examlistTbl.addValueChangeListener(exmlistListner);
        examlistTbl.sort(new Object[]{"examId"}, new boolean[]{true});
        examlistTbl.select(examlistTbl.firstItemId());
    }
   
    
    private void updateExamSummary()
    {
       if(row2!=null){
           
        row2.removeComponent(examsummaryPannel);
        Component examChart = getExamDetailsPieChart();
        examsummaryPannel =  examsummaryPannel = UIUtils.createPanel(UIUtils.getTabSheetPaneView(examChart,
                AdminExamDataProvider.getPresentStudentsForExam(getSelectedExam())  
                , AdminExamDataProvider.getAbsentStudentsForExam(getSelectedExam())));
        row2.addComponent(examsummaryPannel);

       }
    }
 /**
      * adding listener to the remove button for delete topic from upload screen
      * */
     public void addListenertoBtn(Button btnRemove) 
    {
        
        btnRemove.addListener(new Button.ClickListener() 
        {
            public void buttonClick(final Button.ClickEvent event) 
            {
                //UI.getCurrent().addWindow(new ConfirmationDialogueBox());
                
                UI.getCurrent().addWindow(new ConfirmationDialogueBox("Confirmation", "Are you sure you want to remove this exam ?", new ConfirmationDialogueBox.Callback() {

                    @Override
                    public void onDialogResult(boolean flag) 
                    {
                        if(flag)
                        {
                            Object data[] = (Object[]) event.getButton().getData();
                            
                            Table t = (Table) data[0];
                            
                            System.out.println("****"+((ExamBean)data[1]));
                            
                            deleteExamFromDB((ExamBean)data[1]);
                            
                            // temporary removing value change listener of the ExamBean table so that after removing item it will not attempt to display that particular item
                            t.removeValueChangeListener(exmlistListner);
        
                            t.removeItem(data[1]);
                            
                            //restoring the value change listener so that selected uploaded item will be displayed in the right panel
                            t.addValueChangeListener(exmlistListner);
                            t.select(t.firstItemId());
                        }
                    }
                }));
            }
        });
        
        
    }
     
     //get upload id from bean and pass it to service 
     // service will delete it from db
     private void deleteExamFromDB(ExamBean examBean) 
     {
          try 
          {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DELETE_EXAM_BY_ID));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            
            inputJson.put("examId", examBean.getExamId());

            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
          
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
            System.out.println("ou########tNObject="+outNObject.getString(GlobalConstants.STATUS));
            int status = Integer.parseInt(outNObject.getString(GlobalConstants.STATUS));
            
            if(status == GlobalConstants.YES)
            {
                Notification.show("Successfully deleted exam", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Exam deletion failed", Notification.Type.WARNING_MESSAGE);
            }
            
        } catch (JSONException ex) 
        {
            Notification.show("Exam deletion failed", Notification.Type.WARNING_MESSAGE);
            ex.printStackTrace();
        }
     }

    private Component getExamDetailsPieChart() {
        
        
        
        ExamBean eb = getSelectedExam().get(0);
        HashMap<String,Double> dataMap = new HashMap<String,Double>();
        
        dataMap.put("Absent", ((double) eb.getTotalStudents() - (double) eb.getAppearedStudents()));

        dataMap.put("Fail", (double) eb.getFailedStudents());

        dataMap.put("Passed", (double) eb.getPassedStudents());

        return CustomPieChart.createChart(dataMap,"Passed");
        
    }
    
   /*  private ColumnChart getAvgAndTopIncentiveColumnChart() 
    {
        ColumnChart cc = new ColumnChart();
        //UIUtils.setColumnChartContainerDataSource(cc, columChartValuesMap);
        cc.setHeight("420px");
        cc.setWidth("250px");
        cc.addXAxisLabel("payout");
        
        cc.addColumn("payout");
        //cc.addColumn(GlobalConstants.Attainment);AvgNationalPayout
        //String payoutChartCaption=GlobalConstants.emptyString;
        
        
           // payoutChartCaption="column chart";            
            cc.add("", new double[]{ new Double(30)});
            cc.add("", new double[]{new Double(10)});
            cc.add("", new double[]{new Double(20)});
        
        
//        VerticalLayout vl = new VerticalLayout();
//        vl.setCaption(payoutChartCaption);
//        vl.setSizeFull();
//        vl.addComponent(cc);
        
        return cc;
    } */

     
    
    

}
