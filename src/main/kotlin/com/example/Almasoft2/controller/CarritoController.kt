package com.example.cronograma.controller

import com.example.cronograma.model.Carrito
import com.example.cronograma.service.CarritoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/carrito")
class CarritoController(
    private val carritoService: CarritoService
) {

    @GetMapping("/{usuarioId}")
    fun obtenerCarrito(@PathVariable usuarioId: Int): List<Carrito> {
        return carritoService.obtenerCarrito(usuarioId)
    }

    @PostMapping
    fun agregar(@RequestBody carrito: Carrito): String {
        return carritoService.agregarAlCarrito(carrito)
    }

    @DeleteMapping("/{usuarioId}/{productoId}")
    fun eliminar(
        @PathVariable usuarioId: Int,
        @PathVariable productoId: Int
    ): String {
        return carritoService.eliminarProducto(usuarioId, productoId)
    }
    @PostMapping("/confirmar-pago/{usuarioId}/{metodo}")
    fun confirmarPago(
        @PathVariable usuarioId: Int,
        @PathVariable metodo: String
    ): Map<String, String> {

        val mensaje = carritoService.confirmarPago(usuarioId, metodo)

        return mapOf("mensaje" to mensaje)
    }
}