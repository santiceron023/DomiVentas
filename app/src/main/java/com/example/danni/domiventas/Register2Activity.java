package com.example.danni.domiventas;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText eNombreNegocio, eNombrePropietario,eDireccion, eNumeroCelular;
    String TipoNegocio, Email;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private Spinner sTipoTienda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        eNombreNegocio=(EditText)findViewById(R.id.eNombreNegocio);
        eNombrePropietario=(EditText)findViewById(R.id.eNombrePropietario);
        eDireccion=(EditText)findViewById(R.id.eDireccion);
        eNumeroCelular=(EditText)findViewById(R.id.eNumeroCelular);
        sTipoTienda=(Spinner)findViewById(R.id.sTipoTienda);

        Bundle data = getIntent().getExtras();
        Email=data.getString("Email");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_negocio, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTipoTienda.setAdapter(adapter);
        sTipoTienda.setOnItemSelectedListener(Register2Activity.this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TipoNegocio = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void bContinue(View view) {
        if(eNombreNegocio.getText().toString().isEmpty()){eNombreNegocio.setError("Complete this field");return;}
        if(eNombrePropietario.getText().toString().isEmpty()){eNombrePropietario.setError("Complete this field");return;}
        if(eDireccion.getText().toString().isEmpty()){eDireccion.setError("Complete this field");return;}
        if(eNumeroCelular.getText().toString().isEmpty()){eNumeroCelular.setError("Complete this field");return;}


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(eNumeroCelular.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Actualizado",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        Intent intent = new Intent(Register2Activity.this,Register3Activity.class);
        intent.putExtra("Email",Email);
        intent.putExtra("Nombre",eNombreNegocio.getText().toString());
        intent.putExtra("Propietario",eNombrePropietario.getText().toString());
        intent.putExtra("Direccion",eDireccion.getText().toString());
        intent.putExtra("Celular",eNumeroCelular.getText().toString());
        intent.putExtra("Tipo",TipoNegocio);
        startActivity(intent);
        //FirebaseAuth.getInstance().signOut();
        finish();
    }
}
