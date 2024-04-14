package ie.thirdfloor.csis.ul.laedsgo;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import ie.thirdfloor.csis.ul.laedsgo.databinding.ActivityMainBinding;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck.LeadsDeckDocument;
import ie.thirdfloor.csis.ul.laedsgo.ui.maps.MapsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private static final String TAG = "MainActivity";
    private static ArrayList<LeadsDeckDocument> leadsDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getLeadDeckFromDB();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.messages, R.id.forum, R.id.maps, R.id.laed_deck, R.id.profile)
                .build();
        // Retrieve the NavHostFragment using its ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        // Get the NavController from the NavHostFragment
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Hiding navbar on login
        // Check if the current destination is not the LoginFragment
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });

        navController.navigate(R.id.loginFragment);
    }

    private void getLeadDeckFromDB() {
        leadsDeck = new ArrayList<>();

        LeadsDeckConnection leadsDeckConnection = new LeadsDeckConnection();
        MutableLiveData<ArrayList<IDocument>> leadsDeckLiveMutableData = new MutableLiveData<>(new ArrayList<IDocument>());

        leadsDeckLiveMutableData.observe(this, iDocuments -> {
            for (IDocument document : iDocuments) {
                LeadsDeckDocument leadDocument = (LeadsDeckDocument) document;
                leadsDeck.add(leadDocument);
            }
        });
        leadsDeckConnection.getAll(leadsDeckLiveMutableData);
    }

    public ArrayList<LeadsDeckDocument> getLeadsDeck() {
        return leadsDeck;
    }
}
