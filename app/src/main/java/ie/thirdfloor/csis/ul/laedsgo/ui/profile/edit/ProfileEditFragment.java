package ie.thirdfloor.csis.ul.laedsgo.ui.profile.edit;

import static android.app.Activity.RESULT_OK;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProfileEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String updatedPhoto = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        setDefaultProfilePhoto();

        Button gotoProfile = view.findViewById(R.id.cancelBtn);

        gotoProfile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Navigation.findNavController(v).navigate(R.id.profile);
            }
        }));

        Button saveUpdate = view.findViewById(R.id.saveBtn);

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCollection collection = new ProfileCollection();

                EditText usernameText = view.findViewById(R.id.editUsername);
                EditText bioText = view.findViewById(R.id.editBio);

                String name = usernameText.getText().toString();
                String bio = bioText.getText().toString();

                if (!name.isEmpty()) {
                    collection.updateUsername(ProfileInfo.getId(), name);
                }

                if (!bio.isEmpty()) {
                    collection.updateBio(ProfileInfo.getId(), bio);
                }

                if(!updatedPhoto.isEmpty()) {
                    collection.updateProfilePicture(ProfileInfo.getId(), updatedPhoto);
                }

                ProfileInfo.setProfile(((ProfileDocument)(ProfileInfo.getProfile().getValue())).UID);

                Navigation.findNavController(v).navigate(R.id.profile);
            }
        });

        Button selectImage = view.findViewById(R.id.selectProfilePictureBtn);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            AssetFileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(selectedImage , "r");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            long fileSize = fileDescriptor.getLength();

            Log.i("see size", Long.toString(fileSize));


            if (fileSize < 100000){
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();

                    updatedPhoto = Base64.encodeToString(bytes, Base64.DEFAULT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    ImageView selectProfilePhotoResult = getView().findViewById(R.id.profilePhoto);
                    selectProfilePhotoResult.setImageURI(selectedImage);
                } catch (Exception ex) {
                    Log.i("see size", ex.getMessage());
                }
            } else {
                setDefaultProfilePhoto();
            }
        }
    }

    private void setDefaultProfilePhoto() {
        ImageView selectProfilePhotoResult = getView().findViewById(R.id.profilePhoto);
        ProfileDocument profile = (ProfileDocument) ProfileInfo.getProfile().getValue();

        byte[] decodedString = Base64.decode(profile.profilePhoto, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        selectProfilePhotoResult.setImageBitmap(decodedByte);
    }

}