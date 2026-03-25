package com.example.cronograma.controller

import com.example.cronograma.model.ContratoPlan
import com.example.cronograma.service.ContratoPlanService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/contrato-plan")
class ContratoPlanController(
    private val service: ContratoPlanService
) {

    @GetMapping
    fun obtenerTodos(): List<ContratoPlan> {
        return service.obtenerTodos()
    }

    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Int): ContratoPlan? {
        return service.obtenerPorId(id)
    }

    @PostMapping
    fun crear(@RequestBody relacion: ContratoPlan): String {
        return service.crear(relacion)
    }

    @PutMapping("/{id}")
    fun actualizar(
        @PathVariable id: Int,
        @RequestBody relacion: ContratoPlan
    ): String {
        return service.actualizar(id, relacion)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Int): String {
        return service.eliminar(id)
    }
}