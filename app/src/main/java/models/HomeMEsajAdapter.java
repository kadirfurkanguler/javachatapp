package models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfinal.R;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class HomeMEsajAdapter extends RecyclerView.Adapter<HomeMEsajAdapter.CHolder> {
    private ArrayList<MesajData> mesajlar;
    private Context context;
    private MesajListener listener;

    public HomeMEsajAdapter() {
    }

    public ArrayList<MesajData> getMesajlar() {
        return mesajlar;
    }

    public void setMesajlar(ArrayList<MesajData> mesajlar) {
        this.mesajlar = mesajlar;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MesajListener getListener() {
        return listener;
    }

    public void setListener(MesajListener listener) {
        this.listener = listener;
    }

    public HomeMEsajAdapter(ArrayList<MesajData> mesajlar, Context context, MesajListener listener) {
        this.mesajlar = mesajlar;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeMEsajAdapter.CHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homemessageitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMEsajAdapter.CHolder holder, int position) {
        holder.mesajAdi.setText(mesajlar.get(position).getMesajAdi());
        holder.mesajDesc.setText(mesajlar.get(position).getMesaj());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickedItem(mesajlar.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mesajlar.size();
    }
    static  class CHolder extends  RecyclerView.ViewHolder{
        TextView mesajAdi, mesajDesc;
        CardView cardView;
        public CHolder(@NonNull View itemView) {
            super(itemView);

            mesajDesc = itemView.findViewById(R.id.home_mesaj_icerik);
            mesajAdi = itemView.findViewById(R.id.home_message_title);
            cardView =itemView.findViewById(R.id.home_mesaj_cart);
        }
    }
}
