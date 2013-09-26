/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.vaadin.demo.dashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.MasteParmBean;
import com.quick.bean.MyDashBoardBean;
import com.quick.bean.Userprofile;
import com.quick.entity.Notices;
import com.quick.entity.Whatsnew;
import com.quick.entity.Whoisdoingwhat;
import com.quick.table.MyDashBoardDataProvider;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.text.DecimalFormat;

import com.vaadin.data.Property;
import com.quick.data.DataProvider;
import com.quick.data.Generator;
import com.quick.utilities.DateUtil;
import com.quick.utilities.UIUtils;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DashboardView extends VerticalLayout implements View, Property.ValueChangeListener {

    private Table t;
    private MyDashBoardDataProvider boardDataProvider = new MyDashBoardDataProvider();
    private  List<Whatsnew> whatsnewsList;
    private  List<MasteParmBean> whosDoingWhatFromDB;
    private  List<Notices> noticeses;
    private  Table whatsNewTable;
    private  Table whosDoingWhatTable;
    private static final String strViewMore="View More";

    
    
    
    public DashboardView() {
        
        

    }

    private CssLayout createPanel(Component content) {
        CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        Button configure = new Button();
        configure.addStyleName("configure");
        configure.addStyleName("icon-cog");
        configure.addStyleName("icon-only");
        configure.addStyleName("borderless");
        configure.setDescription("Configure");
        configure.addStyleName("small");
        configure.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        panel.addComponent(configure);

        panel.addComponent(content);
        return panel;
    }

    private Userprofile profile;
    
    @Override
    public void enter(ViewChangeEvent event) {
//        DataProvider dataProvider = ((DashboardUI) getUI()).dataProvider;
//        t.setContainerDataSource(dataProvider.getRevenueByTitle());
        profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        getDashBoardData();
        buildDashboardUI();
    }
    
    private void buildDashboardUI()
    {
        setSizeFull();
        addStyleName("dashboard-view");

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        final Label title = new Label("Titali Dashboard");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 0.5f);

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
                ((DashboardUI) getUI()).clearDashboardButtonBadge();
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

      /*  Button edit = new Button();
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
        top.setComponentAlignment(edit, Alignment.MIDDLE_LEFT); */

        HorizontalLayout row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 1.5f);
        
        
        whatsNewTable = boardDataProvider.getWhatsNewForme(whatsnewsList,this);
        //row.addComponent(createPanel(getWhatsNewLayout()));
        //row.addComponent(UIUtils.createPanel(whatsNewTable));
        row.addComponent(whatsNewTable);
        
        /* TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to:\n· Zoom in and out in the Sales view\n· Filter the transactions and drag a set of them to the Reports tab\n· Create a new report\n· Change the schedule of the movie theater");
        notes.setSizeFull(); */
        
        CssLayout panel = createPanel(boardDataProvider.getMyNoticeBoard(noticeses));
        panel.addStyleName("notes");
        row.addComponent(panel);
        
//        row.setExpandRatio(whatsNewTable, 3);
//        row.setExpandRatio(panel, 1);
        
        

        row = new HorizontalLayout();
        row.setMargin(true);
        row.setSizeFull();
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 2);

      /*  t = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId,
                    Property<?> property) {
                if (colId.equals("Revenue")) {
                    if (property != null && property.getValue() != null) {
                        Double r = (Double) property.getValue();
                        String ret = new DecimalFormat("#.##").format(r);
                        return "$" + ret;
                    } else {
                        return "";
                    }
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        t.setCaption("Top 10 Titles by Revenue");

        t.setWidth("100%");
        t.setPageLength(0);
        t.addStyleName("plain");
        t.addStyleName("borderless");
        t.setSortEnabled(false);
        t.setColumnAlignment("Revenue", Align.RIGHT);
        t.setRowHeaderMode(RowHeaderMode.INDEX); */

        whosDoingWhatTable=boardDataProvider.getWhoIsDoingWhat(whosDoingWhatFromDB,this);
        //row.addComponent(createPanel(whosDoingWhatTable));
        row.addComponent(whosDoingWhatTable);
        
        
        //row.addComponent(createPanel(new TopSixTheatersChart()));
        Component c=createPanel(getMyPerformance());
        row.addComponent(c);
        
