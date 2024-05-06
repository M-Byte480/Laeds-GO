package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        comments.observe(getViewLifecycleOwner(), new Observer<ArrayList<IDocument>>() {
            @Override
            public void onChanged(ArrayList<IDocument> iDocuments) {
                ArrayList<CommentModel> commentModels = new ArrayList<>();
                CommentReyclerViewAdapter adapter = new CommentReyclerViewAdapter(getContext(), commentModels);

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


                commentModels.sort(Collections.reverseOrder());

                RecyclerView recyclerView = view.findViewById(R.id.commentList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        commentConnection.getAllCommentsForPost(comments, postId);

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