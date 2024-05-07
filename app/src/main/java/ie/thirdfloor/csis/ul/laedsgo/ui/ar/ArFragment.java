package ie.thirdfloor.csis.ul.laedsgo.ui.ar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ie.thirdfloor.csis.ul.laedsgo.databinding.FragmentArBinding;

public class ArFragment extends Fragment {
    private FragmentArBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}