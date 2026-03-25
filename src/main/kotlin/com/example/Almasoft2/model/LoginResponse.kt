package com.example.Almasoft2.model

data class LoginResponse(
    val token: String,
    val rol: String,
    val usuario_id: Int?,

    val usuario_primer_nombre: String,
    val usuario_segundo_nombre: String,
    val usuario_primer_apellido: String,
    val usuario_segundo_apellido: String,
    val usuario_correo: String
)