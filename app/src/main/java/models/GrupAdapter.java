package models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfinal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GrupAdapter extends RecyclerView.Adapter<GrupAdapter.CHolder> {
    private ArrayList<GrupModel> gruplar;
    private Context context;

    public GrupAdapter(ArrayList<GrupModel> gruplar, Context context) {
        this.gruplar = gruplar;
        this.context = context;
    }

    @NonNull
    @Override
    public GrupAdapter.CHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gruplar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GrupAdapter.CHolder holder, int position) {
        Picasso.get().load(gruplar.get(position).getLink()).into(holder.grupImage);
        holder.grupAdi.setText(gruplar.get(position).getGrupName());
        holder.grupAciklama.setText(gruplar.get(position).getGrupDesc());
    }

    @Override
    public int getItemCount() {
        return gruplar.size();
    }

     static class CHolder extends  RecyclerView.ViewHolder {
        ImageView grupImage;
        TextView grupAdi, grupAciklama;

         public CHolder(@NonNull View itemView) {
             super(itemView);
             grupImage = itemView.findViewById(R.id.grup_resim);
             grupAdi = itemView.findViewById(R.id.grup_ismi_cart);
             grupAciklama =itemView.findViewById(R.id.grup_aciklama_cart);
         }
     }
}
