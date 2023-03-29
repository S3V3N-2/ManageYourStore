package com.example.manageyourstore.assistenza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.manageyourstore.R;
import com.example.manageyourstore.adapter.GaranziaAdapter;
import com.example.manageyourstore.app.Garanzia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AssistenzaFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Garanzia> listaGaranzie;
    private GaranziaAdapter garanziaAdapter;
    private DatabaseReference query;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assistenza, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = view.findViewById(R.id.recycleViewAssistenza);
        refresh = view.findViewById(R.id.refreshAssistenza);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(garanziaAdapter);
        listaGaranzie = new ArrayList<>();

        query = FirebaseDatabase.getInstance().getReference("Garanzie");
        query.addListenerForSingleValueEvent(valueEventListener);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = FirebaseDatabase.getInstance().getReference("Garanzie");
                query.addListenerForSingleValueEvent(valueEventListener);
                refresh.setRefreshing(false);
            }
        });

        return view;
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listaGaranzie.clear();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Garanzia garanzia = dataSnapshot.getValue(Garanzia.class);
                listaGaranzie.add(garanzia);
            }
            garanziaAdapter = new GaranziaAdapter(listaGaranzie, getActivity());
            recyclerView.setAdapter(garanziaAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}