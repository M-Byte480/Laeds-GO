package ie.thirdfloor.csis.ul.laedsgo;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ie.thirdfloor.csis.ul.laedsgo.databinding.ActivityMainBinding;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.DBConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.ProfileCollection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.ProfileDocument;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //DB test
        DBConnection db = new DBConnection();
        db.db = FirebaseFirestore.getInstance();
        ProfileDocument profileDocument = new ProfileDocument();
        ProfileCollection profileCollection = new ProfileCollection(db);

        profileDocument.name = "test2";
        profileDocument.ladsCaught = 0;
        profileDocument.ladsSeen = 1;


        profileCollection.push(profileDocument);
//        profileCollection.get(0);
    }

}