package com.unip.oitavosemestre.tcc.apptcc.activity.ui.abrirChamado;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AbrirChamadoFragment extends Fragment {

    private Button buttonImagem;
    private Button btAbrirChamado;
    private Uri localImagemSelecionada;
    private ImageView imageChamado;
    private ProgressBar progressBar;

    public AbrirChamadoFragment() {
        
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        

        View view = inflater.inflate(R.layout.fragment_abrir_chamado, container, false);

        buttonImagem = view.findViewById(R.id.buttonFoto);
        imageChamado = view.findViewById(R.id.imageFoto);
        btAbrirChamado = view.findViewById(R.id.bt_abrir_chamado);
        progressBar = view.findViewById(R.id.progressBar_abrir_chamado);


        buttonImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btAbrirChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarChamado();
            }
        });

        return view;

    }
//TODO -- Adicionar scroll view e mudar background dos fragments

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK) && data != null){
            localImagemSelecionada = data.getData();

            try {
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), localImagemSelecionada);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                byte[] img = stream.toByteArray();

                //salvando imagem com nome diferente, baseado na data
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmaaaammss");
                String nomeImagem = simpleDateFormat.format( new Date() );

                String path = "imagens/" + nomeImagem + ".jpeg";

                //fazendo uploado da imagem
                final StorageReference ref = ConfiguracaoFirebase.getStorageReference(path);

                StorageMetadata storageMetadata = new StorageMetadata();

                UploadTask uploadTask = ref.putBytes(img);

                progressBar.setVisibility(View.VISIBLE);
                btAbrirChamado.setEnabled(false);
                buttonImagem.setEnabled(false);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.i("Upload Image", "Image uploaded");
                        Toast.makeText(getContext(), "Sucesso ao carregar " + localImagemSelecionada, Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        btAbrirChamado.setEnabled(true);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Failed Image", "Image failed upload");
                                Toast.makeText(getContext(), "Falha ao carregar imagem", Toast.LENGTH_SHORT).show();
                            }
                        });


                imageChamado.setImageDrawable(new BitmapDrawable(imagem));
                buttonImagem.setAlpha(0);

            } catch (IOException e) {
                Log.i("Imagem Incêndio", "Não foi possível enviar a imagem.");
                e.printStackTrace();
            }

        }

    }

    private void salvarChamado(){

    }

}
