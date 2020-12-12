package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etAPaterno, etAMaterno, etEdad, etEscolaridad, etCorreo, etFacebook;
    FloatingActionButton fabGuardar, fabListar;

    ProgressDialog progressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String updateId, updateNombre, updateApaterno, updateAmaterno, updateEdad, updateEscolaridad, updateCorreo, updateFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        etAPaterno = findViewById(R.id.etAPaterno);
        etAMaterno = findViewById(R.id.etAMaterno);
        etEdad = findViewById(R.id.etEdad);
        etEscolaridad = findViewById(R.id.etEscolaridad);
        etCorreo = findViewById(R.id.etCorreo);
        etFacebook = findViewById(R.id.etFacebook);


        fabGuardar = findViewById(R.id.fabGuardar);
        fabListar = findViewById(R.id.fabListar);

        progressDialog = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Agregar registro");


        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            actionBar.setTitle("Actualizar Datos");

            updateId = bundle.getString("updateId");
            updateNombre = bundle.getString("updateNombre");
            updateApaterno = bundle.getString("updateApaterno");
            updateAmaterno = bundle.getString("updateAmaterno");
            updateEdad = bundle.getString("updateEdad");
            updateEscolaridad = bundle.getString("updateEscolaridad");
            updateCorreo = bundle.getString("updateCorreo");
            updateFacebook = bundle.getString("updateFacebook");

            etNombre.setText(updateNombre);
            etAPaterno.setText(updateApaterno);
            etAMaterno.setText(updateAmaterno);
            etEdad.setText(updateEdad);
            etEscolaridad.setText(updateEscolaridad);
            etCorreo.setText(updateCorreo);
            etFacebook.setText(updateFacebook);

        } else {
            actionBar.setTitle("Agregar");
        }


        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String id = updateId;
                    String nombre = etNombre.getText().toString().trim();
                    String apaterno = etAPaterno.getText().toString().trim();
                    String amaterno = etAMaterno.getText().toString().trim();
                    String edad = etEdad.getText().toString().trim();
                    String escolaridad= etEscolaridad.getText().toString().trim();
                    String correo = etCorreo.getText().toString().trim();
                    String facebook = etFacebook.getText().toString().trim();


                    actualizarDatos(id, nombre, apaterno, amaterno, edad, escolaridad, correo, facebook);

                } else {
                    String nombre = etNombre.getText().toString().trim();
                    String apaterno = etAPaterno.getText().toString().trim();
                    String amaterno = etAMaterno.getText().toString().trim();
                    String edad = etEdad.getText().toString().trim();

                    String escolaridad = etEscolaridad.getText().toString().trim();
                    String corre = etCorreo.getText().toString().trim();
                    String facebook = etFacebook.getText().toString().trim();

                    cargarDatos(nombre, apaterno, amaterno, edad, escolaridad, corre, facebook);
                }
            }
        });


        fabListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivityPerson.class));
                finish();
            }
        });

    }


    private void cargarDatos(String nombre, String apaterno, String amaterno, String edad, String escolaridad, String correo, String facebook) {
        progressDialog.setTitle("Agregar datos");
        progressDialog.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("nombre", nombre);
        doc.put("apaterno", apaterno);
        doc.put("amaterno", amaterno);
        doc.put("edad", edad);
        doc.put("escolaridad", escolaridad);
        doc.put("correo", correo);
        doc.put("facebook", facebook);


        db.collection("Documents").document(id).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Datos almacenados con éxito...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Ha ocurrido un error..." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void actualizarDatos(String id, String nombre, String apaterno, String amaterno, String edad, String escolaridad, String correo, String facebook) {
        progressDialog.setTitle("Actualizando datos a Firebase");
        progressDialog.show();

        /*
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("nombre", nombre);
        doc.put("apaterno", apaterno);
        doc.put("amaterno", amaterno);
        doc.put("sexo", sexo);
        doc.put("direccion", direccion);
        doc.put("facebook", facebook);
        doc.put("instagram", instagram);

         */


        db.collection("Documents")
                .document(id).update(
                "nombre", nombre,
                "apaterno", apaterno,
                "amaterno", amaterno,
                "edad", edad,
                "escolaridad", escolaridad,
                "correo", correo,
                "facebook", facebook
        )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Actualización exitosa...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Ha ocurrido un error..." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}