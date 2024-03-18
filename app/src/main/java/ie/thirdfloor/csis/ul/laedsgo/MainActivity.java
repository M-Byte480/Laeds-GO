package ie.thirdfloor.csis.ul.laedsgo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ie.thirdfloor.csis.ul.laedsgo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.messages, R.id.forum, R.id.ar, R.id.laed_deck, R.id.profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }


    public void onLikeButtonClick(View view) {
        Log.i(TAG, "Like Button Clicked");
    }

    public void onDislikeButtonClick(View view){
        Log.i(TAG, "Dislike Button Clicked");
    }

    public void openComments(View view){
        Log.i(TAG, "Opening Comments");
    }

    public void openProfile(View view){
        Log.i(TAG, "Requesting to view profile");
    }
}