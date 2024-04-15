package ie.thirdfloor.csis.ul.laedsgo.ui.view_profile;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ie.thirdfloor.csis.ul.laedsgo.R;

public class ViewProfileFragment extends Fragment {

    public static ViewProfileFragment newInstance() {
        return new ViewProfileFragment();
    }

    private ViewProfileViewModel mViewModel;

    private static final String TAG = "ViewProfileFragment:";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Log.i(TAG, "handleOnBackPressed: BACK PRESS");

                // We remove the transaction
                removeFragmentFromStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }


    private void removeFragmentFromStack(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(this).commit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

}