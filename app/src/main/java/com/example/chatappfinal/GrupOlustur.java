package com.example.chatappfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import models.GrupAdapter;
import models.GrupModel;


public class GrupOlustur extends Fragment {
    Button createGroup, selectImage;
    ImageView prevImage;
    Uri imageUri;
    String link;
    TextInputEditText groupName, groupDesc;

    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore store;
    RecyclerView recyclerView;
    ArrayList<GrupModel> gruplar;
    GrupAdapter grupAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View rootView = inflater.inflate(R.layout.fragment_grup_olustur, container, false);
        prevImage = rootView.findViewById(R.id.prew_image);
        createGroup = rootView.findViewById(R.id.grup_olustur);
        selectImage = rootView.findViewById(R.id.simge_sec);
        groupName = rootView.findViewById(R.id.grup_adi);
        groupDesc = rootView.findViewById(R.id.grup_aciklamasi);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store = FirebaseFirestore.getInstance();

        recyclerView = rootView.findViewById(R.id.gruplar);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(false);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        gruplar = new ArrayList<>();
        grupAdapter = new GrupAdapter(gruplar, getContext());
        recyclerView.setAdapter(grupAdapter);
        Getir();
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupName.getText().toString().isEmpty() || groupDesc.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Bos Alan Birakmayiniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imageUri == null){
                    Toast.makeText(getActivity(), "Resim Seciniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                Upload();
            }
        });
        return rootView;
    }
    private  void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    private  void Upload() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Lutfen Bekleyin");
        dialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss", Locale.ROOT);
        Date now = new Date();
        String file = sdf.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/"+file);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        prevImage.setImageURI(null);
                        Toast.makeText(getActivity(), "Resim Yuklendi",Toast.LENGTH_SHORT).show();
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Resim Yuklenemedi",Toast.LENGTH_SHORT).show();
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            String userId= user.getUid();
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        String groupId = UUID.randomUUID().toString();
                                        GrupModel grup  = new GrupModel(userId, groupName.getText().toString(), groupDesc.getText().toString(), task.getResult().toString(), groupId);
                                        store.collection("gruplar")
                                                .add(grup)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getActivity(), "Grup Olusturuldu",Toast.LENGTH_SHORT).show();
                                                        groupDesc.setText("");
                                                        groupName.setText("");
                                                        Getir();                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    }
                });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 100 && data != null && data.getData()!= null){
            imageUri = data.getData();
            prevImage.setImageURI(imageUri);
        }
    }
    private void Getir(){
        store.collection("gruplar")
                .whereEqualTo("userId", user.getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            gruplar.clear();
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                GrupModel grupModel = documentSnapshot.toObject(GrupModel.class);
                                gruplar.add(grupModel);
                            }
                            grupAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}