package com.example.danni.domiventas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarProductoActivity extends AppCompatActivity {
    private EditText eNombreProducto, eMarca, ePrecio, eCantidad;
    private ImageView imagenProducto;
    private StorageReference mStorage;
    private String Celular;
    private Uri foto;
    private NuevoProducto nuevoProducto;
    private static final int GALLERY_INTENT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        eNombreProducto=(EditText)findViewById(R.id.eNombreProducto);
        eMarca = (EditText)findViewById(R.id.eMarca);
        ePrecio =(EditText)findViewById(R.id.ePrecio);
        eCantidad=(EditText)findViewById(R.id.eCantidad);
        imagenProducto=(ImageView)findViewById(R.id.imagenProducto);

        mStorage = FirebaseStorage.getInstance().getReference();





        SharedPreferences sharedPrefs = getSharedPreferences("SharedPreferencesVentas", this.MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular", "no_cargo_celular");
    }

    public void addProducto(View view) {
        if(eNombreProducto.getText().toString().isEmpty()){eNombreProducto.setError("Complete this field");return;}
        if(eMarca.getText().toString().isEmpty()){eMarca.setError("Complete this field");return;}
        if(ePrecio.getText().toString().isEmpty()){ePrecio.setError("Complete this field");return;}
        if(eCantidad.getText().toString().isEmpty()){eCantidad.setError("Complete this field");return;}

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Productos").child(Celular).child(eNombreProducto.getText().toString());

        nuevoProducto=new NuevoProducto(eMarca.getText().toString(), eNombreProducto.getText().toString(),
                                        ePrecio.getText().toString(), eCantidad.getText().toString(),foto.toString());
        myRef.setValue(nuevoProducto);
        eMarca.setText("");
        eNombreProducto.setText("");
        ePrecio.setText("");
        eCantidad.setText("");
        imagenProducto.setImageResource(R.drawable.ic_image_black_24dp);
        Toast.makeText(AgregarProductoActivity.this, "Producto Agregado Exitosamente", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("fotosproductos").child(Celular).child(eNombreProducto.getText().toString());
            Toast.makeText(AgregarProductoActivity.this, "Subiendo imagen ...", Toast.LENGTH_SHORT).show();
            filePath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri urlFotoProducto = taskSnapshot.getDownloadUrl();
                            foto=urlFotoProducto;
                            Toast.makeText(AgregarProductoActivity.this, "Subida exitosa", Toast.LENGTH_SHORT).show();
                            Glide.with(AgregarProductoActivity.this)
                                    .load(urlFotoProducto)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imagenProducto);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AgregarProductoActivity.this, "Subida fallo", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    public void addImagen(View view) {
        if(eNombreProducto.getText().toString().isEmpty()){eNombreProducto.setError("Complete este campo antes de subir una imagen");return;}
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
