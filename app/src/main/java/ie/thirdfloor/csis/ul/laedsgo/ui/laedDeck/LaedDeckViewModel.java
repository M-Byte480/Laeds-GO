package ie.thirdfloor.csis.ul.laedsgo.ui.laedDeck;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LaedDeckViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LaedDeckViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fucking Dack");
    }

    public LiveData<String> getText() {
        return mText;
    }
}