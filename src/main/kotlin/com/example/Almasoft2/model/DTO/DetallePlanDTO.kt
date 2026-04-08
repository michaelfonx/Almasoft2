package com.example.cronograma.model.DTO

import com.example.cronograma.model.Plan
import com.example.Almasoft2.model.Servicio
import com.example.cronograma.model.Producto

data class DetallePlanDTO(
    val plan: Plan,
    val servicios: List<Servicio>,
    val productos: List<Producto>
)