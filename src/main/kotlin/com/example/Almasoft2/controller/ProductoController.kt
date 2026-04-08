package com.example.cronograma.controller

import com.example.cronograma.model.Producto
import com.example.cronograma.service.ProductoService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
class ProductoController(
    private val productoService: ProductoService
) {

    @GetMapping
    fun obtenerProductos(): List<Map<String, Any>> {
        return productoService.obtenerProductos()
    }

    @GetMapping("/{id}")
    fun obtenerProductoPorId(@PathVariable id: Int): Producto? {
        return productoService.obtenerProductoPorId(id)
    }

    @PostMapping
    fun crearProducto(@RequestBody producto: Producto): String {
        return productoService.crearProducto(producto)
    }

    @PutMapping("/{id}")
    fun actualizarProducto(
        @PathVariable id: Int,
        @RequestBody producto: Producto
    ): String {
        return productoService.actualizarProducto(id, producto)
    }

    @DeleteMapping("/{id}")
    fun eliminarProducto(@PathVariable id: Int): String {
        return productoService.eliminarProducto(id)
    }


    @GetMapping("/buscar")
    fun buscar(@RequestParam nombre: String): List<Producto> {
        return productoService.buscarProductos(nombre)
    }
    @GetMapping("/full")
    fun obtenerProductosFull(): List<Map<String, Any>> {
        return productoService.obtenerProductosConCategoria()
    }
}