package ie.thirdfloor.csis.ul.laedsgo.dbConnection.forums;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.DBConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.ICollectionConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.forums.ForumsDocument;

public class ForumsCollection implements ICollectionConnection {

    private final DBConnection dbConnection = new DBConnection();
    private static final String TAG = "ForumsDocument.java";
    @Override
    public void push(IDocument item) throws InterruptedException {
        dbConnection.db.collection("forms")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.i(TAG, document.toObject(ForumsDocument.class).toString() + " data in document");
                        Integer docId = Integer.parseInt(document.getId()) + 1;
                        Map<String, Object> map = convertDocumentToMap((ForumsDocument) item, docId);
                        dbConnection.db.collection("forms")
                                .document(docId.toString())
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
    public void get(int id, MutableLiveData<IDocument> mToForum) {
        dbConnection.db.collection("forums")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.ForumsDocument doc = document.toObject(ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.ForumsDocument.class);
                                mToForum.setValue(doc);
                            }
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getAll(MutableLiveData<ArrayList<IDocument>> mForumsList) {
        dbConnection.db.collection("forums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<IDocument> arrayList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.ForumsDocument doc = document.toObject(ie.thirdfloor.csis.ul.laedsgo.dbConnection.post.ForumsDocument.class);
                                arrayList.add(doc);
                            }

                            mForumsList.setValue(arrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private Map<String, Object> convertDocumentToMap(ie.thirdfloor.csis.ul.laedsgo.dbConnection.forums.ForumsDocument item, Integer docId) {
        Map<String, Object> newDoc = new HashMap<>();

        newDoc.put("id", docId);
        newDoc.put("message", item.message);
        newDoc.put("timestamp", item.timestamp);
        newDoc.put("userId", item.userId);



        return newDoc;
    }

}
