/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.inject.Named;
import java.io.Serializable;
import java.sql.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author ABC
 */
@Named(value="login")
@ManagedBean
@SessionScoped

public class login implements Serializable{
    
 private String id;
 private String password;
 private FacebookAccount theAccount;
 private String securityQuestion;
 private String answer;
 private boolean displaySecQuestionDropdown;
 private String showPassword;
 private boolean displayPassword;
 private String error_message;
 /**
  * Create a new instance of Login
  */
 
 public String login(){
     
     // load the driver
     try{
         Class.forName("com.mysql.jdbc.Driver");
     }catch(Exception e){
         error_message = e.getMessage();
         return("internalError");
     }
     
     //Database Access
     final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
     //three variables
     Connection conn = null;
     Statement stat = null;
     ResultSet rs = null;
     
     try{
         conn = DriverManager.getConnection(DB_URL,"root","");
         stat = conn.createStatement();
         rs = stat.executeQuery("Select * from facebook_account where user_ID = '" + id + "'");
         
         if(!rs.next()){
             // No ID found
             return("loginNotOk");
         }else{
             if(!rs.getString(5).equals(password)){
                 //Login Successful
                 theAccount = new FacebookAccount(rs.getString(1),rs.getString(2));
                 //Brokerage Account Display Menu
                 return("welcome");
             }else{
                 return("loginNotOk");
             }
         }
     }catch(SQLException e){
         e.printStackTrace();
         return e.getMessage();
     }finally{
         try{
             conn.close();
             stat.close();
             rs.close();
         }catch(Exception e){
             e.printStackTrace();
             return(e.getMessage());
         }
     }
 }
 
 public String displaySecurityQuestion(){
      // load the driver
     try{
         Class.forName("com.mysql.jdbc.Driver");
     }catch(Exception e){
         e.printStackTrace();
         // return to internalError.xhtml
         error_message = e.getMessage();
         return("internalError");
     }
     
     // Database Access
     final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
     // three variables
     Connection conn = null;
     Statement stat = null;
     ResultSet rs = null;
     
     try{
         conn = DriverManager.getConnection(DB_URL,"root","");
         stat = conn.createStatement();
         rs = stat.executeQuery("Select * from facebook_account where user_ID = '" + id + "'");
         if(rs.next()){
             // generate a random value between 0 and 1
             int randVal = (int)(Math.random()*2+1);
             // variable to store the column number of the Security question according to the random value
             int qColVal = 6;
             // determining the column values of the question and answer based on the randVal. i.e., if 1 security question 1 else security question 2
             if(randVal == 1){
                 qColVal = 6;
             }else{
                 qColVal = 8;
             }
             int qCode = rs.getInt(qColVal);
             // retrieving the security question from securityquestions table
             rs = stat.executeQuery("select * from facebook_securityquestions where secq_ID = '" + qCode + "'");
             if(rs.next()){
                 securityQuestion = rs.getString(2);
             }
             displaySecQuestionDropdown = true;
             return("forgotPassword");
         }else{
             return("userNotFound");
         }
     }catch(SQLException e){
         e.printStackTrace();
         // return to internalError.xhtml
         error_message = e.getMessage();
         return("internalError");
     }finally{
         try{
             conn.close();
             stat.close();
             rs.close();
         }catch(Exception e){
             e.printStackTrace();
             // return to internalError.xhtml
             error_message = e.getMessage();
             return("internalError");
         }
     }
 }
 
 public String forgotPassword(){
     
     String existingAnswer = "";
     
     // load the driver
     try{
         Class.forName("com.mysql.jdbc.Driver");
     }catch(Exception e){
         e.printStackTrace();
         // return to internalError.xhtml
         error_message = e.getMessage();
         return("internalError");
     }
     
     // Database Access
     final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
     // three variables
     Connection conn = null;
     Statement stat = null;
     ResultSet rs = null;
     
     try{
         conn = DriverManager.getConnection(DB_URL,"root","");
         stat = conn.createStatement();
         rs = stat.executeQuery(("Select * from facebook_securityquestions where question = '" + securityQuestion + "'"));
         if(rs.next()){
             int qCode = rs.getInt(1);
             // retrieving the security question from securityquestions table
             rs = stat.executeQuery("Select * from facebook_account where user_ID = '" + id + "'");
             if(rs.next()){
                 if(qCode == rs.getInt(6)){
                     existingAnswer = rs.getString(7);
                 }else{
                     existingAnswer = rs.getString(9);
                 }
                 showPassword = rs.getString(5);
             }
         }
         if(answer.equals(existingAnswer)){
             displayPassword = true;
             return("forgotPassword");
         }else{
             return("answerNotFound");
         }
     }catch(SQLException e){
         e.printStackTrace();
         // return to internalError.xhtml
         return("internalError");
     }finally{
         try{
             conn.close();
             stat.close();
             rs.close();
         }catch(Exception e){
             // return internalError.xhtml
             error_message = e.getMessage();
             return("internalError");
         }
     }
 }
 
 public String redirectToLogin(){
     displayPassword = false;
     displaySecQuestionDropdown = false;
     password = "";
     securityQuestion = "";
     showPassword = "";
     id = "";
     return("index");
 }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FacebookAccount getTheAccount() {
        return theAccount;
    }

    public void setTheAccount(FacebookAccount theAccount) {
        this.theAccount = theAccount;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isDisplaySecQuestionDropdown() {
        return displaySecQuestionDropdown;
    }

    public void setDisplaySecQuestionDropdown(boolean displaySecQuestionDropdown) {
        this.displaySecQuestionDropdown = displaySecQuestionDropdown;
    }

    public String getShowPassword() {
        return showPassword;
    }

    public void setShowPassword(String showPassword) {
        this.showPassword = showPassword;
    }

    public boolean isDisplayPassword() {
        return displayPassword;
    }

    public void setDisplayPassword(boolean displayPassword) {
        this.displayPassword = displayPassword;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
 
 
}
