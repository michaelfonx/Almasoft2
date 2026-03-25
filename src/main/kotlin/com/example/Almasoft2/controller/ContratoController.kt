package com.example.cronograma.controller

import com.example.cronograma.model.Contrato
import com.example.cronograma.dto.MiPlanDTO
import com.example.cronograma.service.ContratoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ContratoController(
    private val contratoService: ContratoService
) {

    @GetMapping("/contratos")
    fun verContratos(): List<Contrato> {
        return contratoService.obtenerContratos()
    }

    @GetMapping("/contratos/{id}")
    fun verContratoPorId(@PathVariable id: Int): Contrato? {
        return contratoService.obtenerContratoPorId(id)
    }

    @PostMapping("/contratos")
    fun crearContrato(@RequestBody contrato: Contrato): String {
        return contratoService.crearContrato(contrato)
    }

    @PutMapping("/contratos/{id}")
    fun actualizarContrato(
        @PathVariable id: Int,
        @RequestBody contrato: Contrato
    ): String {
        return contratoService.actualizarContrato(id, contrato)
    }

    @DeleteMapping("/contratos/{id}")
    fun eliminarContrato(@PathVariable id: Int): String {
        return contratoService.eliminarContrato(id)
    }

    // 🔥 NUEVO ENDPOINT
    @GetMapping("/mi-plan/{clienteId}")
    fun obtenerMiPlan(@PathVariable clienteId: Int): MiPlanDTO? {
        return contratoService.obtenerMiPlan(clienteId)
    }
}