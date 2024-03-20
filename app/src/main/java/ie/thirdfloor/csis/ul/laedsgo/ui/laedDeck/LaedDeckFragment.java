package ie.thirdfloor.csis.ul.laedsgo.ui.laedDeck;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentLaedDeckBinding;


public class LaedDeckFragment extends Fragment {

    private FragmentLaedDeckBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LaedDeckViewModel laedDeckViewModel =
                new ViewModelProvider(this).get(LaedDeckViewModel.class);

        binding = FragmentLaedDeckBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLaedDeck;
        laedDeckViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}