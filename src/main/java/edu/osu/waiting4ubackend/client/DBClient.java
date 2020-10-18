package edu.osu.waiting4ubackend.client;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import edu.osu.waiting4ubackend.entity.Admin;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBClient {
    private static final String ADMINS_COLLECTION_NAME = "admins";
    private Firestore db;

    public DBClient() throws IOException {
        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId("waiting4u")
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();
        this.db = firestoreOptions.getService();
    }

    public boolean userNameExits(String userName) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(ADMINS_COLLECTION_NAME).whereEqualTo("userName", userName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return !documents.isEmpty();
    }

    public boolean emailExits(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(ADMINS_COLLECTION_NAME).whereEqualTo("email", email).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return !documents.isEmpty();
    }

    public String saveAdmin(Admin admin) throws ExecutionException, InterruptedException {
        //asynchronously ?
        String id = db.collection(ADMINS_COLLECTION_NAME).document().getId();
        admin.setId(id);
        DocumentReference documentReference = db.collection(ADMINS_COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> result = documentReference.set(admin);
        result.get();
        return id;
    }
}
