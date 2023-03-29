package com.example.manageyourstore.assistenza;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.manageyourstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AssistenzaRitiroDialog extends DialogFragment {

    private String IDGaranzia;
    private String nomeProdotto;
    private String nomeAcquirente;
    private String descrizioneProblema;

    private TextView nomeProdottoTV;
    private TextView nomeAcquirenteTV;
    private TextView descrizioneProblemaText;
    private Button bottoneRitira;

    private FirebaseDatabase database;
    private DatabaseReference queryRitiro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_assistenza_ritiro, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        nomeProdottoTV = view.findViewById(R.id.nomeProdottoAssistenzaRitiro);
        nomeAcquirenteTV = view.findViewById(R.id.nomeAcquirenteAssistenzaRitiro);
        descrizioneProblemaText = view.findViewById(R.id.descrizioneAssistenzaRitiroEditText);
        bottoneRitira = view.findViewById(R.id.ritiraBottone);

        IDGaranzia = getArguments().getString("IDGaranzia");
        nomeProdotto = getArguments().getString("nomeProdotto");
        nomeAcquirente = getArguments().getString("acquirente");
        descrizioneProblema = getArguments().getString("descrizioneProblema");

        nomeProdottoTV.setText(nomeProdotto);
        nomeAcquirenteTV.setText(nomeAcquirente);
        descrizioneProblemaText.setText(descrizioneProblema);

        database = FirebaseDatabase.getInstance();
        queryRitiro = database.getReference("Garanzie").child(IDGaranzia);

        bottoneRitira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                queryRitiro.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                Toast.makeText(getContext(), "Articolo Ritirato con Successo", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }
}
