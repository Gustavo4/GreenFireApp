package com.unip.oitavosemestre.tcc.apptcc.activity.ui.registro;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unip.oitavosemestre.tcc.apptcc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroFragment extends Fragment {


    public RegistroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

}
