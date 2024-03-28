package ie.thirdfloor.csis.ul.laedsgo.dbConnection.leadsDeck;

import static android.content.ContentValues.TAG;

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

public class LeadsDeckConnection implements ICollectionConnection {
    private final DBConnection dbConnection = new DBConnection();
    @Override
    public void push(IDocument item) throws InterruptedException {
        dbConnection.db.collection("leadsDeck")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("test", document.toObject(LeadsDeckDocument.class).toString() + " data in document");
                                Integer docId = Integer.parseInt(document.getId()) + 1;
                                Map<String, Object> map = convertDocumentToMap((LeadsDeckDocument) item, docId);
                                dbConnection.db.collection("leadsDeck")
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
        dbConnection.db.collection("leadsDeck")
                .whereEqualTo("leadId", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LeadsDeckDocument doc = document.toObject(LeadsDeckDocument.class);
                                mLead.setValue(doc);
                            }
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getAll(MutableLiveData<ArrayList<IDocument>> mLeadsList) {
        dbConnection.db.collection("leadsDeck")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<IDocument> arrayList = mLeadsList.getValue();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LeadsDeckDocument doc = document.toObject(LeadsDeckDocument.class);
                                arrayList.add(doc);
                                Log.i("test", doc.toString());
                            }

                            mLeadsList.setValue(arrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private Map<String, Object> convertDocumentToMap(LeadsDeckDocument item, Integer docId) {
        Map<String, Object> newDoc = new HashMap<>();

        newDoc.put("leadId", docId);
        newDoc.put("name", item.name);
        newDoc.put("description", item.description);
        newDoc.put("model", item.model);
        newDoc.put("picture", item.picture);
        newDoc.put("rarity", item.rarity);

        return newDoc;
    }
}
