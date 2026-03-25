package com.example.cronograma.controller

import com.example.cronograma.model.Pago
import com.example.cronograma.service.PagoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PagoController(
    private val pagoService: PagoService
) {

    // GET TODOS
    @GetMapping("/pagos")
    fun verPagos(): List<Pago> {
        return pagoService.obtenerPagos()
    }

    // GET POR ID
    @GetMapping("/pagos/{id}")
    fun verPagoPorId(@PathVariable id: Int): Pago? {
        return pagoService.obtenerPagoPorId(id)
    }

    // POST
    @PostMapping("/pagos")
    fun crearPago(@RequestBody pago: Pago): String {
        return pagoService.crearPago(pago)
    }

    // PUT
    @PutMapping("/pagos/{id}")
    fun actualizarPago(
        @PathVariable id: Int,
        @RequestBody pago: Pago
    ): String {
        return pagoService.actualizarPago(id, pago)
    }

    // DELETE
    @DeleteMapping("/pagos/{id}")
    fun eliminarPago(@PathVariable id: Int): String {
        return pagoService.eliminarPago(id)
    }

    // 🔥 CLAVE PARA TU APP
    @GetMapping("/pagos/contrato/{id}")
    fun pagosPorContrato(@PathVariable id: Int): List<Pago> {
        return pagoService.obtenerPagosPorContrato(id)
    }
}