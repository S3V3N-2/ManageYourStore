package com.example.manageyourstore.ordini;

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
import com.example.manageyourstore.adapter.OrdineAdapter;
import com.example.manageyourstore.app.Ordine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrdiniFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Ordine> listaOrdini;
    private OrdineAdapter ordineAdapter;
    private DatabaseReference query;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ordini, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = root.findViewById(R.id.recycleViewOrdini);
        refresh = root.findViewById(R.id.refreshOrdini);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(ordineAdapter);
        listaOrdini = new ArrayList<>();
        
        query = FirebaseDatabase.getInstance().getReference("Ordini");
        query.addListenerForSingleValueEvent(valueEventListener);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = FirebaseDatabase.getInstance().getReference("Ordini");
                query.addListenerForSingleValueEvent(valueEventListener);
                refresh.setRefreshing(false);
            }
        });

        return root;
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listaOrdini.clear();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                Ordine ordine = snapshot.getValue(Ordine.class);
                listaOrdini.add(ordine);
            }
            ordineAdapter = new OrdineAdapter(getActivity(), listaOrdini);
            recyclerView.setAdapter(ordineAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
}