package com.example.danni.domiventas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromocionesFragment extends Fragment {
    private ListView listaPromos;
    private ArrayList<NuevaPromo> promociones;
    private StorageReference storageRef;
    private String Celular;


    public PromocionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promociones, container, false);

        listaPromos=(ListView)view.findViewById(R.id.listaPromos);
        promociones=new ArrayList<NuevaPromo>();
        final Adapter adapter = new Adapter(getActivity(),promociones);
        listaPromos.setAdapter(adapter);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("SharedPreferencesVentas", getActivity().MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular", "no_cargo_celular");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Promociones").child(Celular);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot promocionesSnapshot:dataSnapshot.getChildren()){
                    promociones.add(promocionesSnapshot.getValue(NuevaPromo.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgregarPromocionActivity.class);
                startActivity(intent);
                // getActivity().finish();
            }
        });
        return view;
    }

    class Adapter extends ArrayAdapter<NuevaPromo> {

        public Adapter(Context context, ArrayList<NuevaPromo> promociones) {
            super(context, R.layout.lista_promociones, promociones);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_promociones,null);
            NuevaPromo promocion = getItem(position);

            TextView tNombreProducto = (TextView)item.findViewById(R.id.tNombreProducto);
            tNombreProducto.setText(promocion.getNombre());
            TextView tCantidad = (TextView)item.findViewById(R.id.tCantidad);
            tCantidad.setText(promocion.getTamaño());
            TextView tMarca=(TextView)item.findViewById(R.id.tMarca);
            tMarca.setText(promocion.getMarca());
            TextView tPrecioAntes = (TextView)item.findViewById(R.id.tPrecioAntes);
            tPrecioAntes.setText(promocion.getPrecio_antes());
            TextView tPrecioAhora = (TextView)item.findViewById(R.id.tPrecioAhora);
            tPrecioAhora.setText(promocion.getPrecio_ahora());

            ImageView imProducto = (ImageView)item.findViewById(R.id.imProducto);

            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(promocion.getFoto());

            Glide.with(item.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into(imProducto);

            return item;

        }
    }
}
