package ie.thirdfloor.csis.ul.laedsgo.entities;

import androidx.annotation.NonNull;

import java.util.UUID;

public class DiscoveryPost {
    private UUID id;
    private UUID userId;
    private String content;
    private int likes;
    private int dislikes;

    private boolean isLiked;
    private boolean isDisliked;

    private String details;
    private String location;


    public DiscoveryPost(){

    }

    public DiscoveryPost(UUID id, UUID userId, String content, int likes, int dislikes, boolean isLiked, boolean isDisliked, String details, String location) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.details = details;
        this.location = location;
    }

    public DiscoveryPost(String id, String userId, String content, int likes, int dislikes, boolean isLiked, boolean isDisliked, String details, String location) {
        this.id = UUID.fromString(id);
        this.userId = UUID.fromString(userId);
        this.content = content;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isLiked = isLiked;
        this.isDisliked = isDisliked;
        this.details = details;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return content;
    }
}
