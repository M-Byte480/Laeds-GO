package ie.thirdfloor.csis.ul.laedsgo.entities;

import java.util.UUID;

public class Post {
    private UUID id;
    private String content;
    private String details;
    private String location;


    public Post(UUID id, String content, String details, String location) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    public Post(String id, String content, String details, String location) {
        this(UUID.fromString(id), content, details, location);
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

    @Override
    public String toString() {
        return content;
    }
}
