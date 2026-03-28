package com.example.cronograma.model.DTO

import com.example.cronograma.dto.PagoDTO

data class MiPlanDTO(
    val contrato_id: Int? = null,
    val plan_nombre: String?,
    val plan_precio: Double?,
    val plan_descripcion: String?,
    val servicios: List<String>?,
    val productos: List<String>?,
    val pagos: List<PagoDTO>?
)

