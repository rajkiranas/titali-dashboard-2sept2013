/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.ExamQueAnsBean;
import com.quick.bean.QuickLearn;
import com.quick.bean.Userprofile;
import com.quick.data.MasterDataProvider;
import com.quick.entity.Std;
import com.quick.global.GlobalConstants;
import com.quick.utilities.UIUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.lang.reflect.Type;
import java.util.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rajkiran
 */
public class CreateStudExamPopup extends Window implements Button.ClickListener,Property.ValueChangeListener{
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
    private List<ExamBean> questionAnswerList;
    private Map<String,ExamBean> questionAnswerMap = new HashMap<String,ExamBean>();
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
    private VerticalSplitPanel baseHorizontalSplit;
    
    private Button createExamBtn;
    private int noOfQue = 0;
    private Button previousbtn;
    private StudentExam studExam;
    private String studExamCaption;
    private String loggedInUser;
    
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
    
    
    
    
    public CreateStudExamPopup(StudentExam studnExam){
        this.studExam = studnExam;
        setModal(true);
        setCaption("Welcome to create Exam");        
        center();        
       // setClosable(false);
        setWidth("80%");
        setHeight("95%");  
        isNewExam=true;
        BuildUI();
    }
    
   public CreateStudExamPopup(StudentExam studExam,ExamBean eb, String studExamCaption,String username) {
       this.loggedInUser=username;
       this.studExamCaption=studExamCaption;
        this.studExam = studExam;
        setModal(true);
        setCaption(GlobalConstants.startExam);
        center();        
       // setClosable(false);
        if(this.studExamCaption.equals(GlobalConstants.viewExam))
        {
            isNewExam=false;
        }
        else
        {
            isNewExam=true;
        }
        
        setWidth("80%");
        setHeight("95%");        
        BuildUI();
        setExamDetailsToForm(eb);
        setExamQuestionAnswersById(eb.getExamId());
        listCnt=getExamQueAnsBeanList().size();
       // to show first question when table value change get call
         getNextQuestion();
      //  setExamQuestionAnswerDetails(getExamQueAnsBeanList());        
    }

    private void BuildUI() 
    {
        setStandardList(MasterDataProvider.getStandardList());
        baseHorizontalSplit = new VerticalSplitPanel();
        baseHorizontalSplit.setSplitPosition(40,Unit.PERCENTAGE);
        baseHorizontalSplit.setSizeFull();
        baseHorizontalSplit.setImmediate(true);
        
        baseHorizontalSplit.setFirstComponent(getfirstcomponet());
        
        baseHorizontalSplit.setSecondComponent(getsecondComponent());
        createExamBtn.setVisible(false);
        
//        Label l = new Label("<br><br><br><br><br><h1><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please enter exam details <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;and create the exam.</b></h1>", ContentMode.HTML);
//        baseLayout.setSecondComponent(l);       
       
        setContent(baseHorizontalSplit);
    } 
    
    private VerticalLayout getfirstcomponet(){
        
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
        
        
        VerticalLayout examDetailsVerticallayout = new VerticalLayout();
       examDetailsVerticallayout.setSizeFull();
       examDetailsVerticallayout.setCaption("Exam details");
        
       HorizontalLayout examDetails = new HorizontalLayout();
       examDetails.setSpacing(true);
        examDetails.setMargin(true);
        examDetails.setSizeFull();
        
        examDetails.addComponent(calsscmb);
        examDetails.addComponent(divcmb);
        examDetails.addComponent(subcbm);
        examDetails.addComponent(examNametxt);
        
        examDetailsVerticallayout.addComponent(examDetails);
        examDetailsVerticallayout.setComponentAlignment(examDetails, Alignment.MIDDLE_CENTER);
        
        examDetails = new HorizontalLayout();
        examDetails.setSpacing(true);
        examDetails.setMargin(true);
        examDetails.setSizeFull();
        
        
        
        examDetails.addComponent(topic);
        examDetails.addComponent(startDate);
        examDetails.addComponent(endDate);
        examDetails.addComponent(examTypeOpt);
        examDetailsVerticallayout.addComponent(examDetails);
        examDetailsVerticallayout.setComponentAlignment(examDetails, Alignment.MIDDLE_CENTER);
        
        examDetails = new HorizontalLayout();
        examDetails.setSpacing(true);
        examDetails.setMargin(true);
        examDetails.setSizeFull();
        
        examDetails.addComponent(marksPerQuestion);
        examDetails.addComponent(passingMarks);
        examDetails.addComponent(contestLine);
        examDetails.addComponent(createExamBtn);
       
       examDetailsVerticallayout.addComponent(examDetails);
       examDetailsVerticallayout.setComponentAlignment(examDetails, Alignment.MIDDLE_CENTER);
       
//       CssLayout cl= UIUtils.createPanel(examDetailsVerticallayout);
//       cl.setCaption("Exam details");
       return examDetailsVerticallayout;
    }

