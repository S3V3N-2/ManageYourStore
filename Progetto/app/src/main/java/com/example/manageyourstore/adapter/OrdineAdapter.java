package com.example.manageyourstore.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.manageyourstore.R;
import com.example.manageyourstore.app.Ordine;
import com.example.manageyourstore.ordini.OrdiniRitiraDialog;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OrdineAdapter extends RecyclerView.Adapter<OrdineAdapter.VersionVH>{

    private List<Ordine> listaOrdini;
    private Context mCtx;

    public OrdineAdapter(Context mCtx, List<Ordine> listaOrdini){
        this.mCtx = mCtx;
        this.listaOrdini = listaOrdini;
    }

    @NonNull
    @Override
    public OrdineAdapter.VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordine_ordini, parent, false);
        return new OrdineAdapter.VersionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VersionVH holder, int position) {
        final Ordine ordine = listaOrdini.get(position);

        holder.nomeArticoloOrdine.setText(ordine.getNome());

        String acquirente = ordine.getNomeAcquirente() + " " + ordine.getCognomeAcquirente();
        holder.acquirenteOrdine.setText(acquirente);

        String dataArrivo = (ordine.getGiornoArrivo() + "/" + ordine.getMeseArrivo() + "/" + ordine.getAnnoArrivo());
        holder.dataArrivoOrdine.setText(dataArrivo);

        String numeroTelefono = "+39 " + ordine.getTelefonoAcquirente();
        holder.telefonoAcquirenteOrdine.setText(numeroTelefono);

        Picasso.get().load(ordine.getImg()).into(holder.imgArticoloOrdine);

        holder.bottoneRitira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentActivity fragmentActivity = (FragmentActivity)(mCtx);
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putString("ID", listaOrdini.get(position).getID());
                bundle.putString("nomeArticolo", listaOrdini.get(position).getNome());
                bundle.putString("imgArticolo", listaOrdini.get(position).getImg());
                bundle.putString("descrizioneArticolo", listaOrdini.get(position).getDescrizioneArticolo());
                bundle.putString("nomeAcquirente", listaOrdini.get(position).getNomeAcquirente());
                bundle.putString("cognomeAcquirente", listaOrdini.get(position).getCognomeAcquirente());
                bundle.putString("numeroTelefono", listaOrdini.get(position).getTelefonoAcquirente());
                bundle.putString("codiceFiscale", listaOrdini.get(position).getCFAcquirente());
                bundle.putInt("giornoArrivo", listaOrdini.get(position).getGiornoArrivo());
                bundle.putInt("meseArrivo", listaOrdini.get(position).getMeseArrivo());
                bundle.putInt("annoArrivo", listaOrdini.get(position).getAnnoArrivo());

                OrdiniRitiraDialog ordiniRitiraDialog = new OrdiniRitiraDialog();
                ordiniRitiraDialog.setArguments(bundle);
                ordiniRitiraDialog.show(fm, "Dialog Ritira Ordine");
            }
        });
    }

    public int getItemCount(){
        return listaOrdini.size();
    }

    public class VersionVH extends RecyclerView.ViewHolder  {
        private ImageView imgArticoloOrdine;
        private TextView nomeArticoloOrdine;
        private TextView acquirenteOrdine;
        private TextView dataArrivoOrdine;
        private TextView telefonoAcquirenteOrdine;
        private Button bottoneRitira;

        public VersionVH(View itemView){
            super(itemView);

            nomeArticoloOrdine = itemView.findViewById(R.id.nomeArticoloOrdine);
            acquirenteOrdine = itemView.findViewById(R.id.acquirenteOrdine);
            dataArrivoOrdine = itemView.findViewById(R.id.dataArrivo);
            imgArticoloOrdine = itemView.findViewById(R.id.immagineOrdine);
            bottoneRitira = itemView.findViewById(R.id.bottoneRitiraOrdine);
            telefonoAcquirenteOrdine = itemView.findViewById(R.id.numeroAcquirente);
        }
    }
}
