package com.unip.oitavosemestre.tcc.apptcc.config;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static FirebaseStorage firebaseStorage;

    public static DatabaseReference getFirebase(){

        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(Context context){
        if( autenticacao == null ){
            FirebaseApp.initializeApp(context);
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static FirebaseStorage getFirebaseStorage(){
        if( firebaseStorage == null ){
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage;
    }

    public static StorageReference getStorageReference(String path){
        final FirebaseStorage firebaseStorage = ConfiguracaoFirebase.getFirebaseStorage();
        final StorageReference storageReference = firebaseStorage.getReference(path);

        return storageReference;
    }

}
