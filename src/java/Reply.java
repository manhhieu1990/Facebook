/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ABC
 */
public class Reply {
    
    private String reply_status_ID;
    private String reply_user_ID;
    private String reply_message;
    private String reply_date;
    
    public Reply(String sid, String uid, String m, String d){
        reply_status_ID = sid;
        reply_user_ID = uid;
        reply_message = m;
        reply_date = d;
    }
    
    //getter and setter

    public String getReply_status_ID() {
        return reply_status_ID;
    }

    public void setReply_status_ID(String reply_status_ID) {
        this.reply_status_ID = reply_status_ID;
    }

    public String getReply_user_ID() {
        return reply_user_ID;
    }

    public void setReply_user_ID(String reply_user_ID) {
        this.reply_user_ID = reply_user_ID;
    }

    public String getReply_message() {
        return reply_message;
    }

    public void setReply_message(String reply_message) {
        this.reply_message = reply_message;
    }

    public String getReply_date() {
        return reply_date;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }
    
}
