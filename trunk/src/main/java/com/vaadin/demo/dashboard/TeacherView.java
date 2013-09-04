/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.MasteParmBean;
import com.quick.bean.Userprofile;
import com.quick.global.GlobalConstants;
import com.quick.table.TeacherTable;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.quick.data.MasterDataProvider;
import com.quick.table.QuickUploadTable;
import com.quick.utilities.ConfirmationDialogueBox;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author vmundhe
 */
public class TeacherView extends VerticalLayout implements View,Button.ClickListener,Property.ValueChangeListener{

    private TabSheet editors;
    private TeacherTable teacherTable;
    private Button addNewTeacherbtn;
    private List<Userprofile> teacherList;
    private Button filterBtn;
    private List<Userprofile> teacherFilterList;
    private Button allTeacherBtn;
    
    public List<Userprofile> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Userprofile> teacherList) {
        this.teacherList = teacherList;
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        setTeacherList(MasterDataProvider.getAllTeacherList());
        addComponent(buildBaseTeacher());  
    }

    private Component buildBaseTeacher() {
        editors = new TabSheet();
        editors.setSizeFull();       
        
        final CssLayout center = new CssLayout();
        center.setSizeFull();
        center.setCaption("All Teachers");        
         
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");        
        toolbar.setHeight("-1px");
        toolbar.setSpacing(false);
        toolbar.setMargin(false);
        center.addComponent(toolbar);     
       
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setMargin(true);
        buttons.setSpacing(true);
        buttons.setSizeUndefined();
        
        allTeacherBtn=new Button("All Teacher",(Button.ClickListener)this);
        allTeacherBtn.addStyleName("default");
        allTeacherBtn.setImmediate(true);
        buttons.addComponent(allTeacherBtn);      
        
        filterBtn=new Button("Search Teacher",(Button.ClickListener)this);
        filterBtn.addStyleName("default");
        filterBtn.setImmediate(true);
        buttons.addComponent(filterBtn);       
        
        addNewTeacherbtn=new Button("Add Teacher",(Button.ClickListener)this);        
        addNewTeacherbtn.addStyleName("default");
        addNewTeacherbtn.setImmediate(true);
        buttons.addComponent(addNewTeacherbtn);       
        toolbar.addComponent(buttons);
        toolbar.setComponentAlignment(buttons, Alignment.TOP_RIGHT); 
             
        center.addComponent(buildStudentTable());
        editors.addComponent(center);        
        return editors;
    }
    
    private Table buildStudentTable() {
        return teacherTable=new TeacherTable(this);
    }

    public TeacherMasterContainer getTeacherMasterContainer() {
        return TeacherMasterContainer.getAllTeacherList(getTeacherList());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        final Button source=event.getButton();
        if(source==addNewTeacherbtn){
            Window w = new AddTeacher(this);
            UI.getCurrent().addWindow(w);
            w.focus();
        }else if(source==filterBtn){
            Window w = new SearchTeacherFilter(this);
            UI.getCurrent().addWindow(w);
            w.focus();
        }else if(source==allTeacherBtn){
            refreshTeacherList();
        }
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
       Property property=event.getProperty();
        if(property==teacherTable){            
            int prn=0;
            Set<Userprofile> userprofiles=(Set<Userprofile>) property.getValue();
            for(Userprofile u:userprofiles){
              prn=u.getPrn();  
            }            
            setTeacherList(getTeacherDetailsByPrn(prn));
            Window w = new AddTeacher(this,getTeacherList());
            UI.getCurrent().addWindow(w);
            w.focus();
        }
    }
    
    
    public static List<Userprofile> getTeacherDetailsByPrn(int prn) {
         List<Userprofile> teacherList = null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8084/titali/rest/UserMaster/getTeacherDetailsByPrn");
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("prn",prn);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }            
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<Userprofile>>() {
            }.getType();
            Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            teacherList= gson.fromJson(outNObject.getString(GlobalConstants.teacher), listType);
        } catch (JSONException ex) {
           // Logger.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return teacherList;
            
    }

    public TeacherTable getTeacherTable() {
        return teacherTable;
    }
    
    public void searchFilterCriteria(String searchCriteria, String searchValue) {
        searchStudentFilterCriteria(searchCriteria,searchValue);
        
   }

    private void searchStudentFilterCriteria(String searchCriteria, String searchValue) {
       teacherFilterList=getsearchTeacherFilterCriteria(searchCriteria,searchValue);
       if(teacherFilterList!=null && !teacherFilterList.isEmpty()){
           teacherTable.removeAllItems();
           teacherTable.setContainerDataSource(TeacherMasterContainer.getAllTeacherList(teacherFilterList));
           teacherTable.setVisibleColumns(TeacherMasterContainer.NATURAL_COL_ORDER_TEACHER_INFO);
           teacherTable.setColumnHeaders(TeacherMasterContainer.COL_HEADERS_ENGLISH_TEACHER_INFO);
       }else{
           Notification.show("No such record found",Notification.Type.WARNING_MESSAGE);           
       }
    }

  private List<Userprofile> getsearchTeacherFilterCriteria(String searchCriteria, String searchValue) {
      List<Userprofile> searchTeacherList = null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8084/titali/rest/UserMaster/getsearchTeacherFilterCriteria");
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("searchCriteria",searchCriteria);
                inputJson.put("searchValue",searchValue);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }            
            
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<Userprofile>>() {
            }.getType();
            
            searchTeacherList= new Gson().fromJson(outNObject.getString(GlobalConstants.TEACHERLIST), listType);
        } catch (JSONException ex) {
          //  Logger.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return searchTeacherList;
            
    }

    public void addListenertoBtn(Button btnRemove) 
    {
        
        btnRemove.addListener(new Button.ClickListener() 
        {
            public void buttonClick(final Button.ClickEvent event) 
            {
                //UI.getCurrent().addWindow(new ConfirmationDialogueBox());
                
                UI.getCurrent().addWindow(new ConfirmationDialogueBox("Confirmation", "Are you sure you want to remove this teacher ?", new ConfirmationDialogueBox.Callback() {

                    @Override
                    public void onDialogResult(boolean flag) 
                    {
                        if(flag)
                        {
                            
                            Object data[] = (Object[]) event.getButton().getData();
                            
                            TeacherTable t = (TeacherTable) data[0];
                            t.removeValueChangeListener(TeacherView.this);
                            
                            deleteTeacherFromDB((Userprofile)data[1]);
                            
                            // temporary removing value change listener of the quick upload table so that after removing item it will not attempt to display that particular item
                          //  t.removeValueChangeListener(QuickUpload.this);
        
                          //  t.removeItem(data[1]);
                            
                            //restoring the value change listener so that selected uploaded item will be displayed in the right panel
                          //  t.addValueChangeListener(QuickUpload.this);
                            t.select(t.firstItemId());
                            
                            t.addValueChangeListener(TeacherView.this);
                        }
                    }

                   
                }));
            }
        });
        
        
    }
     
    
        private void deleteTeacherFromDB(Userprofile userprofile) {
            try {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.DELETE_TEACHER_FROM_DB));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
            try {
                inputJson.put("username", userprofile.getUsername());
                inputJson.put("prn", userprofile.getPrn());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            String output = response.getEntity(String.class);
            JSONObject output1 = new JSONObject(output);
            if(output1.get(GlobalConstants.STATUS).equals(String.valueOf(GlobalConstants.YES))){
                Notification.show("Delete Successful");
            }else{
                 Notification.show("Delete Failed");
            }

        } catch (Exception ex) {
            Notification.show("Delete Failed");
            ex.printStackTrace();
        }
        }
        
        
       
    
     public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{this,rowItemBean,""};
            btnRemove.setData(arr);
        }

    private void refreshTeacherList() {
           teacherTable.removeAllItems();
           teacherTable.setContainerDataSource(TeacherMasterContainer.getAllTeacherList(MasterDataProvider.getAllTeacherList()));
           teacherTable.setVisibleColumns(TeacherMasterContainer.NATURAL_COL_ORDER_TEACHER_INFO);
           teacherTable.setColumnHeaders(TeacherMasterContainer.COL_HEADERS_ENGLISH_TEACHER_INFO);
           teacherTable.addGeneratedColumn("Remove", new Table.ColumnGenerator() 
            {
            @Override
                public Object generateCell(Table source, Object rowItemBean, Object columnId) 
                {
                    Button btnRemove=new Button("Remove");
                    btnRemove.setImmediate(true);
                    btnRemove.setStyleName(BaseTheme.BUTTON_LINK);
                    setItemData(btnRemove,rowItemBean);
                    addListenertoBtn(btnRemove);               
                    return btnRemove;
                }
            });
    }
    }
    


