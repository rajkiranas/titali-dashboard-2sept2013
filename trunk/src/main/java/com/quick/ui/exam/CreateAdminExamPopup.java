/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.ExamQueAnsBean;
import com.quick.bean.MasteParmBean;
import com.quick.bean.QuickLearn;
import com.quick.bean.Userprofile;
import com.quick.data.MasterDataProvider;
import com.quick.entity.Std;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rajkiran
 */
public class CreateAdminExamPopup extends Window implements Button.ClickListener,Property.ValueChangeListener{
    private ComboBox calsscmb;
    private ComboBox divcmb;
    private ComboBox subcbm;
    private TextField topic;
    private DateField startDate;
    private DateField endDate;
    private TextField marksPerQuestion;
    private TextField passingMarks;
    //private Button descriptiveBtn;
    private TextField question;
    private CheckBox op1chk;
    private CheckBox op2chk;
    private CheckBox op3chk;
    private CheckBox op4chk;
    private TextField op1txt;
    private TextField op2txt;
    private TextField op3txt;
    private TextField op4txt;
    private TextField contestLine;
    private Button createQueBtn;
    private Button finishExamButton;
    private List<Std> standardList;
    private List<QuickLearn> subjectList;
    private List<ExamBean> questionList = new ArrayList<ExamBean>();
    private boolean isNewExam=false;
    private Button nextBtn;
    private List<ExamQueAnsBean> examQueAnsBeanList;
    //private TextArea descAnsTextArea;
    private int listCnt=0; 
    private List<QuickLearn> divisionList;
    private OptionGroup examTypeOpt;
    private TextField examNametxt;
    private static final List<String> examTypeList = Arrays.asList(new String[]{
        "Objective", "Descriptive"});
                //"Objective", "Descriptive","Hybrid"});
    HorizontalSplitPanel baseLayout;
    HorizontalLayout examTypeLayout;
    private Button createExamBtn;
    private int noOfQue = 0;
    private Button previousbtn;
    private AdminExam adminExam;
    
    public List<Std> getStandardList() {
        return standardList;
    }

    public void setStandardList(List<Std> standardList) {
        this.standardList = standardList;
    }

