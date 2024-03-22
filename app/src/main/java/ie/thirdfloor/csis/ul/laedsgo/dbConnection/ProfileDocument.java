package ie.thirdfloor.csis.ul.laedsgo.dbConnection;

import java.util.Date;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class ProfileDocument implements IDocument {
    public int id;
    public String name;
    public int ladsSeen;
    public int ladsCaught;

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
