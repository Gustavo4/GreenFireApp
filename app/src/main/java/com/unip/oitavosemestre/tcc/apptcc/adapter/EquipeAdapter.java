package com.unip.oitavosemestre.tcc.apptcc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;
import com.unip.oitavosemestre.tcc.apptcc.model.Usuario;

import java.util.ArrayList;

public class EquipeAdapter extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarios;
    private Usuario usuario;
    private Context context;

    public EquipeAdapter(Context c, ArrayList<Usuario> objects) {
        super(c, 0, objects);
        this.usuarios = objects;
        this.context = c;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (usuarios != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_equipe, parent, false);

            TextView nome = view.findViewById(R.id.txt_nome_usuario_equipe);
            TextView email = view.findViewById(R.id.txt_email_usuario_equipe);

            usuario = usuarios.get(position);

            nome.setText("Nome: " + usuario.getNome());
            email.setText("Email: " + usuario.getEmail());

        }
        return view;
    }
}

