package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentDiscoveryPostsRootBinding;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;

public class DiscoveryPostsRoot extends Fragment {
    private static final String TAG = "DiscoveryPostsRoot";
    private FragmentDiscoveryPostsRootBinding binding;
    private MutableLiveData<IDocument> loggedInUser = new MutableLiveData<>();
    private ProfileCollection profileCollection = new ProfileCollection();
    public DiscoveryPostsRoot() {
        // Required empty public constructor
    }

    public static DiscoveryPostsRoot newInstance() {

        return new DiscoveryPostsRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        profileCollection.get(0, loggedInUser);

        if(savedInstanceState != null){
            Log.i(TAG, "onViewCreated: savedInstance");
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiscoveryPostsRootBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(savedInstanceState != null){
            Log.i(TAG, "onViewCreated: savedInstance");
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button goToPostsButton = view.findViewById(R.id.makePostBtn);

        goToPostsButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Navigation.findNavController(v).navigate(R.id.make_post);
            }
        }));

        if(savedInstanceState != null){
            Log.i(TAG, "onViewCreated: savedInstance");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}