package ie.thirdfloor.csis.ul.laedsgo.entities;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ForumPostModel implements Comparable<DiscoveryPostModel> {

    private Integer id;
    private String userId;
    private String username;
    private String content;
    private String time;
    private ArrayList<Object> comments;
    private Integer commentCount;

    public ForumPostModel(){

    }

    public ForumPostModel(Integer id, String userId, String username, String content, String time) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.time = time;
    }


    @NonNull
    @Override
    public String toString() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

     public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<Object> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Object> comments) {
        this.comments = comments;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public int compareTo(DiscoveryPostModel o) {
        return 0;
    }
}