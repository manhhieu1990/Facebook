/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
/**
 *
 * @author ABC
 */
@Named(value = "registration")
@ManagedBean
@RequestScoped
public class Registration {

    private String id;
    private String f_name;
    private String l_name;
    private String email;
    private String password;
    private String secq1;
    private String answer1;
    private String secq2;
    private String answer2;
    private ArrayList<String> secquestions = new ArrayList<String>();

    public Registration() {

        try {
             Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        //three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL,"root",null);
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from facebook_securityquestions");
            while (rs.next()) {
               secquestions.add(rs.getString(2));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                conn.close();
                stat.close();
                rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }// end constructor Registration
    
    public String register(){
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        //three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
       try{
            conn = DriverManager.getConnection(DB_URL,"root",null);
            stat = conn.createStatement();
           rs = stat.executeQuery("Select * from facebook_account where user_ID = '" + id + "' or email = '" + email + "'");
           if(rs.next()){
               return("Either you already had account for the same User_ID or email address.");
           }else{
               rs = stat.executeQuery("Select * from facebook_securityquestions where question = '" + secq1 + "'");
               int secq1ID = 0;
               if(rs.next()){
                    secq1ID = rs.getInt(1);
               }
               
               rs = stat.executeQuery("Select * from facebook_securityquestions where question = '" + secq2 + "'");
               int secq2ID = 0;
               if(rs.next()){
                   secq2ID = rs.getInt(1);
               }
               
               if(secq1ID == secq2ID){
                   return("Can not have the same security questions.");
               }else{
                   int t = stat.executeUpdate("Insert into facebook_account (user_ID,first_name,last_name,"
                                             +"email,password,secq1_ID,a_secq1_ID,secq2_ID,a_secq2_ID) "
                                             +"values ('"
                                             + id + "','" + f_name + "','" + l_name + "','" + email + "','" + password + "','"
                                             + secq1ID + "','" + answer1 + "','" + secq2ID + "','" + answer2 + "')");
                   
                   //Successful
                   return("Registration Successful! Please return to login your account page.");
               }
           }
        }catch(Exception e){
            e.printStackTrace();
            return("Internal Error! Please try again later.");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecq1() {
        return secq1;
    }

    public void setSecq1(String secq1) {
        this.secq1 = secq1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getSecq2() {
        return secq2;
    }

    public void setSecq2(String secq2) {
        this.secq2 = secq2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public ArrayList<String> getSecquestions() {
        return secquestions;
    }

    public void setSecquestions(ArrayList<String> secquestions) {
        this.secquestions = secquestions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
 
}
