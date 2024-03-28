package ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile;

import java.util.Date;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class ProfileDocument implements IDocument {
    public int id = 0;
    public String name = "default";
    public int ladsSeen = 0;
    public int ladsCaught = 0;

    public Date timestamp;

    @Override
    public String toString() {
        return "ProfileDocument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ladsSeen=" + ladsSeen +
                ", ladsCaught=" + ladsCaught +
                '}';
    }
}
