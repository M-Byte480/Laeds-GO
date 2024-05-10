package ie.thirdfloor.csis.ul.laedsgo.ui.login;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ie.thirdfloor.csis.ul.laedsgo.ProfileInfo.ProfileInfo;
import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private ImageButton buttonLogIn;
    private FirebaseUser currentUser;
    private static final String TAG = "LoginFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseAuth.getInstance().signOut();

        //Grabbing button ids
        //Logout button in fragment_profile.xml
        buttonLogIn = (ImageButton) root.findViewById(R.id.signInButton);

        //Login eventListener
        buttonLogIn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            System.out.println("Sign in clicked");
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );

            // Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(true)
                    .setTheme(R.style.FirebaseUI_AuthMethodPicker)
                    .build();
            signInLauncher.launch(signInIntent);
        });

        return root;
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    public ActivityResultLauncher<Intent> getSignInLauncher() {
        return signInLauncher;
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                Toast.makeText(getContext(), "Signed in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Signed in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                ProfileInfo.setProfile(currentUser.getUid());

                System.out.println(
                        "Signed in as " + user.getEmail() + "\n" +
                                "User ID: " + user.getUid() + "\n" +
                                "Display Name: " + user.getDisplayName() + "\n"
                );
                Navigation.findNavController(requireView()).navigate(R.id.maps);

            }
        } else {
            if (response == null) {
                System.out.println("Sign in cancelled");
            } else {
                System.out.println("Sign in failed");
                System.out.println(Objects.requireNonNull(response.getError()).getErrorCode());
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}