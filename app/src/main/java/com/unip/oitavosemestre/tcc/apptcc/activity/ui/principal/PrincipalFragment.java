package com.unip.oitavosemestre.tcc.apptcc.activity.ui.principal;


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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.adapter.ChamadoAdapter;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.helper.Preferencias;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;
import com.unip.oitavosemestre.tcc.apptcc.model.Situacao;
import com.unip.oitavosemestre.tcc.apptcc.model.Usuario;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Chamado> chamados;
    private DatabaseReference chamadoReference;
    private ValueEventListener valueEventListenerChamados;

    public PrincipalFragment() {
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
        // Inflate the layout for this fragment
        if (chamados != null)
            chamados.clear();
        else
            chamados = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        listView = view.findViewById(R.id.lv_chamados);

        chamadoReference = ConfiguracaoFirebase.getFirebase().child("chamado");
//        Query query = chamadoReference.child("chamado").orderByChild("status").equalTo("Aberto");
        valueEventListenerChamados = chamadoReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Listar chamados
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {

                            Log.i("Dados", "Dados em chamado: " + dados);

                            Chamado chamado = dados.getValue(Chamado.class);
                            chamados.add(chamado);

                            adapter = new ChamadoAdapter(getContext(), chamados);
                            adapter.notifyDataSetChanged();

                            listView.setAdapter(adapter);

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
