/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ABC
 */
public class FacebookAccount {

    private String id;
    private String f_name;
    private String l_name;
    private int status;
    private int followers;
    private int following;
    private String status_message;
    private String status_reply_message;
    private String joined_Date;
    //variables for search
    private String search_String;
    private String search_id;
    private String search_fname;
    private String search_lname;
    private int search_statusCount;
    private int search_followersCount;
    private int search_followingCount;
    private String search_joined_Date;
    private boolean displayFollowButton;

    private ArrayList<Status> userStatus = new ArrayList<>();
    private ArrayList<Status> searchStatus = new ArrayList<Status>();
    private ArrayList<TrendingTopics> trend = new ArrayList<TrendingTopics>();

    private String error_message;

    public FacebookAccount(String uid, String fname) {
        id = uid;
        f_name = fname;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        //variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", null);
            stat = conn.createStatement();
            // count the number of status this account already posted
            rs = stat.executeQuery("Select count(1) from facebook_status where user_ID = '" + id + "'");
            if (rs.next()) {
                status = rs.getInt(1);
            }

            // count the number of user this account is following
            rs = stat.executeQuery("Select count(1) from facebook_follow where user_ID = '" + id + "'");
            if (rs.next()) {
                following = rs.getInt(1);
            }

            // count the number of user who is following this account
            rs = stat.executeQuery("Select count(1) from facebook_follow where follow_user_ID = '" + id + "'");
            if (rs.next()) {
                followers = rs.getInt(1);
            }

            // display this account's joined date, first and last name
            rs = stat.executeQuery("Select * from facebook_account where user_ID = '" + id + "'");
            if (rs.next()) {
                Date d = rs.getDate(10);
                joined_Date = d.toString();
                l_name = rs.getString(3);
            }

            rs = stat.executeQuery("Select * from facebook_status where user_ID IN (Select follow_user_ID from facebook_follow where "
                    + "user_ID = '" + id + "') or user_ID = '" + id + "' order by status_time DESC");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(2);
                String status_userID = rs.getString(3);
                String status_message = rs.getString(4);
                String comment_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, status_userID, status_message, comment_time);
                stat1 = conn.createStatement();

