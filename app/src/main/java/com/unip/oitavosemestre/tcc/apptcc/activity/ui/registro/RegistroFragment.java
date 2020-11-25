package com.unip.oitavosemestre.tcc.apptcc.activity.ui.registro;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.adapter.RegistroAdapter;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.model.RegistroChamado;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<RegistroChamado> registroChamados;
    private DatabaseReference chamadoReference;
    private ValueEventListener valueEventListenerChamados;


    public RegistroFragment() {
        // Required empty public constructor
    }

      @Override
    public void onStart() {
        super.onStart();
        chamadoReference.addValueEventListener( valueEventListenerChamados );
    }

    @Override
    public void onStop() {
        super.onStop();
        chamadoReference.removeEventListener( valueEventListenerChamados );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        registroChamados = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        listView = view.findViewById(R.id.lv_registros);

        chamadoReference = ConfiguracaoFirebase.getFirebase().child("registroChamado");
        valueEventListenerChamados = chamadoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Limpar lista
                registroChamados.clear();

                //Listar registroChamados
                if (dataSnapshot.exists()){
                    Log.i("Dados", "Dados em registro: " + dataSnapshot.getValue());
                    for (DataSnapshot dados: dataSnapshot.getChildren()) {
                        RegistroChamado registroChamado = dados.getValue(RegistroChamado.class);

                        registroChamados.add(registroChamado);
                        if (registroChamados.size() > 0 && registroChamados != null) {
                            adapter = new RegistroAdapter(getContext(), registroChamados);
                            listView.setAdapter(adapter);
                        } else {
                            return;
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

}
