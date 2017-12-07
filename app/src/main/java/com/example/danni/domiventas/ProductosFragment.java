package com.example.danni.domiventas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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

import static com.example.danni.domiventas.R.id.fab;
import static com.example.danni.domiventas.R.id.view;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductosFragment extends Fragment {
    private ListView listaProductos;
    private ArrayList<NuevoProducto> productos;
    private StorageReference storageRef;
    private String Celular;

    public ProductosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_productos, container, false);

        listaProductos=(ListView) view.findViewById(R.id.listaProductos);

        productos=new ArrayList<NuevoProducto>();
        final Adapter adapter = new Adapter(getActivity(),productos);
        listaProductos.setAdapter(adapter);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("SharedPreferencesVentas", getActivity().MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular", "no_cargo_celular");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Productos").child(Celular);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot productosSnapshot:dataSnapshot.getChildren()){
                    productos.add(productosSnapshot.getValue(NuevoProducto.class));
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
                Intent intent = new Intent(getActivity(), AgregarProductoActivity.class);
                startActivity(intent);
               // getActivity().finish();
            }
        });

        return view;
    }

    class Adapter extends ArrayAdapter<NuevoProducto>{

        public Adapter(Context context, ArrayList<NuevoProducto> productos) {
            super(context, R.layout.lista_productos, productos);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_productos,null);
            NuevoProducto producto = getItem(position);

            TextView tNombreProducto = (TextView)item.findViewById(R.id.tNombreProducto);
            tNombreProducto.setText(producto.getNombre());
            TextView tCantidad = (TextView)item.findViewById(R.id.tCantidad);
            tCantidad.setText(producto.getTamaño());
            TextView tMarca=(TextView)item.findViewById(R.id.tMarca);
            tMarca.setText(producto.getMarca());
            TextView tPrecio = (TextView)item.findViewById(R.id.tPrecio);
            tPrecio.setText(producto.getPrecio());

            ImageView imProducto = (ImageView)item.findViewById(R.id.imProducto);

            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(producto.getFoto());

            Glide.with(item.getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .into(imProducto);

            return item;

        }
    }

}
