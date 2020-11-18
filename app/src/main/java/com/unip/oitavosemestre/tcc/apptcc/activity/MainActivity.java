package com.unip.oitavosemestre.tcc.apptcc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unip.oitavosemestre.tcc.apptcc.R;
import com.unip.oitavosemestre.tcc.apptcc.config.ConfiguracaoFirebase;
import com.unip.oitavosemestre.tcc.apptcc.helper.Base64Custom;
import com.unip.oitavosemestre.tcc.apptcc.helper.Preferencias;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth autenticacao;
    private TextView nome;
    private TextView email;
    private FirebaseUser usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "OLá", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao(getApplicationContext());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_principal, R.id.nav_abrir_chamado, R.id.nav_registros,
                R.id.nav_equipe, R.id.nav_sobre)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View navHeaderView = navigationView.getHeaderView(0);
        nome = navHeaderView.findViewById(R.id.textNome);
        email = navHeaderView.findViewById(R.id.textEmail);

        Preferencias preferencias = new Preferencias(MainActivity.this);
        String nomeUsuarioLogado = preferencias.getNome();

        if ( nomeUsuarioLogado != null){
            nome.setText("Olá, " + nomeUsuarioLogado);
        }

        if ( usuarioLogado != null) {
            email.setText(usuarioLogado.getEmail());
        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == R.id.item_sair){
            deslogarUsuario();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        autenticacao.signOut();
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deslogarUsuario();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if ( usuarioLogado == null ){
            deslogarUsuario();
        }
    }

}
