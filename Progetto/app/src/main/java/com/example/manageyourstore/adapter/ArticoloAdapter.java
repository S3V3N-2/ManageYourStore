package com.example.manageyourstore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.manageyourstore.R;
import com.example.manageyourstore.app.Articolo;
import com.example.manageyourstore.stock.StockOrdinaDialog;
import com.example.manageyourstore.stock.StockVenditaDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticoloAdapter extends RecyclerView.Adapter<ArticoloAdapter.VersionVH>   {

    private List<Articolo> listaArticoli;
    private Context mCtx;

    public ArticoloAdapter(Context mCtx, List<Articolo> listaArticoli){
        this.mCtx = mCtx;
        this.listaArticoli = listaArticoli;
    }

    @NonNull
    @Override
    public ArticoloAdapter.VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articolo_stock, parent, false);
        return new VersionVH(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ArticoloAdapter.VersionVH holder, int position) {

        final Articolo articolo = listaArticoli.get(position);

        holder.nome.setText(articolo.getNome());

        if(articolo.getQuantita() == 1){
            String quantita = articolo.getQuantita() + " Disponibile";
            holder.quantita.setText(quantita);
        } else {
            String quantita = articolo.getQuantita() + " Disponibili";
            holder.quantita.setText(quantita);
        }
        if(articolo.getQuantita() == 0){
            holder.quantita.setTextColor(Color.RED);
        }

        String prezzo = articolo.getPrezzo() + " €";
        holder.prezzo.setText(prezzo);

        Picasso.get().load(articolo.getImg()).into(holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity fragmentActivity = (FragmentActivity)(mCtx);
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putString("ID", listaArticoli.get(position).getID());
                bundle.putString("name", listaArticoli.get(position).getNome());
                bundle.putString("img", listaArticoli.get(position).getImg());
                bundle.putString("descrizione", listaArticoli.get(position).getDescrizione());
                bundle.putInt("quantita", listaArticoli.get(position).getQuantita());

                if(articolo.getQuantita() == 0){
                    StockOrdinaDialog stockOrdinaDialog = new StockOrdinaDialog();
                    stockOrdinaDialog.setArguments(bundle);
                    stockOrdinaDialog.show(fm, "Dialog Ordini");
                } else {
                    StockVenditaDialog stockVenditaDialog = new StockVenditaDialog();
                    stockVenditaDialog.setArguments(bundle);
                    stockVenditaDialog.show(fm, "Dialog Vendita");
                }
            }
        });
    }

    public int getItemCount(){
        return listaArticoli.size();
    }


    public class VersionVH extends RecyclerView.ViewHolder  {
        private CardView cardView;
        private ImageView img;
        private TextView nome, quantita, prezzo;

        public VersionVH(View itemView){
            super(itemView);

            nome = itemView.findViewById(R.id.nomeArticolo);
            quantita = itemView.findViewById(R.id.quantita);
            prezzo = itemView.findViewById(R.id.prezzo);
            img = itemView.findViewById(R.id.immagine);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
