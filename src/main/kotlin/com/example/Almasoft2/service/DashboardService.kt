package com.example.Almasoft2.service

import com.example.Almasoft2.model.DashboardClienteResponse
import com.example.Almasoft2.service.ServicioService
import com.example.cronograma.service.ContratoService
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val contratoService: ContratoService,
    private val servicioService: ServicioService
) {

    fun obtenerDashboard(usuarioId: Int): DashboardClienteResponse {

        val contrato = contratoService.obtenerContratoPorCliente(usuarioId)

        val servicios = servicioService.listarServicios()

        val afiliados = emptyList<com.example.Almasoft2.model.Usuario>()

        val pagos = listOf("Sin pagos aún")

        return DashboardClienteResponse(
            contrato,
            servicios,
            afiliados,
            pagos
        )
    }
}