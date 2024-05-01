package ie.thirdfloor.csis.ul.laedsgo.ui.discovery_posts;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.TOLPostDocument;

public class PostEntryFragment extends Fragment {
    private static final String LOG = "PostEntryFragment.java";
    private EditText multilineTextEdit;
    private TextView characterCountLeftView;
    private final int  maxCharacterCount = 272;

    private TOLPostCollection dbConnection = new TOLPostCollection();

    public PostEntryFragment() {
        // Required empty public constructor
    }


    public static PostEntryFragment newInstance() {
        PostEntryFragment fragment = new PostEntryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_entry, container, false);

        this.multilineTextEdit = view.findViewById(R.id.user_input);
        this.characterCountLeftView = view.findViewById(R.id.characterCountLeft);

        this.multilineTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                int charactersLeftToUse = maxCharacterCount - s.length();
                characterCountLeftView.setText(String.valueOf(charactersLeftToUse));
            }
        });

        Button publishBtn = view.findViewById(R.id.publishBtn);

        publishBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String textWithoutLeadingSpaces = multilineTextEdit.getText().toString().trim();
                int textLength = textWithoutLeadingSpaces.length();
                if(textLength > 0){
                    Log.i(LOG, "Publish Btn Clicked");

                    TOLPostDocument post = new TOLPostDocument();

                    post.userId = ProfileInfo.getId();

                    post.message = textWithoutLeadingSpaces;

                    try {
                        dbConnection.push(post);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(getContext(), "Successfully Published", Toast.LENGTH_LONG).show();

                    requireActivity().getSupportFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getContext(), "There is not enough content to post", Toast.LENGTH_LONG).show();
                }
            }
        }));
        return view;
    }


}