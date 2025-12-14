package com.example.sensapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class UsuarioFragment : Fragment(R.layout.fragment_usuario) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreoUsuario)
        val txtUid    = view.findViewById<TextView>(R.id.txtUidUsuario)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val user = auth.currentUser
        if (user != null) {
            txtCorreo.text = user.email ?: "—"
            txtUid.text    = user.uid
        } else {
            txtCorreo.text = "Sin sesión"
            txtUid.text    = "—"
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}
