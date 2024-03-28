package ie.thirdfloor.csis.ul.laedsgo.dbConnection.comments;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ie.thirdfloor.csis.ul.laedsgo.dbConnection.DBConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.ICollectionConnection;
import ie.thirdfloor.csis.ul.laedsgo.dbConnection.interfeces.IDocument;

public class CommentConnection implements ICollectionConnection {
    private final DBConnection dbConnection = new DBConnection();
    @Override
    public void push(IDocument item) {
        dbConnection.db.collection("comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("test", document.toObject(CommentDocument.class).toString() + " data in document");
                                Integer docId = Integer.parseInt(document.getId()) + 1;
                                Map<String, Object> map = convertDocumentToMap((CommentDocument) item, docId);
                                dbConnection.db.collection("comments")
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
    public void get(int id, MutableLiveData<IDocument> mLead) {
        dbConnection.db.collection("comments")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommentDocument doc = document.toObject(CommentDocument.class);
                                mLead.setValue(doc);
                                Log.i("test", doc.toString());
                            }
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getAll(MutableLiveData<ArrayList<IDocument>> mCommentsList) {
        dbConnection.db.collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<IDocument> arrayList = mCommentsList.getValue();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommentDocument doc = document.toObject(CommentDocument.class);
                                arrayList.add(doc);
                                Log.i("test", doc.toString());
                            }

                            mCommentsList.setValue(arrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private Map<String, Object> convertDocumentToMap(CommentDocument item, Integer docId) {
        Map<String, Object> newDoc = new HashMap<>();

        newDoc.put("id", docId);
        newDoc.put("userId", item.userId);
        newDoc.put("message", item.message);
        newDoc.put("parentId", item.parentId);
        newDoc.put("type", item.type);
        newDoc.put("timestamp", Timestamp.now());

        return newDoc;
    }
}
