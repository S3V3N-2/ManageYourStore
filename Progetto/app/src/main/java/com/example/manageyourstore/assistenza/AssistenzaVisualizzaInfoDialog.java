package com.example.manageyourstore.assistenza;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.manageyourstore.R;

public class AssistenzaVisualizzaInfoDialog extends DialogFragment {

    private String IDGaranzia;
    private String nomeProdotto;
    private String nomeAcquirente;
    private String dataRitiro;
    private String descrizioneProblema;

    private TextView nomeProdottoTV;
    private TextView nomeAcquirenteTV;
    private TextView dataRitiroTV;
    private TextView descrizioneProblemaTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_assistenza_info, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        IDGaranzia = getArguments().getString("IDGaranzia");
        nomeProdotto = getArguments().getString("nomeProdotto");
        nomeAcquirente = getArguments().getString("acquirente");
        dataRitiro = getArguments().getString("dataRitiro");
        descrizioneProblema = getArguments().getString("descrizioneProblema");

        nomeProdottoTV = view.findViewById(R.id.nomeProdottoInfo);
        nomeAcquirenteTV = view.findViewById(R.id.nomeAcquirenteInfo);
        dataRitiroTV = view.findViewById(R.id.dataRitiroInfoEditText);
        descrizioneProblemaTV = view.findViewById(R.id.descrizioneInfoEditText);

        nomeProdottoTV.setText(nomeProdotto);
        nomeAcquirenteTV.setText(nomeAcquirente);
        dataRitiroTV.setText(dataRitiro);
        descrizioneProblemaTV.setText(descrizioneProblema);

        return view;
    }

}
