package ie.thirdfloor.csis.ul.laedsgo.dbConnection.post;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class TOLPostDocument implements IDocument {
    public Integer id = 0;
    public Integer userId = 0;
    public Integer likes = 0;
    public Integer dislikes = 0;
    public String message = "";
    public Date timestamp = new Date();
    public Map<String, Float> location = new HashMap<>();

    @Override
    public String toString() {
        return "TOLPostDocument{" +
                "id=" + id +
                ", userId=" + userId +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", location=" + location +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
