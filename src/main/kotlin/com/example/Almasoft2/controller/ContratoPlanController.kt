package com.example.cronograma.controller

import com.example.cronograma.model.DTO.MiPlanDTO
import com.example.cronograma.service.ContratoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/contrato-plan")
class ContratoPlanController(
    private val contratoService: ContratoService
) {

    @GetMapping("/mi-plan/{id}")
    fun obtenerMiPlan(@PathVariable id: Int): MiPlanDTO {

        val resultado = contratoService.obtenerMiPlan(id)

        return resultado ?: MiPlanDTO(
            contrato_id = 0,
            plan_nombre = "Sin nombre",
            plan_precio = 0.0,
            plan_descripcion = "Sin descripción",
            servicios = emptyList(),
            productos = emptyList(),
            pagos = emptyList()
        )
    }
}