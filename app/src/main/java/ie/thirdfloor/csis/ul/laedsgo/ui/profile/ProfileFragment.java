package ie.thirdfloor.csis.ul.laedsgo.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentProfileBinding;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileCollection profileCollection;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        if ((ProfileInfo.getId() == 0)) {
            ProfileInfo.setProfile(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        profileCollection.get(ProfileInfo.getId(), mProfile);

        ImageButton buttonLogout;
        buttonLogout = binding.logoutButton;

        //Logout eventListener
        buttonLogout.setOnClickListener(V -> {
            System.out.println("Logout clicked");
            FirebaseAuth.getInstance().signOut();
            /*NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);*/
            Toast.makeText(getContext(), "User " + currentUser.getEmail() + " was signed out", Toast.LENGTH_SHORT).show();
            //Check if there is a user
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
            try {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(currentUser.getEmail());
            } catch (NullPointerException e) {
                System.out.println("No user YIPEEEEEE!");
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ImageButton gotoEditProfileBtn = view.findViewById(R.id.EditBtn);

        gotoEditProfileBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Navigation.findNavController(v).navigate(R.id.editProfile);
            }
        }));

        if(savedInstanceState != null){
            Log.i("Bojo", "onViewCreated: savedInstance");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}