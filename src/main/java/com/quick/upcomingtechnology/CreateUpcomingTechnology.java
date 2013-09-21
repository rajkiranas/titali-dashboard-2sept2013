/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.upcomingtechnology;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.CategoryDistributionBean;
import com.quick.bean.ExamBean;
import com.quick.bean.UpcomingTechnologyBean;
import com.quick.bean.Userprofile;
import com.quick.container.CategoryTechnologyContainer;
import com.quick.container.UpcomingTechnologyContainer;
import com.quick.global.GlobalConstants;
import com.quick.ui.exam.CustomPieChart;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rajkirans
 */
public class CreateUpcomingTechnology extends VerticalLayout implements View ,Button.ClickListener{
    private Table upcomingTechnologyTbl;
    private List<UpcomingTechnologyBean> utBeanList;
    private PopupDateField utDate;
    private TextField utId;
    private TextField bywhom;
    private TextField utLine;
    private TextField utCategory;
    private TextArea utBody;
    private Button createnUT;
    private Button clear;
    private Button delete;
    private int selectedUTId;
    private Userprofile profile;
    private HorizontalLayout relatedTechnologiesAndPieChartLayout;


    private  Property.ValueChangeListener utTblValueChangeListener;

    public int getSelectedUTId() {
        return selectedUTId;
    }

    public void setSelectedUTId(int selectedUTId) {
        this.selectedUTId = selectedUTId;
    }
    
    
    public List<UpcomingTechnologyBean> getUTList() {
        return utBeanList;
    }

