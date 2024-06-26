package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile.ProfileDocument;

public class CommentFragment extends Fragment {
    MutableLiveData<ArrayList<IDocument>> commentsData;
    View view;

    HashMap<Integer, ProfileDocument> profilePic = new HashMap<>();

    public static int postId = 0;


    public CommentFragment() {
    }

    @SuppressWarnings("unused")
    public static CommentFragment newInstance(int columnCount) {
        CommentFragment fragment = new CommentFragment();

        return fragment;
    }

    public static CommentFragment newInstance(MutableLiveData<ArrayList<IDocument>> comments, View view) {
        CommentFragment fragment = new CommentFragment();
        fragment.view = view;
        fragment.commentsData = comments;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_item_list, container, false);
        CommentConnection commentConnection = new CommentConnection();
        MutableLiveData<ArrayList<IDocument>> comments = new MutableLiveData<>();
        ProfileCollection profileConnection = new ProfileCollection();
        Button postComment = view.findViewById(R.id.commentsButton);
        SwipeRefreshLayout postFragmentSwipeRefreshLayout = view.findViewById(R.id.swiperefreshComments);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCommentFragment.postId = postId;
                NavHostFragment navHostFragment = (NavHostFragment) ((AppCompatActivity) getContext()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                navHostFragment.getNavController().navigate(R.id.createComment);
            }
        });

        // On back swipe go to the posts
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment navHostFragment = (NavHostFragment) ((AppCompatActivity) getContext()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                navHostFragment.getNavController().navigate(R.id.messages);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        comments.observe(getViewLifecycleOwner(), new Observer<ArrayList<IDocument>>() {
            @Override
            public void onChanged(ArrayList<IDocument> iDocuments) {
                ArrayList<CommentModel> commentModels = new ArrayList<>();
                CommentReyclerViewAdapter adapter = new CommentReyclerViewAdapter(getContext(), commentModels);
                adapter.clearArray();

                for (IDocument iDocument : iDocuments) {
                    CommentDocument commentDocument = (CommentDocument) iDocument;
                    MutableLiveData<IDocument> profileConnect = new MutableLiveData<>();


                    Observer<IDocument> observer = new Observer<IDocument>() {
                        @Override
                        public void onChanged(IDocument iDocument) {
                            ProfileDocument profileDocument = (ProfileDocument) iDocument;
                            Log.i("CommentFragment", "onChanged: " + profileDocument.name);
                            adapter.addToArray(createComment(commentDocument, profileDocument.name, profileDocument.profilePhoto));
                            Log.i("CommentFragment", "onChanged: " + commentModels.size());

                            profilePic.put(commentDocument.userId, profileDocument);

                            adapter.notifyDataSetChanged();

                        }
                    };
                    if (profilePic.containsKey(commentDocument.userId)) {
                        adapter.addToArray(createComment(commentDocument, profilePic.get(commentDocument.userId).name, profilePic.get(commentDocument.userId).profilePhoto));
                        adapter.notifyDataSetChanged();
                    } else {
                        profileConnect.observe(getViewLifecycleOwner(), observer);
                        profileConnection.get(commentDocument.userId, profileConnect);
                    }

                }

                RecyclerView recyclerView = view.findViewById(R.id.commentList);

                commentModels.sort(Collections.reverseOrder());

                adapter.sortArray();

                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        commentConnection.getAllCommentsForPost(comments, postId);


        postFragmentSwipeRefreshLayout.setOnRefreshListener(() -> {
            final Handler handler = new Handler();

            commentConnection.getAllCommentsForPost(comments, postId);

            handler.postDelayed(() -> {
                if(postFragmentSwipeRefreshLayout.isRefreshing()) {
                    postFragmentSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        });

        return view;
    }

    public static CommentModel createComment(CommentDocument commentDocument, String username, String userPhoto) {

        return new CommentModel(
                commentDocument.id,
                commentDocument.userId,
                commentDocument.message,
                commentDocument.postId,
                0,
                commentDocument.timestamp,
                userPhoto,
                username);
    }

}