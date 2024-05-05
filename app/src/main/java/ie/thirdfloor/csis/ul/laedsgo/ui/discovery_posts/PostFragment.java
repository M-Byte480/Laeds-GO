package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;

/**
 * A fragment representing a list of Items.
 */
public class PostFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "1";
    private static final String TAG = "PostFragment";
    private PostRecyclerViewAdapter adapter;
    ArrayList<DiscoveryPostModel> postsModels = new ArrayList<>();
    private static final TOLPostCollection dbConnection = new TOLPostCollection();

    public MutableLiveData<IDocument> loggedInUser = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<IDocument>> elements = new MutableLiveData<>(new ArrayList<>());
    private SwipeRefreshLayout postFragmentSwipeRefreshLayout;
    private final ProfileCollection profileCollection = new ProfileCollection();

    private int USER_ID;

    RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostFragment() {
    }

    @SuppressWarnings("unused")
    public static PostFragment newInstance(int columnCount) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.USER_ID = ProfileInfo.getId();


        if(savedInstanceState != null){
            Log.i(TAG, "onViewCreated: savedInstance");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        recyclerView = view.findViewById(R.id.postsRecyclerViewList);
        setupPostsModels(view);
        profileCollection.get(USER_ID, loggedInUser);
        this.adapter = new PostRecyclerViewAdapter(getContext(), postsModels, loggedInUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        postFragmentSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        postFragmentSwipeRefreshLayout.setOnRefreshListener(() -> {
            final Handler handler = new Handler();
            Log.i(TAG, "Refresh");

            dbConnection.getAll(elements);
            handler.postDelayed(() -> {
                if(postFragmentSwipeRefreshLayout.isRefreshing()) {
                    postFragmentSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        });

        return view;
    }

    private void setupPostsModels(View view){
        elements.observe(getViewLifecycleOwner(), iDocuments -> {
            ArrayList<DiscoveryPostModel> discoveryPosts = new ArrayList<>();
            for (IDocument iDocument : iDocuments) {
                TOLPostDocument post = (TOLPostDocument) iDocument;

                discoveryPosts.add(
                        createPost(post)
                );
            }

            discoveryPosts.sort(Collections.reverseOrder());
            int oldItems = adapter.itemCount;

            recyclerView = view.findViewById(R.id.postsRecyclerViewList);
            adapter.setArray(discoveryPosts);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            int diff = adapter.itemCount - oldItems;

            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRangeInserted(0, diff);
        });

        dbConnection.getAll(elements);

    }

    private static DiscoveryPostModel createPost(TOLPostDocument p) {

        return new DiscoveryPostModel(
                p.id,
                String.valueOf(p.userId),
                String.valueOf(p.userId),
                p.likes,
                p.dislikes,
                false, false,
                p.message,
                p.location.get("lat") + ", " + p.location.get("lon"),
                p.timestamp.toString());
    }
}