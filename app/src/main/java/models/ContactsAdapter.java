package models;

import android.content.Context;
import android.media.Image;
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

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.CHolder> {
     private Context context;
     private List<Contact> contactList;
    private ContactListener listener;

    @NonNull
    @Override
    public ContactsAdapter.CHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rehberitem, parent, false));
    }

    public ContactsAdapter(Context context, List<Contact> contactList, ContactListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
    }

    public ContactsAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.CHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.isim.setText(contact.getName());
        holder.numara.setText(contact.getPhone());
        if(contact.getPhoto() != null) {
            Picasso.get().load(contact.getPhone()).into(holder.image);
        } else {
            holder.image.setImageResource(R.mipmap.ic_launcher_round);
        }
        holder.rehber_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(contactList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
    static  class CHolder extends  RecyclerView.ViewHolder{

        ImageView image ;
        TextView isim, numara;
        CardView rehber_cart;
        public CHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rehber_resim);
            isim = itemView.findViewById(R.id.rehber_isim);
            numara  = itemView.findViewById(R.id.rehber_numara);
            rehber_cart = itemView.findViewById(R.id.rehber_cart);
        }
    }
}