    public List<QuickLearn> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<QuickLearn> subjectList) {
        this.subjectList = subjectList;
    }
    
    
    
    
    public CreateAdminExamPopup(AdminExam adminExam){
        this.adminExam = adminExam;
        setModal(true);
        setCaption("Welcome to create Exam");        
        center();        
       // setClosable(false);
        setWidth("80%");
        setHeight("80%");  
        isNewExam=true;
        BuildUI();
    }
    
   public CreateAdminExamPopup(AdminExam adminExam,ExamBean eb) {
        this.adminExam = adminExam;
        setModal(true);
        setCaption("Welcome to Edit Exam");        
        center();        
       // setClosable(false);
        setWidth("80%");
        setHeight("80%");        
        BuildUI();
        setExamDetailsToForm(eb);
        setExamQuestionAnswersById(eb.getExamId());
            listCnt=getExamQueAnsBeanList().size();
       // to show first question when table value change get call
         getNextQuestion();
      //  setExamQuestionAnswerDetails(getExamQueAnsBeanList());        
    }

    private void BuildUI() {
        setStandardList(MasterDataProvider.getStandardList());
        baseLayout = new HorizontalSplitPanel();
        baseLayout.setSplitPosition(400,Sizeable.UNITS_PIXELS);
        baseLayout.setSizeFull();
        baseLayout.setImmediate(true);
        
        baseLayout.setFirstComponent(getfirstcomponet());
        
        if(this.getCaption().equals("Welcome to Edit Exam"))
        {
              baseLayout.setSecondComponent(getsecondComponent());
              createExamBtn.setVisible(false);
        }
        else
        {
             Label l = new Label("<br><br><br><br><br><h1><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please enter exam details <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;and create the exam.</b></h1>", ContentMode.HTML);
             baseLayout.setSecondComponent(l);       
        }
       
        setContent(baseLayout);
       
    } 
    
    private VerticalLayout getfirstcomponet(){
        
        FormLayout examDetails = new FormLayout();
        
        
       divcmb=new ComboBox("Division");
       divcmb.setInputPrompt("Division");
       divcmb.setNullSelectionAllowed(false);
       divcmb.setImmediate(true);
       divcmb.addItem(GlobalConstants.SELECT);
       divcmb.setValue(GlobalConstants.SELECT);
       
       calsscmb = new ComboBox("Class");
       calsscmb.setInputPrompt("Select");
       calsscmb.setNullSelectionAllowed(false);
       calsscmb.addItem(GlobalConstants.SELECT);
       
       Iterator it=getStandardList().iterator();
        while(it.hasNext()){
            Std s=(Std) it.next();
            calsscmb.addItem(s.getStd());            
        }
        calsscmb.setValue(GlobalConstants.SELECT);
        
        calsscmb.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                 if(!calsscmb.getValue().equals("Select")){
                    String std=String.valueOf(calsscmb.getValue());
                    setSubjectList(MasterDataProvider.getSubjectBystd(std));
                    setDivisionList(MasterDataProvider.getDivisionBystd(std));
                    if(!getSubjectList().isEmpty()){
                         Iterator subit=getSubjectList().iterator();
                         while(subit.hasNext()){
                         QuickLearn s=(QuickLearn) subit.next();
                         subcbm.addItem(s.getSub());            
                       }  
                    }   
                    
                    if(!getDivisionList().isEmpty()){
                         Iterator divit=getDivisionList().iterator();
                         while(divit.hasNext()){
                         QuickLearn s=(QuickLearn) divit.next();
                         divcmb.addItem(s.getFordiv());            
                       }  
                    }
               }
            }
        });
       
       
       
       
       subcbm = new ComboBox("Subject");
       subcbm.setInputPrompt("Select");
       subcbm.setNullSelectionAllowed(false);
       subcbm.addItem(GlobalConstants.SELECT);
       subcbm.setValue(GlobalConstants.SELECT);
       
       examNametxt=new TextField("Exam Name");
       
       examTypeOpt=new OptionGroup("Exam Type",examTypeList);
       examTypeOpt.setStyleName("horizontal");
       examTypeOpt.setRequired(true);
       examTypeOpt.select("Objective");
       examTypeOpt.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            //String examType=(String) event.getProperty().getValue();
            setVisibilityOfQuestionsLayoutAccordingToExamType();
                
            }
           
        });
      
      
       topic =new TextField("Topic");
       startDate = new DateField("Start Date");
       endDate = new DateField("End Date");
       marksPerQuestion =new TextField("Marks per Que");
       passingMarks = new TextField("Passing Marks");
       contestLine = new TextField("Contest-line");
       
        createExamBtn = new Button("Create exam");
        createExamBtn.setStyleName("default");
        createExamBtn.addClickListener(this);
        createExamBtn.setImmediate(true);
        
        examDetails.addComponent(calsscmb);
        examDetails.addComponent(divcmb);
        examDetails.addComponent(subcbm);
        examDetails.addComponent(examNametxt);
        examDetails.addComponent(examTypeOpt);
        examDetails.addComponent(topic);
        examDetails.addComponent(startDate);
        examDetails.addComponent(endDate);
        examDetails.addComponent(marksPerQuestion);
        examDetails.addComponent(passingMarks);
        examDetails.addComponent(contestLine);
        examDetails.addComponent(createExamBtn);
        
        
       VerticalLayout examDetailsVerticallayout = new VerticalLayout();
       examDetailsVerticallayout.setSizeFull();
       
       examDetailsVerticallayout.addComponent(examDetails);
       examDetailsVerticallayout.setComponentAlignment(examDetails, Alignment.MIDDLE_CENTER);
       return examDetailsVerticallayout;
    }

    private VerticalLayout getsecondComponent() {
        
       VerticalLayout  examDetailsBaselayput = new VerticalLayout();
        examDetailsBaselayput.setSizeFull();
      
//         descriptiveBtn = new Button();
//         descriptiveBtn.setVisible(false);
//         descriptiveBtn.setImmediate(true);
//        descriptive.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(ClickEvent event) {
//                    op1chk.setVisible(!op1chk.isVisible());
//                    op2chk.setVisible(!op2chk.isVisible());
//                    op3chk.setVisible(!op3chk.isVisible());
//                    op4chk.setVisible(!op4chk.isVisible());
//                    op1txt.setVisible(!op1txt.isVisible());
//                    op2txt.setVisible(!op2txt.isVisible());
//                    op3txt.setVisible(!op3txt.isVisible());
//                    op4txt.setVisible(!op4txt.isVisible());
////                    if(descriptive.getCaption().equals("Descriptive"))
////                    {
////                         descriptive.setCaption("Objective");
////                    }
////                    else 
////                    {
////                        descriptive.setCaption("Descriptive");
////                        questionans.setVisible(true);
////                    }
//                   
//            }
//        });
        
        question = new TextField("Question");
        question.setCaption("Question");        
        question.setVisible(false);
        question.setInputPrompt("Enter question here");
        
//        descAnsTextArea= new TextArea();
//        descAnsTextArea.setVisible(false);
//        descAnsTextArea.setInputPrompt("Answer");
//        descAnsTextArea.setWidth("100%");

        AbsoluteLayout layout = new AbsoluteLayout();
        layout.setHeight("300px");
        layout.setWidth("500px");
        
        op1chk = new CheckBox();
        op1chk.setVisible(false);
        op1chk.setImmediate(true);
        op1chk.addValueChangeListener(this);
        
        op2chk = new CheckBox();
        op2chk.setVisible(false);
        op2chk.setImmediate(true);
        op2chk.addValueChangeListener(this);
        
        op3chk = new CheckBox();
        op3chk.setVisible(false);
        op3chk.setImmediate(true);
        op3chk.addValueChangeListener(this);
        
        op4chk = new CheckBox();
        op4chk.setVisible(false);
        op4chk.setImmediate(true);
        op4chk.addValueChangeListener(this);
         
        op1txt = new TextField();
        op1txt.setVisible(false);
        op1txt.setInputPrompt("Option 1");
        op2txt = new TextField();
        op2txt.setVisible(false);
        op2txt.setInputPrompt("Option 2");
        op3txt = new TextField();
        op3txt.setVisible(false);
        op3txt.setInputPrompt("Option 3");
        op4txt = new TextField();
        op4txt.setVisible(false);
        op4txt.setInputPrompt("Option 4");
        examTypeLayout = new HorizontalLayout();
       
       if(isNewExam){
            createQueBtn = new Button("Create Question");
            createQueBtn.addClickListener((Button.ClickListener)this);
            createQueBtn.setImmediate(true);
            createQueBtn.setStyleName("default");
            
            finishExamButton = new Button("Finish");
            finishExamButton.addClickListener((Button.ClickListener)this);
            finishExamButton.setImmediate(true);
            finishExamButton.setStyleName("default");
            
            layout.addComponent(createQueBtn, "left:50px;top:220px;");
            layout.addComponent(finishExamButton, "left:210px;top:220px;");
            
            createQueBtn.setVisible(false);
            finishExamButton.setVisible(false);
       }else{
           nextBtn = new Button("Next");
           nextBtn.addClickListener((Button.ClickListener)this);
           nextBtn.setImmediate(true);
           nextBtn.setStyleName("default");  
           
           previousbtn = new Button("Previous");
           previousbtn.addClickListener((Button.ClickListener)this);
           previousbtn.setImmediate(true);
           previousbtn.setStyleName("default");  
           layout.addComponent(nextBtn, "left:50px;top:220px;");
           layout.addComponent(previousbtn, "left:140px;top:220px;");
       }       
       
        examTypeLayout.addComponent(question);       
        layout.addComponent(examTypeLayout, "left:50px;top:0px;");
        
        //layout.addComponent(descAnsTextArea, "left:50px;top:60px;");     
        
        layout.addComponent(op1chk, "left:20px;top:60px;");
        layout.addComponent(op1txt, "left:50px;top:60px;");
        
        layout.addComponent(op2chk, "left:20px;top:95px;");
        layout.addComponent(op2txt, "left:50px;top:95px;");
        
        layout.addComponent(op3chk, "left:20px;top:130px;");
        layout.addComponent(op3txt, "left:50px;top:130px;");
        
        layout.addComponent(op4chk, "left:20px;top:165px;");
        layout.addComponent(op4txt, "left:50px;top:165px;");  
        
//        layout.addComponent(descriptiveBtn,"left:265px;top:20px;");
       

        examDetailsBaselayput.addComponent(layout);
        examDetailsBaselayput.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

        
       setVisibilityOfQuestionsLayoutAccordingToExamType();
       //examTypeOpt.setReadOnly(true); because exam type cannot be changed once exam created
        examTypeOpt.setReadOnly(true);
        return examDetailsBaselayput;      
    }

   
    @Override
    public void buttonClick(ClickEvent event) {
        Button btn = event.getButton();
        if(btn==createQueBtn){
            
            createNewQuestion();
            noOfQue++;
            
        }
        else if(btn == finishExamButton)
        {
           createExam(); 
        }
        else if(btn==nextBtn)
        {   
            getNextQuestion();
             
        }else if(btn==previousbtn){
            getPreviouseQuestion();
        }
        else if(btn == createExamBtn){
            if(validateExmDtls()){
                 baseLayout.setSecondComponent(getsecondComponent());
                 
            }
           
        }
    }

    private void createNewQuestion() {
        if(validateQuesAnsForm()){
            
         ExamBean examBean = new ExamBean();
         examBean.setQueString(question.getValue());
         examBean.setOption1(op1txt.getValue());
         examBean.setOption2(op2txt.getValue());
         examBean.setOption3(op3txt.getValue());
         examBean.setOption4(op4txt.getValue());
         examBean.setMarksPerQuestion(Integer.parseInt(marksPerQuestion.getValue()));
         
           if(examTypeOpt.getValue().equals("Objective")){
                 examBean.setExType(1);
            }else if(examTypeOpt.getValue().equals("Descriptive")){
                 examBean.setExType(2);
            }else{
                 examBean.setExType(3);
            }
         
         if(op1chk.getValue())
         {
              examBean.setAns(op1txt.getValue());
         }
         else if(op2chk.getValue())
         {
              examBean.setAns(op2txt.getValue());
         }
         else if(op3chk.getValue())
         {
             examBean.setAns(op3txt.getValue());
         }
         else
         {
             examBean.setAns(op4txt.getValue());
         }
        
          addIntoQuestionList(examBean);
          //belwo passed to new checkbox is dummy to reset values of existing checkboxes
          checkSelectedAnswer(new CheckBox());
          op1txt.setValue(GlobalConstants.emptyString);
          op2txt.setValue(GlobalConstants.emptyString);
          op3txt.setValue(GlobalConstants.emptyString);
          op4txt.setValue(GlobalConstants.emptyString);
          question.setValue(GlobalConstants.emptyString); 
       
        }
        
    }

    private void addIntoQuestionList(ExamBean examBean)
    {
        questionList.add(examBean);
    }

    private void createExam() {

        //exam nmae and exam type is reamining to change
        if (validateQuesAnsForm()) {
            try {
                JSONObject examJSONObject = new JSONObject();
                examJSONObject.put("std", calsscmb.getValue());
                examJSONObject.put("sub", subcbm.getValue());
                examJSONObject.put("topic", topic.getValue());
                examJSONObject.put("startDt", startDate.getValue());
                examJSONObject.put("endDt", endDate.getValue());
                examJSONObject.put("passingMarks", passingMarks.getValue());
                examJSONObject.put("marksForQuestion", marksPerQuestion.getValue());
                examJSONObject.put("exType", examTypeOpt.getValue());
                examJSONObject.put("contestline", contestLine.getValue());
                examJSONObject.put("noOfQuestions", noOfQue);
                examJSONObject.put("createdBy", ((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile)).getName());
                examJSONObject.put("exName", examNametxt.getValue());


                if (examTypeOpt.getValue().equals("Objective")) {
                    examJSONObject.put("exType", 1);
                } else if (examTypeOpt.getValue().equals("Descriptive")) {
                    examJSONObject.put("exType", 2);
                } else {
                    examJSONObject.put("exType", 3);
                }

                examJSONObject.put("fordiv", divcmb.getValue());



                Gson gson = new Gson();
                String json = gson.toJson(questionList);
                examJSONObject.put(GlobalConstants.EXAMQUESTIONLIST, json);
                saveExamDetails(examJSONObject);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }finally{
                adminExam.updateExamList();
                CreateAdminExamPopup.this.close();
            }
        }

    }

    private void saveExamDetails(JSONObject inputJson) {       
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.CREATE_EXAM_URL));
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            String output = response.getEntity(String.class);
      
    }

    private void setExamDetailsToForm(ExamBean eb) {
        
        calsscmb.setValue(eb.getStd()); 
        divcmb.setValue(eb.getFordiv()); 
        subcbm.setValue(eb.getSub());
        examNametxt.setValue(eb.getExName());
        examTypeOpt.setReadOnly(false);
        
        if(eb.getExType()==1){
           examTypeOpt.setValue("Objective"); 
        }else if(eb.getExType()==2){
           examTypeOpt.setValue("Descriptive");
        }
        /* else{
           examTypeOpt.setValue("Hybrid");  
        } */
        
        examTypeOpt.setReadOnly(true);        
        topic.setValue(eb.getTopic());
        startDate.setValue(eb.getStartDt());
        endDate.setValue(eb.getEndDt());
        marksPerQuestion .setValue(String.valueOf(eb.getMarksPerQuestion()));
        passingMarks.setValue(String.valueOf(eb.getPassingMarks()));
        contestLine.setValue(String.valueOf(eb.getNoOfQuestions()));
    }

    private void setExamQuestionAnswersById(int examId) {
        setExamQueAnsBeanList(getExamQuestionAnswersById(examId));
    }

    public List<ExamQueAnsBean> getExamQueAnsBeanList() {
        return examQueAnsBeanList;
    }

    public void setExamQueAnsBeanList(List<ExamQueAnsBean> examQueAnsBeanList) {
        this.examQueAnsBeanList = examQueAnsBeanList;
    }

    private List<ExamQueAnsBean> getExamQuestionAnswersById(int examId) {
        
         List<ExamQueAnsBean> examList = null;
        try 
        {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8084/titali/rest/examResource/getExamQuestionById");
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
                inputJson.put("exmId", examId);               
             }catch(Exception ex)
             {                 
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<ExamQueAnsBean>>() {
            }.getType();
            
            examList= new Gson().fromJson(outNObject.getString(GlobalConstants.EXAMLIST), listType);
        } catch (JSONException ex)
        {
          //  Logger.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return examList;
    }

    private void setExamQuestionAnswerDetails(List<ExamQueAnsBean> examQueAnsBeanList) {   
        
        ExamQueAnsBean bean=examQueAnsBeanList.get(--listCnt);
           if(bean.getExType()==1){
//                descriptiveBtn.setCaption("Objective");  
                op1txt.setValue(bean.getOption1());
                op2txt.setValue(bean.getOption2());
                op3txt.setValue(bean.getOption3());
                op4txt.setValue(bean.getOption4());
           }
//           else{
//                //descriptiveBtn.setCaption("Descriptive");  
//               // descAnsTextArea.setVisible(true);
//                descAnsTextArea.setValue(""+bean.getAns());                
//           }            
                question.setValue(""+bean.getQuestion());
    }
    
    public List<QuickLearn> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(List<QuickLearn> divisionList) {
        this.divisionList = divisionList;
    }

   

    private void setVisibilityOfQuestionsLayoutAccordingToExamType() {
        
        String examType = GlobalConstants.emptyString + examTypeOpt.getValue();


        if (createQueBtn != null) {
            createQueBtn.setVisible(true);
        }
        if (finishExamButton != null) {
            finishExamButton.setVisible(true);
        }


        if (examType.equals("Objective")) {

            //baseLayout.removeComponent(baseLayout.getSecondComponent()); 
            question.setVisible(true);            
            op1chk.setVisible(true);
            op2chk.setVisible(true);
            op3chk.setVisible(true);
            op4chk.setVisible(true);
            op1txt.setVisible(true);
            op2txt.setVisible(true);
            op3txt.setVisible(true);
            op4txt.setVisible(true);
            
//            descAnsTextArea.setVisible(false);
//            descriptiveBtn.setVisible(false);
//            descriptiveBtn.setCaption("Objective");

        }

        else if (examType.equals("Descriptive")) {
            if (baseLayout.getSecondComponent() != null) {
                //  baseLayout.removeComponent(baseLayout.getSecondComponent()); 
//                descriptiveBtn.setVisible(false);
//                descriptiveBtn.setCaption("Descriptive");
                question.setVisible(true);
                
                op1chk.setVisible(false);
            op2chk.setVisible(false);
            op3chk.setVisible(false);
            op4chk.setVisible(false);
            op1txt.setVisible(false);
            op2txt.setVisible(false);
            op3txt.setVisible(false);
            op4txt.setVisible(false);
                //descAnsTextArea.setVisible(false);

            }
        }
        /* else if (examType.equals("Hybrid")) {
            if (baseLayout.getSecondComponent() != null) {
                //  baseLayout.removeComponent(baseLayout.); 
                descriptiveBtn.setVisible(true);
                descriptiveBtn.setCaption("Objective");
                question.setVisible(true);

                descriptiveBtn.setCaption("Descriptive");
                op1chk.setVisible(true);
                op2chk.setVisible(true);
                op3chk.setVisible(true);
                op4chk.setVisible(true);
                op1txt.setVisible(true);
                op2txt.setVisible(true);
                op3txt.setVisible(true);
                op4txt.setVisible(true);
                descAnsTextArea.setVisible(false);



                descriptiveBtn.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {

                        if (descriptiveBtn.getCaption().equals("Objective")) {
                            descriptiveBtn.setCaption("Descriptive");
                            op1chk.setVisible(true);
                            op2chk.setVisible(true);
                            op3chk.setVisible(true);
                            op4chk.setVisible(true);
                            op1txt.setVisible(true);
                            op2txt.setVisible(true);
                            op3txt.setVisible(true);
                            op4txt.setVisible(true);
                            descAnsTextArea.setVisible(false);
                        } else {
                            descriptiveBtn.setCaption("Objective");
                            descAnsTextArea.setVisible(false);
                            op1chk.setVisible(false);
                            op2chk.setVisible(false);
                            op3chk.setVisible(false);
                            op4chk.setVisible(false);
                            op1txt.setVisible(false);
                            op2txt.setVisible(false);
                            op3txt.setVisible(false);
                            op4txt.setVisible(false);
                        }
                    }
                });


            }
        } */
    }
    
    
     private boolean validateQuesAnsForm()
    {
        
        if(question.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter Question.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(op1txt.isVisible() && op1txt.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter option1.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(op2txt.isVisible() && op2txt.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter option2.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else if(op3txt.isVisible() && op3txt.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter option3.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else if(op4txt.isVisible() && op4txt.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter option4.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else if((op1chk.isVisible()) && !(op1chk.getValue()||op2chk.getValue()||op3chk.getValue()||op4chk.getValue()))
        {
                Notification.show("Please enter correct answer.", Notification.Type.WARNING_MESSAGE);
                return false;
        }
        else
        {
            return true;
        }
    }
    
    
    

    private boolean validateExmDtls() {
        if(calsscmb.getValue().equals(GlobalConstants.SELECT)){
            Notification.show("Please select class.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(divcmb.getValue().equals(GlobalConstants.SELECT)){
            Notification.show("Please select division", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(subcbm.getValue().equals(GlobalConstants.SELECT)){
            Notification.show("Please select subject.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else if(examNametxt.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter exam name .", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(topic.getValue().trim().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter exam topic.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(startDate.getValue()==null){
            Notification.show("Please enter start date.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else if(endDate.getValue()==null){
            Notification.show("Please enter end date.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else if(marksPerQuestion.getValue().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter marks per question", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(passingMarks.getValue().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter passing marks.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else if(contestLine.getValue().equals(GlobalConstants.emptyString)){
            Notification.show("Please enter contest line.", Notification.Type.WARNING_MESSAGE);
            return false;
        } 
        else
        {
            return true;
        }
    }

    private void getNextQuestion() {
        if(listCnt>0){
                ExamQueAnsBean bean=getExamQueAnsBeanList().get(--listCnt);
               
                    if(bean.getExType()==1){
                            //descriptiveBtn.setCaption("Objective");  
                            op1txt.setValue(bean.getOption1());
                            op2txt.setValue(bean.getOption2());
                            op3txt.setValue(bean.getOption3());
                            op4txt.setValue(bean.getOption4());
                            
                             if(op1txt.getValue().equals(bean.getAns()))
                            {
                                 checkSelectedAnswer(op1chk);
                            }
                            else if(op2txt.getValue().equals(bean.getAns()))
                            {
                                checkSelectedAnswer(op2chk);
                            }
                            else if(op3txt.getValue().equals(bean.getAns()))
                            {
                                checkSelectedAnswer(op3chk);
                            }
                            else
                            {
                                checkSelectedAnswer(op4chk);
                            }
                            
                            
                    }
//                    else{
//                            //descriptiveBtn.setCaption("Descriptive"); 
//                           // descAnsTextArea.setVisible(true);
//                            //descAnsTextArea.setValue(""+bean.getAns());
//                    }            
                    question.setValue(""+bean.getQuestion()); 
            }else{
                Notification.show("No other questions available");
            }          
    }
    
    
      private void getPreviouseQuestion() {
        if(listCnt<getExamQueAnsBeanList().size()-1){
                ExamQueAnsBean bean=getExamQueAnsBeanList().get(++listCnt);
               
                    if(bean.getExType()==1){
//                            descriptiveBtn.setCaption("Objective");  
                            op1txt.setValue(bean.getOption1());
                            op2txt.setValue(bean.getOption2());
                            op3txt.setValue(bean.getOption3());
                            op4txt.setValue(bean.getOption4());
                            
                            if(op1txt.getValue().equals(bean.getAns()))
                            {
                                 checkSelectedAnswer(op1chk);
                            }
                            else if(op2txt.getValue().equals(bean.getAns()))
                            {
                                checkSelectedAnswer(op2chk);
                            }
                            else if(op3txt.getValue().equals(bean.getAns()))
                            {
                                checkSelectedAnswer(op3chk);
                            }
                            else
                            {
                                checkSelectedAnswer(op4chk);
                            }
                            
                    }
//                    else{
//                            descriptiveBtn.setCaption("Descriptive"); 
//                           // descAnsTextArea.setVisible(true);
//                            descAnsTextArea.setValue(""+bean.getAns());
//                    }            
                    question.setValue(""+bean.getQuestion()); 
            }else{
                Notification.show("No other questions available");
            }          
    }
      
      public void checkSelectedAnswer(CheckBox chkbox){
          removeValuChangeListnerFromOptChk(op1chk);
          removeValuChangeListnerFromOptChk(op2chk);
          removeValuChangeListnerFromOptChk(op3chk);
          removeValuChangeListnerFromOptChk(op4chk);
          
          op1chk.setValue(false);
          op2chk.setValue(false);
          op3chk.setValue(false);
          op4chk.setValue(false);
          chkbox.setValue(true);
          
          addValuChangeListnerToOptChk(op1chk);
          addValuChangeListnerToOptChk(op2chk);
          addValuChangeListnerToOptChk(op3chk);
          addValuChangeListnerToOptChk(op4chk);
      }
      
      private void removeValuChangeListnerFromOptChk(CheckBox chkbox){
          chkbox.removeValueChangeListener(this);
      }
      
       private void addValuChangeListnerToOptChk(CheckBox chkbox){
          chkbox.addValueChangeListener(this);
      }

    @Override
    public void valueChange(ValueChangeEvent event) {
        CheckBox c = (CheckBox) event.getProperty();
        checkSelectedAnswer(c);
    }
    
}