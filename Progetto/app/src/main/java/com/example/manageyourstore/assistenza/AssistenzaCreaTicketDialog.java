package com.example.manageyourstore.assistenza;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.manageyourstore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AssistenzaCreaTicketDialog extends DialogFragment {

    private String IDGaranzia;
    private String nomeProdotto;
    private String acquirente;

    private String descrizioneProblema;
    private String dataRitiroAssistenza;
    private int statoAssistenza;

    private int giornoArrivo;
    private int meseArrivo;
    private int annoArrivo;

    private TextView nomeProdottoTV;
    private TextView acquirenteTV;
    private TextInputEditText descrizioneProblemaText;
    private TextInputEditText dataRitiroAssistenzaText;
    private Button bottoneCreaTicket;

    private FirebaseDatabase database;
    private DatabaseReference query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_assistenza_crea_ticket, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        IDGaranzia = getArguments().getString("IDGaranzia");
        nomeProdotto = getArguments().getString("nomeProdotto");
        acquirente = getArguments().getString("acquirente");

        nomeProdottoTV = view.findViewById(R.id.nomeProdottoTicket);
        nomeProdottoTV.setText(nomeProdotto);

        acquirenteTV = view.findViewById(R.id.nomeAcquirenteTicket);
        acquirenteTV.setText(acquirente);

        descrizioneProblemaText = view.findViewById(R.id.descrizioneTicketEditText);
        dataRitiroAssistenzaText = view.findViewById(R.id.dataRitiroEditText);
        bottoneCreaTicket = view.findViewById(R.id.bottoneTicket);

        Calendar calendar = Calendar.getInstance();
        final int annoOdierno = calendar.get(Calendar.YEAR);
        final int meseOdierno = calendar.get(Calendar.MONTH) + 1;
        final int giornoOdierno = calendar.get(Calendar.DAY_OF_MONTH);

        dataRitiroAssistenzaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anno, int mese, int giorno) {
                        mese = mese + 1;
                        String date = giorno + "/" + mese + "/" + anno;
                        dataRitiroAssistenzaText.setText(date);
                        dataRitiroAssistenzaText.setTextColor(Color.BLACK);
                        giornoArrivo = giorno;
                        meseArrivo = mese;
                        annoArrivo = anno;
                    }
                }, annoOdierno, meseOdierno, giornoOdierno);
                datePickerDialog.show();
            }
        });

        database = FirebaseDatabase.getInstance();
        query = database.getReference("Garanzie").child(IDGaranzia);

        bottoneCreaTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                descrizioneProblema = descrizioneProblemaText.getEditableText().toString();
                dataRitiroAssistenza = dataRitiroAssistenzaText.getEditableText().toString();
                statoAssistenza = 2;

                if(TextUtils.isEmpty(descrizioneProblema)){
                    descrizioneProblemaText.setError("Descrizione Problema Mancante");
                    return;
                }

                if(TextUtils.isEmpty(dataRitiroAssistenza)){
                    dataRitiroAssistenzaText.setError("Data di Ritiro Mancante");
                    return;
                }

                if (annoOdierno <= annoArrivo) {
                    if (meseOdierno <= meseArrivo) {
                        int casoMese = casoMese(meseArrivo, meseOdierno);
                        switch (casoMese) {
                            case 1:
                                if (giornoOdierno <= giornoArrivo) {
                                    query.child("giornoRitiroAssistenza").setValue(giornoArrivo);
                                    query.child("meseRitiroAssistenza").setValue(meseArrivo);
                                    query.child("annoRitiroAssistenza").setValue(annoArrivo);
                                    query.child("descrizioneProblema").setValue(descrizioneProblema);
                                    query.child("statoAssistenza").setValue(statoAssistenza);
                                    Toast.makeText(getContext(), "Prodotto messo in Assistenza", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Data Non Valida", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                    query.child("giornoRitiroAssistenza").setValue(giornoArrivo);
                                    query.child("meseRitiroAssistenza").setValue(meseArrivo);
                                    query.child("annoRitiroAssistenza").setValue(annoArrivo);
                                    query.child("descrizioneProblema").setValue(descrizioneProblema);
                                    query.child("statoAssistenza").setValue(statoAssistenza);
                                    Toast.makeText(getContext(), "Prodotto messo in Assistenza", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                break;
                            case 3:
                                Toast.makeText(getContext(), "Data non Valida", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(getContext(), "Data non Valida", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Data non Valida", Toast.LENGTH_SHORT).show();
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
        if (meseOdierno < meseArrivo) {
            caso = 2;
        }
        if (meseOdierno > meseArrivo) {
            caso = 3;
        }

        return caso;

    }

}
