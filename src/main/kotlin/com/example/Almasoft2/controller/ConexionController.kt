package com.example.cronograma.controller

import com.example.cronograma.model.Contrato
import com.example.cronograma.service.ConexionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ConexionController(
    private val conexionService: ConexionService
) {

    @GetMapping("/cronogramas")
    fun verCronogramas(): List<Contrato> {
        return conexionService.obtenerContratos()
    }

    @GetMapping("/cronogramas/{id}")
    fun verCronogramaPorId(@PathVariable id: Int): Contrato? {
        return conexionService.obtenerContratoPorId(id)
    }

    @PostMapping("/cronogramas")
    fun crearCronograma(@RequestBody contrato: Contrato): String {
        return conexionService.crearContrato(contrato)
    }

    @PutMapping("/cronogramas/{id}")
    fun actualizarCronograma(
        @PathVariable id: Int,
        @RequestBody contrato: Contrato
    ): String {
        return conexionService.actualizarContrato(id, contrato)
    }

    @DeleteMapping("/cronogramas/{id}")
    fun eliminarCronograma(@PathVariable id: Int): String {
        return conexionService.eliminarContrato(id)
    }
}
