package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostDocument;

public class PostEntryFragment extends Fragment {
    private static final String LOG = "PostEntryFragment.java";
    private EditText multilineTextEdit;
    private TextView characterCountLeftView;
    private final int maxCharacterCount = 272;
    private FusedLocationProviderClient fusedLocationProvider;
    private static Location currentLocation;
    private final TOLPostDocument post = new TOLPostDocument();
    private final TOLPostCollection dbConnection = new TOLPostCollection();
    private static final String TAG = "PostEntryFragment";
    private boolean published = false;


    public PostEntryFragment() {
        // Required empty public constructor
    }


    public static PostEntryFragment newInstance() {

        return new PostEntryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        Task<Location> task = fusedLocationProvider.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                setPostLongitudeAndLatitude(post);
                Log.i(TAG, "onSuccess: Location Grabbed");
            }else{
                Log.i(TAG, "onSuccess: Location is null");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_entry, container, false);

        this.multilineTextEdit = view.findViewById(R.id.user_input);
        this.characterCountLeftView = view.findViewById(R.id.characterCountLeft);

        this.multilineTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                int charactersLeftToUse = maxCharacterCount - s.length();
                characterCountLeftView.setText(String.valueOf(charactersLeftToUse));
            }
        });

        Button publishBtn = view.findViewById(R.id.publishBtn);

        publishBtn.setOnClickListener((v -> {
            String textWithoutLeadingSpaces = multilineTextEdit.getText().toString().trim();
            int textLength = textWithoutLeadingSpaces.length();
            if (textLength > 0) {
                Log.i(LOG, "Publish Btn Clicked");

                post.userId = ProfileInfo.getId();

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                post.message = textWithoutLeadingSpaces;

                if(currentLocation != null){
                    Log.i(TAG, "onClick: " + currentLocation.getLongitude() + " " + currentLocation.getLatitude());

                    post.location.put("lon", (float) currentLocation.getLongitude());
                    post.location.put("lat", (float) currentLocation.getLatitude());

                    try {
                        dbConnection.push(post);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    published = true;
                }


                Toast.makeText(getContext(), "Successfully Published", Toast.LENGTH_LONG).show();

                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "There is not enough content to post", Toast.LENGTH_LONG).show();
            }
        }));
        return view;
    }

    private void setPostLongitudeAndLatitude(TOLPostDocument post) {

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = location -> {
            post.location.put("lon", (float) location.getLongitude());
            post.location.put("lat", (float) location.getLatitude());

            if (published) {
                try {
                    dbConnection.push(post);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                published = false;
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
}