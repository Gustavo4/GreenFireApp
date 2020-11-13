package com.unip.oitavosemestre.tcc.apptcc.activity.ui.abrirChamado;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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

import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.activity.MainActivity;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.helper.BuscarEndereco;
import com.unip.oitavosemestre.tcc.apptcc.helper.ConstantsFetchAddressService;
import com.unip.oitavosemestre.tcc.apptcc.model.Situacao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.layout.simple_dropdown_item_1line;
import static android.app.Activity.RESULT_OK;

public class AbrirChamadoFragment extends Fragment implements LocationListener {

    private Button buttonImagem;
    private Button btAbrirChamado;
    private Uri localImagemSelecionada;
    private ImageView imageChamado;
    private Spinner situacao;
    private EditText descricao;
    private FusedLocationProviderClient client;
    private ImageButton icLocalizacao;
    private TextView txtLocalizao;
    private LocationManager locationManager;
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

        txtLocalizao.setMovementMethod( new ScrollingMovementMethod());

        situacao.setAdapter(new ArrayAdapter<Situacao>(getContext(), simple_dropdown_item_1line, Situacao.values()));

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

        icLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

            }
        });

        client = LocationServices.getFusedLocationProviderClient(getActivity());

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

                btAbrirChamado.setEnabled(false);
                buttonImagem.setEnabled(false);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.i("Upload Image", "Image uploaded");
                        Toast.makeText(getContext(), "Sucesso ao carregar " + localImagemSelecionada, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
            return;
        }

       /* //Pega ultima localização
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    Log.i("Localização null:", location.getLatitude() + " " + location.getLongitude() + " ");
                } else {
                    Log.i("Ultima localização:", location.getLatitude() + " " + location.getLongitude() + " ");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Falha Localização", e.getMessage());
            }
        });*/

        /*final LocationRequest locationRequest = LocationRequest.create();
//        Define intervalo de atualização de localizacao
        locationRequest.setInterval(15 * 1000);

        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builderLocation = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(builderLocation.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("SettingsLocationTrue", locationSettingsResponse.getLocationSettingsStates()
                        .isNetworkLocationPresent() + "");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException){
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 10);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationRequest == null){
                    Log.i("CallBack", "local is null");
                    return;
                }

                for (Location location: locationResult.getLocations()){
                    Log.i("Localização", location.getLatitude() + " ");

                    if (!Geocoder.isPresent()) {
                        Toast.makeText(getContext(), "Sem Geocoder", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startIntentService(location);
                }

            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.i("LocalDisponivel", locationAvailability.isLocationAvailable() + " ");
            }
        };

        client.requestLocationUpdates(locationRequest, locationCallback,null);*/

    }
  /*  private class AddressResultReceiver extends ResultReceiver {


        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null){
                Toast.makeText(getContext(), "Passou pelo onReceive Null", Toast.LENGTH_LONG).show();
                return;}

            final String endereço = resultData.getString(ConstantsFetchAddressService.RESULT_DATA_KEY);


            if (endereço != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Localização: " + endereço, Toast.LENGTH_LONG).show();
                        Log.i("localizacaoGeocoder", "Localizacao " + endereço);
                            txtLocalizao.setText(endereço);
                    }
                });
            }
        }
    }

    private void startIntentService(Location location) {
        Intent intent = new Intent(getContext(), AbrirChamadoFragment.class);
        intent.putExtra(ConstantsFetchAddressService.RECEIVER, resultReceiver);
        intent.putExtra(ConstantsFetchAddressService.LOCATION_DATA_EXTRA, location);
        getActivity().startService(intent);
    }*/

}


















