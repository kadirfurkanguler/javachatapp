package com.example.chatappfinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import models.GrupAddMember;
import models.GrupModel;
import models.HomeGrupAdapter;
import models.HomeMEsajAdapter;
import models.MesajData;
import models.MesajListener;
import models.RehberdenGruba;

public class MesajGonder extends Fragment implements MesajListener, GrupAddMember {
    RecyclerView r_gruplar, r_mesajlar;
    Button sendMessage;
    TextView seciliGrup, seciliMesaj;
    ArrayList<GrupModel> gruplar;
    ArrayList<MesajData> mesajlar;
    HomeMEsajAdapter m_adapter;
    HomeGrupAdapter g_adaper;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore store;
    String seciliGrupId =null, seciliGrupAdi= null, secilenMesaj= null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mesaj_gonder, container, false);
        // Inflate the layout for this fragment

        seciliGrup = rootView.findViewById(R.id.secili_grup);
        seciliMesaj = rootView.findViewById(R.id.secili_mesaj);
        sendMessage = rootView.findViewById(R.id.mesaj_gonder);
        r_gruplar =  rootView.findViewById(R.id.grup_listesi);
        r_mesajlar =  rootView.findViewById(R.id.mesaj_listesi);
        Getir();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    sendSMSMesasge();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });


        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        llm1.setReverseLayout(false);
        llm1.setOrientation(r_gruplar.HORIZONTAL);
        r_gruplar.setLayoutManager(llm1);

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        llm2.setReverseLayout(false);
        llm2.setOrientation(r_mesajlar.HORIZONTAL);
        r_mesajlar.setLayoutManager(llm2);

        gruplar = new ArrayList<>();
        mesajlar = new ArrayList<>();

        g_adaper = new HomeGrupAdapter(gruplar, getContext(), this::onItemClicked);
        m_adapter = new HomeMEsajAdapter(mesajlar, getContext(), this::onClickedItem);


        r_gruplar.setAdapter(g_adaper);
        r_mesajlar.setAdapter(m_adapter);

        return rootView;
    }

    @Override
    public void onItemClicked(GrupModel grupmodel) {
        seciliGrupId = grupmodel.getGrupId();
        seciliGrupAdi = grupmodel.getGrupName();
        seciliGrup.setText("Secildi:" + String.valueOf(grupmodel.getGrupName()));
    }

    @Override
    public void onClickedItem(MesajData mesajData) {
        secilenMesaj = mesajData.getMesaj();
        seciliMesaj.setText("secili:"+ String.valueOf(mesajData.getMesajAdi()));

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
                            g_adaper.notifyDataSetChanged();
                        }
                    }
                });
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
                            m_adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    private  void sendSMSMesasge(){
        if(secilenMesaj != null && seciliGrupId!=null){
            SmsManager smsManager = SmsManager.getDefault();
            store.collection("contacts")
                    .whereEqualTo("grupId", String.valueOf(seciliGrupId))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot d:task.getResult()){
                                    RehberdenGruba rb = new RehberdenGruba(d.get("userId").toString(), d.get("grupId").toString(), d.get("contactPhone").toString());
                                    String number = rb.getContactPhone();
                                    smsManager.sendTextMessage(number, null, secilenMesaj,null, null);
                                }
                                Toast.makeText(getContext(), "Mesaj Başarıyla Gönderildi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(getContext(), "Mesaj Gonderilemedi", Toast.LENGTH_SHORT).show();
        }
    }
}