    private VerticalLayout getsecondComponent() {
        
       
      
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
        question.setWidth("70%");
        
//        descAnsTextArea= new TextArea();
//        descAnsTextArea.setVisible(false);
//        descAnsTextArea.setInputPrompt("Answer");
//        descAnsTextArea.setWidth("100%");

        VerticalLayout vertical = new VerticalLayout();
        vertical.setSpacing(true);
        vertical.setCaption("Question-Answers");
        vertical.setMargin(true);
        
        op1chk = new CheckBox();
        op1chk.setVisible(false);
        op1chk.setImmediate(true);
        op1chk.addValueChangeListener(this);
        op1chk.setEnabled(isNewExam);
        
        op2chk = new CheckBox();
        op2chk.setVisible(false);
        op2chk.setImmediate(true);
        op2chk.addValueChangeListener(this);
        op2chk.setEnabled(isNewExam);
        
        op3chk = new CheckBox();
        op3chk.setVisible(false);
        op3chk.setImmediate(true);
        op3chk.addValueChangeListener(this);
        op3chk.setEnabled(isNewExam);
        
        op4chk = new CheckBox();
        op4chk.setVisible(false);
        op4chk.setImmediate(true);
        op4chk.addValueChangeListener(this);
        op4chk.setEnabled(isNewExam);
         
        op1txt = new TextField();
        op1txt.setVisible(false);
        op1txt.setInputPrompt("Option 1");
        op1txt.setWidth("60%");
        
        op2txt = new TextField();
        op2txt.setVisible(false);
        op2txt.setInputPrompt("Option 2");
        op2txt.setWidth("60%");
        
        op3txt = new TextField();
        op3txt.setVisible(false);
        op3txt.setInputPrompt("Option 3");
        op3txt.setWidth("60%");
        
        op4txt = new TextField();
        op4txt.setVisible(false);
        op4txt.setInputPrompt("Option 4");
        op4txt.setWidth("60%");
        
        vertical.addComponent(getHorizontalLayoutForTwoComponents(new Label(),question));
        
        vertical.addComponent(getHorizontalLayoutForTwoComponents(op1chk,op1txt));
        
        vertical.addComponent(getHorizontalLayoutForTwoComponents(op2chk,op2txt));
        
        vertical.addComponent(getHorizontalLayoutForTwoComponents(op3chk,op3txt));
        
        vertical.addComponent(getHorizontalLayoutForTwoComponents(op4chk,op4txt));
        
       
        HorizontalLayout buttonHorizontal = new HorizontalLayout();
        buttonHorizontal.setSpacing(true);
        buttonHorizontal.setMargin(true);
        
       if(isNewExam){
           
           previousbtn = new Button("Previous");
           previousbtn.addClickListener((Button.ClickListener)this);
           previousbtn.setImmediate(true);
           previousbtn.setStyleName(GlobalConstants.default_style);  
           
           nextBtn = new Button("Next");
           nextBtn.addClickListener((Button.ClickListener)this);
           nextBtn.setImmediate(true);
           nextBtn.setStyleName(GlobalConstants.default_style);  
            
            finishExamButton = new Button("Finish");
            finishExamButton.addClickListener((Button.ClickListener)this);
            finishExamButton.setImmediate(true);
            finishExamButton.setStyleName(GlobalConstants.default_style);
            
            buttonHorizontal.addComponent(previousbtn);
            buttonHorizontal.addComponent(nextBtn);
            buttonHorizontal.addComponent(finishExamButton);
            
            buttonHorizontal.setComponentAlignment(previousbtn, Alignment.MIDDLE_CENTER);
            buttonHorizontal.setComponentAlignment(nextBtn, Alignment.MIDDLE_CENTER);
            buttonHorizontal.setComponentAlignment(finishExamButton, Alignment.MIDDLE_CENTER);
            
            /* createQueBtn.setVisible(true);
            finishExamButton.setVisible(true); */
       }else{
           nextBtn = new Button("Next");
           nextBtn.addClickListener((Button.ClickListener)this);
           nextBtn.setImmediate(true);
           nextBtn.setStyleName(GlobalConstants.default_style);  
           
           previousbtn = new Button("Previous");
           previousbtn.addClickListener((Button.ClickListener)this);
           previousbtn.setImmediate(true);
           previousbtn.setStyleName(GlobalConstants.default_style);
           
           buttonHorizontal.addComponent(previousbtn);
           buttonHorizontal.addComponent(nextBtn);
           
           buttonHorizontal.setComponentAlignment(previousbtn, Alignment.MIDDLE_CENTER);
            buttonHorizontal.setComponentAlignment(nextBtn, Alignment.MIDDLE_CENTER);
       }
       

//       CssLayout cl=UIUtils.createPanel(vertical);
//        cl.setCaption("Question-Answers");
//        cl.setWidth("98%");
//        cl.setHeight("98%");
        
//        CssLayout clButton=UIUtils.createPanel(buttonHorizontal);
//        
//        clButton.setWidth("98%");
//        clButton.setHeight("98%");
        
        
//        VerticalLayout  examDetailsBaselayput = new VerticalLayout();
//        examDetailsBaselayput.setSizeFull();
//        
//        examDetailsBaselayput.addComponent(cl);
//        examDetailsBaselayput.setComponentAlignment(cl, Alignment.BOTTOM_CENTER);
//        examDetailsBaselayput.setExpandRatio(cl, 2);
//        
//        examDetailsBaselayput.addComponent(clButton);
//        examDetailsBaselayput.setComponentAlignment(clButton, Alignment.TOP_CENTER);
//        examDetailsBaselayput.setExpandRatio(clButton, 1);
        
        
//       
//        examDetailsBaselayput.addComponent(vertical);
//        examDetailsBaselayput.setComponentAlignment(vertical, Alignment.MIDDLE_CENTER);

       
       vertical.addComponent(buttonHorizontal);
       vertical.setComponentAlignment(buttonHorizontal, Alignment.TOP_CENTER);
       setVisibilityOfQuestionsLayoutAccordingToExamType();
       //examTypeOpt.setReadOnly(true); because exam type cannot be changed once exam created
        examTypeOpt.setReadOnly(true);
        return vertical;      
    }
    