//        row.setExpandRatio(whosDoingWhatTable, 3);
//        row.setExpandRatio(c, 1);

        
    }

    Window notifications;

    private void buildNotifications(ClickEvent event) {
        notifications = new Window("Notifications");
        VerticalLayout l = new VerticalLayout();
        l.setMargin(true);
        l.setSpacing(true);
        notifications.setContent(l);
        notifications.setWidth("350px");
        notifications.addStyleName("notifications");
        notifications.setClosable(false);
        notifications.setResizable(false);
        notifications.setDraggable(false);
        notifications.setPositionX(event.getClientX() - event.getRelativeX());
        notifications.setPositionY(event.getClientY() - event.getRelativeY());
        notifications.setCloseShortcut(KeyCode.ESCAPE, null);

        Label label = new Label(
                "<hr><b>"
                        +whatsnewsList.get(0).getDisplaynotification()
                +"</b><br><span>"+DateUtil.getTimeIntervalOfTheActivity(whatsnewsList.get(0).getReleasedate())+"</span><br>"
                        , ContentMode.HTML);
        l.addComponent(label);

        label = new Label(
                "<hr><b>"
                        +whatsnewsList.get(1).getDisplaynotification()
                +"</b><br><span>"+DateUtil.getTimeIntervalOfTheActivity(whatsnewsList.get(0).getReleasedate())+"</span><br>"
                        , ContentMode.HTML);
        l.addComponent(label);
    }
    
    
    private void getDashBoardData(){
          try {
           

            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DASHBOARD_URL));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("standard", profile.getStd());
                inputJson.put("division", profile.getDiv());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);

          
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<Whatsnew>>() {
            }.getType();
            Gson whatsNewGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            whatsnewsList = whatsNewGson.fromJson(outNObject.getString(GlobalConstants.WHATSNEW), listType);
            
            
            
             Type listType1 = new TypeToken<ArrayList<MasteParmBean>>() {
            }.getType();
            
             Gson whoIsDoingWhatGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            whosDoingWhatFromDB = whoIsDoingWhatGson.fromJson(outNObject.getString(GlobalConstants.WHOSEDOINGWHAT), listType1);
            
            
            
             Type listType2 = new TypeToken<ArrayList<Notices>>() {
            }.getType();
            
             Gson noticesGson = new GsonBuilder().setDateFormat(GlobalConstants.gsonTimeFormat).create();       
            noticeses = noticesGson.fromJson(outNObject.getString(GlobalConstants.NOTICES), listType2);
            
           
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }

    }

    /**
     * below method gets called when student clicks on any of the whats new item
     * further the control gets transfer to the quick learn screen
     * which shows the selected item
     * necessary data to show the selected item is set from this method
     * @param event 
     */
    
    
    @Override
    public void valueChange(ValueChangeEvent event) {
        Property property =event.getProperty();
        if(property==whatsNewTable){
             MyDashBoardBean whatsnewItem= (MyDashBoardBean)property.getValue();
             navigateToQuickLearnTopic(whatsnewItem.getItemid());
        }
        else if(property==whosDoingWhatTable){
             MyDashBoardBean whatsnewItem= (MyDashBoardBean)property.getValue();
             navigateToQuickLearnTopic(whatsnewItem.getUploadId());
        }
    }

    /**
     * this method sets required parameters to set the selected topic on quick learn screen
     * after setting this details it uses ui navigator to navigate the control to quick learn screen
     */
    private void navigateToQuickLearnTopic(String quickLearnUploadId) {
        getSession().setAttribute("uploadIdToNavigate", quickLearnUploadId);
        getUI().getNavigator().navigateTo("/learn");
        
    }

    private Component getWhatsNewLayout() {
        
        VerticalLayout parent = new VerticalLayout();
        parent.setSizeFull();        
        parent.addComponent(whatsNewTable);
        parent.setExpandRatio(whatsNewTable, 4);
        //whatsNewTable.
        
        
        Button viewMoreBtn = new Button(strViewMore);
        viewMoreBtn.setImmediate(true);
        viewMoreBtn.setStyleName(BaseTheme.BUTTON_LINK);
//        viewMoreBtn.setWidth("100%");
//        viewMoreBtn.setHeight("3%");
//        
//        VerticalLayout l = new VerticalLayout();
//        l.setWidth("100%");
//        l.addComponent(viewMoreBtn);
//        l.setComponentAlignment(viewMoreBtn, Alignment.BOTTOM_CENTER);
        
        parent.addComponent(viewMoreBtn);
        parent.setComponentAlignment(viewMoreBtn, Alignment.BOTTOM_CENTER);
        parent.setExpandRatio(viewMoreBtn, 0.5f);
//               
        
        return parent;
    }

    private Component getMyPerformance() 
    {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setCaption("My Achievement");
        hl.setSizeFull();
        Label l = new Label("<table width='50%' height='100%' border='0' bgcolor='purple'><tr><td align=center><font face='verdana' color='white' align=center><h1><b>4.5</b></h1> out of 5</font></td></tr></table>", ContentMode.HTML);
        l.setSizeFull();
        hl.addComponent(l);
        return hl;
    }

}
