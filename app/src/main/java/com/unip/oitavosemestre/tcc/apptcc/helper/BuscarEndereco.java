package com.unip.oitavosemestre.tcc.apptcc.helper;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuscarEndereco extends IntentService {

    private ResultReceiver receiver;

    public BuscarEndereco() {
        super("fetchaddress");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if ( intent == null) return;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(ConstantsFetchAddressService.LOCATION_DATA_EXTRA);
        receiver = intent.getParcelableExtra(ConstantsFetchAddressService.RECEIVER);

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            Toast.makeText(this, "Serviçõ não disponível", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(this, "Latitude e longitude não encontradas", Toast.LENGTH_SHORT).show();
        }

        if ( addresses == null || addresses.isEmpty() ) {
            Toast.makeText(this, "Endereço não encontrado", Toast.LENGTH_SHORT).show();
            entregarResultado(ConstantsFetchAddressService.FAILURE_RESULT, "Nenhum endereço encontrado");
        } else {
//            separa itens dentro do addresses para exibir de forma correta
            Address address = addresses.get(0);
            List<String> addressList = new ArrayList<>();

            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                addressList.add(address.getAddressLine(i));
            }

            entregarResultado(ConstantsFetchAddressService.SUCCESS_RESULT, TextUtils.join("|", addressList));
        }



    }

    private void entregarResultado(int resultCode, String msg) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantsFetchAddressService.RESULT_DATA_KEY, msg);
        receiver.send(resultCode, bundle);
    }

}