    private Component getHorizontalLayoutForTwoComponents(Component a, Component b)
    {
        HorizontalLayout h = new HorizontalLayout();
        
        h.setSizeFull();
        Label dummy = new Label(GlobalConstants.emptyString);
        
        h.addComponent(dummy);
        h.addComponent(a);
        h.addComponent(b);
        
        h.setExpandRatio(dummy, 1.25f);
        h.setExpandRatio(a, 0.25f);
        h.setExpandRatio(b, 5f);
        
        return h;
    }

   
    @Override
    public void buttonClick(ClickEvent event) {
        Button btn = event.getButton();
        if(btn==createQueBtn){
            if(validateQuesAnsForm())
            {
                createQuestionAnswerResponse();
                noOfQue++;
            }
            
            
        }
        else if(btn == finishExamButton)
        {
            if(validateQuesAnsForm())
            {
                createQuestionAnswerResponse();
                submitStudentExamResponseToDB(); 
            }
        }
        else if(btn==nextBtn)
        {
            if(validateQuesAnsForm())
            {
                createQuestionAnswerResponse();
                noOfQue++;
                getNextQuestion();
            }
             
        }else if(btn==previousbtn)
        {
            if(validateQuesAnsForm())
            {
                createQuestionAnswerResponse();
                getPreviouseQuestion();                
            }
            
        }
        else if(btn == createExamBtn){
            if(validateExmDtls()){
                 baseHorizontalSplit.setSecondComponent(getsecondComponent());
            }
        }
    }

    private void createQuestionAnswerResponse() 
    {
        
        
            
         ExamBean examBean = new ExamBean();
         examBean.setExamId(examIdForSubmittingAnswers);
         examBean.setUsername(this.loggedInUser);
         examBean.setQuestionId(String.valueOf(this.questionId));
         
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
          
          /* //belwo passed to new checkbox is dummy to reset values of existing checkboxes
          checkSelectedAnswer(new CheckBox());
          op1txt.setValue(GlobalConstants.emptyString);
          op2txt.setValue(GlobalConstants.emptyString);
          op3txt.setValue(GlobalConstants.emptyString);
          op4txt.setValue(GlobalConstants.emptyString);
          question.setValue(GlobalConstants.emptyString);  */
       
      
        
    }

