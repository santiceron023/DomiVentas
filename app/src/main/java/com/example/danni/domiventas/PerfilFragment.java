package com.example.danni.domiventas;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private String Celular;
    private Uri foto;
    private NuevoUsuario infoUsuario;
    private StorageReference storageRef,storageReference,storageRef3;

    private Button bEditar, bEditar2;
    private LinearLayout lyVistaPerfil, lyVistaEditar;
    private TextView eNombre, ePropietario, eTipo, eDireccion,eCostoEnvio,ePedidoMin, eTiempo,tCelular,tEmail;
    private TextView tNombre,tPropietario,tTipo,tDireccion,tCostoEnvio,tPedidoMin,tTiempo;
    private CircleImageView imageView2;
    private static final int GALLERY_INTENT=1;
    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

      View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        lyVistaEditar=(LinearLayout)view.findViewById(R.id.lyVistaEditar);
        lyVistaPerfil=(LinearLayout)view.findViewById(R.id.lyVistaPerfil);

        lyVistaPerfil.setVisibility(View.VISIBLE);
        lyVistaEditar.setVisibility(View.GONE);

        storageRef3= FirebaseStorage.getInstance().getReference();

        eNombre= (TextView)view.findViewById(R.id.eNombre);
        ePropietario =(TextView)view.findViewById(R.id.ePropietario);
        eTipo = (TextView)view.findViewById(R.id.eTipo);
        eDireccion = (TextView)view.findViewById(R.id.eDireccion);
        eCostoEnvio = (TextView)view.findViewById(R.id.eCostoEnvio);
        ePedidoMin = (TextView)view.findViewById(R.id.ePedidoMin);
        eTiempo=(TextView)view.findViewById(R.id.eTiempo);
        tCelular= (TextView)view.findViewById(R.id.tCelular);
        tEmail=(TextView)view.findViewById(R.id.tEmail);

        tNombre= (TextView)view.findViewById(R.id.tNombre);
        tPropietario =(TextView)view.findViewById(R.id.tPropietario);
        tTipo = (TextView)view.findViewById(R.id.tTipo);
        tDireccion = (TextView)view.findViewById(R.id.tDireccion);
        tCostoEnvio = (TextView)view.findViewById(R.id.tCostoEnvio);
        tPedidoMin = (TextView)view.findViewById(R.id.tPedidoMin);
        tTiempo=(TextView)view.findViewById(R.id.tTiempo);


        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("SharedPreferencesVentas", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editorSP= sharedPrefs.edit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Celular = user.getDisplayName();
            editorSP.putString("Celular",Celular);
            editorSP.commit();

            //INFORMACION DE PERFIL EN EL NAVDRAWER
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Tiendas").child(Celular);

            infoUsuario=new NuevoUsuario();


            final CircleImageView imageView = (CircleImageView)view.findViewById(R.id.imageView);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    infoUsuario=dataSnapshot.getValue(NuevoUsuario.class);

                    tNombre.setText(infoUsuario.getNombre());
                    tPropietario.setText(infoUsuario.getPropietario());
                    tTipo.setText(infoUsuario.getTipo());
                    tCelular.setText(infoUsuario.getCelular());
                    tDireccion.setText(infoUsuario.getDireccion());
                    tEmail.setText(infoUsuario.getEmail());
                    tCostoEnvio.setText(infoUsuario.getCostoEnvio());
                    tPedidoMin.setText(infoUsuario.getPedidoMin());
                    tTiempo.setText(infoUsuario.getTiempoEnvio());

                    foto=Uri.parse(infoUsuario.getFoto());

                    storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(infoUsuario.getFoto());

                    Glide.with(getContext())
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .into(imageView);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{

        }
        bEditar = (Button)view.findViewById(R.id.bEditar);
        bEditar2 =(Button)view.findViewById(R.id.bEditar2);
        imageView2 = (CircleImageView)view.findViewById(R.id.imageView2);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyVistaPerfil.setVisibility(View.GONE);
                lyVistaEditar.setVisibility(View.VISIBLE);

                eNombre.setText(infoUsuario.getNombre());
                ePropietario.setText(infoUsuario.getPropietario());
                eTipo.setText(infoUsuario.getTipo());
                eTiempo.setText(infoUsuario.getTiempoEnvio());
                eDireccion.setText(infoUsuario.getDireccion());
                eCostoEnvio.setText(infoUsuario.getCostoEnvio());
                ePedidoMin.setText(infoUsuario.getPedidoMin());

                storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(infoUsuario.getFoto());

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(imageView2);

                //INFORMACION DE PERFIL EN EL NAVDRAWER

            }
        });

        bEditar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eNombre.getText().toString().isEmpty()){eNombre.setError("Complete this field");return;}
                if(ePropietario.getText().toString().isEmpty()){ePropietario.setError("Complete this field");return;}
                if(eTiempo.getText().toString().isEmpty()){eTiempo.setError("Complete this field");return;}
                if(eDireccion.getText().toString().isEmpty()){eDireccion.setError("Complete this field");return;}
                if(eTipo.getText().toString().isEmpty()){eTipo.setError("Complete this field");return;}
                if(ePedidoMin.getText().toString().isEmpty()){ePedidoMin.setError("Complete this field");return;}
                if(eCostoEnvio.getText().toString().isEmpty()){eCostoEnvio.setError("Complete this field");return;}

                lyVistaPerfil.setVisibility(View.VISIBLE);
                lyVistaEditar.setVisibility(View.GONE);



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Tiendas").child(Celular);
                infoUsuario=new NuevoUsuario(tEmail.getText().toString(),eNombre.getText().toString(),ePropietario.getText().toString(),eDireccion.getText().toString(),foto.toString(),Celular,eTipo.getText().toString(), eCostoEnvio.getText().toString(),ePedidoMin.getText().toString(),eTiempo.getText().toString());
                myRef.setValue(infoUsuario);
               // storageRef=FirebaseStorage.getInstance().getReferenceFromUrl(infoUsuario.getFoto());
                //storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(infoUsuario.getFoto());
                Toast.makeText(getContext(),"Perfil Actualizado", Toast.LENGTH_SHORT).show();


            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            //storageRef= FirebaseStorage.getInstance().getReference();
            StorageReference filePath = storageRef.child(infoUsuario.getEmail()+"edit"); //.child("fotostiendas").child(infoUsuario.getEmail());
            Toast.makeText(getContext(), "Subiendo imagen ...", Toast.LENGTH_SHORT).show();
            filePath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri urlFotoProducto = taskSnapshot.getDownloadUrl();
                            foto=urlFotoProducto;
                            Toast.makeText(getContext(), "Subida exitosa", Toast.LENGTH_SHORT).show();
                            Glide.with(getActivity())
                                    .load(urlFotoProducto)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imageView2);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Subida fallo", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

}
