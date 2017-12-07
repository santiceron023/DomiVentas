package com.example.danni.domiventas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;

public class Register3Activity extends AppCompatActivity {
    private EditText eCostoEnvio, ePedidoMin, eTiempoEnvio;
    private TextView tCambiarImagen;
    private ImageView imagenNegocio;
    private StorageReference mStorage;
    private Uri foto;
    private NuevoUsuario nuevoUsuario;
    private String Email, Nombre, Propietario, Celular, Direccion, Tipo;
    private static final int GALLERY_INTENT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        eCostoEnvio=(EditText)findViewById(R.id.eCostoEnvio);
        ePedidoMin=(EditText)findViewById(R.id.ePedidoMin);
        eTiempoEnvio=(EditText)findViewById(R.id.eTiempoEnvio);
        imagenNegocio=(ImageView)findViewById(R.id.imagenNegocio);
        tCambiarImagen=(TextView)findViewById(R.id.tCambiarImagen);

        Bundle data = getIntent().getExtras();
        Email=data.getString("Email");
        Nombre = data.getString("Nombre");
        Propietario=data.getString("Propietario");
        Direccion=data.getString("Direccion");
        Celular=data.getString("Celular");
        Tipo=data.getString("Tipo");


        mStorage = FirebaseStorage.getInstance().getReference();

        tCambiarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        imagenNegocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("fotostiendas").child(Email);

            filePath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri urlFotoPerfil = taskSnapshot.getDownloadUrl();
                    foto=urlFotoPerfil;
                    Toast.makeText(Register3Activity.this, "Subida exitosa", Toast.LENGTH_SHORT).show();
                    Glide.with(Register3Activity.this)
                            .load(urlFotoPerfil)
                            .fitCenter()
                            .centerCrop()
                            .into(imagenNegocio);
                }
            })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register3Activity.this, "Subida fallo", Toast.LENGTH_SHORT).show();

                    }
                });



        }
    }

    public void bRegister(View view) {

        if(eCostoEnvio.getText().toString().isEmpty()){eCostoEnvio.setError("Complete this field");return;}
        if(ePedidoMin.getText().toString().isEmpty()){ePedidoMin.setError("Complete this field");return;}
        if(eTiempoEnvio.getText().toString().isEmpty()){eTiempoEnvio.setError("Complete this field");return;}
        int optLog = 0;
        SharedPreferences sharedPrefs = getSharedPreferences("SharedPreferencesVentas", this.MODE_PRIVATE);
        SharedPreferences.Editor editorSP= sharedPrefs.edit();
        editorSP.putString("Celular",Celular);
        editorSP.putInt("optLog",optLog);
        editorSP.commit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tiendas").child(Celular);

        nuevoUsuario=new NuevoUsuario(Email, Nombre, Propietario, Direccion, foto.toString(), Celular, Tipo, eCostoEnvio.getText().toString(), ePedidoMin.getText().toString(), eTiempoEnvio.getText().toString());
        myRef.setValue(nuevoUsuario);
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Register3Activity.this,LoginActivity.class);
        Toast.makeText(Register3Activity.this, "Usuario Creado Exitosamente", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}
