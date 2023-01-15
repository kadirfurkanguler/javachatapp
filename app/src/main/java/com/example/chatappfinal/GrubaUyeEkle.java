package com.example.chatappfinal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import models.Contact;
import models.ContactListener;
import models.ContactsAdapter;
import models.GrupAddMember;
import models.GrupAddMemberAdapter;
import models.GrupModel;
import models.RehberdenGruba;


public class GrubaUyeEkle extends Fragment implements GrupAddMember, ContactListener {
    RecyclerView recyclerViewRehber, recyclerViewGruplar;
    ArrayList<GrupModel> gruplar;
    TextView selectedGrup;
    GrupAddMemberAdapter adapter;
    ContactsAdapter cadapter;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore store;
    ArrayList<Contact> rehber;
    String seciliGrupId = null;
    String seciliGrupAdi = null;
    String seciliKisiNo = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user  = auth.getCurrentUser();
        store = FirebaseFirestore.getInstance();
        rehber = new ArrayList<>();
        cadapter = new ContactsAdapter(getContext(), rehber, this::onItemClicked);
        Getir();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gruba_uye_ekle, container, false);
        selectedGrup = rootView.findViewById(R.id.selected_group_name);
        recyclerViewGruplar = rootView.findViewById(R.id.select_group_name);
        recyclerViewRehber =  rootView.findViewById(R.id.rehber_listesi);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(recyclerViewGruplar.HORIZONTAL);
        llm.setReverseLayout(false);
        recyclerViewGruplar.setLayoutManager(llm);

        LinearLayoutManager llmr = new LinearLayoutManager(getActivity());
        llmr.setOrientation(recyclerViewGruplar.VERTICAL);
        llmr.setReverseLayout(false);
        recyclerViewRehber.setLayoutManager(llmr);


        gruplar = new ArrayList<>();
        adapter = new GrupAddMemberAdapter(gruplar,getContext(), this::onItemClicked);
        recyclerViewGruplar.setAdapter(adapter);
        recyclerViewRehber.setAdapter(cadapter);
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(response.getPermissionName().equals(Manifest.permission.READ_CONTACTS)){
                            RehberGetir();
                        }
                    }
                    private void  RehberGetir () {
                         rehber.clear();
                        Cursor phoses = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                        while (phoses.moveToNext()){
                            String name = phoses.getString(phoses.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String number = phoses.getString(phoses.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String image = phoses.getString(phoses.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            Contact contact = new Contact(name, number, image);
                            rehber.add(contact);
                        }
                        cadapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getActivity(), "Izin vermeniz lazim", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.cancelPermissionRequest();
                    }
                }).check();



        return rootView;
    }

    private  void Getir(){
        store.collection("gruplar")
                .whereEqualTo("userId", String.valueOf(user.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            gruplar.clear();
                            for(QueryDocumentSnapshot d : task.getResult()){
                                GrupModel grupModel = d.toObject(GrupModel.class);
                                gruplar.add(grupModel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onItemClicked(GrupModel grupmodel) {
        seciliGrupAdi = grupmodel.getGrupName();
        seciliGrupId =grupmodel.getGrupId();
        selectedGrup.setText(String.valueOf("Secilen :" + seciliGrupAdi));
    }

    @Override
    public void onItemClicked(Contact contact) {

        seciliKisiNo = contact.getPhone();

        if(seciliKisiNo == null) {
            Toast.makeText(getActivity(), "Numarasi olan bi kisiyi sec", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Gruba Uye Ekle");
        builder.setMessage(seciliGrupAdi + "grubuna" + seciliKisiNo+ "eklenecek");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = user.getUid();
                RehberdenGruba data  = new RehberdenGruba(userId,seciliGrupId, seciliKisiNo);
                store.collection("contacts")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Basari ile kaydedildi", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
        builder.show();
    }
}