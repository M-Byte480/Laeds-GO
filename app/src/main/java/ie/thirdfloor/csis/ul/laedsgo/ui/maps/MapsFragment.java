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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.ArFragment;

import android.widget.Toast;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.MainActivity;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentMapsBinding;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.*;

public class MapsFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private FragmentMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mymap;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private ArrayList<LeadsDeckDocument> leadDeck;
    private boolean markersSet = false;

    private ArFragment arFragment;

    @SuppressLint("MissingPermission")
    private void onMapReady(GoogleMap googleMap) {
        mymap = googleMap;
        mymap.setMyLocationEnabled(true);
        mymap.setInfoWindowAdapter(new MapsInfoWindowAdaptor(requireContext()));
        googleMap.setOnInfoWindowClickListener(this);
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
                                    .title(String.format("%d. %s",lead.leadId, lead.name))
                                    .snippet(lead.description);
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

        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            initializeMap();
        } else {
            requestLocationPermission();
        }

        leadDeck = ((MainActivity) requireActivity()).getLeadsDeck();

        arFragment = new ArFragment();
        arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                    if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING)
                        return;

                    Anchor anchor = hitresult.createAnchor();
                    placeObject(arFragment, anchor, R.raw.model);
                }
        );

        return binding.getRoot();
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }
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

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Log.d("Maps Fragment", "Opening AR fragment");

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup)getView().getParent()).getId(), arFragment, "ar")
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
    private void placeObject(ArFragment arFragment, Anchor anchor, int uri) {
        ModelRenderable.builder()
                .setSource(arFragment.getContext(), uri)
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(modelRenderable -> addNodeToScene(arFragment, anchor, modelRenderable))
                .exceptionally(throwable -> {
                            Toast.makeText(arFragment.getContext(), "Error:" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            return null;
                        }
                );
    }

    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }
}