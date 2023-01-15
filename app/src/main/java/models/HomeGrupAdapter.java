package models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfinal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeGrupAdapter extends RecyclerView.Adapter<HomeGrupAdapter.CHolder> {
    private ArrayList<GrupModel> gruplar;
    private Context context;
    private  GrupAddMember listener;

    public HomeGrupAdapter(ArrayList<GrupModel> gruplar, Context context, GrupAddMember listener) {
        this.gruplar = gruplar;
        this.context = context;
        this.listener = listener;
    }

    public HomeGrupAdapter() {
    }

    @NonNull
    @Override
    public HomeGrupAdapter.CHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new CHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mesajgondergrup, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeGrupAdapter.CHolder holder, int position) {
        Picasso.get().load(gruplar.get(position).getLink()).into(holder.grupImage);
        holder.grupAdi.setText(gruplar.get(position).getGrupName());
        holder.grupDesc.setText(gruplar.get(position).getGrupDesc());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(gruplar.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return gruplar.size();
    }
    static  class CHolder extends  RecyclerView.ViewHolder{
            ImageView grupImage;
            TextView grupAdi, grupDesc;
            CardView cardView;
        public CHolder(@NonNull View itemView) {
            super(itemView);
            grupImage = itemView.findViewById(R.id._grupaddmemberimage);
            grupAdi =  itemView.findViewById(R.id._add_grup_member_name);
            grupDesc =  itemView.findViewById(R.id._grupadddesc);
            cardView = itemView.findViewById(R.id._home_grup_item);
        }
    }
}