                // if rs.next() is true, it means someone already liked this status so boolean setDisplayLikeButton will be set as false, else it will be set as true.
                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                    s.setDisplayUnlikeButton(true);
                } else {
                    s.setDisplayLikeButton(true);
                    s.setDisplayUnlikeButton(false);
                }

                // samething, if rs.next() is true, it means user already reply this status, so boolean setDisplayReplies will be set as true 
                // => this reply will be displayed, else, setDisplayReplies will be set as false => no replies will be displayed.
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayReplies(true);
                } else {
                    s.setDisplayReplies(false);
                }

                // display the number of like for this status
                rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                if (rs1.next()) {
                    s.setLike_count(rs1.getInt(1));
                } else {
                    s.setLike_count(0);
                }

                // initialize an arraylist named status replies with Reply data type.
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();

                // if rs.next() is true, it means that there are some replies for this status.
                // r is an instance of Reply constructor.
                // add r to the arraylist statusReplies if rs.next() is true.
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' order by reply_timestamp ASC");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            trend.clear();
            rs = stat.executeQuery("Select * from facebook_trending order by trending_count DESC");
            int counter = 1;
            while (rs.next() && counter < 6) {
                trend.add(new TrendingTopics(rs.getString(1), rs.getInt(2)));
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } //end method FacebookAccount

    public String myProfile() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            // return to internalError.xhtml
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();

            // display the number of this user's status
            rs = stat.executeQuery("Select count(1) from facebook_status where user_ID = '" + id + "'");
            if (rs.next()) {
                status = rs.getInt(1);
            }

            // display the number of people who are following this user
            rs = stat.executeQuery("Select count(1) from facebook_follow where user_ID = '" + id + "'");
            if (rs.next()) {
                following = rs.getInt(1);
            }

            // display the number of people whom this user is following
            rs = stat.executeQuery("Select count(1) from facebook_follow where follow_user_ID = '" + id + "'");
            if (rs.next()) {
                followers = rs.getInt(1);
            }
            userStatus.clear();
            rs = stat.executeQuery("Select * from facebook_status where user_ID = '" + id + "' order by status_time DESC");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(2);
                String status_userID = rs.getString(3);
                String share_this_message = rs.getString(4);
                String comment_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, status_userID, share_this_message, comment_time);
                stat1 = conn.createStatement();

                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                    s.setDisplayUnlikeButton(true);
                } else {
                    s.setDisplayLikeButton(true);
                    s.setDisplayUnlikeButton(false);
                }
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayReplies(true);
                } else {
                    s.setDisplayReplies(false);
                }
                rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                if (rs1.next()) {
                    s.setLike_count(rs1.getInt(1));
                } else {
                    s.setLike_count(0);
                }
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' order by reply_timestamp ASC");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            return ("myProfile");
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    } // end method myProfile

    // Start method status
    public String status(String pageToDirect) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            // return to internalError.xhtml
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();

            // start inserting user_ID of user, and his status_message into facebook_status table
            int j = stat.executeUpdate("Insert into facebook_status (share_ID, user_ID, status_message) "
                    + "values(' ','" + id + "','" + status_message + "')");
            status++;
            ArrayList<String> hashTags = new ArrayList<String>();

            // create String array sub_status_message by splitting String status_message by # key
            String[] sub_status_message = status_message.split("#");

            // for loop to get the hash tag message
            for (int i = 1; i < sub_status_message.length; i++) {
                String sub_status_message_each = sub_status_message[i]; // divide status message into sub status message
                int spaceIndex = sub_status_message_each.indexOf(' ');
                String hashTag = "#" + sub_status_message_each;
                if (spaceIndex > 0) {
                    hashTag = sub_status_message_each.substring(0, spaceIndex);
                    hashTag = "#" + hashTag;
                }
                hashTags.add(hashTag);
            }

            // Check facebook_trending table, if hash tag message in this table, then update trending count, if not, insert hash tag message into that table
            for (String h : hashTags) {
                rs = stat.executeQuery("Select * from facebook_trending where trending_topic = '" + h + "'");
                if (rs.next()) {
                    int k = stat.executeUpdate("Update facebook_trending set trending_count = trending_count + 1 where trending_topic = '" + h + "'");
                } else {
                    int k = stat.executeUpdate("Insert into facebook_trending values ('" + h + "',1)");
                }
            }

            userStatus.clear();

            // check if is there any user is following you, so that he/she can like, or reply to your status (curretn user is able to like or reply to his own status)
            rs = stat.executeQuery("Select * from facebook_status where user_ID in (Select follow_user_ID from "
                    + "facebook_follow where user_ID = '" + id + "') or user_ID = '" + id + "' order by status_time desc");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(2);
                String status_user_ID = rs.getString(3);
                String status_message = rs.getString(4);
                String status_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, status_user_ID, status_message, status_time);
                stat1 = conn.createStatement();

                // condition to display like button for status (if current user is following you, he/she will be able to see that like button, 
                // otherwise he/she won't be able to see the like button
                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                    s.setDisplayUnlikeButton(true);
                } else {
                    s.setDisplayLikeButton(true);
                    s.setDisplayUnlikeButton(false);
                }
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayReplies(true);
                } else {
                    s.setDisplayReplies(false);
                }
                rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                if (rs1.next()) {
                    if (rs1.getInt(1) > 0) {
                        s.setLike_count(rs1.getInt(1));
                    } else {
                        s.setLike_count(0);
                    }
                }
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_id = '" + status_ID + "' order by reply_timestamp asc");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            trend.clear();
            rs = stat.executeQuery("Select * from facebook_trending order by trending_count desc");
            int counter = 1;
            while (rs.next() && counter < 6) {
                trend.add(new TrendingTopics(rs.getString(1), rs.getInt(2)));
                counter++;
            }
            status_message = null;
            return ("welcome");
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }

    } //end method status

    public String search() {

        search_id = search_String;
        search_String = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from facebook_account where user_ID = '" + search_id + "'");
            if (rs.next()) {
                search_fname = rs.getString(2);
                search_lname = rs.getString(3);
                Date d = rs.getDate(10);
                search_joined_Date = d.toString();
                search_statusCount = 0;
                search_followersCount = 0;
                search_followingCount = 0;
                rs = stat.executeQuery("Select count(1) from facebook_status where user_ID = '" + search_id + "'");
                if (rs.next()) {
                    search_statusCount = rs.getInt(1);
                }
                rs = stat.executeQuery("Select count(1) from facebook_follow where user_ID = '" + search_id + "'");
                if (rs.next()) {
                    search_followingCount = rs.getInt(1);
                }
                rs = stat.executeQuery("Select count(1) from facebook_follow where follow_user_ID = '" + search_id + "'");
                if (rs.next()) {
                    search_followersCount = rs.getInt(1);
                }
                rs = stat.executeQuery("Select * from facebook_follow where user_ID = '" + id + "' and follow_user_ID = '" + search_id + "'");
                if (rs.next()) {
                    displayFollowButton = false;
                } else {
                    displayFollowButton = true;
                }
                searchStatus.clear();
                rs = stat.executeQuery("Select * from facebook_status where user_ID = '" + search_id + "' order by status_time desc");
                while (rs.next()) {
                    String status_ID = rs.getString(1);
                    String share_ID = rs.getString(2);
                    String statusUser_ID = rs.getString(3);
                    String status_message = rs.getString(4);
                    String status_time = rs.getString(5);
                    Status s = new Status(status_ID, share_ID, statusUser_ID, status_message, status_time);
                    stat1 = conn.createStatement();
                    rs1 = stat1.executeQuery("Select follow_user_ID from facebook_follow where user_Id = '" + id + "' and follow_user_ID = '" + search_id + "'");
                    if (rs1.next()) {
                        // display reply text area
                        s.setDisplayReplyTextArea(true);

                        // display share button
                        s.setDisplayShareButton(true);
                        // display like and unlike buttons
                        rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                        if (rs1.next()) {
                            s.setDisplayLikeButton(false);
                            s.setDisplayUnlikeButton(true);
                        } else {
                            s.setDisplayLikeButton(true);
                            s.setDisplayUnlikeButton(false);
                        }

                        // display reply button
                        rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                        if (rs1.next()) {
                            s.setDisplayReplies(true);
                        } else {
                            s.setDisplayReplies(false);
                        }

                        // display number of people who like your status
                        rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                        if (rs1.next()) {
                            s.setLike_count(rs1.getInt(1));
                        } else {
                            s.setLike_count(1);
                        }
                        ArrayList<Reply> statusReplies = new ArrayList<Reply>();

                        // display replies to your status
                        rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "'");
                        while (rs1.next()) {
                            Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                            statusReplies.add(r);
                        }
                        s.setReplies(statusReplies);
                    } else {
                        s.setDisplayLikeButton(false);
                        s.setDisplayUnlikeButton(false);
                        s.setDisplayShareButton(false);
                        s.setDisplayReplies(false);
                        s.setDisplayReplyTextArea(false);
                    }

                    searchStatus.add(s);
                }
                return ("searchFound");
            } else {
                return ("searchNotFound");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    } // end method search

    // start method follow
    public String follow() {

        // add jdbc library class
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            int i = stat.executeUpdate("Insert into facebook_follow (user_ID, follow_user_ID) values('" + id + "', '" + search_id + "')");
            search_followersCount++;
            following++;
            displayFollowButton = false;
            userStatus.clear();
            rs = stat.executeQuery("Select * from facebook_status where user_ID in (Select follow_user_ID from facebook_follow "
                    + "where user_ID = '" + id + "') or user_ID = '" + id + "' order by status_time desc");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(3);
                String statusUser_ID = rs.getString(2);
                String status_message = rs.getString(4);
                String status_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, statusUser_ID, status_message, status_time);
                stat1 = conn.createStatement();
                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                } else {
                    s.setDisplayLikeButton(true);
                }
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayReplies(true);
                } else {
                    s.setDisplayReplies(false);
                }
                rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                if (rs1.next()) {
                    s.setLike_count(rs1.getInt(1));
                } else {
                    s.setLike_count(0);
                }
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_id = '" + status_ID + "' order by reply_timestamp asc");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            return ("searchFound");
        } catch (SQLException e) {
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    }
// end method follow()

    // start method unfollow
    public String unFollow() {

        // add jdbc library class
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            int i = stat.executeUpdate("Delete from facebook_follow where user_ID = '" + id + "' and follow_user_ID = '" + search_id + "'");
            search_followersCount--;
            following--;
            displayFollowButton = true;
            userStatus.clear();
            rs = stat.executeQuery("Select * from facebook_status where user_ID in (Select follow_user_ID from facebook_follow "
                    + "where user_ID = '" + id + "') or user_ID = '" + id + "' order by status_time desc");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(3);
                String statusUser_ID = rs.getString(2);
                String status_message = rs.getString(4);
                String status_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, statusUser_ID, status_message, status_time);
                stat1 = conn.createStatement();
                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                } else {
                    s.setDisplayLikeButton(true);
                }
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setLike_count(rs1.getInt(1));
                } else {
                    s.setLike_count(0);
                }
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_id = '" + status_ID + "' order by reply_timestamp asc");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            return ("searchFound");
        } catch (SQLException e) {
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    }
// end method unfollow

    // start method share
    public String share(Status s1, String pageToRedirect) {

        // add jdbc library class
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        //three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            int i = stat.executeUpdate("Insert into facebook_status (user_ID,share_ID,status_message) "
                    + "values ('" + id + "','" + s1.getStatus_ID() + "','" + s1.getStatus_message() + "')");
            status++;
            userStatus.clear();
            rs = stat.executeQuery("Select * from facebook_status where user_ID in (select follow_user_ID from facebook_follow where "
                    + "user_ID = '" + id + "') or user_ID = '" + id + "' order by status_time desc");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(2);
                String statusUser_ID = rs.getString(3);
                String status_message = rs.getString(4);
                String status_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, statusUser_ID, status_message, status_time);
                stat1 = conn.createStatement();
                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                    s.setDisplayUnlikeButton(true);
                } else {
                    s.setDisplayLikeButton(true);
                    s.setDisplayUnlikeButton(false);
                }
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayReplies(true);
                } else {
                    s.setDisplayReplies(false);
                }
                rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                if (rs1.next()) {
                    s.setLike_count(rs1.getInt(1));
                } else {
                    s.setLike_count(0);
                }
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_id = '" + status_ID + "' order by reply_timestamp asc");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            trend.clear();
            rs = stat.executeQuery("Select * from facebook_trending order by trending_count desc");
            int counter = 1;
            while (rs.next() && counter < 6) {
                trend.add(new TrendingTopics(rs.getString(1), rs.getInt(2)));
                counter++;
            }
            return (pageToRedirect);
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    }
//end method share

    // start method likeStatus
    public String likeStatus(Status s, String pageToRedirect) {

        // add jdbc class library
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            int i = stat.executeUpdate("Insert into status_like (like_status_ID, like_user_ID) values ('" + s.getStatus_ID() + "','" + id + "')");
            s.setDisplayLikeButton(false);
            s.setDisplayUnlikeButton(true);
            int statusLikeCount = s.getLike_count();
            s.setLike_count(statusLikeCount + 1);
            return (pageToRedirect);
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    }
    // end method likeStatus

    // start method unlikeStatus
    public String unlikeStatus(Status s, String pageToRedirect) {

        // add jdbc class library
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return ("internalError");
        }

        // Database access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            int i = stat.executeUpdate("Delete from status_like where like_status_ID = '" + s.getStatus_ID() + "' and like_user_ID = '" + id + "'");
            s.setDisplayLikeButton(true);
            s.setDisplayUnlikeButton(false);
            int statusLikeCount = s.getLike_count();
            s.setLike_count(statusLikeCount - 1);
            return (pageToRedirect);
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                conn.close();
                stat.close();
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    }
// end method unlikeStatus

    // start method replyStatus
    public String replyStatus(Status s1, String pageToRedirect) {

        // add jdbc class library
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        }

        // Database Access
        final String DB_URL = "jdbc:mysql://localhost:3306/facebook";
        // three variables
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat1 = null;
        ResultSet rs1 = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stat = conn.createStatement();
            int i = stat.executeUpdate("Insert into status_reply (reply_status_ID,reply_user_ID,reply_message) values"
                    + "('" + s1.getStatus_ID() + "','" + id + "','" + status_reply_message + "')");
            userStatus.clear();
            rs = stat.executeQuery("Select * from facebook_status where user_ID in (Select follow_user_ID from facebook_follow where "
                    + "user_ID = '" + id + "') or user_ID = '" + id + "' order by status_time desc");
            while (rs.next()) {
                String status_ID = rs.getString(1);
                String share_ID = rs.getString(2);
                String statusUser_ID = rs.getString(3);
                String status_message = rs.getString(4);
                String status_time = rs.getString(5);
                Status s = new Status(status_ID, share_ID, statusUser_ID, status_message, status_time);
                stat1 = conn.createStatement();
                rs1 = stat1.executeQuery("Select * from status_like where like_status_ID = '" + status_ID + "' and like_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayLikeButton(false);
                } else {
                    s.setDisplayLikeButton(true);
                }
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_ID = '" + status_ID + "' and reply_user_ID = '" + id + "'");
                if (rs1.next()) {
                    s.setDisplayReplies(true);
                } else {
                    s.setDisplayReplies(false);
                }
                rs1 = stat1.executeQuery("Select count(1) from status_like where like_status_ID = '" + status_ID + "'");
                if (rs1.next()) {
                    s.setLike_count(rs1.getInt(1));
                } else {
                    s.setLike_count(0);
                }
                ArrayList<Reply> statusReplies = new ArrayList<Reply>();
                rs1 = stat1.executeQuery("Select * from status_reply where reply_status_id = '" + status_ID + "' order by reply_timestamp asc");
                while (rs1.next()) {
                    Reply r = new Reply(status_ID, rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    statusReplies.add(r);
                }
                s.setReplies(statusReplies);
                userStatus.add(s);
            }
            status_reply_message = "";
            return (pageToRedirect);
        } catch (SQLException e) {
            e.printStackTrace();
            error_message = e.getMessage();
            return ("internalError");
        } finally {
            try {
                stat.close();
                conn.close();
                rs.close();
                if (stat1 != null) {
                    stat1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                error_message = e.getMessage();
                return ("internalError");
            }
        }
    }

    // end method replyStatus
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getStatus_reply_message() {
        return status_reply_message;
    }

    public void setStatus_reply_message(String status_reply_message) {
        this.status_reply_message = status_reply_message;
    }

    public String getJoined_Date() {
        return joined_Date;
    }

    public void setJoined_Date(String joined_Date) {
        this.joined_Date = joined_Date;
    }

    public String getSearch_String() {
        return search_String;
    }

    public void setSearch_String(String search_String) {
        this.search_String = search_String;
    }

    public String getSearch_fname() {
        return search_fname;
    }

    public void setSearch_fname(String search_fname) {
        this.search_fname = search_fname;
    }

    public String getSearch_lname() {
        return search_lname;
    }

    public void setSearch_lname(String search_lname) {
        this.search_lname = search_lname;
    }

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }

    public int getSearch_statusCount() {
        return search_statusCount;
    }

    public void setSearch_statusCount(int search_statusCount) {
        this.search_statusCount = search_statusCount;
    }

    public int getSearch_followersCount() {
        return search_followersCount;
    }

    public void setSearch_followersCount(int search_followersCount) {
        this.search_followersCount = search_followersCount;
    }

    public int getSearch_followingCount() {
        return search_followingCount;
    }

    public void setSearch_followingCount(int search_followingCount) {
        this.search_followingCount = search_followingCount;
    }

    public String getSearch_joined_Date() {
        return search_joined_Date;
    }

    public void setSearch_joined_Date(String search_joined_Date) {
        this.search_joined_Date = search_joined_Date;
    }

    public boolean isDisplayFollowButton() {
        return displayFollowButton;
    }

    public void setDisplayFollowButton(boolean displayFollowButton) {
        this.displayFollowButton = displayFollowButton;
    }

    public ArrayList<Status> getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(ArrayList<Status> userStatus) {
        this.userStatus = userStatus;
    }

    public ArrayList<Status> getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(ArrayList<Status> searchStatus) {
        this.searchStatus = searchStatus;
    }

    public ArrayList<TrendingTopics> getTrend() {
        return trend;
    }

    public void setTrend(ArrayList<TrendingTopics> trend) {
        this.trend = trend;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

}
