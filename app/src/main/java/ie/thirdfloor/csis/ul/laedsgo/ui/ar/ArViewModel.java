package ie.thirdfloor.csis.ul.laedsgo.ui.ar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ArViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}