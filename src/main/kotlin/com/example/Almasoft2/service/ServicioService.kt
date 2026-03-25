package com.example.Almasoft2.service

import com.example.Almasoft2.model.Servicio
import com.example.Almasoft2.repository.ServicioRepository
import org.springframework.stereotype.Service

@Service
class ServicioService(private val repo: ServicioRepository) {

    fun listarServicios(): List<Servicio> {
        return repo.findAll()
    }

    fun obtenerPorId(id: Int): Servicio? {
        return repo.findById(id).orElse(null)
    }

    fun guardar(servicio: Servicio): Servicio {
        return repo.save(servicio)
    }

    fun actualizar(id: Int, servicio: Servicio): Servicio {

        val servicioExistente = repo.findById(id).orElseThrow()

        servicioExistente.servicioNombre = servicio.servicioNombre
        servicioExistente.servicioDescripcion = servicio.servicioDescripcion
        servicioExistente.servicioPrecio = servicio.servicioPrecio

        return repo.save(servicioExistente)
    }

    fun eliminar(id: Int) {
        repo.deleteById(id)
    }

    // 🔥 NUEVO
    fun obtenerServiciosPorCliente(clienteId: Int): List<Servicio> {
        return repo.obtenerServiciosPorCliente(clienteId)
    }
}