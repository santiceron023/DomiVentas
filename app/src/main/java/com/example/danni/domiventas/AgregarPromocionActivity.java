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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarPromocionActivity extends AppCompatActivity {
    private EditText eNombreProducto, eMarca, ePrecioAntes, ePrecioAhora , eCantidad;
    private ImageView imagenProducto;
    private StorageReference mStorage;
    private String Celular;
    private Uri foto;
    private NuevaPromo nuevaPromo;
    private PromoGeneral promoGeneral;
    private static final int GALLERY_INTENT=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_promocion);
        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        eNombreProducto=(EditText)findViewById(R.id.eNombreProducto);
        eMarca = (EditText)findViewById(R.id.eMarca);
        ePrecioAntes =(EditText)findViewById(R.id.ePrecioAntes);
        ePrecioAhora =(EditText)findViewById(R.id.ePrecioAhora);
        eCantidad=(EditText)findViewById(R.id.eCantidad);
        imagenProducto=(ImageView)findViewById(R.id.imagenProducto);

        mStorage = FirebaseStorage.getInstance().getReference();

        SharedPreferences sharedPrefs = getSharedPreferences("SharedPreferencesVentas", this.MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular", "no_cargo_celular");
    }
    public void addProducto(View view) {
        if(eNombreProducto.getText().toString().isEmpty()){eNombreProducto.setError("Complete this field");return;}
        if(eMarca.getText().toString().isEmpty()){eMarca.setError("Complete this field");return;}
        if(ePrecioAntes.getText().toString().isEmpty()){ePrecioAntes.setError("Complete this field");return;}
        if(ePrecioAhora.getText().toString().isEmpty()){ePrecioAhora.setError("Complete this field");return;}
        if(eCantidad.getText().toString().isEmpty()){eCantidad.setError("Complete this field");return;}

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Promociones").child(Celular).child(eNombreProducto.getText().toString());

        nuevaPromo=new NuevaPromo(foto.toString(),eMarca.getText().toString(), eNombreProducto.getText().toString(),ePrecioAhora.getText().toString(),
                ePrecioAntes.getText().toString(), eCantidad.getText().toString());

        myRef.setValue(nuevaPromo);


        DatabaseReference myRef2 = database.getReference("Tiendas").child(Celular).child("nombre");
        final DatabaseReference myRef3 = database.getReference("TodasPromociones").child(eNombreProducto.getText().toString());
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                promoGeneral=new PromoGeneral(foto.toString(),eMarca.getText().toString(), eNombreProducto.getText().toString(),ePrecioAhora.getText().toString(),
                        ePrecioAntes.getText().toString(), eCantidad.getText().toString(), value);
                myRef3.setValue(promoGeneral);

                eMarca.setText("");
                eNombreProducto.setText("");
                ePrecioAntes.setText("");
                ePrecioAhora.setText("");
                eCantidad.setText("");
                imagenProducto.setImageResource(R.drawable.ic_image_black_24dp);
                Toast.makeText(AgregarPromocionActivity.this, "Promoción Agregada Exitosamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("fotospromociones").child(Celular).child(eNombreProducto.getText().toString());
            Toast.makeText(AgregarPromocionActivity.this, "Subiendo imagen ...", Toast.LENGTH_SHORT).show();
            filePath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri urlFotoProducto = taskSnapshot.getDownloadUrl();
                            foto=urlFotoProducto;
                            Toast.makeText(AgregarPromocionActivity.this, "Subida exitosa", Toast.LENGTH_SHORT).show();
                            Glide.with(AgregarPromocionActivity.this)
                                    .load(urlFotoProducto)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imagenProducto);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AgregarPromocionActivity.this, "Subida fallo", Toast.LENGTH_SHORT).show();

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


