package com.example.manageyourstore.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.manageyourstore.R;
import com.example.manageyourstore.app.Garanzia;
import com.example.manageyourstore.assistenza.AssistenzaCreaTicketDialog;
import com.example.manageyourstore.assistenza.AssistenzaRitiroDialog;
import com.example.manageyourstore.assistenza.AssistenzaVisualizzaInfoDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class GaranziaAdapter extends RecyclerView.Adapter <GaranziaAdapter.VersionVH>{

    private List<Garanzia> listaGaranzie;
    private Context mCtx;
    private String acquirente;

    private FirebaseDatabase database;
    private DatabaseReference queryFineGaranzia;

    private int giornoOdierno = generaGiornoOdierno();
    private int meseOdierno = generaMeseOdierno();
    private int annoOdierno = generaAnnoOdierno();
    private String dataRitiroGaranzia;

    public GaranziaAdapter(List<Garanzia> listaGaranzie, Context mCtx) {
        this.listaGaranzie = listaGaranzie;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public GaranziaAdapter.VersionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.garanzia_garanzie, parent, false);
        return new VersionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VersionVH holder, int position) {

        final Garanzia garanzia = listaGaranzie.get(position);

        acquirente = garanzia.getNomeAcquirente() + " " + garanzia.getCognomeAcquirente();

        holder.nomeArticoloText.setText(garanzia.getNomeArticolo());
        holder.acquirenteText.setText(acquirente);

        String dataGaranzia = garanzia.getGiornoFineGaranzia() + "/" +
                garanzia.getMeseFineGaranzia() + "/" + garanzia.getAnnoFineGaranzia();
        holder.dataFineGaranziaText.setText(dataGaranzia);

        if(garanzia.getStatoAssistenza() == 1){
            holder.stato.setVisibility(View.INVISIBLE);
            holder.bottone.setText("Ticket");
        }

        FragmentActivity fragmentActivity = (FragmentActivity)(mCtx);
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();

        dataRitiroGaranzia = garanzia.getGiornoRitiroAssistenza() + "/" +
                garanzia.getMeseRitiroAssistenza() + "/" + garanzia.getAnnoRitiroAssistenza();

        Bundle bundle = new Bundle();
        bundle.putString("IDGaranzia", garanzia.getID());
        bundle.putString("nomeProdotto", garanzia.getNomeArticolo());
        bundle.putString("acquirente", garanzia.getNomeAcquirente() + " " + garanzia.getCognomeAcquirente());
        bundle.putString("dataRitiro", dataRitiroGaranzia);
        bundle.putString("descrizioneProblema", garanzia.getDescrizioneProblema());

        database = FirebaseDatabase.getInstance();
        queryFineGaranzia = database.getReference("Garanzie").child(garanzia.getID());

        if (annoOdierno >= garanzia.getAnnoFineGaranzia()) {
            if (meseOdierno >= garanzia.getMeseFineGaranzia()) {
                int casoMese = casoMese(garanzia.getMeseFineGaranzia(), meseOdierno);
                switch (casoMese) {
                    case 1:
                        if (giornoOdierno >= garanzia.getGiornoFineGaranzia() + 1) {
                            rimuoviOrdine(queryFineGaranzia);
                        }
                        break;
                    case 2:
                        rimuoviOrdine(queryFineGaranzia);
                        break;
                }
            }
        }

        if(garanzia.getDescrizioneProblema().equals("null")){
            garanzia.setAnnoFineGaranzia(1);
        } else {
            if (annoOdierno >= garanzia.getAnnoRitiroAssistenza()){
                if(meseOdierno >= garanzia.getMeseRitiroAssistenza()){
                    int casoMese = casoMese(garanzia.getMeseRitiroAssistenza(), meseOdierno);
                    switch (casoMese){
                        case 1:
                            if(giornoOdierno >= garanzia.getGiornoRitiroAssistenza()){
                                garanzia.setStatoAssistenza(3);
                                holder.stato.setImageResource(R.drawable.icons8_final_state);
                                holder.bottone.setText("Ritira");
                            } else {
                                garanzia.setStatoAssistenza(2);
                                holder.stato.setImageResource(R.drawable.icons8_final_state_red);
                                holder.bottone.setText("Info");
                            }
                            break;
                        case 2:
                            garanzia.setStatoAssistenza(3);
                            holder.stato.setImageResource(R.drawable.icons8_final_state);
                            holder.bottone.setText("Ritira");
                            break;
                    }
                } else {
                    garanzia.setStatoAssistenza(2);
                    holder.stato.setImageResource(R.drawable.icons8_final_state_red);
                    holder.bottone.setText("Info");
                }
            } else {
                garanzia.setStatoAssistenza(2);
                holder.stato.setImageResource(R.drawable.icons8_final_state_red);
                holder.bottone.setText("Info");
            }
        }


        holder.bottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (garanzia.getStatoAssistenza()){
                    case 1:
                        AssistenzaCreaTicketDialog dialogTicket = new AssistenzaCreaTicketDialog();
                        dialogTicket.setArguments(bundle);
                        dialogTicket.show(fm, "Crea Ticket Dialog");
                        break;
                    case 2:
                        AssistenzaVisualizzaInfoDialog dialogInfo = new AssistenzaVisualizzaInfoDialog();
                        dialogInfo.setArguments(bundle);
                        dialogInfo.show(fm, "Visualizza Info Dialog");
                        break;
                    case 3:
                        AssistenzaRitiroDialog dialogRitiro = new AssistenzaRitiroDialog();
                        dialogRitiro.setArguments(bundle);
                        dialogRitiro.show(fm, "Ritira Dialog");
                        break;
                    default:
                        Toast.makeText(mCtx, "ERRORE - CODICE ASSISTENZA NON VALIDO", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        Picasso.get().load(garanzia.getImg()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return listaGaranzie.size();
    }

    public class VersionVH extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView nomeArticoloText, acquirenteText, dataFineGaranziaText;
        private ImageView stato;
        private Button bottone;

        public VersionVH(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.immagineArticoloAssistenza);
            nomeArticoloText = itemView.findViewById(R.id.nomeArticoloAssistenza);
            acquirenteText = itemView.findViewById(R.id.acquirenteAssistenza);
            dataFineGaranziaText = itemView.findViewById(R.id.dataFineGaranzia);
            stato = itemView.findViewById(R.id.statoAssistenza);
            bottone = itemView.findViewById(R.id.bottoneAssistenza);

        }
    }

    private int generaGiornoOdierno() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return gregorianCalendar.get(Calendar.DAY_OF_MONTH);
    }


    private int generaMeseOdierno() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return gregorianCalendar.get(Calendar.MONTH) + 1;
    }


    private int generaAnnoOdierno() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return gregorianCalendar.get(Calendar.YEAR);
    }

    private int casoMese(int meseArrivo, int meseOdierno) {

        int caso = 0;

        if (meseOdierno == meseArrivo) {
            caso = 1;
        }
        if (meseOdierno > meseArrivo) {
            caso = 2;
        }

        return caso;

    }

    private void rimuoviOrdine(DatabaseReference query) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}