package com.example.cronograma.model

data class Contrato(
    val contrato_id: Int? = null,
    val contrato_estado: Boolean,
    val contrato_valor: Double,
    val cliente_id: Int
)