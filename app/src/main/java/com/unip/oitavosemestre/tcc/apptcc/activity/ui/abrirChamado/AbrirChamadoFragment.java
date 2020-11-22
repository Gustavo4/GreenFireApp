package com.unip.oitavosemestre.tcc.apptcc.activity.ui.abrirChamado;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.activity.ui.principal.PrincipalFragment;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.helper.Base64Custom;
import com.unip.oitavosemestre.tcc.apptcc.helper.Preferencias;
import com.unip.oitavosemestre.tcc.apptcc.model.Chamado;
import com.unip.oitavosemestre.tcc.apptcc.model.Situacao;
import com.unip.oitavosemestre.tcc.apptcc.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.layout.simple_dropdown_item_1line;
import static android.app.Activity.RESULT_OK;

public class AbrirChamadoFragment extends Fragment implements LocationListener {

    private Button buttonImagem;
    private Button btAbrirChamado;
    private Spinner situacao;
    private EditText etDescricao;
    private ImageButton icLocalizacao;
    private TextView txtLocalizao;
    private ImageView imageChamado;

    private Uri imageUri;
    private String imagemUploaded;

    private FusedLocationProviderClient client;
    private LocationManager locationManager;

    private DatabaseReference firebase;
    private FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth autenticacao;
    private StorageReference storageReference;
    private StorageTask mUploadTask;


    private Chamado chamado;
    private String localizacao;
    private Situacao situacoes;
    private String descricao;
    private String nomeUsuario;
    private String idUsuarioLogado;

//    private AddressResultReceiver resultReceiver;

    public AbrirChamadoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_abrir_chamado, container, false);

        buttonImagem = view.findViewById(R.id.buttonFoto);
        imageChamado = view.findViewById(R.id.imageFoto);
        btAbrirChamado = view.findViewById(R.id.bt_abrir_chamado);
        situacao = view.findViewById(R.id.spSituacao);
        icLocalizacao = view.findViewById(R.id.icLocalizacao);
        txtLocalizao = view.findViewById(R.id.txtLocalizacao);
        etDescricao = view.findViewById(R.id.textDescricao);

        Preferencias preferencias = new Preferencias(getContext());
        nomeUsuario = preferencias.getNome();

        descricao = etDescricao.getText().toString();

        txtLocalizao.setMovementMethod( new ScrollingMovementMethod());

        ArrayAdapter<Situacao> itemsArray=new ArrayAdapter<Situacao>(getContext(),android.R.layout.simple_spinner_dropdown_item, Situacao.values());
        situacao.setAdapter(itemsArray);

        situacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Situacao selectedItem = Situacao.valueOf(String.valueOf(adapterView.getItemAtPosition(i)));

                situacoes = selectedItem;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        buttonImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ValueEventListener valueEventListenerUsuario = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Recupera mensagens
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    idUsuarioLogado = autenticacao.getCurrentUser().getEmail();

                    if (usuario.getEmail().equals(idUsuarioLogado)) {
                        nomeUsuario = usuario.getNome();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btAbrirChamado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamado = new Chamado();
                chamado.setId(Base64Custom.codificarBase64(usuarioLogado.getEmail()));
                if ( nomeUsuario != null){
                    chamado.setNomeUsuario(nomeUsuario);
                }
                chamado.setSituacao(situacoes);
                chamado.setImagem(imagemUploaded);

                if (etDescricao.getText().toString() == null || etDescricao.getText().toString().isEmpty() || localizacao == null || localizacao.isEmpty()){
                    Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_LONG).show();
                } else {
                    chamado.setDescricao(etDescricao.getText().toString());
                    chamado.setLocalizacao(localizacao);

                    Boolean retornoSalvarChamado = chamado.salvar(chamado);

                    if (!retornoSalvarChamado) {
                        Toast.makeText(getContext(), "Erro ao salvar chamado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Chamado aberto com sucesso!", Toast.LENGTH_SHORT).show();

                        /*String key = chamado.getKey();
                        Log.i("Key", chamado.getKey());*/

                        PrincipalFragment principalFragment = new PrincipalFragment();

                      /*  Bundle bundle = new Bundle();
                        bundle.putString("key", key);
                        principalFragment.setArguments(bundle);*/

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStackImmediate();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, principalFragment, principalFragment.getTag())
                                .addToBackStack(null).commit();
                    }

                }


            }
        });

        icLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

            }
        });

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        firebase = ConfiguracaoFirebase.getFirebase();
//        resultReceiver = new AddressResultReceiver(null);
        return view;

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            Toast.makeText(getContext(), "Localização: " + address, Toast.LENGTH_SHORT).show();

            txtLocalizao.setText(address);
            localizacao = address;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK) && data != null) {
            imageUri = data.getData();

            try {
                Bitmap imagem = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                byte[] img = stream.toByteArray();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmaaaammss");
                String newNameImg = simpleDateFormat.format( new Date() );
                String path = "imagens/" + newNameImg + ".jpeg";

                if (imageUri != null) {
                    storageReference = ConfiguracaoFirebase.getStorageReference(path);

                    mUploadTask = storageReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            imagemUploaded = downloadUrl.toString();
                                            Log.i("Download Img", "URL de download: " + imagemUploaded);
                                        }
                                    });

                                    Toast.makeText(getContext(), "Sucesso ao fazer Upload!", Toast.LENGTH_LONG).show();
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Falha ao fazer upload", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Nenhum item selecionado", Toast.LENGTH_SHORT).show();
                }


                imageChamado.setImageDrawable(new BitmapDrawable(imagem));
                buttonImagem.setAlpha(0);


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
            return;
        }
    }

}


















