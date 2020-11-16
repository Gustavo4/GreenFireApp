package com.unip.oitavosemestre.tcc.apptcc.activity.ui.principal;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.adapter.ChamadoAdapter;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.helper.Preferencias;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Chamado> chamados;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerChamados;


    public PrincipalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( valueEventListenerChamados );
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener( valueEventListenerChamados );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chamados = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        listView = view.findViewById(R.id.lv_chamados);

        adapter = new ChamadoAdapter(getContext(), chamados );
        listView.setAdapter( adapter );

        //Recuperar contatos do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuario = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                    .child("chamado").child(idUsuario);




        //Listener para recuperar contatos
        valueEventListenerChamados = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                chamados.clear();

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Chamado chamado = dados.getValue( Chamado.class );
                    chamados.add( chamado);


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

     /*   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                // recupera dados a serem passados
                Contato contato = contatos.get(position);

                // enviando dados para conversa activity
                intent.putExtra("nome", contato.getNome() );
                intent.putExtra("email", contato.getEmail() );

                startActivity(intent);

            }
        });*/


        return view;
    }

}
