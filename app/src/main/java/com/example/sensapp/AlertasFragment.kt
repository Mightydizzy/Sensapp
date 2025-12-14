package com.example.sensapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlertasFragment : Fragment(R.layout.fragment_alertas) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerAlertas)

        // Layout vertical
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Datos de prueba por ahora (luego vendrán del ESP32/API)
        val datosPrueba = listOf(
            Alerta("10-11-2025", "12:41:03", "Patio", "PIR 01", "Advertencia"),
            Alerta("10-11-2025", "12:39:18", "Entrada", "PIR 02", "Crítico"),
            Alerta("10-11-2025", "12:30:55", "Escalera", "PIR 03", "Info")
        )

        recycler.adapter = AlertasAdapter(datosPrueba)
    }
}
