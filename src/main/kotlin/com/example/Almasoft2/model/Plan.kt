package com.example.cronograma.model

data class Plan(
    val plan_id: Int? = null,
    val plan_nombre: String,
    val plan_precio: Double,
    val plan_estado: Boolean
)