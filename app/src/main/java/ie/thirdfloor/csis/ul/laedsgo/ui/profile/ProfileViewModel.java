package ie.thirdfloor.csis.ul.laedsgo.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<IDocument> mProfile;

    public ProfileViewModel() {
        mProfile = new MutableLiveData<>(new ProfileDocument());
    }

    public MutableLiveData<IDocument> getmProfile() {
        return mProfile;
    }
}