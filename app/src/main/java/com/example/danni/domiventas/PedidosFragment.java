package com.example.danni.domiventas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosFragment extends Fragment {
    private ListView listaProdAgrupados;

    private String Celular;
    private FirebaseDatabase database;

    private ArrayList<Pedido_Usuario> pedido_usuario, pedidos_generales;

    public PedidosFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_pedidos, container, false);
        listaProdAgrupados=(ListView)view.findViewById(R.id.listaPedidosAgrupados);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("SharedPreferencesVentas", getActivity().MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular","vacio");

        pedido_usuario=new ArrayList<Pedido_Usuario>();
        pedidos_generales=new ArrayList<Pedido_Usuario>();

        final Adapter adapter = new Adapter(getActivity(),pedidos_generales);
        listaProdAgrupados.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pedidos_Tienda").child(Celular);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos_generales.clear();
                for(DataSnapshot padre : dataSnapshot.getChildren()){
                    for(DataSnapshot hijito : padre.getChildren()){
                        pedido_usuario.clear();
                        int cantidadTotal=0, precioTotal=0;
                        for(DataSnapshot hijo: hijito.getChildren()){


                            pedido_usuario.add(hijo.getValue(Pedido_Usuario.class));

                        }
                        for(int i=0;i<pedido_usuario.size();i++) {

                            cantidadTotal+=Integer.valueOf(pedido_usuario.get(i).getCantidad());
                            precioTotal+=Integer.valueOf(pedido_usuario.get(i).getTotal());
                        }
                        String totales = Integer.toString(precioTotal);
                        String cantidades = Integer.toString(cantidadTotal);
                        //Poner Numero de pedido TAMBIEN
                        pedidos_generales.add(new Pedido_Usuario(padre.getKey(),cantidades,totales,hijito.getKey()));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listaProdAgrupados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PedidoActivity.class);
                intent.putExtra("ClienteId",pedidos_generales.get(i).getNombre());
                intent.putExtra("Items",pedidos_generales.get(i).getCantidad());
                intent.putExtra("Total",pedidos_generales.get(i).getTotal());
                intent.putExtra("Pedido",pedidos_generales.get(i).getPedido());

                getActivity().startActivity(intent);
            }
        });


        return view; 
    }
    class Adapter extends ArrayAdapter<Pedido_Usuario> {
        public Adapter(Context context, ArrayList<Pedido_Usuario> pedidos) {
            super(context, R.layout.lista_pedido_general,pedidos);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(getContext());
            View item =inflater.inflate(R.layout.lista_pedido_general,null);

            Pedido_Usuario pedido = getItem(position);

            TextView tNombre=(TextView)item.findViewById(R.id.tNombre);
            tNombre.setText(pedido.getNombre());
            TextView tCantidad=(TextView)item.findViewById(R.id.tCantidad);
            tCantidad.setText(pedido.getCantidad());
            TextView tTotal=(TextView)item.findViewById(R.id.tTotal);
            tTotal.setText(pedido.getTotal());
            return item;
        }

    }

}


/* database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pedidos_Tienda").child(Celular);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot padre : dataSnapshot.getChildren()){
                    for(DataSnapshot hijito : padre.getChildren()){
                        pedido_usuario.clear();
                        int cantidadTotal=0, precioTotal=0;
                        for(DataSnapshot hijo: hijito.getChildren()){
                            pedido_usuario.add(hijo.getValue(Pedido_Usuario.class));
                        }
                        for(int i=0;i<pedido_usuario.size();i++) {
                            cantidadTotal+=Integer.valueOf(pedido_usuario.get(i).getCantidad());
                            precioTotal+=Integer.valueOf(pedido_usuario.get(i).getTotal());
                        }
                        String totales = Integer.toString(precioTotal);
                        String cantidades = Integer.toString(cantidadTotal);
                        //Poner Numero de pedido TAMBIEN
                        pedidos_generales.add(new Pedido_Usuario(padre.getKey(),cantidades,totales,hijito.getKey()));
                    }
                    }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
