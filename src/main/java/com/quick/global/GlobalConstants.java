
package com.quick.global;


import com.quick.forum.ForumView;
import com.quick.games.GamesView;
import com.quick.notices.CreateNotices;
import com.quick.ui.exam.AdminExam;
import com.quick.ui.exam.StudentExam;
import com.quick.upcomingtechnology.CreateUpcomingTechnology;
import com.vaadin.demo.dashboard.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class GlobalConstants
{
    
    
    
    

    /** Creates a new instance of EQ_GlobalConstants */
    public GlobalConstants() {
    }   
  
    public static final String PRODUCT_NAME="PRODUCT_NAME";
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
    public static final String wordOfTheDay = "wordOfTheDay";
    public static final String WHOSEDOINGWHAT = "Whodwht";
    public static final String STANDARDLIST = "standardList";
    public static final String STUDENTLIST = "studentList";
    public static final String QUALIFICATIONLIST = "qualificationList";
    public static final String ISROLLNOEXIST="isRollNoExist";
    public static final String TEACHERLIST = "teacherList";
    public static final String emptyString="";
    public static final String spaceString=" ";
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
    public static final String startExam = "Start Exam";
    public static final String viewExam = "View Exam";
    public static final String gsonTimeFormat="yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String application_json="application/json";
    public static final String dateFormatMMddyyyy="MM/dd/yyyy";
    public static final String Technologies="Technologies";
    public static final String category_distribution = "category_distribution";
    public static final String eventDetailsList="eventDetailsList";
    public static final String dictWordList="dictWordList";
    public static final String eventLikes="eventLikes";
    public static final String eventComments="eventComments";
    public static final String FILE_UPLOAD_PATH="FILE_UPLOAD_PATH";
    public static final String FILE_DOWNLOAD_PATH="FILE_DOWNLOAD_PATH";
    public static final String SCHOOL_NAME="SCHOOL_NAME";
    public static final String UPLOAD_TOPIC_IMAGES_PATH="UPLOAD_TOPIC_IMAGES_PATH";
    public static final String LEARN_MORE_IMG="LEARN_MORE_IMG";
    public static final String IMAGES_PATH="IMAGES_PATH";
    public static final String FULL_STOP=".";
    public static final String TOPIC_INTRO_LENGTH="TOPIC_INTRO_LENGTH";
    public static final String tripple_dots="...";
    public static final String IMAGE_HEIGHT="IMAGE_HEIGHT";
    public static final String IMAGE_WIDTH="IMAGE_WIDTH";
    
    public static final String ROUT_DASHBOARD="/dashboard";
    public static final String ROUT_LEARN="/learn";
    public static final String ROUT_TECH_NEWS="/Tech-news";
    public static final String ROUT_EXAMS="/Exams";
    public static final String ROUT_NOTICES="/Notices";
    public static final String ROUT_FORUM="/Forum";
    public static final String ROUT_PLANNER="/Planner";
    public static final String ROUT_GAMES="/Games";
    public static final String ROUT_PLAY="/Play";
    public static final String ROUT_REPORTS="/reports";
    public static final String ROUT_TOPICS="/topics";
    public static final String ROUT_EXAM_ADMIN="/Exam-Admin";
    public static final String ROUT_STUDENTS="/students";
    public static final String ROUT_TEACHERS="/teachers";
    public static final String ROUT_DICT="/dictionary";
    
     // == exam ============
    public static final String EXAMRESOURCE = "examResource";
    public static final String EXAMLIST="examList";
    public static final String subjectWiseAvgPerformance="subjectWiseAvgPerformance";
    public static final String subwiseAvgScoreForStud="subwiseAvgScoreForStud";
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
   public static final String GET_EXAM_DETAILS_BY_ID="GET_EXAM_DETAILS_BY_ID";
   public static final String GET_EXAM_LIST="GET_EXAM_LIST";
   public static final String GET_EXAM_QUESTIONS_BY_EXAM_ID="GET_EXAM_QUESTIONS_BY_EXAM_ID";
   public static final String SUBMIT_QUE_ANS_RESPONSE="SUBMIT_QUE_ANS_RESPONSE";
   public static final String GET_ALL_NOTICES="GET_ALL_NOTICES";
   public static final String SAVE_NOTICE="SAVE_NOTICE";
   public static  final String DELETE_NOTICE="DELETE_NOTICE";
   public static final String SAVE_TECHNOLOGY="SAVE_TECHNOLOGY";
   public static  final String DELETE_TECHNOLOGY="DELETE_TECHNOLOGY";
   public static  final String GET_ALL_TECHNOLOGY="GET_ALL_TECHNOLOGY";
   public static  final String GET_TECHNOLOGY_BY_CATEGORY="GET_TECHNOLOGY_BY_CATEGORY";
   public static  final String GET_SUB_WISE_COMPARISON="GET_SUB_WISE_COMPARISON";
   public static  final String GET_All_FORUM_EVENTS="GET_All_FORUM_EVENTS";
   public static  final String GET_FORUM_EVENT_BY_ID="GET_FORUM_EVENT_BY_ID";
   public static  final String GET_WORD_LIST="GET_WORD_LIST";
   public static  final String SEARCH_WORD_LIST="SEARCH_WORD_LIST";
   public static  final String SAVE_NEW_WORD_DETAILS="SAVE_NEW_WORD_DETAILS";
   public static  final String SAVE_EVENT_DETAILS="SAVE_EVENT_DETAILS";   
   public static  final String GET_STUD_QUICK_LEARN_DTLS="GET_STUD_QUICK_LEARN_DTLS";
   public static  final String SAVE_EVENT_LIKE="SAVE_EVENT_LIKE";
   public static  final String SAVE_EVENT_COMMENT="SAVE_EVENT_COMMENT";
   public static  final String FETCH_EVENT_LIKES_BY_ID="FETCH_EVENT_LIKES_BY_ID";
   public static  final String FETCH_SUBS_BY_STD="FETCH_SUBS_BY_STD";
   public static  final String GET_TOPIC_LIST_FOR_ME="GET_TOPIC_LIST_FOR_ME";
   public static  final String GET_SUB_BY_STD="GET_SUB_BY_STD";
   public static  final String SAVE_TEACHER="SAVE_TEACHER";
   public static  final String GET_TEACHER_STD_DIV_SUB_ASSOCIATION="GET_TEACHER_STD_DIV_SUB_ASSOCIATION";
   public static  final String GET_STUD_DTLS_BY_PRN="GET_STUD_DTLS_BY_PRN";
   public static  final String GET_SEARCH_STUD_FILTER_CRITERIA="GET_SEARCH_STUD_FILTER_CRITERIA";
   public static  final String GET_TEACHER_DTLS_BY_PRN="GET_TEACHER_DTLS_BY_PRN";
   public static  final String GET_SEARCH_TEACHER_FILTER_CRITERIA="GET_SEARCH_TEACHER_FILTER_CRITERIA";
   public static  final String GET_ALL_STUD_LIST="GET_ALL_STUD_LIST";
   public static  final String GET_QUALIFICATION_LIST="GET_QUALIFICATION_LIST";
   public static  final String GET_ALL_TEACHER_LIST="GET_ALL_TEACHER_LIST";
   public static  final String IS_USERNAME_ALREADY_EXISTS="IS_USERNAME_ALREADY_EXISTS";
   public static  final String GET_DIV_BY_STD="GET_DIV_BY_STD";
   public static  final String SAVE_STUDENT="SAVE_STUDENT";
   public static  final String IS_ROLL_NO_ALREADY_EXISTS="IS_ROLL_NO_ALREADY_EXISTS";
   
   
   public static  final String MemoryGame="MemoryGame";
   public static  final String CrosswordGame="CrosswordGame";   
   public static  final String HalloweenGame="HalloweenGame";
   public static  final String TypingGame="TypingGame";
   public static  final String QuizfindGame="QuizfindGame";
   public static  final String NumbersGame="NumbersGame";
   public static  final String ScrambledGame="ScrambledGame";
   public static  final String FoodGame="FoodGame";
   public static  final String FamilyGame="FamilyGame";
   public static  final String FaceGame="FaceGame";
   
   public static  final String ColorsGame="ColorsGame";
   public static  final String ChristmasGame="ChristmasGame";
   public static  final String BikestormGame="BikestormGame";
   public static  final String SudokuGame="SudokuGame";
   
   public static  final String DisneyColoringGame="DisneyColoringGame";
   public static  final String EightWordsGame="EightWordsGame";
   public static  final String IdeaWorkshopGame="IdeaWorkshopGame";
   public static  final String WordmazeGame="WordmazeGame";
   
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
