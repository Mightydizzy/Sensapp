package com.example.sensapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_GOOGLE_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Si ya está logueado, pasa directo a Main
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val email   = findViewById<EditText>(R.id.loginEmail)
        val pass    = findViewById<EditText>(R.id.loginPass)
        val btn     = findViewById<Button>(R.id.btnLogin)
        val irReg   = findViewById<TextView>(R.id.btnIrRegistro)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)

        // ---------- Login con correo/contraseña ----------
        btn.setOnClickListener {
            val e = email.text.toString().trim()
            val p = pass.text.toString().trim()

            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al iniciar sesión: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // ---------- Ir a registro ----------
        irReg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // ---------- Configurar Google Sign-In ----------
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }
    }

    @Deprecated("onActivityResult está deprecated, pero nos sirve para este proyecto")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Error al iniciar con Google: ${t.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } catch (e: ApiException) {
                Toast.makeText(this, "Inicio con Google cancelado o fallido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
