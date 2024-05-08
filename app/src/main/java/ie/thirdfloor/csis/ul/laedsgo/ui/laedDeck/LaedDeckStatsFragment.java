package ie.thirdfloor.csis.ul.laedsgo.ui.laedDeck;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckDocument;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LaedDeckStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LaedDeckStatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LaedDeckStatsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LeadsDeckConnection laedConnection = new LeadsDeckConnection();

    MutableLiveData<IDocument> lad = new MutableLiveData<>();
    RecyclerView recycler;
    private MyLaedRecyclerViewAdapter adapter;
    public static Integer laedID;

    public LaedDeckStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LaedDeckStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LaedDeckStatsFragment newInstance(String param1, String param2) {
        LaedDeckStatsFragment fragment = new LaedDeckStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_laed_deck_stats2, container, false);

        lad.observe(getViewLifecycleOwner(), new Observer<IDocument>() {
            @Override
            public void onChanged(IDocument iDocument) {

                LeadsDeckDocument lad = (LeadsDeckDocument) iDocument;

                String statsRarity = lad.rarity;
                String statsName = lad.name;
                String statsDescription = lad.description;
                String statsPhoto = lad.picture;

                ((TextView) rootView.findViewById(R.id.statsId)).setText("ID : " + String.valueOf(laedID));
                ((TextView) rootView.findViewById(R.id.statsDescription)).setText("Description : " + statsDescription);
                ((TextView) rootView.findViewById(R.id.statsName)).setText("Name : " + statsName);
                ((TextView) rootView.findViewById(R.id.statsRarity)).setText("Rarity : " + statsRarity);

                byte[] decodedString = Base64.decode(String.valueOf(statsPhoto), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                ((ImageView) rootView.findViewById(R.id.statsPhoto)).setImageBitmap(decodedByte);

                Button statsLaedDeckButton = rootView.findViewById(R.id.statsLaedDeck);

                statsLaedDeckButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager =  ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
                        navHostFragment.getNavController().popBackStack();
                    }
                });

            }
        });

        laedConnection.get(laedID, lad);
        return rootView;
    }


}
