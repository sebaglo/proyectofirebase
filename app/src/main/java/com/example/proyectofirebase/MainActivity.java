
package com.example.proyectofirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Declaramos variables
    private EditText txtCodigo, txtNombre,txtDueño, txtDireccion;
    private ListView lista;
    private Spinner spMascota;

    //variable de la conexion a base de datos
    private FirebaseFirestore db;

    //Datos del Spinner
    String[] TiposMasctas = {"Perro", "Gato", "Pajaro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //LLamamos al metodo que cargar la lista
        CargarListaFirestore();

        //Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        //Unimos las variables con los Id de XML
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtDueño = findViewById(R.id.txtDueño);
        txtDireccion = findViewById(R.id.txtDireccion);
        spMascota = findViewById(R.id.spMascota);
        lista = findViewById(R.id.lista);

        //Poblar Spinner de los Tipos de Mascotas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spMascota.setAdapter(adapter);
        }

        //Metodo Enviar lista
    public void enviarDatosFirestore(View view){
        //Obtenemos los campos ingresados en el Formulario
        String codigo = txtCodigo.getText().toString();
        String nombre = txtNombre.getText().toString();
        String dueño = txtDueño.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String tipomascota = spMascota.getSelectedItem().toString();

        //Creamos un mapa con los datos a enviar
        Map<String, Object> mascota = new HashMap<>();
        mascota.put("codigo", codigo);
        mascota.put("nombre", nombre);
        mascota.put("dueño", dueño);
        mascota.put("direccion", direccion);
        mascota.put("tipomascota", tipomascota);

        //Enviamos los datos a firestore
        db.collection("mascotas")
                .document(codigo)
                .set(mascota)
                .addOnSuccessListener(aVoid ->{
                    Toast.makeText(MainActivity.this, "Datos enviados a Firestore Correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(e ->{
                    Toast.makeText(MainActivity.this, "Error al enviar los datos a Firestore" + e.getClass(), Toast.LENGTH_SHORT).show();
                });
    }

    //boton que carga la lista
    public void CargarLista(View view){
        CargarListaFirestore();
    }

        //Metodo cargar lista
    public void CargarListaFirestore(){
        //Obtenemos la instancia de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Hacemos una consulta a la coleccion llamada mascotas
        db.collection("msacotas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            //si la consulta es exitosa, procedera los documentos obtenidos
                            //creando una lista para almacenar las cadenas de informacion de mascotas
                            List<String> listaMascotas = new ArrayList<>();

                            //Recorre todos los datos obtenidos ordenandolos en la lista
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String linea = "||" + document.getString("codigo") + "||" +
                                        document.getString("nombre") + "||" +
                                        document.getString("dueño") + "||" +
                                        document.getString("direccion");
                                listaMascotas.add(linea);
                            }

                            //Crear un ArrayAdapter con la lista de mascotas
                            //y establece el adaptador en el ListView
                            ArrayAdapter<String> adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listaMascotas);
                            lista.setAdapter(adaptador);
                        } else{
                            //se imprimiria en consola si ahi errores al traer los datos
                            Log.e("TAG", "Error al obtener los datos de la Firestore", task.getException());
                        }
                    }
                });
    }
}
