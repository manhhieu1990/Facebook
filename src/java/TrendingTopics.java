/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ABC
 */
public class TrendingTopics {
    
    private String hashTag;
    private int count;
    
    public TrendingTopics(String tag, int c){
        hashTag = tag;
        count = c;
    }
    
    //getters and setters

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}
