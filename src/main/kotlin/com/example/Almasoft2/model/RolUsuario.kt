package com.example.Almasoft2.model

data class RolUsuario(
    val cred_id: Int? = null,
    val rol_id: Int,
    val usuario_id: Int,
    val estado_cred: Boolean = true
)