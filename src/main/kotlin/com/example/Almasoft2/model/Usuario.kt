package com.example.Almasoft2.model

data class Usuario(
    var usuario_id: Int? = null,
    var usuario_primer_nombre: String? = null,
    var usuario_primer_apellido: String? = null,
    var usuario_correo: String? = null,
    var usuario_telefono: String? = null,
    var usuario_documento: Long? = null,
    var usuario_credencial: String? = null
)
