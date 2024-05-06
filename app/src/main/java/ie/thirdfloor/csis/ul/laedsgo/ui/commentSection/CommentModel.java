package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

import java.util.Date;

import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;

public class CommentModel implements Comparable<CommentModel> {
    int id;
    int userId;

    String comment_user_text = "";

    int postId;

    int parentId;

    Date timestamp;

    String image;

    String username;

    public CommentModel(int id, int userId, String comment_user_text, int postId, int parentId, Date timestamp, String image, String username){
        this.id = id;
        this.userId = userId;
        this.comment_user_text = comment_user_text;
        this.postId = postId;
        this.parentId = parentId;
        this.timestamp = timestamp;
        this.image = image;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getComment_user_text() {
        return comment_user_text;
    }

    public int getPostId() {
        return postId;
    }

    public int getParentId() {
        return parentId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setComment_user_text(String comment_user_text) {
        this.comment_user_text = comment_user_text;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return username;
    }

    @Override
    public int compareTo(CommentModel that) {
        return -1*Integer.compare(
                this.id,
                that.id
        );
    }
}
