package ie.thirdfloor.csis.ul.laedsgo.ui.ar;

import static ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo.getLadsSeen;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.ui.maps.MapsFragment;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;

public class myArFragment extends ArFragment implements ArFragment.OnTapArPlaneListener {
    private ModelRenderable lead;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnTapArPlaneListener(this);

        int leadID = MapsFragment.leadID;

        ProfileCollection profileCollection = new ProfileCollection();
        ArrayList<Integer> ladsSeen = getLadsSeen();
        if (! ladsSeen.contains(leadID)) {
            ladsSeen.add(leadID);

            profileCollection.updateLadsSeen(ProfileInfo.getId(), ladsSeen);
        }

        switch (leadID) {
            case 0: buildRenderable(R.raw.relaxaurus);
            case 1: buildRenderable(R.raw.tyrannosaurus);
            case 2: buildRenderable(R.raw.triceratops);
            case 3: buildRenderable(R.raw.spinosaurus);
            default: buildRenderable(R.raw.tiger);
        }
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
            Anchor anchor = hitResult.createAnchor();
            addNodeToScene(anchor);
    }

    private void buildRenderable(int uri) {
        ModelRenderable.builder()
                .setSource(getContext(), uri)
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(modelRenderable -> {
                    lead = modelRenderable;
                })
                .exceptionally(throwable -> {
                            Toast.makeText(getContext(), "Error:" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            return null;
                        }
                );
    }

    private void addNodeToScene(Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(getTransformationSystem());
        node.getScaleController().setMaxScale(0.2f);
        node.getScaleController().setMinScale(0.1f);
        node.setRenderable(lead);
        node.setParent(anchorNode);
        getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }
}