package com.unip.oitavosemestre.tcc.apptcc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;
import com.unip.oitavosemestre.tcc.apptcc.model.RegistroChamado;

import java.util.ArrayList;

public class RegistroAdapter extends ArrayAdapter<RegistroChamado> {

    private ArrayList<RegistroChamado> registroChamados;
    private RegistroChamado registroChamado;
    private Context context;

    public RegistroAdapter(Context c, ArrayList<RegistroChamado> objects) {
        super(c, 0, objects);
        this.registroChamados = objects;
        this.context = c;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (registroChamados != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_registro, parent, false);

            // recupera elemento para exibição
            TextView localizacao = view.findViewById(R.id.txt_localizacao_registro);
            TextView data = view.findViewById(R.id.txt_data_registro);
            TextView nomeUsuario = view.findViewById(R.id.txt_nome_usuario_registro);
            TextView usuarioEncerramento = view.findViewById(R.id.txt_fechado_registro);
            TextView situacao = view.findViewById(R.id.txt_situacao_registro);
            TextView descricao = view.findViewById(R.id.txt_descricao_registro);
            TextView key = view.findViewById(R.id.txt_key_registro);

            registroChamado = registroChamados.get(position);

                key.setText("Código do Chamado Registrado: " + registroChamado.getKey());
                localizacao.setText("Localização Registrada: " + registroChamado.getLocalizacao());
                data.setText("Data Registrada: " + registroChamado.getData());
                nomeUsuario.setText("Aberto por: " + registroChamado.getNomeUsuario());
                usuarioEncerramento.setText("Fechado por: " + registroChamado.getUsuarioEncerramento());
                situacao.setText("Situação Registrada: " + String.valueOf(registroChamado.getSituacao()));
                descricao.setText("Descrição Registrada do Local: " + registroChamado.getDescricao());

        }
        return view;
    }
}
