package com.unip.oitavosemestre.tcc.apptcc.activity.ui.sobre;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unip.oitavosemestre.tcc.apptcc.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * A simple {@link Fragment} subclass.
 */
public class SobreFragment extends Fragment {


    public SobreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String descricao = "O app GreenFire tem como objetivo ajudar no combate " +
                "aos incêndios florestais, proporcionando uma comunicação rápida " +
                "da localização de um incêndio. " +
                "Desta forma, os combatentes que utilizam nosso aplicativo "+
                "podem reagir rapidamente contra uma ameaça ao nosso meio ambiente. ";

        Element versao = new Element();
        versao.setTitle( "Versão 1.0" );

        return new AboutPage( getActivity() )
                .setImage( R.drawable.logo )
                .setDescription( descricao )

                .addGroup("Entre em contato")
                .addEmail("greenfireu8@gmail.com", "Envie um e-mail")
                .addWebsite("https://www.google.com/", "Acesse nosso site")

                .addGroup("Redes sociais")
                /*.addFacebook("jamiltondamasceno", "Facebook")
                .addInstagram("jamiltondamasceno", "Instagram")
                .addTwitter("jamiltondamasceno", "Twitter(")
                .addYoutube("jamiltondamasceno", "Youtube")*/
                .addGitHub("gustavo4", "GitHub")
//                .addPlayStore("com.facebook.katana", "Download App")

                .addItem( versao )

                .create();
        //return inflater.inflate(R.layout.fragment_sobre, container, false);

    }

}
