package com.example.sensapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Si no hay usuario logueado → LoginActivity
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val irAUsuario = intent.getBooleanExtra("irAUsuario", false)

        if (savedInstanceState == null) {
            if (irAUsuario) {
                bottomNav.selectedItemId = R.id.nav_usuario
                openFragment(UsuarioFragment())
            } else {
                bottomNav.selectedItemId = R.id.nav_inicio
                openFragment(InicioFragment())
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio  -> openFragment(InicioFragment())
                R.id.nav_alertas -> openFragment(AlertasFragment())
                R.id.nav_config  -> openFragment(ConfigFragment())
                R.id.nav_usuario -> openFragment(UsuarioFragment())
                R.id.nav_info    -> openFragment(InfoFragment())
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // 👈 USA SIEMPRE R.id...
            .commit()
    }
}
