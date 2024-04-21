package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

public class CommentModel {

    String userName;
    String comment_user_text;

    int image;

    public CommentModel(String userName, String comment_user_text, int image) {
        this.userName = userName;
        this.comment_user_text = comment_user_text;
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment_user_text() {
        return comment_user_text;
    }

    public int getImage() {
        return image;
    }
}
