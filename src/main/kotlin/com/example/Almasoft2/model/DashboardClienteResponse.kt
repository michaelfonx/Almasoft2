package com.example.Almasoft2.model

import com.example.cronograma.model.Contrato
import com.example.Almasoft2.model.Usuario

data class DashboardClienteResponse(

    val contrato: Contrato?,
    val servicios: List<Servicio>,
    val afiliados: List<Usuario>,
    val pagos: List<String> // temporal
)
