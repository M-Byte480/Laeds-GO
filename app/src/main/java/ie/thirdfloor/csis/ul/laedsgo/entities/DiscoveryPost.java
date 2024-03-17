package ie.thirdfloor.csis.ul.laedsgo.entities;

import androidx.annotation.NonNull;

public class DiscoveryPost {

    private String id;
    private String userId;
    private String username;
    private int likes;
    private int dislikes;

    private boolean isLiked;
    private boolean isDisliked;

    private String content;
    private String location;

    private String time;

    public DiscoveryPost(){

    }

    public DiscoveryPost(String id, String userId, String username, int likes, int dislikes, boolean isLiked, boolean isDisliked, String content, String location, String time) {
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLikes(){
        return Integer.toString(likes);
    }

    public String getDislikes(){
        return Integer.toString(dislikes);
    }

    public String getTime(){
        return time;
    }
    @NonNull
    @Override
    public String toString() {
        return username;
    }
}
