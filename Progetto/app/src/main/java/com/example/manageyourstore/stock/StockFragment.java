package com.example.manageyourstore.stock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.manageyourstore.R;
import com.example.manageyourstore.app.Articolo;
import com.example.manageyourstore.adapter.ArticoloAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Articolo> listaArticoli;
    private ArticoloAdapter articoloAdapter;
    private DatabaseReference query;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refresh;
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stock, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = root.findViewById(R.id.recycleViewStock);
        searchView = root.findViewById(R.id.search_view);
        refresh = root.findViewById(R.id.refresh);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(articoloAdapter);
        listaArticoli = new ArrayList<>();

        query = FirebaseDatabase.getInstance().getReference("Articoli");
        query.addListenerForSingleValueEvent(valueEventListener);

        if (searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = FirebaseDatabase.getInstance().getReference("Articoli");
                query.addListenerForSingleValueEvent(valueEventListener);
                refresh.setRefreshing(false);
            }
        });

        return root;
    }

    private void search(String newText) {
        ArrayList<Articolo> lista = new ArrayList<>();
        for(Articolo object : listaArticoli) {
            if (object.getNome().toLowerCase().contains(newText.toLowerCase())) {
                lista.add(object);
            }
        }
        articoloAdapter = new ArticoloAdapter(getActivity(), lista);
        recyclerView.setAdapter(articoloAdapter);
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listaArticoli.clear();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                Articolo articolo = snapshot.getValue(Articolo.class);
                listaArticoli.add(articolo);
            }
            articoloAdapter = new ArticoloAdapter(getActivity(), listaArticoli);
            recyclerView.setAdapter(articoloAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
}