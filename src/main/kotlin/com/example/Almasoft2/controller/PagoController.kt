package com.example.cronograma.controller

import com.example.cronograma.model.Pago
import com.example.cronograma.service.PagoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class PagoController(
    private val pagoService: PagoService
) {


    @GetMapping("/pagos")
    fun verPagos(): List<Pago> {
        return pagoService.obtenerPagos()
    }


    @GetMapping("/pagos/{id}")
    fun verPagoPorId(@PathVariable id: Int): Pago? {
        return pagoService.obtenerPagoPorId(id)
    }


    @PostMapping("/pagos")
    fun crearPago(@RequestBody pago: Pago): String {
        return pagoService.crearPago(pago)
    }


    @PutMapping("/pagos/{id}")
    fun actualizarPago(
        @PathVariable id: Int,
        @RequestBody pago: Pago
    ): String {
        return pagoService.actualizarPago(id, pago)
    }


    @DeleteMapping("/pagos/{id}")
    fun eliminarPago(@PathVariable id: Int): String {
        return pagoService.eliminarPago(id)
    }


    @GetMapping("/pagos/contrato/{id}")
    fun pagosPorContrato(@PathVariable id: Int): List<Pago> {
        return pagoService.obtenerPagosPorContrato(id)
    }
}