    private void addIntoQuestionList(ExamBean examBean)
    {
        questionAnswerMap.put(examBean.getQuestionId(),examBean);        
    }

    private void submitStudentExamResponseToDB() {

        //exam nmae and exam type is reamining to change
        if (validateQuesAnsForm()) {
            try 
            {
//                JSONObject examJSONObject = new JSONObject();
//                examJSONObject.put("std", calsscmb.getValue());
//                examJSONObject.put("sub", subcbm.getValue());
//                examJSONObject.put("topic", topic.getValue());
//                examJSONObject.put("startDt", startDate.getValue());
//                examJSONObject.put("endDt", endDate.getValue());
//                examJSONObject.put("passingMarks", passingMarks.getValue());
//                examJSONObject.put("marksForQuestion", marksPerQuestion.getValue());
//                examJSONObject.put("exType", examTypeOpt.getValue());
//                examJSONObject.put("contestline", contestLine.getValue());
//                examJSONObject.put("noOfQuestions", noOfQue);
//                examJSONObject.put("createdBy", ((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile)).getName());
//                examJSONObject.put("exName", examNametxt.getValue());
//
//
//                if (examTypeOpt.getValue().equals("Objective")) {
//                    examJSONObject.put("exType", 1);
//                } else if (examTypeOpt.getValue().equals("Descriptive")) {
//                    examJSONObject.put("exType", 2);
//                } else {
//                    examJSONObject.put("exType", 3);
//                }
//
//                examJSONObject.put("fordiv", divcmb.getValue());



                JSONObject examJSONObject = new JSONObject();
                Gson gson = new Gson();
                populateQuestionAnsWerListFromMap();
                String questionAnswerListJSON = gson.toJson(questionAnswerList);
                examJSONObject.put("examId",examIdForSubmittingAnswers);
                examJSONObject.put("userId",((Userprofile) getSession().getAttribute(GlobalConstants.CurrentUserProfile)).getUsername());
                examJSONObject.put(GlobalConstants.EXAMQUESTIONLIST, questionAnswerListJSON);
                submitQuestionAnsResponseToDB(examJSONObject);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }finally{
               //////// studExam.updateExamList();
                CreateStudExamPopup.this.close();
            }
        }

    }

