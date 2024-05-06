package ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments;

import java.util.Date;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class CommentDocument implements IDocument, Comparable<CommentDocument> {
    public Integer id = 0;
    public Integer userId = 0;
    public String message = "";
    public Integer parentId = -1;
    public Integer postId = 0;
    public Date timestamp;

    @Override
    public String toString() {
        return "CommentDocument{" +
                "id=" + id +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", parentId=" + parentId +
                ", type=" + postId +
                ", timestamp=" + timestamp +
                '}';
    }

    public int compareTo(CommentDocument comment) {
        return this.timestamp.compareTo(comment.timestamp);
    }
}
