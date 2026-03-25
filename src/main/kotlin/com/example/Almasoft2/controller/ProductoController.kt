package com.example.cronograma.controller

import com.example.cronograma.model.Producto
import com.example.cronograma.service.ProductoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
class ProductoController(
    private val productoService: ProductoService
) {

    // 🔹 GET TODOS
    @GetMapping
    fun obtenerProductos(): List<Producto> {
        return productoService.obtenerProductos()
    }

    // 🔹 GET POR ID
    @GetMapping("/{id}")
    fun obtenerProductoPorId(@PathVariable id: Int): Producto? {
        return productoService.obtenerProductoPorId(id)
    }

    // 🔥 POST
    @PostMapping
    fun crearProducto(@RequestBody producto: Producto): String {
        return productoService.crearProducto(producto)
    }

    // 🔹 PUT
    @PutMapping("/{id}")
    fun actualizarProducto(
        @PathVariable id: Int,
        @RequestBody producto: Producto
    ): String {
        return productoService.actualizarProducto(id, producto)
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    fun eliminarProducto(@PathVariable id: Int): String {
        return productoService.eliminarProducto(id)
    }
}