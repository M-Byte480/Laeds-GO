package ie.thirdfloor.csis.ul.laedsgo.ui.maps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import android.widget.Toast;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.MainActivity;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentMapsBinding;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.*;

public class MapsFragment extends Fragment {
    private FragmentMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mymap;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private ArrayList<LeadsDeckDocument> leadDeck;
    private boolean markersSet = false;

    @SuppressLint("MissingPermission")
    private void onMapReady(GoogleMap googleMap) {
        mymap = googleMap;
        mymap.setMyLocationEnabled(true);
        if (!markersSet) addLeadsToMap();
        markersSet = true;
    }

    private LatLng addNoiseToLocation(LatLng location) {
        double lati = location.latitude + (Math.random()*0.001f - 0.0005f);
        double longi = location.longitude + (Math.random()*0.001f - 0.0005f);

        return new LatLng(lati, longi);
    }

    @SuppressLint("MissingPermission")
    private void addLeadsToMap() {
        Task<Location> updatelocationTask = fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        LatLng user_location = new LatLng(location.getLatitude(), location.getLongitude());
                        mymap.moveCamera(CameraUpdateFactory.newLatLng(user_location));
                        mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_location, 18.0f));

                        for (int i=0; i<((int)(Math.random()*2)+2); i++) {
                            int numberOfLeads = leadDeck.size();
                            if (numberOfLeads == 0) break;

                            LeadsDeckDocument lead = leadDeck.get((int)(Math.random()*numberOfLeads));

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(addNoiseToLocation(user_location))
                                    .title(lead.name)
                                    .contentDescription(lead.description);
                            if (lead.picture != null && !lead.picture.isEmpty()) {
                                byte[] decodedString = Base64.decode(lead.picture, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                decodedByte = Bitmap.createScaledBitmap(decodedByte, 128, 128, false);
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(decodedByte));
                            }
                            mymap.addMarker(markerOptions);
                        }
                    }
                });
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapsBinding.inflate(inflater, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        requestLocationPermission();

        leadDeck = ((MainActivity) requireActivity()).getLeadsDeck();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(requireContext(), perms)) {
            Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
}