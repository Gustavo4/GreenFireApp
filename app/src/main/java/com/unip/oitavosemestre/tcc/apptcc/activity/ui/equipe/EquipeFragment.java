package com.unip.oitavosemestre.tcc.apptcc.activity.ui.equipe;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.unip.oitavosemestre.tcc.apptcc.adapter.EquipeAdapter;
import com.unip.oitavosemestre.tcc.apptcc.adapter.RegistroAdapter;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;
import com.unip.oitavosemestre.tcc.apptcc.model.Usuario;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipeFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Usuario> usuarios;
    private DatabaseReference usuariosReference;
    private ValueEventListener valueEventListenerUsuarios;
    private Usuario usuario;

    public EquipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        usuariosReference.addValueEventListener( valueEventListenerUsuarios );
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosReference.removeEventListener( valueEventListenerUsuarios );
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        usuarios = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_equipe, container, false);

        listView = view.findViewById(R.id.lv_equipe);
        usuariosReference = ConfiguracaoFirebase.getFirebase().child("usuarios");
        valueEventListenerUsuarios = usuariosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Limpar lista
                usuarios.clear();

                //Listar chamados
                if (dataSnapshot.exists()){
                    Log.i("Dados", "Dados em equipe: " + dataSnapshot.getValue());
                    for (DataSnapshot dados: dataSnapshot.getChildren()) {
                        usuario = dados.getValue(Usuario.class);

                        usuarios.add(usuario);
                        adapter = new EquipeAdapter(getContext(), usuarios );
                        listView.setAdapter( adapter );
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent( Intent.ACTION_SEND );

                intent.putExtra( Intent.EXTRA_EMAIL, usuario.getEmail() );
                intent.putExtra( Intent.EXTRA_SUBJECT, "Contato pelo App" );
                intent.putExtra( Intent.EXTRA_TEXT, "Mensagem automática" );

                intent.setType("message/rfc822");

                startActivity( Intent.createChooser( intent, "Escolha um App de e-mail" ) );


            }
        });

        return view;

    }

}
