package ie.thirdfloor.csis.ul.laedsgo.ui.commentSection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments.CommentDocument;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCommentFragment extends Fragment {


    public static int postId = 0;
    EditText commentText;

    public CreateCommentFragment() {
        // Required empty public constructor
    }

    public static CreateCommentFragment newInstance() {
        CreateCommentFragment fragment = new CreateCommentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_comment, container, false);
        Button postButton = view.findViewById(R.id.createCommentButton);
        commentText = view.findViewById(R.id.commentText);
        postButton.setOnClickListener((View v) -> postComment(v));
        return view;
    }

    public void postComment(View view) {
        Log.i("CreateComment", "onClick: " + "test");
        if (commentText.getText().toString().isEmpty()){
            commentText.setError("Please enter a comment");
        } else {
            CommentDocument commentDocument = new CommentDocument();
            commentDocument.userId = ProfileInfo.getId();
            commentDocument.message = commentText.getText().toString();
            commentDocument.parentId = -1;
            commentDocument.postId = postId;
            CommentConnection commentConnection = new CommentConnection();
            Log.i("CreateComment", "onCreateView: " + commentDocument.toString());
            commentConnection.push(commentDocument);
            NavHostFragment navHostFragment = (NavHostFragment) ((AppCompatActivity) getContext()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
            navHostFragment.getNavController().navigate(R.id.commentFragment);
        }
    }
}