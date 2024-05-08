package ie.thirdfloor.csis.ul.laedsgo.ProfileInfo;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;

public class ProfileInfo {
    private static MutableLiveData<IDocument> profile = new MutableLiveData<>(new ProfileDocument());

    public static void setProfile(String uid) {
        ProfileCollection profileCollection = new ProfileCollection();

        profileCollection.getByUID(uid, profile);
    }

    public static int getId() {
        return ((ProfileDocument)profile.getValue()).id;
    }

    public static MutableLiveData<IDocument> getProfile() {
        return profile;
    }

    public static ArrayList<Integer> getLadsSeen() {
        return ((ProfileDocument)profile.getValue()).ladsSeen;
    }

}
