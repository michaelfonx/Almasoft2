package com.example.cronograma.dto

data class MiPlanDTO(
    val contrato_id: Int,
    val plan_nombre: String,
    val plan_precio: Double,
    val plan_descripcion: String,
    val servicios: List<String>,
    val productos: List<String>,
    val pagos: List<PagoDTO>
)