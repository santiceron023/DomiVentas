package com.example.danni.domiventas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PedidoActivity extends AppCompatActivity {
    private ListView listaPedido;
    private ArrayList<Pedido_Usuario> productos;
    private UsuarioCliente cliente;
    private String ClienteId,Items,Total, numeroPedido,Celular;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        listaPedido = (ListView)findViewById(R.id.listaPedido);

        //Desde el main
        intent = new Intent(getApplicationContext(),NavDrawerActivity.class);
        Bundle extras = getIntent().getExtras();
        ClienteId = extras.getString("ClienteId");
        Items = extras.getString("Items");
        Total = extras.getString("Total");
        numeroPedido=extras.getString("Pedido");

        TextView tNumPedido =(TextView)findViewById(R.id.tNumPedido);
        TextView tItems = (TextView)findViewById(R.id.tItemsPedido);
        TextView tTotalPedido = (TextView)findViewById(R.id.tTotalPedido);

        tNumPedido.setText(numeroPedido);
        tItems.setText(Items);
        tTotalPedido.setText(Total);

        setTitle(ClienteId);

        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPrefs = getSharedPreferences("SharedPreferencesVentas",MODE_PRIVATE);
        Celular = sharedPrefs.getString("Celular","no cargo celular");

        productos = new ArrayList<Pedido_Usuario>();
        final Adapter adapter = new Adapter(getApplicationContext(), productos);
        listaPedido.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pedidos_Tienda").child(Celular).child(ClienteId).child(numeroPedido);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot padre: dataSnapshot.getChildren()){
                    productos.add(padre.getValue(Pedido_Usuario.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference myRef2 = database.getReference("Usuarios").child(ClienteId);

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                      cliente = dataSnapshot.getValue(UsuarioCliente.class);
                    TextView tNombreCliente = (TextView)findViewById(R.id.tNombreCliente);
                    tNombreCliente.setText(cliente.getNombre());
                    TextView tCelularCliente = (TextView)findViewById(R.id.tCelularCliente);
                    tCelularCliente.setText(cliente.getCelular());
                    TextView tDireccionCliente=(TextView)findViewById(R.id.tDireccionCliente);
                    tDireccionCliente.setText(cliente.getUbicacion());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    class Adapter extends ArrayAdapter<Pedido_Usuario>{
        public Adapter(Context context, ArrayList<Pedido_Usuario> pedidos) {
            super(context, R.layout.lista_pedido_general,pedidos);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(getContext());
            View item =inflater.inflate(R.layout.lista_pedido_general,null);

            Pedido_Usuario pedido = getItem(position);

            TextView tTotalPedidos = (TextView)item.findViewById(R.id.tTotalPedidos);
            tTotalPedidos.setText("Precio Productos:");
            TextView tCliente = (TextView)item.findViewById(R.id.tCliente);
            tCliente.setText("Nombre Producto:");

            TextView tNombre=(TextView)item.findViewById(R.id.tNombre);
            tNombre.setText(pedido.getNombre());
            TextView tCantidad=(TextView)item.findViewById(R.id.tCantidad);
            tCantidad.setText(pedido.getCantidad());
            TextView tTotal=(TextView)item.findViewById(R.id.tTotal);
            tTotal.setText(pedido.getTotal());
            return item;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void bOk(View view){
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pedidos_Cliente").child(ClienteId).
                child(Celular).child(numeroPedido);
        myRef.removeValue();
        myRef = database.getReference("Pedidos_Tienda").child(Celular).
                child(ClienteId).child(numeroPedido);
        myRef.removeValue();
        startActivity(intent);
        finish();

    }
}
