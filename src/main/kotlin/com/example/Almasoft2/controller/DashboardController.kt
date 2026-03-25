package com.example.Almasoft2.controller

import com.example.Almasoft2.model.DashboardClienteResponse
import com.example.Almasoft2.service.DashboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(

    private val dashboardService: DashboardService

) {

    @GetMapping("/{usuarioId}")
    fun obtenerDashboard(
        @PathVariable usuarioId: Int
    ): DashboardClienteResponse {

        return dashboardService.obtenerDashboard(usuarioId)
    }
}