    private void submitQuestionAnsResponseToDB(JSONObject inputJson) {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.SUBMIT_QUE_ANS_RESPONSE));
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            String output = response.getEntity(String.class);
            
            JSONObject outNObject;
        
            try 
            {
                outNObject = new JSONObject(output);
                int status = Integer.parseInt(outNObject.getString(GlobalConstants.STATUS));
                if(status == GlobalConstants.YES)
                {
                    Notification.show("Successfully submitted exam", Notification.Type.WARNING_MESSAGE);
                }
                else
                {
                    Notification.show("Exam submission failed", Notification.Type.WARNING_MESSAGE);
                }
            } 
            catch (JSONException ex) 
            {
                Notification.show("Exam submission failed", Notification.Type.WARNING_MESSAGE);
                ex.printStackTrace();
            }
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
        
         List<ExamQueAnsBean> examQuestionAnswerBean = null;
        try 
        {
            Client client = Client.create();
            WebResource webResource = client.resource(GlobalConstants.getProperty(GlobalConstants.GET_EXAM_QUESTIONS_BY_EXAM_ID));
            //String input = "{\"userName\":\"raj\",\"password\":\"FadeToBlack\"}";
            JSONObject inputJson = new JSONObject();
             try
             {           
                inputJson.put("exmId", examId);
                //setting boolean value - to decide whether to bring answers of the questions or not
                if(this.studExamCaption.equals(GlobalConstants.startExam))
                {
                    inputJson.put("isSendAns", false);
                }
                else if(this.studExamCaption.equals(GlobalConstants.viewExam))
                {
                    inputJson.put("isSendAns", true);
                }
             }catch(Exception ex)
             {                 
                 ex.printStackTrace();
             }
            
            ClientResponse response = webResource.type(GlobalConstants.application_json).post(ClientResponse.class, inputJson);
            
            JSONObject outNObject = null;
            String output = response.getEntity(String.class);
            outNObject = new JSONObject(output);

            Type listType = new TypeToken<ArrayList<ExamQueAnsBean>>() {
            }.getType();
            
            examQuestionAnswerBean= new Gson().fromJson(outNObject.getString(GlobalConstants.EXAMLIST), listType);
        } catch (JSONException ex)
        {
          //  Logger.getLogger(AddStudent.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return examQuestionAnswerBean;
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
            if (baseHorizontalSplit.getSecondComponent() != null) {
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
        //first iff block 
        //this will not validate if it is view only
        //i.e student is just view the exam
        if(!isNewExam)
        {
            return true;
        }
        
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

    private int examIdForSubmittingAnswers;
    // question id changes frequently, when user navigates using next or previous button
    private int questionId;
    private void getNextQuestion() {
        if(listCnt>0){
            
                ExamQueAnsBean bean=getExamQueAnsBeanList().get(--listCnt);
                examIdForSubmittingAnswers = bean.getExamId();
                questionId=bean.getQuestionId();
                // 1 is objective type exams
                    if(bean.getExType()==1){
                            //descriptiveBtn.setCaption("Objective");  
                        
                            op1txt.setReadOnly(false);
                            op2txt.setReadOnly(false);
                            op3txt.setReadOnly(false);
                            op4txt.setReadOnly(false);
                            
                            op1txt.setValue(bean.getOption1());
                            op2txt.setValue(bean.getOption2());
                            op3txt.setValue(bean.getOption3());
                            op4txt.setValue(bean.getOption4());
                            
                            op1txt.setReadOnly(true);
                            op2txt.setReadOnly(true);
                            op3txt.setReadOnly(true);
                            op4txt.setReadOnly(true);
                            
                             /* if(op1txt.getValue().equals(bean.getAns()))
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
                            } */
                            
                            
                    }
//                    else{
//                            //descriptiveBtn.setCaption("Descriptive"); 
//                           // descAnsTextArea.setVisible(true);
//                            //descAnsTextArea.setValue(""+bean.getAns());
//                    }            
                    question.setReadOnly(false);
                    question.setValue(GlobalConstants.emptyString+bean.getQuestion()); 
                    question.setReadOnly(true);
            }else{
                Notification.show("No other questions available");
            }          
    }
    
    
      private void getPreviouseQuestion() {
        if(listCnt<getExamQueAnsBeanList().size()-1){
                ExamQueAnsBean bean=getExamQueAnsBeanList().get(++listCnt);
                examIdForSubmittingAnswers = bean.getExamId();
                questionId=bean.getQuestionId();
                // 1 is objective type exams
                    if(bean.getExType()==1){
//                            descriptiveBtn.setCaption("Objective");  
                            op1txt.setReadOnly(false);
                            op2txt.setReadOnly(false);
                            op3txt.setReadOnly(false);
                            op4txt.setReadOnly(false);
                            
                            op1txt.setValue(bean.getOption1());
                            op2txt.setValue(bean.getOption2());
                            op3txt.setValue(bean.getOption3());
                            op4txt.setValue(bean.getOption4());
                            
                            op1txt.setReadOnly(true);
                            op2txt.setReadOnly(true);
                            op3txt.setReadOnly(true);
                            op4txt.setReadOnly(true);
                            
                            /* if(op1txt.getValue().equals(bean.getAns()))
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
                            } */
                            
                    }
//                    else{
//                            descriptiveBtn.setCaption("Descriptive"); 
//                           // descAnsTextArea.setVisible(true);
//                            descAnsTextArea.setValue(""+bean.getAns());
//                    }            
                    question.setReadOnly(false);
                    question.setValue(GlobalConstants.emptyString+bean.getQuestion()); 
                    question.setReadOnly(true);
            }else{
                Notification.show("No other questions available");
            }          
    }
      
      private void checkSelectedAnswer(CheckBox chkbox){
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

    private void populateQuestionAnsWerListFromMap() 
    {
        questionAnswerList = new ArrayList<ExamBean>();
        Set<String> keySet = questionAnswerMap.keySet();
        Iterator<String> itr = keySet.iterator();
        String key=GlobalConstants.emptyString;
        while(itr.hasNext())
        {
            key = itr.next();
            questionAnswerList.add(questionAnswerMap.get(key));
        }
    }
    
}