    public void setTechnologiesList(List<UpcomingTechnologyBean> utBeanList) {
        this.utBeanList = utBeanList;
    }
    
    
    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        addStyleName("schedule");
        profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        
    }
    
    public CreateUpcomingTechnology(){
        setTechnologiesList(fetchAllTechnologies());
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName("toolbar");
        addComponent(top);
        
        final Label title = new Label("Technologies");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);

        
        HorizontalLayout row = new HorizontalLayout();
        row.setSizeFull();
        row.setMargin(new MarginInfo(true, true, false, true));
        row.setSpacing(true);
        addComponent(row);
        setExpandRatio(row, 1.5f);
       // Component c=buildTabSheetLayout();
        
        //form created first because below method tries to set its values on table value change
        Component form = UIUtils.createPanel(getUTForm());
        
        Component l = getTechnologyListingAndGraphLayout();
        
        row.addComponent(l);
        
        row.addComponent(form);
        row.setComponentAlignment(form, Alignment.MIDDLE_RIGHT);
    }

    private Component getUTListView() 
    {
        //noticetbl = new Table();
        
        upcomingTechnologyTbl = new Table(){
            
            SimpleDateFormat df =
               new SimpleDateFormat(GlobalConstants.dateFormatMMddyyyy);
        
      @Override
       protected String formatPropertyValue(Object rowId,
           Object colId, Property property) {
       // Format by property type
       if (property.getType() == Date.class) {
           if(property.getValue()!=null)           
           return df.format((Date)property.getValue());
           else
             return GlobalConstants.emptyString;
       }
       return super.formatPropertyValue(rowId, colId, property);
   
}
  };
        
        
        
        upcomingTechnologyTbl.addStyleName("borderless");
        ///noticetbl.setSortEnabled(false);
        upcomingTechnologyTbl.setWidth("100%");
        upcomingTechnologyTbl.setPageLength(10);
        upcomingTechnologyTbl.setSelectable(true);
        upcomingTechnologyTbl.setImmediate(true); // react at once when something is selected
        upcomingTechnologyTbl.setContainerDataSource(UpcomingTechnologyContainer.getUTContainer(getUTList()));
        upcomingTechnologyTbl.setVisibleColumns(UpcomingTechnologyContainer.NATURAL_COL_ORDER_TECHNOLOGIES);
        upcomingTechnologyTbl.setColumnHeaders(UpcomingTechnologyContainer.COL_HEADERS_ENGLISH_TECHNOLOGIES);
        utTblValueChangeListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                UpcomingTechnologyBean bean =(UpcomingTechnologyBean)event.getProperty().getValue();
               updateUtFormField(bean);
               setCategorywiseTechnologyList(fetchRelatedTechnologies(bean.getCategory()));               
               
               if(mailTableAddedFlag==1)
               {
                getRelatedTechnologiesAndPieChartLayout();
               }
            }
        };
        
        upcomingTechnologyTbl.addValueChangeListener(utTblValueChangeListener);
        upcomingTechnologyTbl.sort(new Object[]{"technologydate"}, new boolean[]{false});
        upcomingTechnologyTbl.select(upcomingTechnologyTbl.firstItemId());
        return upcomingTechnologyTbl;
    }

    private List<CategoryDistributionBean> categorywiseTechnologyList=null;
    private static final String technology_details="Technology Details";
    
    private Component getUTForm() {
        VerticalLayout utForm = new VerticalLayout();
        //utForm.setCaption(technology_details);
        //utForm.setSizeFull();
        utForm.setSpacing(true);
        
        utId = new TextField("Technology Id");
        utId.setImmediate(true);
        utId.setWidth("15%");
        utId.setReadOnly(true);
        utId.setVisible(false);
        
        utDate = new PopupDateField("Date");
        utDate.setImmediate(true);
        utDate.setWidth("25%");
        utDate.setVisible(false);
        
        
        
        bywhom = new TextField();
        bywhom.setImmediate(true);
        bywhom.setWidth("30%");
        bywhom.setReadOnly(true);
        Component bywhomLayout =getHorizontalLayoutForTwoComponents("Created by",bywhom);
        
        
        
        
        utLine = new TextField();
        utLine.setImmediate(true);
        utLine.setWidth("40%");
        Component utLineLayout =getHorizontalLayoutForTwoComponents("Name",utLine);
        
        
        utCategory = new TextField();
        utCategory.setImmediate(true);
        utCategory.setWidth("40%");
        
        Component utCategoryLayout = getHorizontalLayoutForTwoComponents("Category",utCategory);
        
        
        utBody = new TextArea();
        utBody.setImmediate(true);
        utBody.setWidth("97%");
       // utBody.setHeight("100%");
        utBody.setRows(14);
        
        Component utBodyLayout = getHorizontalLayoutForTwoComponents("Description",utBody);
       
        
        //utForm.addComponent(utId);
        //utForm.addComponent(utDate);
        utForm.addComponent(bywhomLayout);
        utForm.addComponent(utLineLayout);
        utForm.addComponent(utCategoryLayout);
        utForm.addComponent(utBodyLayout);
        
        //profile=(Userprofile)getSession().getAttribute(GlobalConstants.CurrentUserProfile);
        //if(profile.get)
        
        createnUT = new Button("Save technology");
        createnUT.setStyleName(GlobalConstants.default_style);
        createnUT.addClickListener((Button.ClickListener)this);
        createnUT.setImmediate(true);
        
        
        clear = new Button("New technology");
        clear.addClickListener((Button.ClickListener)this);
        clear.setImmediate(true);
        clear.setStyleName(GlobalConstants.default_style);
        
         
        delete = new Button("Delete technology");
        delete.addClickListener((Button.ClickListener)this);
        delete.setImmediate(true);
        delete.setStyleName(GlobalConstants.default_style);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        
        buttonLayout.addComponent(clear);
        buttonLayout.addComponent(createnUT);        
        buttonLayout.addComponent(delete);
        
        
        VerticalLayout mainVertical = new VerticalLayout();
        mainVertical.setSizeFull();
        mainVertical.addComponent(utForm);
        mainVertical.addComponent(buttonLayout);
        
        mainVertical.setExpandRatio(utForm, 3f);
        mainVertical.setExpandRatio(buttonLayout, 0.5f);
        mainVertical.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
        
        return mainVertical;
    }
    
    private Component getHorizontalLayoutForTwoComponents(String s, Component c)
    {
        HorizontalLayout h = new HorizontalLayout();
        Label l = new Label(s);
        h.setSizeFull();
        h.addComponent(l);
        h.addComponent(c);
        h.setExpandRatio(l, 0.5f);
        h.setExpandRatio(c, 3f);
        return h;
    }

   
    private List<UpcomingTechnologyBean> fetchAllTechnologies() {
        List<UpcomingTechnologyBean> technologyList=null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_ALL_TECHNOLOGY));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
