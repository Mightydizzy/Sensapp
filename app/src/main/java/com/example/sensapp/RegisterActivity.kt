package com.example.sensapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val correo   = findViewById<EditText>(R.id.userEmail)
        val pass     = findViewById<EditText>(R.id.userClave)
        val btnCrear = findViewById<Button>(R.id.userCrear)

        btnCrear.setOnClickListener {
            val email = correo.text.toString().trim()
            val pwd   = pass.text.toString().trim()

            if (email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Correo y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuario creado. Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show()
                        finish() // vuelve al LoginActivity
                    } else {
                        Toast.makeText(
                            this,
                            "Error al crear usuario: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}
