package ie.thirdfloor.csis.ul.laedsgo.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ie.thirdfloor.csis.ul.laedsgo.R;
import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button buttonLogout = root.findViewById(R.id.logoutButton);

        //Logout eventListener
        buttonLogout.setOnClickListener(V -> {
            System.out.println("Logout clicked");
            FirebaseAuth.getInstance().signOut();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            Toast.makeText(getContext(), "User " + currentUser.getEmail() + " was signed out", Toast.LENGTH_SHORT).show();
            //Check if there is a user
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
            try {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println(currentUser.getEmail());
            } catch (NullPointerException e) {
                System.out.println("No user YIPEEEEEE!");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}