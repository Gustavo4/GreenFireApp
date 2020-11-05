package com.unip.oitavosemestre.tcc.apptcc.activity.ui.abrirChamado;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unip.oitavosemestre.tcc.apptcc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbrirChamadoFragment extends Fragment {

    private Button buttonImagem;

    public AbrirChamadoFragment() {
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        buttonImagem = container.findViewById(R.id.bt_Image_Incendio);
        
        buttonImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        return inflater.inflate(R.layout.fragment_abrir_chamado, container, false);

    }

    private void selectImage() {
        Intent intent = new Intent();
    }

}
