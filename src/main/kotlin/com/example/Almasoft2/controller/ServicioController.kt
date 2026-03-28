package com.example.Almasoft2.controller

import com.example.Almasoft2.model.Servicio
import com.example.Almasoft2.service.ServicioService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/servicio")
class ServicioController(private val service: ServicioService) {


    @GetMapping
    fun obtenerServicios(): List<Servicio> {
        return service.listarServicios()
    }


    @GetMapping("/{id}")
    fun obtenerServicio(@PathVariable id: Int): Servicio? {
        return service.obtenerPorId(id)
    }


    @PostMapping
    fun crearServicio(@RequestBody servicio: Servicio): Servicio {
        return service.guardar(servicio)
    }


    @PutMapping("/{id}")
    fun actualizarServicio(
        @PathVariable id: Int,
        @RequestBody servicio: Servicio
    ): Servicio {
        return service.actualizar(id, servicio)
    }


    @DeleteMapping("/{id}")
    fun eliminarServicio(@PathVariable id: Int) {
        service.eliminar(id)
    }
    @GetMapping("/cliente/{id}/servicios")
    fun obtenerServiciosPorCliente(@PathVariable id: Int): List<Servicio> {
        return service.obtenerServiciosPorCliente(id)
    }
}