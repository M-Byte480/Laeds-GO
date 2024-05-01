package ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class ProfileDocument implements IDocument {
    public int id = 0;
    public String UID = "";
    public String name = "default";
    public int ladsSeen = 0;
    public int ladsCaught = 0;
    public String profilePhoto = "";
    public String bio = "";
    public Date timestamp;
    public ArrayList<Integer> likedPosts = new ArrayList();
    public ArrayList<Integer> dislikedPosts = new ArrayList();

    @Override
    public String toString() {
        return "ProfileDocument{" +
                "id=" + id +
                ", UID=" + UID + '\'' +
                ", name='" + name + '\'' +
                ", ladsSeen=" + ladsSeen +
                ", ladsCaught=" + ladsCaught +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", bio='" + bio + '\'' +
                ", timestamp=" + timestamp +
                ", likes={" + likedPosts.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}" +
                ", dislikes={" + dislikedPosts.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}" +
                '}';
    }
}
