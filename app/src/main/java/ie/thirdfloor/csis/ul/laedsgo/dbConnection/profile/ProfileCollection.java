package ie.thirdfloor.csis.ul.laedsgo.dbConnection.profile;

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

public class ProfileCollection implements ICollectionConnection {

    DBConnection dbConnection = new DBConnection();

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
                                Integer docId = Integer.parseInt(document.getId()) + 1;
                                Map<String, Object> map = convertDocumentToMap((ProfileDocument) item, docId);
                                dbConnection.db.collection("profile")
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

    public void push(IDocument item, MutableLiveData<IDocument> mProfile) {
        dbConnection.db.collection("profile")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Integer docId = Integer.parseInt(document.getId()) + 1;
                                Map<String, Object> map = convertDocumentToMap((ProfileDocument) item, docId);
                                dbConnection.db.collection("profile")
                                        .document(docId.toString())
                                        .set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mProfile.setValue((ProfileDocument) item);
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
    public void get(int id, MutableLiveData<IDocument> mProfile) {
        dbConnection.db.collection("profile")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProfileDocument doc = document.toObject(ProfileDocument.class);
                                mProfile.setValue(doc);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getAll(MutableLiveData<ArrayList<IDocument>> mProfileList) {
        dbConnection.db.collection("profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<IDocument> arrayList = mProfileList.getValue();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProfileDocument doc = document.toObject(ProfileDocument.class);
                                arrayList.add(doc);
                            }

                            mProfileList.setValue(arrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void update(MutableLiveData<IDocument> item) {
        if (item.getValue() instanceof ProfileDocument) {
            ProfileDocument profileDocument = (ProfileDocument) item.getValue();
            dbConnection.db.collection("profile")
                    .document(String.valueOf(profileDocument.id))
                    .set(convertDocumentToMap(profileDocument, profileDocument.id))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("test", "Updated document successfully");
                            } else {
                                Log.i("test", "Failed to update document");
                            }
                        }
                    });
        } else {
            Log.e("test", "Invalid document type for update");
        }
    }


    public void getByUID(String uid, MutableLiveData<IDocument> mProfile) {
        dbConnection.db.collection("profile")
                .whereEqualTo("UID", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() != 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ProfileDocument doc = document.toObject(ProfileDocument.class);
                                    Log.d("Bojo", doc.toString());
                                    mProfile.setValue(doc);
                                }
                            } else {
                                ProfileDocument newProfile = new ProfileDocument();
                                newProfile.UID = uid;

                                push(newProfile, mProfile);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void updateUsername(int id, String username) {
        dbConnection.db.collection("profile").
                document("" + id).
                update("name", username);
    }

    public void updateBio(int id, String bio) {
        dbConnection.db.collection("profile").
                document("" + id).
                update("bio", bio);
    }

    public void updateProfilePicture(int id, String profilePhoto) {
        dbConnection.db.collection("profile").
                document("" + id).
                update("profilePhoto", profilePhoto);
    }

    public int getUserId(){
        return 0;
    }

    private Map<String, Object> convertDocumentToMap(ProfileDocument document, Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", id);
        map.put("UID", document.UID);
        map.put("name", document.name);
        map.put("ladsSeen", document.ladsSeen);
        map.put("ladsCaught", document.ladsCaught);
        map.put("profilePhoto", document.profilePhoto);
        map.put("bio", document.bio);
        map.put("timestamp", Timestamp.now());
        map.put("likedPosts", document.likedPosts);
        map.put("dislikedPosts", document.dislikedPosts);

        return map;
    }
}
