package ie.thirdfloor.csis.ul.laedsgo.ui.ar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentArBinding;


public class ArFragment extends Fragment {

    private FragmentArBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ArViewModel arViewModel =
                new ViewModelProvider(this).get(ArViewModel.class);

        binding = FragmentArBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAr;
        arViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button goToPostsButton = view.findViewById(R.id.goToPosts);

        goToPostsButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_postsFragment2);
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}