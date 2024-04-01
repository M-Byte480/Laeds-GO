package ie.thirdfloor.csis.ul.laedsgo.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentProfileBinding;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileCollection profileCollection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileCollection = new ProfileCollection();

        MutableLiveData<IDocument> mProfile = profileViewModel.getmProfile();
        mProfile.observe(getViewLifecycleOwner(), new Observer<IDocument>() {
            @Override
            public void onChanged(IDocument iDocument) {
                ProfileDocument profileDocument = (ProfileDocument) iDocument;

                binding.NickName.setText(profileDocument.name);
                binding.Bio.setText(profileDocument.bio);

                byte[] decodedString = Base64.decode(profileDocument.profilePhoto, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                binding.profilePhoto.setImageBitmap(decodedByte);
            }
        });

        profileCollection.get(0, mProfile);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}