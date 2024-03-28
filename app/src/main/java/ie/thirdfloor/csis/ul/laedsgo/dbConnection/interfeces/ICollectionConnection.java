package ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;

public interface ICollectionConnection {
    public void push(IDocument item) throws InterruptedException;

    public void get(int id, MutableLiveData<IDocument> mProfile);

    public void getAll(MutableLiveData<ArrayList<IDocument>> mProfileList);
}
