package com.example.sensapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlertasAdapter(
    private val items: List<Alerta>
) : RecyclerView.Adapter<AlertasAdapter.AlertaViewHolder>() {

    class AlertaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFechaHora: TextView = view.findViewById(R.id.txtFechaHora)
        val txtLugarSensor: TextView = view.findViewById(R.id.txtLugarSensor)
        val txtNivel: TextView = view.findViewById(R.id.txtNivel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta, parent, false)
        return AlertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertaViewHolder, position: Int) {
        val alerta = items[position]
        holder.txtFechaHora.text = "${alerta.fecha} ${alerta.hora}"
        holder.txtLugarSensor.text = "${alerta.lugar} · ${alerta.sensor}"
        holder.txtNivel.text = "Nivel: ${alerta.nivel}"
    }

    override fun getItemCount(): Int = items.size
}
