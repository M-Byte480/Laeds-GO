package ie.thirdfloor.csis.ul.laedsgo.dbConnection.post;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.DBConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.ICollectionConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class TOLPostCollection implements ICollectionConnection {
    private final DBConnection dbConnection = new DBConnection();
    private static final String TAG = "TOLPostsCollection.java";
    @Override
    public void push(IDocument item) throws InterruptedException {
        dbConnection.db.collection("tolPost")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i(TAG, document.toObject(TOLPostDocument.class).toString() + " data in document");
                                Integer docId = Integer.parseInt(document.getId()) + 1;
                                Map<String, Object> map = convertDocumentToMap((TOLPostDocument) item, docId);
                                dbConnection.db.collection("tolPost")
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
    public void get(int id, MutableLiveData<IDocument> mTolPost) {
        dbConnection.db.collection("tolPost")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TOLPostDocument doc = document.toObject(TOLPostDocument.class);
                                mTolPost.setValue(doc);
                            }
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getAll(MutableLiveData<ArrayList<IDocument>> mTolPostList) {
        dbConnection.db.collection("tolPost")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<IDocument> arrayList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TOLPostDocument doc = document.toObject(TOLPostDocument.class);
                                arrayList.add(doc);
                            }

                            mTolPostList.setValue(arrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void incrementLike(Integer id, Integer incr){
        dbConnection.db.collection("tolPost")
                .document(String.valueOf(id))
                .update("likes", FieldValue.increment(incr));
    }

    public void incrementDislike(Integer id, Integer incr){
        dbConnection.db.collection("tolPost")
                .document(String.valueOf(id))
                .update("dislikes", FieldValue.increment(incr));
    }

    public void incrementCommentCount(Integer id){
        dbConnection.db.collection("tolPost")
                .document(String.valueOf(id))
                .update("commentCount", FieldValue.increment(1));
    }

    private Map<String, Object> convertDocumentToMap(TOLPostDocument item, Integer docId) {
        Map<String, Object> newDoc = new HashMap<>();

        newDoc.put("id", docId);
        newDoc.put("userId", item.userId);
        newDoc.put("likes", item.likes);
        newDoc.put("dislikes", item.dislikes);
        newDoc.put("message", item.message);
        newDoc.put("timestamp", item.timestamp);
        newDoc.put("location", item.location);
        newDoc.put("commentCount", item.commentCount);

        return newDoc;
    }
}
