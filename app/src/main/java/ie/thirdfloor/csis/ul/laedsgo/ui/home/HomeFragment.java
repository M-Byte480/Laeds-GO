package ie.thirdfloor.csis.ul.laedsgo.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private Button buttonLogIn;
    private Button buttonLogout;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //Force logout user for testing
        FirebaseAuth.getInstance().signOut();

        //Grabbing button ids
        buttonLogout = (Button) root.findViewById(R.id.buttonHomeLogout);
        buttonLogIn = (Button) root.findViewById(R.id.buttonHomeSignIn);

        //Check for user to display correct button
        userLogInStatus();

        //Logout eventListener
        buttonLogout.setOnClickListener(V -> {
            System.out.println("Logout clicked");
            FirebaseAuth.getInstance().signOut();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
            Toast.makeText(getContext(), "User " + currentUser.getEmail() + " was signed out", Toast.LENGTH_SHORT).show();
            //Check if there is a user
            //Need to get user and this state, cant use data field currentUser
            try {
                FirebaseUser logoutCheck = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(logoutCheck.getEmail());
            }catch (NullPointerException e){
                System.out.println("No user YIPEEEEEE!");
            }
        });

        //Login eventListener
        buttonLogIn.setOnClickListener(v -> {
            // Your button click logic here
            System.out.println("Sign in clicked");
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );

            // Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });

        return root;
    }
    //check if a user is logged in or not
    public void userLogInStatus(){
        try {
            if (currentUser != null) {
                //User is signed in
                //Hide login button, show Logout
                //root.findViewById(R.id.buttonHomeSignIn);
                buttonLogIn.setVisibility(View.INVISIBLE);
                buttonLogout.setVisibility(View.VISIBLE);
            } else {
            /*Would assume in the future we will have a sign in page
             so this will need functionality to keep unsigned users on the login page*/

                //No user is signed in
                //Show login button, hide Logout
                buttonLogIn.setVisibility(View.VISIBLE);
                buttonLogout.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e){
            System.out.println("LogIn and Logout button null pointer");
        }

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

            if(user != null){
                Toast.makeText(getContext(), "Signed in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Signed in as " + user.getEmail(), Toast.LENGTH_SHORT).show();
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(
                        "Signed in as " + user.getEmail() + "\n" +
                                "User ID: " + user.getUid() + "\n" +
                                "Display Name: " + user.getDisplayName() + "\n"
                );
                userLogInStatus();
            }
        } else {
            if(response == null) {
                System.out.println("Sign in cancelled");
            }else {
                System.out.println("Sign in failed");
                System.out.println(Objects.requireNonNull(response.getError()).getErrorCode());
            }
            userLogInStatus();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*public void onClickSignIn(View view) {
        System.out.println("Sign in clicked");
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }*/
}
