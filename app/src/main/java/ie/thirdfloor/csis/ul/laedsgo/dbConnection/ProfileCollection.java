package ie.thirdfloor.csis.ul.laedsgo.dbConnection;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.ICollectionConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class ProfileCollection implements ICollectionConnection {

    DBConnection dbConnection;
    Integer testDocId = 0;
    private volatile boolean testValotile = false;

    public ProfileCollection(DBConnection db){
        dbConnection = db;
    }

    @Override
    public void push(IDocument item) {
        dbConnection.db.collection("profile")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("test", document.toObject(ProfileDocument.class).toString() + " data in document");
                                testDocId = Integer.parseInt(document.getId()) + 1;
                                Map<String, Object> map = convertDocumentToMap((ProfileDocument) item, testDocId);
                                dbConnection.db.collection("profile")
                                        .document(testDocId.toString())
                                        .set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("test", "added new doc");
                                                } else {
                                                    Log.i("test", "did not add new doc");
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public IDocument get(int id) {
//        System.out.println("test get");
        dbConnection.db.collection("profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map dcData = document.getData();
                                System.out.println(dcData.toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            System.out.println("Exception: " + task.getException().toString());
                        }
                    }
                });
        return null;
    }

    private Map<String, Object> convertDocumentToMap(ProfileDocument document, Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", id);
        map.put("name", document.name);
        map.put("ladsSeen", document.ladsSeen);
        map.put("ladsCaught", document.ladsCaught);
        map.put("timestamp", Timestamp.now());

        return map;
    }
}
