/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author ABC
 */
public class Status {

    private String status_ID;
    private String share_ID;
    private String status_userID;
    private String status_message;
    private String status_date;
    private int like_count;
    private boolean displayLikeButton;
    private boolean displayUnlikeButton;
    private boolean displayShareButton;
    private boolean displayReplyTextArea;
    private boolean displayReplies;
    private ArrayList<Reply> replies = new ArrayList<Reply>();

    public Status(String sid, String shd, String suid, String smsg, String sd) {
        status_ID = sid;
        share_ID = shd;
        status_userID = suid;
        status_message = smsg;
        status_date = sd;
    }

    public String getStatus_ID() {
        return status_ID;
    }

    public void setStatus_ID(String status_ID) {
        this.status_ID = status_ID;
    }

    public String getShare_ID() {
        return share_ID;
    }

    public void setShare_ID(String share_ID) {
        this.share_ID = share_ID;
    }

    public String getStatus_userID() {
        return status_userID;
    }

    public void setStatus_userID(String status_userID) {
        this.status_userID = status_userID;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getStatus_date() {
        return status_date;
    }

    public void setStatus_date(String status_date) {
        this.status_date = status_date;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public boolean isDisplayLikeButton() {
        return displayLikeButton;
    }

    public void setDisplayLikeButton(boolean displayLikeButton) {
        this.displayLikeButton = displayLikeButton;
    }

    public boolean isDisplayReplies() {
        return displayReplies;
    }

    public boolean isDisplayUnlikeButton() {
        return displayUnlikeButton;
    }

    public void setDisplayUnlikeButton(boolean displayUnlikeButton) {
        this.displayUnlikeButton = displayUnlikeButton;
    }
    
    public void setDisplayReplies(boolean displayReplies) {
        this.displayReplies = displayReplies;
    }

    public boolean isDisplayShareButton() {
        return displayShareButton;
    }

    public void setDisplayShareButton(boolean displayShareButton) {
        this.displayShareButton = displayShareButton;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public boolean isDisplayReplyTextArea() {
        return displayReplyTextArea;
    }

    public void setDisplayReplyTextArea(boolean displayReplyTextArea) {
        this.displayReplyTextArea = displayReplyTextArea;
    }

}
