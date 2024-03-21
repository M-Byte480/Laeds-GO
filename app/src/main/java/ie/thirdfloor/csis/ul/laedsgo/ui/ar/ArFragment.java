package ie.thirdfloor.csis.ul.laedsgo.ui.ar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}