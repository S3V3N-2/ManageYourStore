package com.example.manageyourstore.stock;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.manageyourstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class StockVenditaDialog extends DialogFragment {

    private String nome;
    private String img;
    private String descrizione;
    private String ID;
    private int quantita;

    private String nomeAcquirente;
    private String cognomeAcquirente;
    private String numeroTelefonoAcquirente;
    private String IDGaranzia;
    private String descrizioneProblema;
    private String dataRitiroAssistenza;
    private int statoAssistenza;

    private int giornoFineGaranzia;
    private int meseFineGaranzia;
    private int annoFineGaranzia;

    private ImageView ivImg;
    private TextView tvNome;
    private TextView tvDescrizione;
    private Button bottoneVendi;
    private Switch switchDialog;
    private TextInputEditText nomeAcquirenteText;
    private TextInputEditText cognomeAcquirenteText;
    private TextInputEditText telefonoAcquirenteText;

    private FirebaseDatabase database;
    private DatabaseReference garanziaQuery;
    private DatabaseReference vendiQuery;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stock_vendita, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ID = getArguments().getString("ID");
        quantita = getArguments().getInt("quantita");

        img = getArguments().getString("img");
        ivImg = view.findViewById(R.id.immagineArticoloVendita);
        Picasso.get().load(img).into(ivImg);

        nome = getArguments().getString("name");
        tvNome = view.findViewById(R.id.nomeProdottoVendita);
        tvNome.setText(nome);

        descrizione = getArguments().getString("descrizione");
        tvDescrizione = view.findViewById(R.id.descrizioneArticoloVendita);
        tvDescrizione.setText(descrizione);

        switchDialog = view.findViewById(R.id.switchGaranzia);
        bottoneVendi = view.findViewById(R.id.bottoneVendi);

        database = FirebaseDatabase.getInstance();
        garanziaQuery = database.getReference("Garanzie");
        vendiQuery = FirebaseDatabase.getInstance().getReference("Articoli");

        nomeAcquirenteText = view.findViewById(R.id.textNomeVendita);
        cognomeAcquirenteText = view.findViewById(R.id.textCognomeVendita);
        telefonoAcquirenteText = view.findViewById(R.id.textNumeroVendita);

        //Verifica se è on o off
        switchDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nomeAcquirenteText.setEnabled(true);
                    cognomeAcquirenteText.setEnabled(true);
                    telefonoAcquirenteText.setEnabled(true);
                } else {
                    nomeAcquirenteText.setEnabled(false);
                    cognomeAcquirenteText.setEnabled(false);
                    telefonoAcquirenteText.setEnabled(false);
                }
            }
        });

        bottoneVendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchDialog.isChecked()){

                    nomeAcquirente = nomeAcquirenteText.getEditableText().toString();
                    cognomeAcquirente = cognomeAcquirenteText.getEditableText().toString();
                    numeroTelefonoAcquirente = telefonoAcquirenteText.getEditableText().toString();
                    giornoFineGaranzia = generaGiornoFineGaranzia();
                    meseFineGaranzia = generaMeseFineGaranzia();
                    annoFineGaranzia = generaAnnoFineGaranzia();
                    descrizioneProblema = "null";
                    dataRitiroAssistenza = "null";
                    statoAssistenza = 1;

                    IDGaranzia = garanziaQuery.push().getKey();

                    if(TextUtils.isEmpty(nomeAcquirente)){
                        nomeAcquirenteText.setError("Nome Acquirente Mancante");
                        return;
                    }

                    if(TextUtils.isEmpty(cognomeAcquirente)){
                        cognomeAcquirenteText.setError("Cognome Acquirente Mancante");
                        return;
                    }

                    if(TextUtils.isEmpty(numeroTelefonoAcquirente)){
                        telefonoAcquirenteText.setError("Numero Telefono Acquirente Mancante");
                        return;
                    }

                    if(numeroTelefonoAcquirente.length() != 10){
                        telefonoAcquirenteText.setError("Numero Telefono Non Valido");
                        return;
                    }

                    Map<String, Object> garanzie = new HashMap<>();

                    garanzie.put("ID", IDGaranzia);
                    garanzie.put("nomeAcquirente", nomeAcquirente);
                    garanzie.put("cognomeAcquirente", cognomeAcquirente);
                    garanzie.put("numeroTelefonoAcquirente", numeroTelefonoAcquirente);
                    garanzie.put("img", img);
                    garanzie.put("giornoFineGaranzia", giornoFineGaranzia);
                    garanzie.put("meseFineGaranzia", meseFineGaranzia);
                    garanzie.put("annoFineGaranzia", annoFineGaranzia);
                    garanzie.put("nomeArticolo", nome);
                    garanzie.put("statoAssistenza", statoAssistenza);
                    garanzie.put("descrizioneProblema", descrizioneProblema);
                    garanzie.put("giornoRitiroAssistenza", 0);
                    garanzie.put("meseRitiroAssistenza", 0);
                    garanzie.put("annoRitiroAssistenza", 0);

                    garanziaQuery.child(IDGaranzia).setValue(garanzie).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.v("STATO", "ARTICOLO VENDUTO CON SUCCESSO - IN GARANZIA");
                            dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v("STATO", "IMPOSSIBILE VENDERE ARTICOLO");
                        }
                    });
                    vendiQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child(ID).child("quantita").setValue(quantita - 1);
                            //Toast.makeText(getActivity(), nome + " Venduto con Successo con Garanzia", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else {
                    vendiQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child(ID).child("quantita").setValue(quantita - 1);
                            Toast.makeText(getContext(), nome + " Venduto con Successo", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dismiss();
                        }
                    });
                }
            }
        });
        return view;
    }

    private int generaGiornoFineGaranzia() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH);
    }


    private int generaMeseFineGaranzia() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.MONTH) + 1;
    }


    private int generaAnnoFineGaranzia() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.YEAR) + 2;
    }

}