//            try {
//                inputJson.put("standard", "MCA-I");
//                inputJson.put("division", "A");
//            } catch (JSONException ex) {
//                ex.printStackTrace();
//            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);

          
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<UpcomingTechnologyBean>>() {
            }.getType();
            
             technologyList=new Gson().fromJson(outNObject.getString(GlobalConstants.Technologies), listType);
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        return technologyList;
    }
    
    private List<CategoryDistributionBean> fetchRelatedTechnologies(String category) {
        List<CategoryDistributionBean> relatedTechnologyList=null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_TECHNOLOGY_BY_CATEGORY));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("category", category);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);

          
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<CategoryDistributionBean>>() {
            }.getType();
            
             relatedTechnologyList=new Gson().fromJson(outNObject.getString(GlobalConstants.category_distribution), listType);
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
        return relatedTechnologyList;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Button source = event.getButton();
        if(source==createnUT){
            createNewTechnology();
            updateUtList();
        }else if(source==clear){
            clearForm();
        }else if(source==delete){
            deleteTechnology();
            updateUtList();            
        }
    }
    
    
    private void clearForm() {
        utId.setReadOnly(false);
        utId.setValue(GlobalConstants.emptyString);
        utId.setReadOnly(true);
        utDate.setValue(null);
        
        bywhom.setReadOnly(false);
        bywhom.setValue(profile.getName());
        bywhom.setReadOnly(true);
        
        utLine.setValue(GlobalConstants.emptyString);
        utCategory.setValue(GlobalConstants.emptyString);
        utBody.setValue(GlobalConstants.emptyString);
         
    }

    private void createNewTechnology() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SAVE_TECHNOLOGY));
            JSONObject inputJson = new JSONObject();
            try 
            {
                if(utId.getValue()!=null && !utId.getValue().trim().equals(GlobalConstants.emptyString))
                {
                    inputJson.put("technologyId", utId.getValue());
                }
                else
                {
                    inputJson.put("technologyId", 0);
                }
                inputJson.put("technologydate", new Date().getTime());
                inputJson.put("bywhom", bywhom.getValue());
                inputJson.put("technologyline", utLine.getValue());
                inputJson.put("category", utCategory.getValue());
                inputJson.put("technologybody", utBody.getValue());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
            int status = Integer.parseInt(outNObject.getString(GlobalConstants.STATUS));
            
            if(status == GlobalConstants.YES)
            {
                Notification.show("Successfully created technology", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Technology creation failed", Notification.Type.WARNING_MESSAGE);
            }

          
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }

    
     private void deleteTechnology() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DELETE_TECHNOLOGY));
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("technologyId",getSelectedUTId());
              
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);
            int status = Integer.parseInt(outNObject.getString(GlobalConstants.STATUS));
            
            if(status == GlobalConstants.YES)
            {
                Notification.show("Successfully deleted technology", Notification.Type.WARNING_MESSAGE);
            }
            else
            {
                Notification.show("Technology deletion failed", Notification.Type.WARNING_MESSAGE);
            }

          
            
        } catch (JSONException ex) 
        {
            ex.printStackTrace();
        }
    }

    
    private void updateUtFormField(UpcomingTechnologyBean bean) {
        
        utId.setReadOnly(false);
        utId.setValue(String.valueOf(bean.getTechnologyid()));
        utId.setReadOnly(true);
        
        utDate.setValue(bean.getTechnologydate());
        
        bywhom.setReadOnly(false);
        bywhom.setValue(bean.getBywhom());        
        bywhom.setReadOnly(true);
        
        utLine.setValue(bean.getTechnologyline());
        utCategory.setValue(bean.getCategory());
        utBody.setValue(bean.getTechnologybody()); 
        setSelectedUTId(bean.getTechnologyid());
    }

    private void updateUtList() 
    {
        upcomingTechnologyTbl.removeValueChangeListener(utTblValueChangeListener);
        upcomingTechnologyTbl.getContainerDataSource().removeAllItems();
        setTechnologiesList(fetchAllTechnologies());
        upcomingTechnologyTbl.setContainerDataSource(UpcomingTechnologyContainer.getUTContainer(getUTList()));
        upcomingTechnologyTbl.setVisibleColumns(UpcomingTechnologyContainer.NATURAL_COL_ORDER_TECHNOLOGIES);
        upcomingTechnologyTbl.setColumnHeaders(UpcomingTechnologyContainer.COL_HEADERS_ENGLISH_TECHNOLOGIES);
        upcomingTechnologyTbl.addValueChangeListener(utTblValueChangeListener);
        upcomingTechnologyTbl.sort(new Object[]{"technologydate"}, new boolean[]{false});
        upcomingTechnologyTbl.select(upcomingTechnologyTbl.firstItemId());
    }

    private VerticalLayout leftFirstComponentLayout;
    int mailTableAddedFlag=0;
    private Component getTechnologyListingAndGraphLayout() 
    {
        leftFirstComponentLayout = new VerticalLayout();
        leftFirstComponentLayout.setSizeFull();
        leftFirstComponentLayout.setSpacing(true);
        //main technology listing
        leftFirstComponentLayout.addComponent(UIUtils.createPanel(getUTListView()));
        mailTableAddedFlag=1;        
        getRelatedTechnologiesAndPieChartLayout();
        return UIUtils.createPanel(leftFirstComponentLayout);
    }
    
    
    private HorizontalLayout getRelatedTechnologiesAndPieChartLayout()
    {
        if(relatedTechnologiesAndPieChartLayout!=null)
        {
            leftFirstComponentLayout.removeComponent(relatedTechnologiesAndPieChartLayout);
        }
        
        //below table and peichart
        relatedTechnologiesAndPieChartLayout = new HorizontalLayout();
        relatedTechnologiesAndPieChartLayout.setSizeFull();
        relatedTechnologiesAndPieChartLayout.setSpacing(true);
        
        Component table=getRelatedTechnologiesTable();
        updateRelatedTechnologiesTable();

        relatedTechnologiesAndPieChartLayout.addComponent(table);
        relatedTechnologiesAndPieChartLayout.setComponentAlignment(table,Alignment.TOP_CENTER);
        relatedTechnologiesAndPieChartLayout.setExpandRatio(table, 1);        
        
        
        
        
        HashMap map = getDataMapForRelatedTechnologiesPieChart(getCategorywiseTechnologyList());
        buildPieChart(map);
        
        
        //adding below table and peichart to the left vertical layout
        leftFirstComponentLayout.addComponent(relatedTechnologiesAndPieChartLayout);
        
        return relatedTechnologiesAndPieChartLayout;
        
    }
    
    private void buildPieChart(HashMap dataMap)
    {
//        if(pieChart!=null)
//        {
//            relatedTechnologiesAndPieChartLayout.removeComponent(pieChart);
//        }
        
        Component pieChart;
        pieChart =getTechnologyUsageDistributionPieChart(dataMap);
        relatedTechnologiesAndPieChartLayout.addComponent(pieChart);
        relatedTechnologiesAndPieChartLayout.setComponentAlignment(pieChart,Alignment.MIDDLE_CENTER);
        relatedTechnologiesAndPieChartLayout.setExpandRatio(pieChart, 2.5f);
        
    }
    
    private Component getTechnologyUsageDistributionPieChart(HashMap map) {
        
//        HashMap<String,Double> dataMap = new HashMap<String,Double>();
//        
//        dataMap.put("Java",50d);
//        dataMap.put(".Net", 30d);
//        dataMap.put("C", 5d);
//        dataMap.put("C++", 15d);

        Component chart =CustomPieChart.createChart(map,(String)map.keySet().iterator().next(),"Industry popularity");
        chart.setSizeFull();
        
        return chart;
        
    }

    Table relatedTechnologiesTable = new Table();
    private Component getRelatedTechnologiesTable() 
    {
        
        relatedTechnologiesTable.addStyleName("borderless");
        ///noticetbl.setSortEnabled(false);
        relatedTechnologiesTable.setCaption("Related technologies");
        relatedTechnologiesTable.setImmediate(true); // react at once when something is selected
        relatedTechnologiesTable.setWidth("100%");
        relatedTechnologiesTable.setPageLength(5);
        relatedTechnologiesTable.setSelectable(false);
        relatedTechnologiesTable.setContainerDataSource(CategoryTechnologyContainer.getRelatedTechnologiesContainer(getCategorywiseTechnologyList()));
        relatedTechnologiesTable.setVisibleColumns(CategoryTechnologyContainer.NATURAL_COL_ORDER_TECHNOLOGIES);
        relatedTechnologiesTable.setColumnHeaders(CategoryTechnologyContainer.COL_HEADERS_ENGLISH_TECHNOLOGIES);
        /* utTblValueChangeListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
               updateUtFormField((UpcomingTechnologyBean)event.getProperty().getValue());
            }

        }; */
        
        //t.addValueChangeListener(utTblValueChangeListener);
        relatedTechnologiesTable.sort(new Object[]{"technologyName"}, new boolean[]{true});
        //t.select(upcomingTechnologyTbl.firstItemId());
        return relatedTechnologiesTable;
    }
    
    private void updateRelatedTechnologiesTable() 
    {
        relatedTechnologiesTable.getContainerDataSource().removeAllItems();
        //setTechnologiesList(fetchAllTechnologies());
        relatedTechnologiesTable.setContainerDataSource(CategoryTechnologyContainer.getRelatedTechnologiesContainer(getCategorywiseTechnologyList()));
        relatedTechnologiesTable.setVisibleColumns(CategoryTechnologyContainer.NATURAL_COL_ORDER_TECHNOLOGIES);
        relatedTechnologiesTable.setColumnHeaders(CategoryTechnologyContainer.COL_HEADERS_ENGLISH_TECHNOLOGIES);
        relatedTechnologiesTable.sort(new Object[]{"technologyName"}, new boolean[]{true});
    }
    
    
    //provides input to piechart of related tech nologies
    private HashMap getDataMapForRelatedTechnologiesPieChart(List<CategoryDistributionBean> categorywiseTechnologyList) 
            {
                HashMap map = new HashMap();
                
                for(CategoryDistributionBean bean:categorywiseTechnologyList)
                {
                    map .put(bean.getTechnologyName(), bean.getPercentage());
                }                
                return map;                
            }

    /**
     * @return the categorywiseTechnologyList
     */
    public List<CategoryDistributionBean> getCategorywiseTechnologyList() {
        return categorywiseTechnologyList;
    }

    /**
     * @param categorywiseTechnologyList the categorywiseTechnologyList to set
     */
    public void setCategorywiseTechnologyList(List<CategoryDistributionBean> categorywiseTechnologyList) {
        this.categorywiseTechnologyList = categorywiseTechnologyList;
    }

   
    
}
