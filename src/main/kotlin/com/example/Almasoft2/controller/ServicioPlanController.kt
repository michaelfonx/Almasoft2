package com.example.cronograma.controller

import com.example.cronograma.model.ServicioPlan
import com.example.cronograma.service.ServicioPlanService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/servicio-plan")
class ServicioPlanController(
    private val service: ServicioPlanService
) {

    @GetMapping
    fun obtenerTodos(): List<ServicioPlan> {
        return service.obtenerTodos()
    }

    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Int): ServicioPlan? {
        return service.obtenerPorId(id)
    }

    @PostMapping
    fun crear(@RequestBody relacion: ServicioPlan): String {
        return service.crear(relacion)
    }

    @PutMapping("/{id}")
    fun actualizar(
        @PathVariable id: Int,
        @RequestBody relacion: ServicioPlan
    ): String {
        return service.actualizar(id, relacion)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Int): String {
        return service.eliminar(id)
    }
}