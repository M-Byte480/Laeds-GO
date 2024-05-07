package ie.thirdfloor.csis.ul.laedsgo.ui.laedDeck;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostDocument;
import ie.thirdfloor.csis.ul.laedsgo.entities.DiscoveryPostModel;
import ie.thirdfloor.csis.ul.laedsgo.entities.LaedDeckModel;
import ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts.PostRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 */
public class LaedDeckFragmentV2 extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    ArrayList<LaedDeckModel> laedModels = new ArrayList<>();
    private MutableLiveData<ArrayList<IDocument>> elements = new MutableLiveData<>(new ArrayList<>());

    LeadsDeckConnection laedConnection = new LeadsDeckConnection();
    RecyclerView recycler;
    private MyLaedRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LaedDeckFragmentV2() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LaedDeckFragmentV2 newInstance(int columnCount) {
        LaedDeckFragmentV2 fragment = new LaedDeckFragmentV2();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laed_deck_v2_list, container, false);
        recycler = view.findViewById(R.id.laedDeckList);
        adapter = new MyLaedRecyclerViewAdapter(elements.getValue());
        if(elements.getValue() == null || elements.getValue().size() == 0){
            setupPostsModels(view, inflater);
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyLaedRecyclerViewAdapter( elements.getValue()));
        }
        return view;
    }

    private void setupPostsModels(View view, LayoutInflater inflater){
        elements.observe(getViewLifecycleOwner(), new Observer<ArrayList<IDocument>>() {
            @Override
            public void onChanged(ArrayList<IDocument> iDocuments) {
                recycler = view.findViewById(R.id.laedDeckList);
                adapter.setLaeds(iDocuments);
                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));

                recycler.getAdapter().notifyDataSetChanged();
            }
        });

        laedConnection.getAll(elements);

    }


//    private LaedDeckModel createPost(LeadsDeckDocument dbDoc) {
//        return new LaedDeckModel(
//
//        )
//    }
}