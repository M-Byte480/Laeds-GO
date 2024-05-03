package ie.thirdfloor.csis.ul.laedsgo.ui.view_profile;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;

public class ViewProfileFragment extends Fragment {

    private String userId;
    private final ProfileCollection profileDB = new ProfileCollection();
    private final MutableLiveData<IDocument> profile = new MutableLiveData<>();
    private ViewProfileViewModel mViewModel;

    private static final String TAG = "ViewProfileFragment:";
    private static final HashMap<String, IDocument> cache = new HashMap<>();

    public static ViewProfileFragment newInstance() {
        return new ViewProfileFragment();
    }

    public static ViewProfileFragment newInstance(String id) {
        ViewProfileFragment profileFragment = new ViewProfileFragment();
        profileFragment.userId = id;
        return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                // We remove the transaction
                removeFragmentFromStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);


        profile.observe(getViewLifecycleOwner(), new Observer<IDocument>() {
            @Override
            public void onChanged(IDocument document) {
                ProfileDocument profileDocument = (ProfileDocument) document;
                ImageView profileImage = getView().findViewById(R.id.viewProfilePhoto);
                TextView profileName = getView().findViewById(R.id.viewProfileName);
                TextView profileBio = getView().findViewById(R.id.viewProfileBio);

                byte[] decodedString = Base64.decode(profileDocument.profilePhoto, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                profileImage.setImageBitmap(decodedByte);
                profileName.setText(profileDocument.name);
                profileBio.setText(profileDocument.bio);

                cache.put(userId, document);
            }
        });

        if(cache.get(userId) != null){
            ProfileDocument profileDocument = (ProfileDocument) cache.get(userId);
            ImageView profileImage = view.findViewById(R.id.viewProfilePhoto);
            TextView profileName = view.findViewById(R.id.viewProfileName);
            TextView profileBio = view.findViewById(R.id.viewProfileBio);

            byte[] decodedString = Base64.decode(profileDocument.profilePhoto, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            profileImage.setImageBitmap(decodedByte);
            profileName.setText(profileDocument.name);
            profileBio.setText(profileDocument.bio);
        }else{
            profileDB.getUserById(Integer.valueOf(userId), profile);
        }


        return view;
    }

    private void removeFragmentFromStack(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(this).commit();
    }



}