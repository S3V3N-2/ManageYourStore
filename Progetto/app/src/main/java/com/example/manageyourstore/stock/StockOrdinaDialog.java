package com.example.manageyourstore.stock;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.manageyourstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StockOrdinaDialog extends DialogFragment {

    private String nome;
    private String img;
    private String descrizione;
    private String ID;

    private String nomeAcquirente;
    private String cognomeAcquirente;
    private String numeroTelefonoAcquirente;
    private String codiceFiscaleAcquirente;
    private String descrizioneArticolo;

    private int giornoArrivo;
    private int meseArrivo;
    private int annoArrivo;

    private ImageView ivImg;
    private TextView tvNome;
    private TextView tvDescrizione;
    private Button bottoneOrdina;

    private TextInputEditText nomeAcquirenteText;
    private TextInputEditText cognomeAcquirenteText;
    private TextInputEditText telefonoAcquirenteText;
    private TextInputEditText CFAcquirenteText;

    private FirebaseDatabase database;
    private DatabaseReference query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stock_ordina, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ID = getArguments().getString("ID");

        img = getArguments().getString("img");
        ivImg = view.findViewById(R.id.immagineArticoloOrdine);
        Picasso.get().load(img).into(ivImg);

        nome = getArguments().getString("name");
        tvNome = view.findViewById(R.id.nomeProdottoOrdine);
        tvNome.setText(nome);

        descrizione = getArguments().getString("descrizione");
        tvDescrizione = view.findViewById(R.id.descrizioneArticoloOrdine);
        tvDescrizione.setText(descrizione);

        nomeAcquirenteText = view.findViewById(R.id.textNomeOrdine);
        cognomeAcquirenteText = view.findViewById(R.id.textCognomeOrdine);
        telefonoAcquirenteText = view.findViewById(R.id.textNumeroOrdine);
        CFAcquirenteText = view.findViewById(R.id.textCodiceFiscaleOrdine);

        bottoneOrdina = view.findViewById(R.id.bottoneOrdina);

        database = FirebaseDatabase.getInstance();
        query = database.getReference("Ordini");

        bottoneOrdina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nomeAcquirente = nomeAcquirenteText.getEditableText().toString();
                cognomeAcquirente = cognomeAcquirenteText.getEditableText().toString();
                numeroTelefonoAcquirente = telefonoAcquirenteText.getEditableText().toString();
                descrizioneArticolo = descrizione;
                codiceFiscaleAcquirente = CFAcquirenteText.getEditableText().toString().toUpperCase();

                meseArrivo = generaMese();
                giornoArrivo = generaGiorno(meseArrivo);
                annoArrivo = 2021;

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

                if(TextUtils.isEmpty(codiceFiscaleAcquirente)){
                    CFAcquirenteText.setError("Codice Fiscale Acquirente Mancante");
                    return;
                }

                if(codiceFiscaleAcquirente.length() != 16){
                    CFAcquirenteText.setError("Codice Fiscale non Valido");
                    return;
                }


                String IDOrdine = query.push().getKey();


                Map<String, Object> ordini = new HashMap<>();

                ordini.put("ID", IDOrdine);
                ordini.put("nome", nome);
                ordini.put("img", img);
                ordini.put("nomeAcquirente", nomeAcquirente);
                ordini.put("cognomeAcquirente", cognomeAcquirente);
                ordini.put("giornoArrivo", giornoArrivo);
                ordini.put("meseArrivo", meseArrivo);
                ordini.put("annoArrivo", annoArrivo);
                ordini.put("telefonoAcquirente", numeroTelefonoAcquirente);
                ordini.put("CFAcquirente", codiceFiscaleAcquirente);
                ordini.put("descrizioneArticolo", descrizioneArticolo);


                query.child(IDOrdine).setValue(ordini)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Ordine Creato Correttamente",
                                        Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Errore - Ordine Non Creato",
                                Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });


            }
        });

        return view;
    }

    private int generaGiorno(int mese){

        Random random = new Random();
        int giorno = 1;

        if(mese == 1 || mese == 3 || mese == 5 || mese == 7 || mese == 8 || mese == 10 || mese == 12){
            giorno = random.nextInt(30) + 1;
        }

        if (mese == 4 || mese == 6 || mese == 9 || mese == 11){
            giorno = random.nextInt(29) + 1;
        }

        if (mese == 2){
            giorno = random.nextInt(27) + 1;
        }

        return giorno;
    }

    private int generaMese(){
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.MONTH) + 2;
    }
}
