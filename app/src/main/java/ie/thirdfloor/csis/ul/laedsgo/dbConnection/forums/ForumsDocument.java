package ie.thirdfloor.csis.ul.laedsgo.dbConnection.forums;

import java.util.Date;


import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class ForumsDocument implements IDocument {
    public Integer id = 0;
    public Integer userId = 0;
    public String message = "";
    public Date timestamp = new Date();


    @Override
    public String toString() {
        return "TOLPostDocument{" +
                "id=" + id +
                ", message " + message  +
                ", timestamp='" + timestamp + '\'' +
                ", userId=" + userId +
                '}';
    }
}
