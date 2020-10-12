package com.example.github_dmos5.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.github_dmos5.R;
import com.example.github_dmos5.api.RetrofitService;
import com.example.github_dmos5.model.GitHub;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 64;
    private static final String BASE_URL = "https://api.github.com/users/";

    private Retrofit retrofit;

    private EditText editTextUser;
    private Button buttonBuscar;

    private ConstraintLayout dadosConstrain;
    private TextView textViewNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLayoutElements();

    }

    private void getLayoutElements() {
        editTextUser = findViewById(R.id.edittext_user);
        buttonBuscar = findViewById(R.id.button_buscar);
        buttonBuscar.setOnClickListener(this);

        dadosConstrain = findViewById(R.id.constraint_dados);
        textViewNome = findViewById(R.id.  textview_nome);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(buttonBuscar)){
            if(temPermissao()) {
                buscaUsuarioGit();
            }else{
                solicitaPermissao();
            }
        }
    }

    private void buscaUsuarioGit(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        String usuario = editTextUser.getText().toString();

        usuario += "/repos/";

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<GitHub> call = retrofitService.getDados(usuario);

        call.enqueue(new Callback<GitHub>() {
            @Override
            public void onResponse(Call<GitHub> call, Response<GitHub> response) {
                if(response.isSuccessful()){
                    GitHub git = response.body();
                    updateUI(git);
                }
            }

            @Override
            public void onFailure(Call<GitHub> call, Throwable t) { //arrumar aqui
                Toast.makeText(MainActivity.this, "erro ao buscar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean temPermissao(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitaPermissao(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage(R.string.explicacao_permissao)
                    .setPositiveButton(R.string.botao_fornecer, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.botao_nao_fornecer, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {

                if (permissions[i].equalsIgnoreCase(Manifest.permission.INTERNET) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    buscaUsuarioGit();
                }

            }
        }
    }


    private void updateUI(GitHub git){
        dadosConstrain.setVisibility(View.VISIBLE);
        textViewNome.setText(git.getName());
    }

}