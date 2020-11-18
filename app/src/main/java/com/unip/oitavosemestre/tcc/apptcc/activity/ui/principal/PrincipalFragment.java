package com.unip.oitavosemestre.tcc.apptcc.activity.ui.principal;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.activity.LoginActivity;
import com.unip.oitavosemestre.tcc.apptcc.adapter.ChamadoAdapter;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.helper.Base64Custom;
import com.unip.oitavosemestre.tcc.apptcc.helper.Preferencias;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;
import com.unip.oitavosemestre.tcc.apptcc.model.Situacao;
import com.unip.oitavosemestre.tcc.apptcc.model.Usuario;

import java.util.ArrayList;

import static com.unip.oitavosemestre.tcc.apptcc.model.Situacao.Controlado;
import static com.unip.oitavosemestre.tcc.apptcc.model.Situacao.Perigoso;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Chamado> chamados;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerChamados;
    private String idUsuarioAuth;
    private FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();



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

        //Recuperar chamados do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioPreferencias = preferencias.getIdentificador();

       /* Bundle arguments = getArguments();
        String key = arguments.getString("key");*/

       if (idUsuarioPreferencias == null){
           idUsuarioAuth = Base64Custom.codificarBase64(usuarioLogado.getEmail());
           firebase = ConfiguracaoFirebase.getFirebase()
                   .child("chamado").child(idUsuarioAuth);
       } else {
           firebase = ConfiguracaoFirebase.getFirebase()
                   .child("chamado").child(idUsuarioPreferencias);
       }
            valueEventListenerChamados = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //Limpar lista
                    chamados.clear();

                    //Listar chamados
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Chamado chamado =  dados.getValue(Chamado.class);
                        chamados.add(chamado);

                    }

                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
