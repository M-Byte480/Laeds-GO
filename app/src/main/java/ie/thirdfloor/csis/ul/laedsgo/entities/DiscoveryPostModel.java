package ie.thirdfloor.csis.ul.laedsgo.entities;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DiscoveryPostModel implements Comparable<DiscoveryPostModel> {

    private Integer id;
    private String userId;
    private String username;
    private int likes;
    private int dislikes;

    private boolean isLiked;
    private boolean isDisliked;

    private String content;
    private String location;

    private String time;

    private ArrayList<Object> comments;
    private int commentCount;

    public DiscoveryPostModel(){

    }

    public DiscoveryPostModel(Integer id, String userId, String username, int likes, int dislikes, boolean isLiked, boolean isDisliked, String content, String location, String time) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.content = content;
        this.location = location;
        this.time = time;
    }


    public boolean checkIfPostIsLikedAndDisliked(){
        return isLiked && isDisliked;
    }

    public void setLiked(){
        isLiked = true;
        isDisliked = false;
    }

    public void setDisliked(){
        isLiked = false;
        isDisliked = true;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void incrementLikes(){
        this.likes++;
    }

    public void decrementLikes(){
        this.likes--;
    }

    public void incremenetDislikes(){
        this.dislikes++;
    }

    public void decrementDislikes(){
        this.dislikes--;
    }
    @Override
    public int compareTo(DiscoveryPostModel that) {
        return Integer.compare(
                this.id,
                that.id
        );
    }
}