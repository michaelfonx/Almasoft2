package com.example.cronograma.model

data class Producto(
    val producto_id: Int? = null,
    val producto_nombre: String,
    val producto_descripcion: String,
    val producto_precio: Double,
    val producto_stock: Int,
    val producto_estado: Boolean,
    val subcategoria_id: Int
)