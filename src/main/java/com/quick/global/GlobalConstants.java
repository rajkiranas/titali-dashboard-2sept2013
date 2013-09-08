
package com.quick.global;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GlobalConstants
{
    
    
    
    

    /** Creates a new instance of EQ_GlobalConstants */
    public GlobalConstants() {
    }   
  
    public static final String CMS="CMS Dashboard";
    public static final String Footer_Line="POWERED BY SATERI SYSTEMS INC.";
    public static final String Logout="Logout";
    public static final String DATEFORMAT = "MM/dd/yyyy";
    public static final String REGEX_PATTERN="[a-z]+";;
    public static final String SELECT="select";
    private static Properties properties = new Properties();
    public static final String userName = "userName";
    public static final String password = "password";
    public static final String KEY="sateri@gmail.com";
    public static final String IV ="initialvector123";
    public static final int YES = 1;
    public static final int NO = 0;
    public static final String isAuthenticated = "isAuthenticated";
    public static final String admin = "admin";
    public static final String teacher = "teacher";
    public static final String student = "student";
    public static final String role="role";
    public static final String WHATSNEW = "whatsNew";
    public static final String NOTICES = "notices";
    public static final String WHOSEDOINGWHAT = "Whodwht";
    public static final String STANDARDLIST = "standardList";
    public static final String STUDENTLIST = "studentList";
    public static final String QUALIFICATIONLIST = "qualificationList";
    public static final String ISROLLNOEXIST="isRollNoExist";
    public static final String TEACHERLIST = "teacherList";
    public static final String emptyString="";
    public static final String HYPHEN = "-";
    public static final String ISUSERNAMEEXIST="isUsernameExist";
    public static final String QUICKLEARNLIST = "quickLearnList";
    public static final String STDSUBLIST = "stdsub";
    public static final String STATUS = "Status";
    public static final String MYQUICKNOTEs="quicknotes";
    public static final String subjectList = "subjectList";
    public static final String divisionList = "divisionList";
    public static final String teacherStdDivSubIdList = "teacherStdDivSubIdList";
    public static final String Upload_Topics = "Upload Topics";
    public static final String New="New";
    public static final String Save = "Save";
    public static final String Cancel = "Cancel";
    public static final String toolbar_style = "toolbar";
    public static final String h1_style = "h1";
    public static final String default_style = "default";
    public static final String going_through="going through";
    public static final String  DASH = "-";
    
    
     // == exam ============
    public static final String EXAMRESOURCE = "examResource";
    public static final String EXAMLIST="examList";
    public static final String EXAMQUESTIONLIST ="examQuestionList";
    public static final String CurrentUserProfile = "CurrentUserProfile";
    
    //===========URL FOR SERVICES===========
   public static final String DASHBOARD_URL="DASHBOARD_URL";
   public static final String LOGIN_URL="LOGIN_URL";
   public static final String STANDARD_LISTING_URL="STANDARD_LISTING_URL";
   public static final String QUICK_LEARN_UPLOAD_LIST_URL="QUICK_LEARN_UPLOAD_LIST_URL";
   public static final String SAVE_UPLOAD_DETAILS_URL="SAVE_UPLOAD_DETAILS_URL";
   public static final String GET_QUICK_LEARN_BY_UPLOAD_ID="GET_QUICK_LEARN_BY_UPLOAD_ID";
   public static final String DELETE_TOPIC_BY_UPLOAD_ID="DELETE_TOPIC_BY_UPLOAD_ID";
   public static final String SEND_WHOS_DOING_WHAT_NOTIFICATIONS="SEND_WHOS_DOING_WHAT_NOTIFICATIONS";
   public static final String DELETE_TEACHER_FROM_DB="DELETE_TEACHER_FROM_DB";
   public static final String DELETE_STUDENT_FROM_DB="DELETE_STUDENT_FROM_DB";
   public static final String UPDATE_USER_NOTES_FOR_TOPIC_URL="UPDATE_USER_NOTES_FOR_TOPIC_URL";
   public static final String CREATE_EXAM_URL="CREATE_EXAM_URL";
   public static final String DELETE_EXAM_BY_ID="DELETE_EXAM_BY_ID";
   public static final String GET_PRESENT_STUD_FOR_EXAM="GET_PRESENT_STUD_FOR_EXAM";
   public static final String GET_ABSENT_STUD_FOR_EXAM ="GET_ABSENT_STUD_FOR_EXAM" ;
   public static final String gsonTimeFormat="yyyy-MM-dd'T'HH:mm:ss'Z'";
   //public static final String UserMasterResource = "/UserMaster";
    
    static
    {
        try {
            loadProperties();
        } catch (IOException ex) {
          //  logger.debug("Exception occured in loadProperties() method, Exception=", ex);
            //Logger.getLogger(GlobalConstants.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    
    
    
    static class PropertyLoader {

        public InputStream getProperty() {
            InputStream l_objInputStream = getClass().getClassLoader().
                    getResourceAsStream("Default.properties");
            return l_objInputStream;
        }
    }

    private static void loadProperties() throws IOException {
            GlobalConstants.PropertyLoader PL = new GlobalConstants.PropertyLoader();
            InputStream inputStream = PL.getProperty();
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
             // logger.debug("Properties File not found!");
            }
    }
 
     public static String getProperty(String key)
     {
         return properties.getProperty(key);
     }
}
