package com.example.Almasoft2.model

data class Usuario(
    val usuario_id: Int? = null,
    val usuario_primer_nombre: String,
    val usuario_segundo_nombre: String?,
    val usuario_primer_apellido: String,
    val usuario_segundo_apellido: String?,
    val usuario_documento: Int,
    val usuario_correo: String,
    val usuario_direccion: String,
    val usuario_credencial: String
)
