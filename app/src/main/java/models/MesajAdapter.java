package models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfinal.R;

import java.util.ArrayList;

public class MesajAdapter extends RecyclerView.Adapter<MesajAdapter.CHolder> {
    private ArrayList<MesajData> mesajDataList;
    private Context context;

    public MesajAdapter(ArrayList<MesajData> mesajDataList, Context context) {
        this.mesajDataList = mesajDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MesajAdapter.CHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MesajAdapter.CHolder holder, int position) {
        holder.mesajTitle.setText(mesajDataList.get(position).getMesajAdi());
        holder.mesajDesc.setText(mesajDataList.get(position).getMesaj());
    }

    @Override
    public int getItemCount() {
        return mesajDataList.size();
    }
    static class  CHolder extends RecyclerView.ViewHolder{
        TextView mesajTitle, mesajDesc;

        public CHolder(@NonNull View itemView) {
            super(itemView);

            mesajDesc = itemView.findViewById(R.id.mesaj_icerik);
            mesajTitle =itemView.findViewById(R.id.message_title);
        }
    }
}
