package ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments;

import java.util.Date;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class CommentDocument implements IDocument {
    public Integer id = 0;
    public Integer userId = 0;
    public String message = "";
    public Integer parentId = -1;
    public Integer type = 0;
    public Date timestamp;

    @Override
    public String toString() {
        return "CommentDocument{" +
                "id=" + id +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", parentId=" + parentId +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
}
