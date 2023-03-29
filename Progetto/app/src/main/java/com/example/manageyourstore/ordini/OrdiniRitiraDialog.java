package com.example.manageyourstore.ordini;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class OrdiniRitiraDialog extends DialogFragment {

    private String IDOrdine;
    private String nomeArticolo;
    private String imgArticolo;
    private String descrizioneArticolo;
    private String nomeAcquirente;
    private String cognomeAcquirente;
    private String numeroTelefono;
    private String CFAcquirente;

    private int giornoArrivo;
    private int meseArrivo;
    private int annoArrivo;

    private int giornoOdierno;
    private int meseOdierno;
    private int annoOdierno;

    private int giornoFineGaranzia;
    private int meseFineGaranzia;
    private int annoFineGaranzia;

    private ImageView imgArticoloIV;
    private TextView nomeArticoloTV;
    private TextView descrizioneProdottoTV;
    private TextInputEditText nomeAcquirenteTV;
    private TextInputEditText cognomeAcquirenteTV;
    private TextInputEditText numeroTelefonoTV;
    private TextInputEditText CFAcquirenteInput;
    private Switch aSwitch;
    private Button bottoneRitira;

    private FirebaseDatabase database;
    private DatabaseReference queryRitiro;
    private DatabaseReference queryGaranzia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ritira_ordine, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        IDOrdine = getArguments().getString("ID");
        giornoArrivo = getArguments().getInt("giornoArrivo");
        meseArrivo = getArguments().getInt("meseArrivo");
        annoArrivo = getArguments().getInt("annoArrivo");

        giornoOdierno = generaGiornoOdierno();
        meseOdierno = generaMeseOdierno();
        annoOdierno = generaAnnoOdierno();

        nomeArticolo = getArguments().getString("nomeArticolo");
        nomeArticoloTV = view.findViewById(R.id.nomeProdottoOrdineRitiro);
        nomeArticoloTV.setText(nomeArticolo);

        imgArticolo = getArguments().getString("imgArticolo");
        imgArticoloIV = view.findViewById(R.id.immagineArticoloOrdineRitiro);
        Picasso.get().load(imgArticolo).into(imgArticoloIV);

        descrizioneArticolo = getArguments().getString("descrizioneArticolo");
        descrizioneProdottoTV = view.findViewById(R.id.descrizioneArticoloOrdineRitiro);
        descrizioneProdottoTV.setText(descrizioneArticolo);

        nomeAcquirente = getArguments().getString("nomeAcquirente");
        nomeAcquirenteTV = view.findViewById(R.id.textNomeOrdineRitiro);
        nomeAcquirenteTV.setText(nomeAcquirente);

        cognomeAcquirente = getArguments().getString("cognomeAcquirente");
        cognomeAcquirenteTV = view.findViewById(R.id.textCognomeOrdineRitiro);
        cognomeAcquirenteTV.setText(cognomeAcquirente);

        numeroTelefono = getArguments().getString("numeroTelefono");
        numeroTelefonoTV = view.findViewById(R.id.textNumeroOrdineRitiro);
        numeroTelefonoTV.setText(numeroTelefono);

        CFAcquirente = getArguments().getString("codiceFiscale");
        CFAcquirenteInput = view.findViewById(R.id.textCodiceFiscaleOrdineRitiro);

        aSwitch = view.findViewById(R.id.switchGaranziaRitiro);
        bottoneRitira = view.findViewById(R.id.bottoneRitiro);

        database = FirebaseDatabase.getInstance();
        queryRitiro = database.getReference("Ordini").child(IDOrdine);
        queryGaranzia = database.getReference("Garanzie");

        bottoneRitira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(CFAcquirenteInput.getEditableText().toString())) {
                    CFAcquirenteInput.setError("Codice Fiscale Mancante");
                }

                if (aSwitch.isChecked()) {
                    if (annoOdierno >= annoArrivo) {
                        if (meseOdierno >= meseArrivo) {
                            int casoMese = casoMese(meseArrivo, meseOdierno);
                            switch (casoMese) {
                                case 1:
                                    if (giornoOdierno >= giornoArrivo) {
                                        if (CFAcquirenteInput.getEditableText().toString().equalsIgnoreCase(CFAcquirente)) {
                                            rimuoviOrdine(queryRitiro);
                                            Toast.makeText(getContext(), "Ritiro avvenuto con Successo - Messo in Garanzia", Toast.LENGTH_SHORT).show();
                                            aggiungiGaranzia(queryGaranzia);
                                            dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Codice Fiscale non Valido per il Ritiro", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Oggetto non pronto per il Ritiro", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 2:
                                    if (CFAcquirenteInput.getEditableText().toString().equalsIgnoreCase(CFAcquirente)) {
                                        rimuoviOrdine(queryRitiro);
                                        Toast.makeText(getContext(), "Ritiro avvenuto con Successo - Messo in Garanzia", Toast.LENGTH_SHORT).show();
                                        aggiungiGaranzia(queryGaranzia);
                                        dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Codice Fiscale non Valido per il Ritiro", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 3:
                                    Toast.makeText(getContext(), "Oggetto non pronto per il ritiro", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(getContext(), "Oggetto non pronto per il Ritiro", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Oggetto non pronto per il Ritiro", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (annoOdierno >= annoArrivo) {
                        if (meseOdierno >= meseArrivo) {
                            int casoMese = casoMese(meseArrivo, meseOdierno);
                            switch (casoMese) {
                                case 1:
                                    if (giornoOdierno >= giornoArrivo) {
                                        if (CFAcquirenteInput.getEditableText().toString().equalsIgnoreCase(CFAcquirente)) {
                                            rimuoviOrdine(queryRitiro);
                                            Toast.makeText(getContext(), "Ritiro avvenuto con Successo", Toast.LENGTH_SHORT).show();
                                            aggiungiGaranzia(queryGaranzia);
                                            dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Codice Fiscale non Valido per il Ritiro", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Oggetto non pronto per il Ritiro", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 2:
                                    if (CFAcquirenteInput.getEditableText().toString().equalsIgnoreCase(CFAcquirente)) {
                                        rimuoviOrdine(queryRitiro);
                                        Toast.makeText(getContext(), "Ritiro avvenuto con Successo", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Codice Fiscale non Valido per il Ritiro", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 3:
                                    Toast.makeText(getContext(), "Oggetto non pronto per il ritiro", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(getContext(), "Oggetto non pronto per il Ritiro", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Oggetto non pronto per il Ritiro", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private int casoMese(int meseArrivo, int meseOdierno) {

        int caso = 0;

        if (meseOdierno == meseArrivo) {
            caso = 1;
        }
        if (meseOdierno > meseArrivo) {
            caso = 2;
        }
        if (meseOdierno < meseArrivo) {
            caso = 3;
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

    private void aggiungiGaranzia(DatabaseReference query) {

        giornoFineGaranzia = generaGiornoFineGaranzia();
        meseFineGaranzia = generaMeseFineGaranzia();
        annoFineGaranzia = generaAnnoFineGaranzia();
        String descrizioneProblema = "null";
        int statoAssistenza = 1;
        String IDGaranzia = query.push().getKey();

        Map<String, Object> garanzie = new HashMap<>();
        garanzie.put("ID", IDGaranzia);
        garanzie.put("nomeAcquirente", nomeAcquirente);
        garanzie.put("cognomeAcquirente", cognomeAcquirente);
        garanzie.put("numeroTelefonoAcquirente", numeroTelefono);
        garanzie.put("img", imgArticolo);
        garanzie.put("giornoFineGaranzia", giornoFineGaranzia);
        garanzie.put("meseFineGaranzia", meseFineGaranzia);
        garanzie.put("annoFineGaranzia", annoFineGaranzia);
        garanzie.put("nomeArticolo", nomeArticolo);
        garanzie.put("statoAssistenza", statoAssistenza);
        garanzie.put("descrizioneProblema", descrizioneProblema);
        garanzie.put("giornoRitiroAssistenza", 0);
        garanzie.put("meseRitiroAssistenza", 0);
        garanzie.put("annoRitiroAssistenza", 0);

        query.child(IDGaranzia).setValue(garanzie).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v("STATO", "ARTICOLO RIMOSSO DA ORDINI - IN GARANZIA");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("STATO", "IMPOSSIBILE RITIRARE ARTICOLO");
            }
        });
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