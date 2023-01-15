package com.example.chatappfinal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import models.MesajAdapter;
import models.MesajData;


public class MesajOlustur extends Fragment {
    Button createMessage;
    FirebaseAuth auth;
    FirebaseUser user;
    TextInputEditText mesajAdi, mesaj;
    FirebaseFirestore store;
    RecyclerView recyclerView;
    MesajData mesajData;
    ArrayList<MesajData> mesajlar;
    MesajAdapter mesajAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mesaj_olustur, container, false);

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store = FirebaseFirestore.getInstance();

        createMessage = rootView.findViewById(R.id.mesaj_olustur);
        mesajAdi = rootView.findViewById(R.id.mesaj_adi);
        mesaj = rootView.findViewById(R.id.mesaj_kismi);
        recyclerView = rootView.findViewById(R.id.mesaj_list);
        LinearLayoutManager lls = new LinearLayoutManager(getContext());
        lls.setOrientation(LinearLayoutManager.VERTICAL);
        lls.setReverseLayout(false);
        recyclerView.setLayoutManager(lls);

        mesajlar= new ArrayList<>();
        mesajAdapter = new MesajAdapter(mesajlar, getContext());
        recyclerView.setAdapter(mesajAdapter);

        Getir();
        createMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mesaj.getText().toString()) || TextUtils.isEmpty(mesajAdi.getText().toString())){
                    Toast.makeText(getActivity(), "Bos Alan birakmayin", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String userId = user.getUid();
                    mesajData = new MesajData(userId, mesajAdi.getText().toString(), mesaj.getText().toString());

                    store.collection("message")
                            .add(mesajData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(getActivity(), "Mesaj Olusturuldu", Toast.LENGTH_SHORT).show();
                                Getir();
                            });
                }
            }
        });
        return rootView;
    }
    private void Getir(){
        store.collection("message")
                .whereEqualTo("userId" , String.valueOf(user.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            mesajlar.clear();
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                MesajData mesajData = documentSnapshot.toObject(MesajData.class);
                                mesajlar.add(mesajData);
                            }
                            mesajAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}