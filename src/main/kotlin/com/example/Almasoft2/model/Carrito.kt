package com.example.cronograma.model

data class Carrito(
    val carrito_id: Int? = null,
    val usuario_id: Int,
    val producto_id: Int,
    val cantidad: Int,
    val precio_unitario: Double
)